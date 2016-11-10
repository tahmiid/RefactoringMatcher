package ca.concordia.refactoringmatcher;

import java.util.HashSet;
import java.util.List;

public interface RefactoringPatternFinder {

	List<HashSet<RefactoringData>> findSimilarRefactorings(List<RefactoringData> refactorings);
}
