package ca.concordia.refactoringmatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedRefactoringMinerImpl extends GitHistoryRefactoringMinerImpl implements ExtendedRefactoringMiner {

	private static final int gitfaultTolerance = 5;
	private static Logger logger = LoggerFactory.getLogger(ExtendedRefactoringMinerImpl.class);
	private static boolean gitfault = false;
	private static int gitfaultCount = 0;

	public ExtendedRefactoringMinerImpl() {
		super();
	}

	@Override
	public void detectAllWithTimeOut(Repository repository, String branch, RefactoringHandler handler)
			throws Exception {
		gitfault = false;
		gitfaultCount = 0;
		ExtendedGitService gitService = new ExtendedGitServiceImpl() {
			@Override
			public boolean isCommitAnalyzed(String sha1) {
				return handler.skipCommit(sha1);
			}
		};

		ArrayList<String> commits = gitService.getAllCommits(repository);

		double splits = 1;
		int i = 1;
		int splitSize = (int) Math.ceil(commits.size() / splits);
		ArrayList<Thread> repoThreads = new ArrayList<>();
		ArrayList<String> copyAddress = new ArrayList<>();

		while (i <= splits) {
			Repository repo = gitService.duplicate(repository, "temp");
			String path = repo.getDirectory().getAbsolutePath().replaceAll("\\.git", "");
			copyAddress.add(path);
			String startCommit = commits.get(Math.max(((i - 1) * splitSize), 0));
			String endCommit = commits.get(Math.min(i * splitSize - 1, commits.size() - 1));

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						ExtendedGitService gitService = new ExtendedGitServiceImpl() {
							@Override
							public boolean isCommitAnalyzed(String sha1) {
								return handler.skipCommit(sha1);
							}
						};
						RevWalk walk = gitService.createRevsWalkBetweenCommits(repo, startCommit, endCommit);
						detect(repo, handler, new ExtendedGitServiceImpl(), walk.iterator());
						repo.close();
					} catch (CheckoutConflictException e) {
					} catch (Exception e) {
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
			});
			if(gitfault)
				break;
			t.start();
			repoThreads.add(t);
			i++;
		}

		while (repoThreads.size() > 0) {
			Thread threadToRemove = null;
			for (Thread thread : repoThreads) {
				if (!thread.isAlive()) {
					threadToRemove = thread;
					break;
				}
			}

			if (threadToRemove == null) {
			} else {
				repoThreads.remove(threadToRemove);
				threadToRemove = null;
			}
		}

		Thread.sleep(2000);
		for (String string : copyAddress) {
			File directory = new File(string);
			for (File file : directory.listFiles()) {
				try {
					FileDeleteStrategy.FORCE.delete(file);
				} catch (Exception e) {
					logger.warn("Could not delete temporary file: " + file);
				}
			}

		}
		
		if(gitfault)
			throw new Exception("Git Fault");
	}

	private void detect(Repository repository, RefactoringHandler handler, ExtendedGitService gitService,
			Iterator<RevCommit> i) {
		try {
			File metadataFolder = repository.getDirectory();
			File projectFolder = metadataFolder.getParentFile();
			ArrayList<Pair<Long, Thread>> threads = new ArrayList<Pair<Long, Thread>>();

			while (i.hasNext()) {
				if(gitfault)
					break;
				
				RevCommit currentCommit = i.next();
				try {
					Thread t = new Thread(new Runnable() {
						public void run() {
							try {
								detectRefactorings(gitService, repository, handler, projectFolder, currentCommit);
							} catch (CheckoutConflictException e) {
								logger.error("RefactoringMiner error. Skipping commit: " + currentCommit.getName());
								gitfaultCount++;
								e.printStackTrace();
								
								if(gitfaultCount > gitfaultTolerance)
									gitfault = true;
							} catch (Exception e) {
								logger.error("RefactoringMiner error. Skipping commit: " + currentCommit.getName());
								logger.error(e.getStackTrace().toString());
								e.printStackTrace();
							}
						}
					});
					
					if(gitfault)
						break;
					t.start();
					threads.add(Pair.of(System.currentTimeMillis(), t));

					while (threads.size() >= 2) {
						Pair<Long, Thread> threadToRemove = null;
						for (Pair<Long, Thread> thread : threads) {
							if (!thread.getRight().isAlive()) {
								threadToRemove = thread;
								break;
							} else if (System.currentTimeMillis() - thread.getLeft() > 60000) {
								thread.getRight().stop();
								threadToRemove = thread;
								break;
							}
						}

						if (threadToRemove == null) {
							// Thread.sleep(100);
						} else {
							threads.remove(threadToRemove);
							threadToRemove = null;
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					handler.handleException(currentCommit.getId().getName(), e);
				}
			}
		} finally {
		}
	}
}
