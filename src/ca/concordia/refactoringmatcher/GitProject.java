package ca.concordia.refactoringmatcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.concordia.refactoringdata.ExtractMethod;
import ca.concordia.refactoringdata.IRefactoringData;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;

/**
 * @author tahmiid
 *
 */
public class GitProject implements Serializable {

	/**
	 * 
	 */
	private static Logger logger = LoggerFactory.getLogger(GitProject.class);
	private static final long serialVersionUID = 1L;
	private String link;
	private String name;
	private Path localPath;
	private Path outputPath;
	private Repository repository;
	private int commitCount;
	private List<IRefactoringData> allRefactoringData;
	private List<Pair<Refactoring, String>> refactorings;
	private List<String> commits;
	private List<Release> releases;
	private String organization;

	public GitProject(String projectLink, Path projectsDirectory, Path outputDirectory) throws Exception {
		this.link = projectLink;
		this.name = getName(link);
		this.organization = getOrganization(link);
		this.localPath = Paths.get(projectsDirectory + "/" + name + "." + organization);
		this.outputPath = Files.createDirectories(Paths.get(outputDirectory + "/" + name + "." + organization));
		ExtendedGitService gitService = new ExtendedGitServiceImpl();
		this.repository = gitService.cloneIfNotExists(localPath.toString(), link, "master");
		this.commits = getCommits();
		this.commitCount = commits.size();
		this.allRefactoringData = mineOrLoadRefactoringData();
	}

	private List<String> getCommits() {
		try {
			ExtendedGitService gitService = new ExtendedGitServiceImpl();
			return gitService.getAllCommits(repository);
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
			return new ArrayList<String>();
		}
	}

	private String getName(String projectLink) {
		int splitIndex1 = projectLink.lastIndexOf('/');
		projectLink = projectLink.substring(splitIndex1 + 1);
		int splitIndex2 = projectLink.lastIndexOf('.');
		if (splitIndex2 != -1) {
			projectLink = projectLink.substring(0, splitIndex2);
		}

		return projectLink;
	}

	private String getOrganization(String projectLink) {
		int splitIndex1 = projectLink.lastIndexOf('/');
		projectLink = projectLink.substring(0, splitIndex1);
		splitIndex1 = projectLink.lastIndexOf('/');
		projectLink = projectLink.substring(splitIndex1 + 1);
		int splitIndex2 = projectLink.lastIndexOf('.');
		if (splitIndex2 != -1) {
			projectLink = projectLink.substring(0, splitIndex2);
		}

		return projectLink;
	}

	private List<Pair<Refactoring, String>> mineRefactorings() throws Exception {
		List<Pair<Refactoring, String>> refactoringsFromRM = new ArrayList<Pair<Refactoring, String>>();
		ExtendedRefactoringMiner miner = new ExtendedRefactoringMinerImpl();
		miner.detectAllWithTimeOut(repository, "master", refactoringHandler(refactoringsFromRM));
		return refactoringsFromRM;
	}

