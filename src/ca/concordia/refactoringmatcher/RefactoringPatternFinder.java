package ca.concordia.refactoringmatcher;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public interface RefactoringPatternFinder {

	List<RefactoringPair> getSimilarRefactoringPairs();
	
	List<HashSet<RefactoringData>> getSimilarRefactorings();
}
