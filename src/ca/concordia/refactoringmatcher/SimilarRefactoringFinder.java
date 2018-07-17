package ca.concordia.refactoringmatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;

public interface SimilarRefactoringFinder {
	List<RefactoringPair> getSimilarRefactoringPairs(ArrayList<Pair<RefactoringData, Repository>> refactorings, ExtendedGitService gitService) throws IOException, Exception;
}
