package ca.concordia.refactoringmatcher;

import java.io.FileInputStream;
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

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;

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
	private static final long serialVersionUID = 1L;
	private String link;
	private String name;
	private Path localPath;
	private Path outputPath;
	private Repository repository;
	private int commitCount;
	private List<IRefactoringData> refactorings;
	private List<String> commits;
	private List<Release> releases;
	private String organization;
	private ExtendedGitService gitService;

	public GitProject(String projectLink, Path projectsDirectory, Path outputDirectory,
			ExtendedGitService gitService) throws Exception {
		this.link = projectLink;
		this.name = getName(link);
		this.organization = getOrganization(link);
		this.localPath = Paths.get(projectsDirectory + "/" + name + "." + organization);
		this.outputPath = Files.createDirectories(Paths.get(outputDirectory + "/" + name + "." + organization));
		this.gitService = gitService;
		this.repository = gitService.cloneIfNotExists(localPath.toString(), link);
		this.commits = getCommits();
		this.commitCount = commits.size();
		this.refactorings = loadSerializedRefactorings();
	}

	private List<String> getCommits() {
		try {
			return  gitService.getAllCommits(repository);
		} catch (Exception e) {
			System.out.println(e);
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

	private List<IRefactoringData> mineRefactorings() throws Exception {
		List<IRefactoringData> allRefactoringData = new ArrayList<IRefactoringData>();
		ExtendedRefactoringMiner miner = new ExtendedRefactoringMinerImpl();
		miner.detectAllWithTimeOut(repository, "master", refactoringHandler(allRefactoringData));
		return allRefactoringData;
	}

	private RefactoringHandler refactoringHandler(List<IRefactoringData> allRefactoringData) {
		return new RefactoringHandler() {
			@Override
			public void handle(String commitId, List<Refactoring> refactorings) {
				for (Refactoring ref : refactorings) {
					if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						try {
							ExtractMethod refactoring = new ExtractMethod((ExtractOperationRefactoring) ref, link, commitId, repository, gitService);
							allRefactoringData.add(refactoring);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(e);
						}
					} 
				}
			}
		};
	}

	private List<IRefactoringData> loadSerializedRefactorings() throws Exception {
		List<IRefactoringData> refactorings;
		String outputPathString = outputPath + "/" + "refactoringdata";
		if (Files.exists(Paths.get(outputPathString))) {
			FileInputStream fis = new FileInputStream(outputPathString);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactorings = (List<IRefactoringData>) ois.readObject();
			} catch (Exception e) {
				ois.close();
				fis.close();
				refactorings = mineRefactorings();
				writeToFile(refactorings);
			}
			ois.close();
			fis.close();
		} else {
			refactorings = mineRefactorings();
			writeToFile(refactorings);
		}
		return refactorings;
	}

	private void writeToFile(List<IRefactoringData> refactorings) throws IOException {
		Files.deleteIfExists(Paths.get(outputPath + "/" + "refactoringdata"));
		FileOutputStream fos = new FileOutputStream(outputPath + "/" + "refactoringdata");
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactorings);
			oos.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Failed to serialize");
			oos.close();
			fos.close();
			Files.deleteIfExists(Paths.get(outputPath + "/" + "refactoringdata"));
			System.out.println(e);
		}

	}

	public void printReport() {
		int inlineMethod = 0;
		int extractMethod = 0;
		int extractAndMoveMethod = 0;
		for (IRefactoringData refactoringData : getRefactorings()) {
			if (refactoringData.getRefactoring().getRefactoringType() == RefactoringType.INLINE_OPERATION) {
				inlineMethod++;
			} else if (refactoringData.getRefactoring().getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
				extractMethod++;
			} else if (refactoringData.getRefactoring().getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
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

	public List<IRefactoringData> getRefactorings() {
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
