package ca.concordia.refactoringmatcher;

import gr.uom.java.xmi.decomposition.ASTInformation;

import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.Refactoring;

public class RefactoringData {
	
	private Refactoring refactoring;
	private RevCommit commitData;
	private ASTInformation from;
	private ASTInformation to;
	
	
	public RefactoringData(RevCommit commitData, Refactoring ref, ASTInformation from, ASTInformation to) {
		this.refactoring = ref;
		this.commitData = commitData;
		this.from = from;
		this.to = to;
	}
	
	public Refactoring getRefactoring() {
		return refactoring;
	}
	public RevCommit getCommitData() {
		return commitData;
	}
	public ASTInformation getFrom() {
		return from;
	}
	public ASTInformation getTo() {
		return to;
	}
	public String toString()
	{
		return "\nApplied refactoring: " + refactoring.getName() + " at Commit " + commitData.getId().getName() +
				"\nOriginal code: Line-" + from.getStartOffset() + " Length-" + from.getLength() + " Class-" + from.getFilePath() +
				"\nRefactored code: Line-" + to.getStartOffset() + " Length-" + to.getLength() + " Class-" + to.getFilePath();
	}
}
