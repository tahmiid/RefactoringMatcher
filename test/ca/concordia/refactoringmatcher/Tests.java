package ca.concordia.refactoringmatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import ca.concordia.refactoringmatcher.graph.GraphPair;

public class Tests {
	
	public static void main(String[] args) {
		try {
			refactoringMinerTest();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	@Test
	public static void refactoringMinerTest() throws Exception {
		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		ExtendedGitService gitService = new ExtendedGitServiceImpl();

		ArrayList<GitProject> projects = new ArrayList<GitProject>();

		for (String projectLink : projectLinks) {
			GitProject project;
			try {
				project = new GitProject(projectLink, projectsDirectory, outputDirectory);
				projects.add(project);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception Thrown");
			}
		}
	}

	@Test
	public void createProjects() {
		try {
			Path outputDirectory = Files.createDirectories(Paths.get("E:\\SerializedProjects"));
			Path projectsDirectory = Files.createDirectories(Paths.get("E:\\ProjectDataset"));
			ExtendedGitService gitService = new ExtendedGitServiceImpl();

			ArrayList<GithubProject> projects = new ArrayList<GithubProject>();

			for (String projectLink : projectLinks) {
				GithubProject project;
				try {
					project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);
					project.printReport();
					projects.add(project);
				} catch (Exception e) {
					e.printStackTrace();
					fail("Exception Thrown");
				}
			}
			assertEquals(true, true);

		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception Thrown");
		}

	}

	@Test
	public void createPDG() {
		try {
			Path outputDirectory = Files.createDirectories(Paths.get("output"));
			Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
			ExtendedGitService gitService = new ExtendedGitServiceImpl();

			ArrayList<GithubProject> projects = new ArrayList<GithubProject>();
			ArrayList<PDG> pdgs = new ArrayList<PDG>();
			ArrayList<GraphPair> isomorphs = new ArrayList<GraphPair>();

			for (String projectLink : projectLinks) {
				GithubProject project;
				try {
					project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);
					project.printReport();
					projects.add(project);

					for (RefactoringData refactoringData : project.getRefactorings()) {
						Repository repo = project.getRepository();

						MethodDeclaration methodDeclaration = refactoringData.getRefactoredCode()
								.getMethodDeclaration(gitService, repo);
						if (methodDeclaration != null) {
							System.out.println(methodDeclaration.toString());

							MethodObject methodObject = createMethodObject(methodDeclaration);
							PDG pdg = getPDG(methodObject);
							pdgs.add(pdg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					fail("Exception Thrown");
				}
			}		
			
			for (PDG pdg1 : pdgs) {
				for (PDG pdg2 : pdgs) {
					if(pdg1.equals(pdg2))
						continue;
					
					try {
						GraphPair pair = new GraphPair(pdg1, pdg2);
						if(pair.areIsomorph())
						{
							isomorphs.add(pair);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			assertEquals(true, true);

		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception Thrown");
		}
	}

	@Test
	public void pdgTester() throws IOException {
		String wholeText = readFile("../RefactoringMatcher/src/ca/concordia/refactoringmatcher/TestClass.java",
				StandardCharsets.UTF_8);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(wholeText.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

		compilationUnit.accept(new ASTVisitor() {

			public boolean visit(MethodDeclaration methodDeclaration) {
				System.out.println(methodDeclaration.toString());
				MethodObject methodObject = createMethodObject(methodDeclaration);
				CFG cfg = new CFG(methodObject);
				PDG pdg = new PDG(cfg);
				System.out.println();
				return false;
			}
		});

	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private PDG getPDG(MethodObject methodObject) {
		CFG cfg = new CFG(methodObject);
		PDG pdg = new PDG(cfg);
		System.out.println(pdg.getNodes().size());
		System.out.println("Done");

		return pdg;
	}

	private MethodObject createMethodObject(MethodDeclaration methodDeclaration) {
		final ConstructorObject constructorObject = new ConstructorObject(methodDeclaration);
		MethodObject methodObject = new MethodObject(constructorObject);
		return methodObject;
	}

	static String[] projectLinks = {

			 "https://github.com/iluwatar/java-design-patterns.git",
			// "https://github.com/JakeWharton/ActionBarSherlock.git",
			// "https://github.com/alibaba/dubbo.git",
			// "https://github.com/chrisbanes/Android-PullToRefresh.git",
			// "https://github.com/alibaba/fastjson.git",
			// "https://github.com/jfeinstein10/SlidingMenu.git",
			// "https://github.com/JakeWharton/ViewPagerIndicator.git",
			// "https://github.com/square/retrofit.git",
			// "https://github.com/square/okhttp.git",
			// "https://github.com/zxing/zxing.git",
//			 "https://github.com/google/guava.git",
			//
//			"https://github.com/tsantalis/RefactoringMiner",
//			"https://github.com/danilofes/refactoring-toy-example",
			// "https://github.com/elastic/elasticsearch.git",
			// "https://github.com/google/google-java-format.git",
			// "https://github.com/google/ExoPlayer.git",
			// "https://github.com/romuloceccon/jedit.git",
//			 "https://github.com/jfree/jfreechart.git",
//			 "https://github.com/apache/commons-lang.git",
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
//			 "https://github.com/tahmiid/RepositorySearch"
	};
}
