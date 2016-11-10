package ca.concordia.refactoringmatcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import gr.uom.java.xmi.decomposition.ASTInformation;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;

public class RefactoringMatcher {
	public static void main(String[] args) throws Exception {
		//String projectLink = "https://github.com/danilofes/refactoring-toy-example.git";
		// String projectLink = "https://github.com/junit-team/junit5.git";
		// String projectLink = "https://github.com/jfree/jfreechart.git";
		String projectLink = "https://github.com/romuloceccon/jedit"; 
		// String projectLink = "https://github.com/apache/commons-lang";
		
		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		GitService gitService = new GitServiceImpl();
		
		Project project = new Project(projectLink, projectsDirectory, outputDirectory, gitService);
		
		List<RefactoringData> refactorings = project.getAllRefactorings(gitService);

		RefactoringPatternFinder patternFinder = new TokenBasedRefactoringPatternFinder(refactorings, outputDirectory);

		List<HashSet<RefactoringData>> similarRefactorings = patternFinder.findSimilarRefactorings(refactorings);
		
		printReport(refactorings, project, gitService);
	}

	private static void printReport(List<RefactoringData> allRefactoringData, Project project, GitService gitService) throws Exception {
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
		System.out.println("Project: " + project.name);
		System.out.println(project.link);
		System.out.println("Analyzed Commits: " + gitService.countCommits(project.repository, "master"));
		System.out.println("Refactorings Found: " + allRefactoringData.size());
		System.out.println("Inlined Method: " + inlineMethod);
		System.out.println("Extract Method: " + extractMethod);
		System.out.println("Extract and Move Method: " + extractAndMoveMethod);
	}
}