	private RefactoringHandler refactoringHandler(List<Pair<Refactoring, String>> refactoringsFromRM) {
		return new RefactoringHandler() {
			@Override
			public void handle(String commitId, List<Refactoring> refs) {
				if (refs == null)
					return;
				for (Refactoring ref : refs) {
					if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						try {
							refactoringsFromRM.add(Pair.of(ref,commitId));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
				}
			}
		};
	}

	private String lastCommit(String commitId) {
		try {
			return commits.get(commits.indexOf(commitId) + 1);
		} catch (Exception e) {
			logger.error(e.getStackTrace().toString());
			e.printStackTrace();
			return commitId;
		}
	}

	private List<IRefactoringData> mineOrLoadRefactoringData() throws Exception {
		List<IRefactoringData> allRefactoringData;
		String refactoringDataPath = outputPath + "/" + "refactoringdata";
		if (Files.exists(Paths.get(refactoringDataPath))) {
			FileInputStream fis = new FileInputStream(refactoringDataPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				allRefactoringData = (List<IRefactoringData>) ois.readObject();
			} catch (Exception e) {
				logger.error("Could not load serialized object: " + refactoringDataPath);
				ois.close();
				fis.close();
				allRefactoringData = generateRefactoringData(mineOrLoadRefactoringsFromRM());
//				serializeAllRefactoringData(allRefactoringData);
			}
			ois.close();
			fis.close();
		} else {
			allRefactoringData = generateRefactoringData(mineOrLoadRefactoringsFromRM());
//			serializeAllRefactoringData(allRefactoringData);
		}
		return allRefactoringData;
	}

	private List<Pair<Refactoring, String>> mineOrLoadRefactoringsFromRM() throws Exception {
		List<Pair<Refactoring,String>> refactoringsFromRM;
		String refactoringPath = outputPath + "/" + "refactoringsFromRM";
		if(Files.exists(Paths.get(refactoringPath))) {
			FileInputStream fis = new FileInputStream(refactoringPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactoringsFromRM = (List<Pair<Refactoring, String>>) ois.readObject();
			} catch (Exception e) {
				logger.error(e.getStackTrace().toString());
				ois.close();
				fis.close();
				refactoringsFromRM = mineRefactorings();
				serializeRefactoringsFromRM(refactoringsFromRM);
			}
			ois.close();
			fis.close();
		}
		else {
			refactoringsFromRM = mineRefactorings();
			serializeRefactoringsFromRM(refactoringsFromRM);
		}
		return refactoringsFromRM;
	}

	private List<IRefactoringData> generateRefactoringData(List<Pair<Refactoring,String>> refactoringsFromRM) {
		List<IRefactoringData> extractedRefactorings = new ArrayList<IRefactoringData>();
//		for (Pair<Refactoring, String> refactoringFromRM : refactoringsFromRM) {
//			try {
//				if (refactoringFromRM.getLeft().getRefactoringType() == RefactoringType.EXTRACT_OPERATION) { 
//					ExtractOperationRefactoring extractOperationRefactoring = (ExtractOperationRefactoring) refactoringFromRM.getLeft();
//					String commitId = refactoringFromRM.getRight();
//					ExtractMethod extractMethodRefactoring = new ExtractMethod(extractOperationRefactoring, link,
//							commitId, repository);
//					try {
//						extractMethodRefactoring.retrieveCode(repository);
//						extractedRefactorings.add(extractMethodRefactoring);
//					} catch (Exception e) {
//						logger.error("Could not retrive refactoring details. Skipping Refactoring: " + extractMethodRefactoring.toString());
//						logger.error(e.getStackTrace().toString());
//						e.printStackTrace();
//					}
//				}
//			} catch (Exception e) {
//				logger.error("Could not retrive refactoring details. Skipping Refactoring: " + refactoringFromRM.getLeft());
//				logger.error(e.getStackTrace().toString());
//				e.printStackTrace();
//			}
//		}
		return extractedRefactorings;
	}

	private void serializeAllRefactoringData (List<IRefactoringData> refactorings) throws IOException {
		Files.deleteIfExists(Paths.get(outputPath + "/" + "refactoringdata"));
		FileOutputStream fos = new FileOutputStream(outputPath + "/" + "refactoringdata");
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactorings);
			oos.close();
			fos.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace().toString());
			logger.error("Failed to serialize");
			oos.close();
			fos.close();
			Files.deleteIfExists(Paths.get(outputPath + "/" + "refactoringdata"));
		}
	}
	
	private void serializeRefactoringsFromRM(List<Pair<Refactoring, String>> refactoringsFromRM) throws IOException {
		Files.deleteIfExists(Paths.get(outputPath + "/" + "refactoringsFromRM"));
		FileOutputStream fos = new FileOutputStream(outputPath + "/" + "refactoringsFromRM");
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactoringsFromRM);
			oos.close();
			fos.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace().toString());
			logger.error("Failed to serialize");
			oos.close();
			fos.close();
			Files.deleteIfExists(Paths.get(outputPath + "/" + "refactoringsFromRM"));
		}
	}	

	public void printReport() {
/*		int inlineMethod = 0;
		int extractMethod = 0;
		int extractAndMoveMethod = 0;
		for (IRefactoringData refactoringData : getRefactorings()) {
			if (refactoringData.getRefactoring().getRefactoringType() == RefactoringType.INLINE_OPERATION) {
				inlineMethod++;
			} else if (refactoringData.getRefactoring().getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
				extractMethod++;
			} else if (refactoringData.getRefactoring()
					.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
				extractAndMoveMethod++;
			}
		}*/

		logger.info("");
		logger.info("Project \t" + getName());
		logger.info("Link \t" + getLink());
		logger.info("Commits \t" + getCommitCount());
		logger.info("Refactorings \t" + getAllRefactoringData().size());
//		Logger.info("Inlined Methods \t" + inlineMethod);
//		Logger.info("Extract Methods \t" + extractMethod);
//		Logger.info("Extract and Move Methods \t" + extractAndMoveMethod);
		logger.info("");
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getOrganization() {
		return organization;
	}

	public Path getDirectory() {
		return localPath;
	}

	public Path getOutputDirectory() {
		return outputPath;
	}

	public Repository getRepository() {
		return repository;
	}

	public int getCommitCount() {
		return commitCount;
	}

	public List<IRefactoringData> getAllRefactoringData() {
		if (allRefactoringData == null)
			try {
				allRefactoringData = mineOrLoadRefactoringData();
			} catch (Exception e) {
			}
		return allRefactoringData;
	}

	public List<Release> getReleases() {
		if (releases == null) {
			try {
				releases = new ArrayList<Release>();
				Set<Ref> var;
				ExtendedGitService gitService = new ExtendedGitServiceImpl();
				var = gitService.getAllReleaseTags(repository);
				for (Ref ref : var) {
					Release release = new Release(ref, gitService, repository, localPath);
					releases.add(release);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return releases;
	}

	public Release getRelease(String commit) {
		try {
			ExtendedGitService gitService = new ExtendedGitServiceImpl();
			String nextReleaseTag = gitService.getNextTag(repository, commit);
			Release previousRelease = null;
			Release nextRelease = null;

			for (Release release : getReleases()) {
				if (release.getTag().equals(nextReleaseTag)) {
					nextRelease = release;
					break;
				}
				previousRelease = release;
			}

			return nextRelease == null ? previousRelease : nextRelease;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
