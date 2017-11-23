package ca.concordia.refactoringmatcher;

import java.util.List;

public interface RefactoringPatternFinder {

	List<RefactoringPair> getSimilarRefactoringPairs();
	
	List<RefactoringSet> getSimilarRefactorings();
}
