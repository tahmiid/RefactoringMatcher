package ca.concordia.refactoringmatcher;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthStyle;

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
					GraphBasedSimilarRefactoringFinder gbsrf = new GraphBasedSimilarRefactoringFinder();

					List<RefactoringPair> similarRefactoringPairsBySCC = tbsrf.getSimilarRefactoringPairs(project.getRefactorings());
					writeToFile(similarRefactoringPairsBySCC);
					similarRefactoringPairsBySCC = loadSerializedRefactoringPairs();
					
					List<RefactoringPair> similarRefactoringPairsByGraphMatching = gbsrf.getSimilarRefactoringPairs(project.getRefactorings(), project.getRepository(), gitService);

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
	
	private List<RefactoringPair> loadSerializedRefactoringPairs() throws Exception {
		
		List<RefactoringPair> refactoringPairs = new ArrayList<RefactoringPair>();
		
		String outputPathString = "refactoringpairs";
		if (Files.exists(Paths.get(outputPathString))) {
			FileInputStream fis = new FileInputStream(outputPathString);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactoringPairs = (List<RefactoringPair>) ois.readObject();
			} catch (Exception e) {
				System.out.println(e);
			}
			ois.close();
		} else {
		}
		return refactoringPairs;
	}
	
	private void writeToFile(List<RefactoringPair> refactoringPairs) throws IOException {
		Files.deleteIfExists(Paths.get("refactoringpairs"));
		FileOutputStream fos = new FileOutputStream("refactoringpairs");
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactoringPairs);
			oos.close();
		} catch (IOException e) {
			System.out.println("Failed to serialize");
			System.out.println(e);
		}

	}

	String[] projectLinks = {
			"https://github.com/jfree/jfreechart.git",
			// "https://github.com/apache/commons-lang.git",
			// "https://github.com/google/guava.git"
	};

}
