package ca.concordia.refactoringmatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.util.GitServiceImpl;

public class RefactoringMatcher {
	public static void main(String[] args) throws Exception {
//		String projectLink = "https://github.com/danilofes/refactoring-toy-example.git";
//		 String projectLink = "https://github.com/junit-team/junit5.git";
//		 String projectLink = "https://github.com/jfree/jfreechart.git";
		String projectLink = "https://github.com/romuloceccon/jedit";
//		 String projectLink = "https://github.com/apache/commons-lang";

		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		ExtendedGitService gitService = new ExtendedGitServiceImpl();

		GithubProject project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);

		List<RefactoringData> refactorings = project.getRefactorings();

		printReport(refactorings, project);

		RefactoringPatternFinder patternFinder = new TokenBasedRefactoringPatternFinder(refactorings, project.getOutputDirectory());

//		List<HashSet<RefactoringData>> similarRefactorings = patternFinder.getSimilarRefactorings();
		
		List<RefactoringPair> similarRefactoringPairs = patternFinder.getSimilarRefactoringPairs();

		printpairs(similarRefactoringPairs);
		
//		printGroups(similarRefactorings);

	}

	private static void printpairs(List<RefactoringPair> similarRefactoringPairs) {
		System.out.println("Refactorings Pairs: " + similarRefactoringPairs.size());
		System.out.println();
		for (RefactoringPair pair : similarRefactoringPairs) {
			System.out.println(pair.getRefactoringOne());
			System.out.println(pair.getRefactoringTwo());

			System.out.println();
		}
	}

	private static void printGroups(List<HashSet<RefactoringData>> similarRefactorings) {
		System.out.println("Similar Refactorings: " + similarRefactorings.size());
		System.out.println();
		for (HashSet<RefactoringData> set : similarRefactorings) {
			System.out.println("Group Size: " + set.size());
			for (RefactoringData refactoringData : set) {
				System.out.println(refactoringData.toString());
			}
			System.out.println();
		}
	}

	private static void printReport(List<RefactoringData> allRefactoringData, GithubProject project) {
		int inlineMethod = 0;
		int extractMethod = 0;
		int extractAndMoveMethod = 0;
		for (RefactoringData refactoringData : allRefactoringData) {
			if (refactoringData.getType() == RefactoringType.INLINE_OPERATION) {
				inlineMethod++;
			} else if (refactoringData.getType() == RefactoringType.EXTRACT_OPERATION) {
				extractMethod++;
			} else if (refactoringData.getType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
				extractAndMoveMethod++;
			}
		}

		System.out.println();
		System.out.println("Project: " + project.getName());
		System.out.println(project.getLink());
		System.out.println("Analyzed Commits: " + project.getCommitCount());
		System.out.println("Refactorings Found: " + allRefactoringData.size());
		System.out.println("Inlined Method: " + inlineMethod);
		System.out.println("Extract Method: " + extractMethod);
		System.out.println("Extract and Move Method: " + extractAndMoveMethod);
	}
}
