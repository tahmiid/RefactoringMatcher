package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.refactoringminer.util.GitServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedGitServiceImpl extends GitServiceImpl implements ExtendedGitService {

	private static Logger logger = LoggerFactory.getLogger(ExtendedGitServiceImpl.class);

	@Override
	public Repository cloneIfNotExists(String projectPath, String cloneUrl) throws Exception {
		return cloneIfNotExists(projectPath, cloneUrl, "master");
	}

	@Override
	public Repository cloneIfNotExists(String projectPath, String cloneUrl, String branch) throws Exception {
		File folder = new File(projectPath);
		Repository repository;
		if (folder.exists()) {
			RepositoryBuilder builder = new RepositoryBuilder();
			repository = builder.setGitDir(new File(folder, ".git")).readEnvironment().findGitDir().build();
		} else {
			logger.info("Cloning {} ...", cloneUrl);
			Git git = Git.cloneRepository().setDirectory(folder).setURI(cloneUrl).setCloneAllBranches(true)
					.setBranch(branch).call();
			repository = git.getRepository();
		}
		return repository;
	}

	@Override
	public synchronized void checkout(Repository repository, String commitId) throws Exception {
		try {
			super.checkout(repository, commitId);
		} catch (UnsupportedOperationException e) {
			revert(repository);
			logger.error("GitService error. Can not checkout to " + commitId);
			throw e;
		} catch (CheckoutConflictException e) {
			revert(repository);
			logger.error("Checkout conflict error. Can not checkout to " + commitId);
			throw e;
		} catch (JGitInternalException e) {
			revert(repository);
			logger.error("JGitInternal error. Can not checkout to " + commitId);
			throw e;
		} catch (Exception e) {
			revert(repository);
			logger.error(e.getMessage());
			throw e;
		}
	}

	private boolean revert(Repository repository) {
		try (Git git = new Git(repository)) {
			git.revert().setStrategy(MergeStrategy.THEIRS).call();
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return false;
		}
	}

	@Override
	public String getNextTag(Repository repository, String commitId) throws Exception {
		String nextRelease = "";
		final Set<Ref> tags = new HashSet<Ref>();
		final RevWalk walk = new RevWalk(repository);
		walk.reset();
		ObjectId id = repository.resolve(commitId);
		RevCommit commit = walk.parseCommit(id);
		walk.reset();
		for (final Ref ref : repository.getTags().values()) {
			final RevObject obj = walk.parseAny(ref.getObjectId());
			final RevCommit tagCommit;
			if (obj instanceof RevCommit) {
				tagCommit = (RevCommit) obj;
			} else if (obj instanceof RevTag) {
				tagCommit = walk.parseCommit(((RevTag) obj).getObject());
			} else {
				continue;
			}
			if (commit.equals(tagCommit) || walk.isMergedInto(commit, tagCommit)) {
				tags.add(ref);
				nextRelease = ref.getName();
				break;
			}
		}
		return nextRelease;
	}

	@Override
	public Repository duplicate(Repository repository, String path) throws Exception {
		String source = repository.getDirectory().getParent();
		File srcDir = new File(source);
		String name = srcDir.getName();

		int rand = ThreadLocalRandom.current().nextInt(1, 1000);
		String destination = Paths.get(srcDir.getParent(), path, name, rand + "").toString();
		File destDir = new File(destination);

		try {
			FileUtils.copyDirectory(srcDir, destDir);
			Repository newRepo = super.openRepository(destination);
			Files.deleteIfExists(Paths.get(newRepo.getDirectory().getAbsolutePath(), "index.lock"));
			return newRepo;
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Set<Ref> getAllReleaseTags(Repository repository) throws Exception {
		final Set<Ref> tags = new HashSet<Ref>();
		final RevWalk walk = new RevWalk(repository);
		walk.reset();
		for (final Ref ref : repository.getTags().values()) {
			final RevObject obj = walk.parseAny(ref.getObjectId());
			if (!(obj instanceof RevCommit) && !(obj instanceof RevTag)) {
				continue;
			}
			tags.add(ref);
		}
		return tags;
	}

	@Override
	public ArrayList<String> getAllCommits(Repository repository) throws Exception {
		ArrayList<String> commits = new ArrayList<String>();
		String treeName = "refs/heads/master"; // tag or branch
		try (Git git = new Git(repository)) {
			for (RevCommit commit : git.log().add(repository.resolve(treeName)).call()) {
				commits.add(commit.getName());
			}
		} catch (RevisionSyntaxException | GitAPIException | IOException e) {
			logger.error(e.getMessage());
			throw e;
		}
		return commits;
	}

	@Override
	public String getParentCommit(Repository repository, String commitId) throws Exception {
		final RevWalk walk = new RevWalk(repository);
		walk.reset();
		ObjectId id = repository.resolve(commitId);
		RevCommit commit = walk.parseCommit(id);
		return commit.getParents()[0].getName();
	}
}
