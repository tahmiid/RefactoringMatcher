
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
	private Code refactoredCode;
	private String projectName;
	private String name;
	private RefactoringType type;

	public RefactoringData(Refactoring refactoring, Code afterCode, String projectName) {
		this.name = refactoring.getName();
		this.type = refactoring.getRefactoringType();
		this.refactoredCode = afterCode;
		this.projectName = projectName;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public String getRefactoredText() {
		return refactoredCode.getText();
	}
	
	public Code getRefactoredCode() {
		return refactoredCode;
	}
	
	public String toDetailString()
	{
		return "Refactoring:" + name + " (" + refactoredCode.getCommit() + ")\n"+ refactoredCode.getMethodName() + " in file " + refactoredCode.getFileName();
	}
	
	public String toString()
	{
		return getName() + " at commit " + getCommit();
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
}
