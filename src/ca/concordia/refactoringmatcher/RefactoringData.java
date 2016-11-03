package ca.concordia.refactoringmatcher;

import java.io.Serializable;

import org.refactoringminer.api.Refactoring;

public class RefactoringData implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Refactoring refactoring;
	private Code parentCode;
	private Code refactoredCode;
	
	public RefactoringData(Refactoring ref, Code parentCode, Code refactoredCode) {
		this.refactoring = ref;
		this.parentCode = parentCode;
		this.refactoredCode = refactoredCode;
	}

	public Refactoring getRefactoring() {
		return refactoring;
	}

	public Code getParentCode() {
		return parentCode;
	}
	public Code getRefactoredCode() {
		return refactoredCode;
	}
	public String toString()
	{
		return "\nApplied refactoring: " + refactoring.getName() +
				"\nOriginal code: Line-" + parentCode.getStartOffset() + " Length-" + parentCode.getLength() + " Class-" + parentCode.getFilePath() + " at Commit " + parentCode.getCommit() +
				"\nRefactored code: Line-" + refactoredCode.getStartOffset() + " Length-" + refactoredCode.getLength() + " Class-" + refactoredCode.getFilePath()  + " at Commit " + refactoredCode.getCommit();
	}
}
