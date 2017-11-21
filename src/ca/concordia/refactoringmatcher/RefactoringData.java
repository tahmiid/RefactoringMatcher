
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
//	private Refactoring refactoring;
	private Code refactoredCode;
	private String projectName;
	private String name;
	private RefactoringType type;

	public RefactoringData(Refactoring refactoring, Code afterCode, String projectName) {
//		this.refactoring = refactoring;
		this.name = refactoring.getName();
		this.type = refactoring.getRefactoringType();
		this.refactoredCode = afterCode;
		this.projectName = projectName;
	}
//	public Refactoring getRefactoring() {
//		return refactoring;
//	}
	
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
		return "(Refactoring:" + name + ")" + " (" + refactoredCode.getCommit() + ")\n"+ refactoredCode.getMethodName() + " in " + refactoredCode.getFileName();
	}
	
	public String toString()
	{
		return getName() + " at " + getCommit();
	}
	
	public RefactoringType getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
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
