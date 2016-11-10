
package ca.concordia.refactoringmatcher;

import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringType;

public class RefactoringData {	
	private Refactoring refactoring;
	private Code beforeCode;
	private Code afterCode;
	
	public RefactoringData(Refactoring ref, Code beforeCode, Code afterCode) {
		this.refactoring = ref;
		this.beforeCode = beforeCode;
		this.afterCode = afterCode;
	}

	public String getBeforeCodeText() {
		return beforeCode.getText();
	}
	
	public Code getBeforeCode() {
		return beforeCode;
	}
	
	public String getAfterCodeText() {
		return afterCode.getText();
	}
	
	public Code getAfterCode() {
		return afterCode;
	}
	
	public String toString()
	{
		return "\n" + refactoring.getName() +
				"\nOriginal code: Starting Point-" + beforeCode.getStartOffset() + " Length-" + beforeCode.getLength() + " Class-" + beforeCode.getFilePath() + " at Commit " + beforeCode.getCommit() +
				"\nRefactored code: Starting Point-" + afterCode.getStartOffset() + " Length-" + afterCode.getLength() + " Class-" + afterCode.getFilePath()  + " at Commit " + afterCode.getCommit();
	}
	
	public RefactoringType getType()
	{
		return refactoring.getRefactoringType();
	}
	
	public String getName()
	{
		return refactoring.getName();
	}
}
