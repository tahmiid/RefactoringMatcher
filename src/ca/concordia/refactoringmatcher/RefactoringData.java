
package ca.concordia.refactoringmatcher;

import java.io.Serializable;
import java.util.Date;

import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringType;

public class RefactoringData implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Refactoring refactoring;
	private Code refactoredCode;
	private String projectName;

	public RefactoringData(Refactoring refactoring, Code afterCode, String projectName) {
		this.refactoring = refactoring;
		this.refactoredCode = afterCode;
		this.projectName = projectName;
	}
	public Refactoring getRefactoring() {
		return refactoring;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public String getRefactoredText() {
		return refactoredCode.getText();
	}
	
//	public String getRefactoredOperationBody() {
//		return refactoredCode.getMethodBody();
//	}
	
	public Code getRefactoredCode() {
		return refactoredCode;
	}
	
	public String toDetailString()
	{
		return "(Refactoring:" + refactoring.getName() + ")" + " (" + refactoredCode.getCommit() + ")\n"+ refactoredCode.getMethodName() + " in " + refactoredCode.getFileName();
	}
	
	public String toString()
	{
		return getName() + " at " + getCommit();
	}
	
	public RefactoringType getType()
	{
		return refactoring.getRefactoringType();
	}
	
	public String getName()
	{
		return refactoring.getName();
	}
	
	public Commit getCommit()
	{
		return refactoredCode.getCommit();
	}
	
	public String getCommitShort()
	{
		return refactoredCode.getCommitShort();
	}
	
//	public Date getCommitTime()
//	{
//		return afterCode.getCommit().getTime();
//	}
}
