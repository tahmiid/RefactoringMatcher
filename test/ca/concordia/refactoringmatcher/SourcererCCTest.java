package ca.concordia.refactoringmatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SourcererCCTest {

	@Test
	public void test() {
		try {
			Path outputDirectory = Files.createDirectories(Paths.get("output"));
			Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
			ExtendedGitService gitService = new ExtendedGitServiceImpl();

			ArrayList<GitProject> projects = new ArrayList<GitProject>();

			for (String projectLink : projectLinks) {
				GitProject project;
				try {
					project = new GitProject(projectLink, projectsDirectory, outputDirectory, gitService);
					project.printReport();
					projects.add(project);

					SimilarRefactoringFinder patternFinder = new TokenBasedSimilarRefactoringFinder();

//					List<RefactoringPair> similarRefactoringPairs = patternFinder
//							.getSimilarRefactoringPairs(project.getRefactorings());

//					for (RefactoringPair refactoringPair : similarRefactoringPairs) {
//						System.out.println(refactoringPair.toString());
//					}

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

			// "https://github.com/iluwatar/java-design-patterns.git",
			// "https://github.com/JakeWharton/ActionBarSherlock.git",
			// "https://github.com/alibaba/dubbo.git",
			// "https://github.com/chrisbanes/Android-PullToRefresh.git",
			// "https://github.com/alibaba/fastjson.git",
			// "https://github.com/jfeinstein10/SlidingMenu.git",
			// "https://github.com/JakeWharton/ViewPagerIndicator.git",
			// "https://github.com/square/retrofit.git",
			// "https://github.com/square/okhttp.git",
			// "https://github.com/zxing/zxing.git",
			// "https://github.com/google/guava.git",
			//
			// "https://github.com/tsantalis/RefactoringMiner",
			// "https://github.com/danilofes/refactoring-toy-example.git",
			// "https://github.com/elastic/elasticsearch.git",
			// "https://github.com/google/google-java-format.git",
			// "https://github.com/google/ExoPlayer.git",
			// "https://github.com/romuloceccon/jedit.git",
			"https://github.com/jfree/jfreechart.git",
			// "https://github.com/apache/commons-lang.git",
			// "https://github.com/apache/log4j",
			// "https://github.com/apache/nifi-minifi.git",
			// "https://github.com/apache/knox.git",
			// "https://github.com/apache/zeppelin.git",
			// "https://github.com/apache/cassandra.git",
			// "https://github.com/apache/storm.git",
			// "https://github.com/apache/hadoop.git",
			// "https://github.com/apache/kafka.git",
			// "https://github.com/apache/zookeeper.git",
			// "https://github.com/apache/camel.git",
			// "https://github.com/apache/hive.git",
			// "https://github.com/spring-projects/spring-framework.git",
			// "https://github.com/google/guava.git"
	};
}
