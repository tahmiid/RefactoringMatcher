package ca.concordia.refactoringmatcher;

import java.io.Serializable;
import java.util.ArrayList;

import ca.concordia.refactoringdata.IRefactoringData;

public class RefactoringPair implements Serializable{
	private IRefactoringData refactoringOne;
	private IRefactoringData refactoringTwo;
	
	public RefactoringPair(IRefactoringData left, IRefactoringData right) {
		this.refactoringOne = left;
		this.refactoringTwo = right;
	}
	
	public IRefactoringData getRefactoringOne() {
		return refactoringOne;
	}
	public IRefactoringData getRefactoringTwo() {
		return refactoringTwo;
	}
	
	public ArrayList<IRefactoringData> getRefactorings(){
		ArrayList<IRefactoringData> list = new ArrayList<IRefactoringData>();
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
		return "Match:\n" + refactoringOne + "\n" + refactoringTwo;
	}
	
	public boolean fromSameProject(){
		if(refactoringOne.getProjectLink().toLowerCase().equals(refactoringTwo.getProjectLink().toLowerCase()))
			return true;
		else
			return false;
	}
}
