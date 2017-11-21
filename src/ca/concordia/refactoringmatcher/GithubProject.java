package ca.concordia.refactoringmatcher;

import gr.uom.java.xmi.LocationInfo;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author tahmiid
 *
 */
public class GithubProject {

	private String link;
	private String name;
	private Path localPath;
	private Path outputPath;
	private Repository repository;
	private int commitCount;
	private List<RefactoringData> refactorings;
	private List<Release> releases;
	private String organization;
	private ExtendedGitService gitService;

	public GithubProject(String projectLink, Path projectsDirectory, Path outputDirectory,
			ExtendedGitService gitService) throws Exception {
		this.link = projectLink;
		this.name = getName(link);
		this.organization = getOrganization(link);
		this.localPath = Paths.get(projectsDirectory + "/" + name);
		this.outputPath = Files.createDirectories(Paths.get(outputDirectory + "/" + name));
		this.gitService = gitService;
		this.repository = gitService.cloneIfNotExists(localPath.toString(), link);
		this.commitCount = calculateCommitCount();
	}

	private int calculateCommitCount() {
		int commitCount = 0;
		try {
			this.commitCount = gitService.countCommits(repository, "master");
		} catch (Exception e) {
		}
		return commitCount;
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

	private List<RefactoringData> mineRefactorings() throws Exception {
		List<RefactoringData> allRefactoringData = new ArrayList<RefactoringData>();

		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
		miner.detectAll(repository, "master", new RefactoringHandler() {
			@Override
			public void handle(String commitId, List<Refactoring> refactorings) {
				for (Refactoring ref : refactorings) {
					LocationInfo refactoredCodeLocation;
					if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						refactoredCodeLocation = ((ExtractOperationRefactoring) ref).getExtractedOperation().getLocationInfo();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
						refactoredCodeLocation = ((ExtractAndMoveOperationRefactoring) ref).getExtractedOperation().getLocationInfo();
					} else {
						continue;
					}

					try {
						Code refactoredCode = new Code(new Commit(commitId), localPath, refactoredCodeLocation, gitService, repository);
						RefactoringData refactoringData = new RefactoringData(ref, refactoredCode, name);

						boolean exists = false;
						for (RefactoringData existingRefactoring : allRefactoringData) {
							if (existingRefactoring.getRefactoredCode().equals(refactoringData.getRefactoredCode())) {
								exists = true;
								break;
							}
						}
						if (!exists)
							allRefactoringData.add(refactoringData);
					} catch (Exception e) {
						continue;
					}
				}
			}
		});
		return allRefactoringData;
	}

	private List<RefactoringData> loadSerializedRefactorings() throws Exception {
		List<RefactoringData> refactorings;
		String outputPathString = outputPath + "/" + name;
		if (Files.exists(Paths.get(outputPathString))) {
			FileInputStream fis = new FileInputStream(outputPathString);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactorings = (List<RefactoringData>) ois.readObject();
			} catch (Exception e) {
				refactorings = mineRefactorings();
				writeToFile(refactorings);
			}
			ois.close();
		} else {
			refactorings = mineRefactorings();
			writeToFile(refactorings);
		}
		return refactorings;
	}

	private void writeToFile(List<RefactoringData> refactorings) throws IOException {
		Files.deleteIfExists(Paths.get(outputPath + "/" + name));
		FileOutputStream fos = new FileOutputStream(outputPath + "/" + name);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactorings);
			oos.close();
		} catch (IOException e) {
			System.out.println("Failed to serialize");
			System.out.println(e);
		}

	}

	public void printReport() {
		int inlineMethod = 0;
		int extractMethod = 0;
		int extractAndMoveMethod = 0;
		for (RefactoringData refactoringData : getRefactorings()) {
			if (refactoringData.getType() == RefactoringType.INLINE_OPERATION) {
				inlineMethod++;
			} else if (refactoringData.getType() == RefactoringType.EXTRACT_OPERATION) {
				extractMethod++;
			} else if (refactoringData.getType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
				extractAndMoveMethod++;
			}
		}

		System.out.println();
		System.out.println("Project\t" + getName());
		System.out.println("Link\t" + getLink());
		System.out.println("Commits\t" + getCommitCount());
		System.out.println("Refactorings\t" + getRefactorings().size());
		System.out.println("Inlined Methods\t" + inlineMethod);
		System.out.println("Extract Methods\t" + extractMethod);
		System.out.println("Extract and Move Methods\t" + extractAndMoveMethod);
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

	public List<RefactoringData> getRefactorings() {
		if (refactorings == null)
			try {
				refactorings = loadSerializedRefactorings();
			} catch (Exception e) {
			}
		return refactorings;
	}

	public List<Release> getReleases() {
		if (releases == null) {
			try {
				releases = new ArrayList<Release>();
				Set<Ref> var;
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
