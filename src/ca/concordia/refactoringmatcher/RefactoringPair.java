package ca.concordia.refactoringmatcher;

import java.io.Serializable;
import java.util.ArrayList;

public class RefactoringPair implements Serializable{
	private RefactoringData refactoringOne;
	private RefactoringData refactoringTwo;
	
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
	
	public int hashCode() {
        int result = 17;
        result = 37 * result + refactoringOne.hashCode();
        result = 37 * result + refactoringTwo.hashCode(); 
        return result;
    }
	
	public String toString(){
		return "Pair (" + refactoringOne + ", " + refactoringTwo + ")" ;
	}
	
}
