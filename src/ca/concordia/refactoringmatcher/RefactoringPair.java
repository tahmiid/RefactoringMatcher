package ca.concordia.refactoringmatcher;

import java.util.ArrayList;

public class RefactoringPair {
	private RefactoringData refactoringOne;
	private RefactoringData refactoringTwo;
	private String similarBlock;
	
	public RefactoringPair(RefactoringData left, RefactoringData right, String similarBlock) {
		this.refactoringOne = left;
		this.refactoringTwo = right;
		this.similarBlock = similarBlock;
	}
	
	public RefactoringData getRefactoringOne() {
		return refactoringOne;
	}
	public RefactoringData getRefactoringTwo() {
		return refactoringTwo;
	}
	
	public String getSimilarBlock() {
		return similarBlock;
	}

	public ArrayList<RefactoringData> getRefactorings(){
		ArrayList<RefactoringData> list = new ArrayList<RefactoringData>();
		list.add(refactoringOne);
		list.add(refactoringTwo);
		return list;
	}
}
