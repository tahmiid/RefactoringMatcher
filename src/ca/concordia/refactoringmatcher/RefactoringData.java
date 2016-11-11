
package ca.concordia.refactoringmatcher;

import java.io.Serializable;

import org.refactoringminer.api.RefactoringType;

public class RefactoringData implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private RefactoringType type;
	private Code beforeCode;
	private Code afterCode;
	
	public RefactoringData(String name, RefactoringType type, Code beforeCode, Code afterCode) {
		this.name = name;
		this.type = type;
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
		return name + "\n" +
				"Ori code: " + beforeCode.getMethodName() + "() Start-" + beforeCode.getStartOffset() + "(" + beforeCode.getLength() + ") in " + beforeCode.getFilePath() + " at " + beforeCode.getCommit() + "\n" +
				"Ref code: " + afterCode.getMethodName() + "() Start-" + afterCode.getStartOffset() + "(" + afterCode.getLength() + ") in " + afterCode.getFilePath()  + " at " + afterCode.getCommit();
	}
	
	public RefactoringType getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getCommit()
	{
		return afterCode.getCommit();
	}
}
