package ca.concordia.refactoringmatcher;

import java.util.ArrayList;

public class RefactoringPair {
	
	public RefactoringPair(RefactoringData left, RefactoringData right) {
		this.refactoringOne = left;
		this.refactoringTwo = right;
	}
	
	public RefactoringData getRefactoringOne() {
		return refactoringOne;
	}
	public RefactoringData getRefactoringTwo() {
		return refactoringTwo;
	}
	
	public ArrayList<RefactoringData> getRefactorings(){
		ArrayList<RefactoringData> list = new ArrayList<RefactoringData>();
		list.add(refactoringOne);
		list.add(refactoringTwo);
		return list;
	}

	private RefactoringData refactoringOne;
	private RefactoringData refactoringTwo;
}
