package ca.concordia.refactoringmatcher;

import java.util.List;

public interface SimilarRefactoringFinder {
	List<RefactoringPair> getSimilarRefactoringPairs(List<RefactoringData> refactorings);
}
