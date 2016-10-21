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
import gr.uom.java.xmi.diff.RenameOperationRefactoring;

public class RefactoringMatcher {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		GitService gitService = new GitServiceImpl();
		GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
		List<Refactoring> refactoringsForMatching = new ArrayList<Refactoring>();

		Repository repo = gitService.cloneIfNotExists("tmp/refactoring-toy-example",
				"https://github.com/danilofes/refactoring-toy-example.git");

		miner.detectAll(repo, "master", new RefactoringHandler() {
			@Override
			public void handle(RevCommit commitData, List<Refactoring> refactorings) {
				System.out.println("Refactorings at " + commitData.getId().getName());
				for (Refactoring ref : refactorings) {
					System.out.println(ref.toString());

					RefactoringType refType = ref.getRefactoringType();
					if (refType == RefactoringType.RENAME_METHOD || refType == RefactoringType.PULL_UP_OPERATION || refType == RefactoringType.PUSH_DOWN_OPERATION || refType == RefactoringType.INLINE_OPERATION || refType == RefactoringType.EXTRACT_OPERATION || refType == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
						refactoringsForMatching.add(ref);
					}
				}

				
			}
		});
		
		ASTInformation astInformation;
		for (Refactoring ref : refactoringsForMatching) {
			astInformation = parseASTInformation(ref);
			System.out.println(ref.getName());
			System.out.println(astInformation.getFilePath() + " " + astInformation.getStartOffset() + " " + astInformation.getLength());
//			System.out.println(astInformation.getStartOffset() + " " + astInformation.getLength());
		}
		

	}

	private static ASTInformation parseASTInformation(Refactoring ref) {
		if(ref.getRefactoringType() == RefactoringType.RENAME_METHOD)
		{
			return ((RenameOperationRefactoring)ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
		} 
		else if(ref.getRefactoringType() == RefactoringType.PULL_UP_OPERATION)
		{
			return ((PullUpOperationRefactoring)ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
		}
		else if(ref.getRefactoringType() == RefactoringType.PUSH_DOWN_OPERATION)
		{
			return ((PushDownOperationRefactoring)ref).getOriginalOperation().getBody().getCompositeStatement().getAstInformation();
		}
		else if(ref.getRefactoringType() == RefactoringType.INLINE_OPERATION)
		{
			return ((InlineOperationRefactoring)ref).getInlinedToOperation().getBody().getCompositeStatement().getAstInformation();
		}
		else if(ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION)
		{
			return ((ExtractOperationRefactoring)ref).getExtractedFromOperation().getBody().getCompositeStatement().getAstInformation();
		}
		else if(ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION)
		{
			return ((ExtractAndMoveOperationRefactoring)ref).getExtractedOperation().getBody().getCompositeStatement().getAstInformation();
		}
		return null;
	}

}
