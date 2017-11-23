package ca.concordia.refactoringmatcher;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ca.concordia.refactoringmatcher.graph.GraphBasedSimilarRefactoringFinder;

public class GraphVsSourcererCCExperiment {

	@Test
	public void test() {
		try {
			Path outputDirectory = Files.createDirectories(Paths.get("output"));
			Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
			ExtendedGitService gitService = new ExtendedGitServiceImpl();

			ArrayList<GithubProject> projects = new ArrayList<GithubProject>();

			for (String projectLink : projectLinks) {
				GithubProject project;
				try {
					project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);
					project.printReport();
					projects.add(project);

					SimilarRefactoringFinder tbsrf = new TokenBasedSimilarRefactoringFinder();
					SimilarRefactoringFinder gbsrf = new GraphBasedSimilarRefactoringFinder();

					List<RefactoringPair> similarRefactoringPairsBySCC = tbsrf.getSimilarRefactoringPairs(project.getRefactorings());
//					List<RefactoringPair> similarRefactoringPairsByGraphMatching = gbsrf.getSimilarRefactoringPairs(project.getRefactorings());

				} catch (Exception e) {
					e.printStackTrace();
					fail("Exception Thrown");
				}
			}
			assertEquals(true, true);
		} catch (

		IOException e) {
			e.printStackTrace();
			fail("Exception Thrown");
		}
	}

	String[] projectLinks = {
			"https://github.com/jfree/jfreechart.git",
			// "https://github.com/apache/commons-lang.git",
			// "https://github.com/google/guava.git"
	};

}
