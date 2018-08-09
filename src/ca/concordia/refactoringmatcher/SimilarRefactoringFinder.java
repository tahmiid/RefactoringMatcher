package ca.concordia.refactoringmatcher;

import java.util.List;

import ca.concordia.refactoringdata.IRefactoringData;

public interface SimilarRefactoringFinder {
	List<RefactoringPair> getSimilarRefactoringPairs(List<IRefactoringData> refactorings);
}
