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

import gr.uom.java.xmi.UMLOperation;
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
		List<CodeChange> codeChanges = new ArrayList<CodeChange>();

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
		
		
		for (Refactoring ref : refactoringsForMatching) {
			codeChanges.add(parseCodeChange(ref));
		}
		
		for (CodeChange codeChange: codeChanges) {
			System.out.println("Applied refactoring: " + codeChange.getRefactoring().getName());
			System.out.println("Original code: Line-" + codeChange.getFrom().getStartOffset() + " Length-" + codeChange.getFrom().getLength() + " Class-" + codeChange.getFrom().getFilePath() );
			System.out.println("Refactored code: Line-" + codeChange.getTo().getStartOffset() + " Length-" + codeChange.getTo().getLength() + " Class-" + codeChange.getTo().getFilePath() );
			System.out.println();
		}
		

	}

	private static CodeChange parseCodeChange(Refactoring ref) {
		UMLOperation umlOperationBeforeChange = null;
		UMLOperation umlOperationAfterChange = null;
		
		if(ref.getRefactoringType() == RefactoringType.RENAME_METHOD)
		{
			umlOperationBeforeChange = ((RenameOperationRefactoring)ref).getOriginalOperation();
			umlOperationAfterChange = ((RenameOperationRefactoring)ref).getRenamedOperation();
		} 
		else if(ref.getRefactoringType() == RefactoringType.PULL_UP_OPERATION)
		{
			umlOperationBeforeChange = ((PullUpOperationRefactoring)ref).getOriginalOperation();
			umlOperationAfterChange = ((PullUpOperationRefactoring)ref).getMovedOperation();
		}
		else if(ref.getRefactoringType() == RefactoringType.PUSH_DOWN_OPERATION)
		{
			umlOperationBeforeChange = ((PushDownOperationRefactoring)ref).getOriginalOperation();
			umlOperationAfterChange = ((PushDownOperationRefactoring)ref).getMovedOperation();
		}
		else if(ref.getRefactoringType() == RefactoringType.INLINE_OPERATION)
		{
			umlOperationBeforeChange = ((InlineOperationRefactoring)ref).getInlinedOperation();
			umlOperationAfterChange = ((InlineOperationRefactoring)ref).getInlinedToOperation();
		}
		else if(ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION)
		{
			umlOperationBeforeChange = ((ExtractOperationRefactoring)ref).getExtractedFromOperation();
			umlOperationAfterChange = ((ExtractOperationRefactoring)ref).getExtractedOperation();
		}
		else if(ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION)
		{
			umlOperationBeforeChange = ((ExtractAndMoveOperationRefactoring)ref).getExtractedFromOperation();
			umlOperationAfterChange = ((ExtractAndMoveOperationRefactoring)ref).getExtractedOperation();
		}
		
		return new CodeChange(ref, umlOperationBeforeChange.getBody().getCompositeStatement().getAstInformation(), umlOperationAfterChange.getBody().getCompositeStatement().getAstInformation());
	}
}
