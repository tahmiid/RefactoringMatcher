package ca.concordia.refactoringmatcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;

import gr.uom.java.xmi.decomposition.ASTInformation;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;

/**
 * @author tahmiid
 *
 */
public class Project {
	
	private String link;
	private String name;
	private Path directory;
	private Path outputDirectory;
	private Repository repository;
	private int commitCount;
	private List<RefactoringData> refactorings;
	
	public Project(String projectLink, Path projectsDirectory, Path outputDirectory, GitService gitService) throws Exception {
		this.link = projectLink;
		this.name = getName(link);
		this.directory = Paths.get(projectsDirectory + "/" + name);
		this.outputDirectory = Files.createDirectories( Paths.get(outputDirectory + "/" + name));	
		this.repository = gitService.cloneIfNotExists(directory.toString(), link); 
		this.commitCount = gitService.countCommits(repository, "master");
		this.refactorings = loadRefactoringsFromFile(gitService);
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

	private List<RefactoringData> getAllRefactorings(GitService gitService) throws Exception {
		List<RefactoringData> allRefactoringData = new ArrayList<RefactoringData>();

		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
		miner.detectAll(repository, "master", new RefactoringHandler() {
			@Override
			public void handle(RevCommit commitData, List<Refactoring> refactorings) {
				for (Refactoring ref : refactorings) {

					ASTInformation astBeforeChange;
					ASTInformation astAfterChange;

					// Refactor Refactoring Miner

					/*
					 * if (ref.getRefactoringType() ==
					 * RefactoringType.PULL_UP_OPERATION) { astBeforeChange =
					 * ((PullUpOperationRefactoring)
					 * ref).getOriginalOperation().getBody().
					 * getCompositeStatement().getAstInformation();
					 * astAfterChange = ((PullUpOperationRefactoring)
					 * ref).getMovedOperation().getBody().getCompositeStatement(
					 * ).getAstInformation(); } else if
					 * (ref.getRefactoringType() ==
					 * RefactoringType.PUSH_DOWN_OPERATION) { astBeforeChange =
					 * ((PushDownOperationRefactoring)
					 * ref).getOriginalOperation().getBody().
					 * getCompositeStatement().getAstInformation();
					 * astAfterChange = ((PushDownOperationRefactoring)
					 * ref).getMovedOperation().getBody().getCompositeStatement(
					 * ).getAstInformation(); } else
					 */ if (ref.getRefactoringType() == RefactoringType.INLINE_OPERATION) {
						astBeforeChange = ((InlineOperationRefactoring) ref).getInlinedOperation().getBody()
								.getCompositeStatement().getAstInformation();
						astAfterChange = ((InlineOperationRefactoring) ref).getInlinedToOperation().getBody()
								.getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						astBeforeChange = ((ExtractOperationRefactoring) ref).getExtractedFromOperation().getBody()
								.getCompositeStatement().getAstInformation();
						astAfterChange = ((ExtractOperationRefactoring) ref).getExtractedOperation().getBody()
								.getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
						astBeforeChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedFromOperation()
								.getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedOperation().getBody()
								.getCompositeStatement().getAstInformation();
					} else {
						continue;
					}
					 
					Code beforeCode = null;
					Code afterCode = null;
					try {
						Commit commit = new Commit(commitData.getParent(0).getName(), commitData.getParent(0).getFullMessage(), commitData.getParent(0).getCommitTime(), commitData.getParent(0).getCommitterIdent());
						beforeCode = new Code(commit, directory, astBeforeChange, gitService, repository);
						commit = new Commit(commitData.getName(), commitData.getFullMessage(), commitData.getCommitTime(), commitData.getCommitterIdent());
						afterCode = new Code(commit, directory, astAfterChange, gitService, repository);
					} catch (Exception e) {
						continue;
					}
				
					RefactoringData refactoringData = new RefactoringData(ref.getName(), ref.getRefactoringType(), beforeCode, afterCode, name);
					
					boolean exists = false;
					for (RefactoringData existingRefactoring : allRefactoringData) {
						if (existingRefactoring.getAfterCode().equals(refactoringData.getAfterCode())) {
							exists = true;
							break;
						}
					}
					
					if(!exists)
						allRefactoringData.add(refactoringData);
				}
			}
		});
		return allRefactoringData;
	}
	
	private List<RefactoringData> loadRefactoringsFromFile(GitService gitService) throws Exception {
		List<RefactoringData> refactorings;
		if (Files.exists(Paths.get(outputDirectory + "/" + name))) {
			FileInputStream fis = new FileInputStream(outputDirectory + "/" + name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactorings = (List<RefactoringData>) ois.readObject();
			} catch (ClassNotFoundException e) {
				refactorings = getAllRefactorings(gitService);
				writeToFile(refactorings);
			}
			ois.close();
		} else {
			refactorings = getAllRefactorings(gitService);
			writeToFile(refactorings);
		}
		return refactorings;
	}

	private void writeToFile(List<RefactoringData> refactorings) throws IOException {
		Files.deleteIfExists(Paths.get(outputDirectory + "/" + name));
		FileOutputStream fos = new FileOutputStream(outputDirectory + "/" + name);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactorings);
			oos.close();
		} catch (IOException e) {
			System.out.println("Failed to serialize");
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

	public Path getDirectory() {
		return directory;
	}

	public Path getOutputDirectory() {
		return outputDirectory;
	}

	public Repository getRepository() {
		return repository;
	}

	public int getCommitCount() {
		return commitCount;
	}

	public List<RefactoringData> getRefactorings() {
		return refactorings;
	}
	
	
	
}

