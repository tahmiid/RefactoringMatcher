package ca.concordia.refactoringmatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

public class Project {
	
	String link;
	String name;
	Path directory;
	Path outputDirectory;
	Path beforeCodePath;
	Path afterCodePath;
	Repository repository;
	
	public Project(String projectLink, Path projectsDirectory, Path outputDirectory, GitService gitService) throws Exception {
		this.link = projectLink;
		this.name = getName(link);
		this.directory = Files.createDirectories( Paths.get(projectsDirectory + "/" + name));
		this.outputDirectory = Files.createDirectories( Paths.get(outputDirectory + "/" + name));	
		this.repository = gitService.cloneIfNotExists(directory.toString(), link); 
		//List<RefactoringData> allRefactoringData = loadRefactoringDataFromFile(projectName,clonePath,refactoringDataDir, repo);
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

	public List<RefactoringData> getAllRefactorings(GitService gitService) throws Exception {
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
						beforeCode = new Code(commitData.getParent(0).getName(), directory, astBeforeChange, gitService, repository);
						afterCode = new Code(commitData.getName(), directory, astAfterChange, gitService, repository);
					} catch (Exception e) {
						e.printStackTrace();
					}
				
					RefactoringData refactoringData = new RefactoringData(ref, beforeCode, afterCode);
					allRefactoringData.add(refactoringData);
				}
			}
		});
		return allRefactoringData;
	}
}

