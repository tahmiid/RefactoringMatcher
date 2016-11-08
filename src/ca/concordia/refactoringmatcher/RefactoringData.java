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
		return "\n" + refactoring.getName() +
				"\nOriginal code: Starting Point-" + parentCode.getStartOffset() + " Length-" + parentCode.getLength() + " Class-" + parentCode.getFilePath() + " at Commit " + parentCode.getCommit() +
				"\nRefactored code: Starting Point-" + refactoredCode.getStartOffset() + " Length-" + refactoredCode.getLength() + " Class-" + refactoredCode.getFilePath()  + " at Commit " + refactoredCode.getCommit();
//		+		"\nMaping: Parent Code File (" + parentCode.getStartLocationInCodeDatabase() + "-" + parentCode.getEndLocationInCodeDatabase() + ") Refactored Code File (" + refactoredCode.getStartLocationInCodeDatabase() + "-" + refactoredCode.getEndLocationInCodeDatabase() + ")";
	}
}
