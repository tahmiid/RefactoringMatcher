package ca.concordia.refactoringmatcher.clonedetector;

public class HeaderClonePair {
	private CodeLocation headerOne;
	private CodeLocation headerTwo;
	
	public CodeLocation getHeaderOne() {
		return headerOne;
	}
	public void setHeaderOne(CodeLocation headerOne) {
		this.headerOne = headerOne;
	}
	public CodeLocation getHeaderTwo() {
		return headerTwo;
	}
	public void setHeaderTwo(CodeLocation headerTwo) {
		this.headerTwo = headerTwo;
	}
	
}
