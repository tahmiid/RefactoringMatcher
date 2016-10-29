package ca.concordia.refactoringmatcher;

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
import org.refactoringminer.util.GitServiceImpl;

import gr.uom.java.xmi.decomposition.ASTInformation;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;
import gr.uom.java.xmi.diff.PullUpOperationRefactoring;
import gr.uom.java.xmi.diff.PushDownOperationRefactoring;

public class RefactoringMatcher {
	public static void main(String[] args) throws Exception {
		String projectLink = "https://github.com/junit-team/junit5.git";
		GitService gitService = new GitServiceImpl();
		String clonePath = getClonePath(projectLink);
		Repository repo = gitService.cloneIfNotExists(clonePath, projectLink);
		
		List<RefactoringData> allRefactoringData = getAllRefactoringData(clonePath, repo);

		for (RefactoringData refactoringData : allRefactoringData) {
			System.out.println(refactoringData);
			
			String code = refactoringData.getParentCode().extractSourceCode(gitService,repo);
			
			System.out.println(code);
			
			code = refactoringData.getRefactoredCode().extractSourceCode(gitService,repo);
			
			System.out.println(code);
			
			System.out.println();
		}
	}

	private static String getClonePath(String projectLink) {
		int splitIndex1 = projectLink.lastIndexOf('/');
		projectLink = projectLink.substring(splitIndex1);
		int splitIndex2 = projectLink.lastIndexOf('.');
		if (splitIndex2 != -1) {
			projectLink = projectLink.substring(0, splitIndex2);
		}

		return "tmp" + projectLink;
	}

	
	private static List<RefactoringData> getAllRefactoringData(String clonePath, Repository repo) throws Exception {
		List<RefactoringData> allRefactoringData = new ArrayList<RefactoringData>();

		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
		miner.detectAll(repo, "master", new RefactoringHandler() {
			@Override
			public void handle(RevCommit commitData, List<Refactoring> refactorings) {
				for (Refactoring ref : refactorings) {

					ASTInformation astBeforeChange; 
					ASTInformation astAfterChange;

					if (ref.getRefactoringType() == RefactoringType.PULL_UP_OPERATION) {
						astBeforeChange = ((PullUpOperationRefactoring) ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((PullUpOperationRefactoring) ref).getMovedOperation().getBody().getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.PUSH_DOWN_OPERATION) {
						astBeforeChange = ((PushDownOperationRefactoring) ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((PushDownOperationRefactoring) ref).getMovedOperation().getBody().getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.INLINE_OPERATION) {
						astBeforeChange = ((InlineOperationRefactoring) ref).getInlinedOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((InlineOperationRefactoring) ref).getInlinedToOperation().getBody().getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
						astBeforeChange = ((ExtractOperationRefactoring) ref).getExtractedFromOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((ExtractOperationRefactoring) ref).getExtractedOperation().getBody().getCompositeStatement().getAstInformation();
					} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
						astBeforeChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedFromOperation().getBody().getCompositeStatement().getAstInformation();
						astAfterChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedOperation().getBody().getCompositeStatement().getAstInformation();
					} else {
						continue;
					}

					Code parentCode = new Code(commitData.getParent(0).getName(), clonePath + "/" + astBeforeChange.getFilePath(), astBeforeChange.getStartOffset(), astBeforeChange.getLength());
					Code refactoredCode = new Code(commitData.getName(), clonePath + "/" + astAfterChange.getFilePath(), astAfterChange.getStartOffset(), astAfterChange.getLength());
					
					RefactoringData refactoringData = new RefactoringData(ref, parentCode, refactoredCode);
					allRefactoringData.add(refactoringData);
				}
			}
		});
		return allRefactoringData;
	}
}
