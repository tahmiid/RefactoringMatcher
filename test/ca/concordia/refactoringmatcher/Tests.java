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
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.api.RefactoringType;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.Groum;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import gr.uom.java.xmi.LocationInfo;
import gr.uom.java.xmi.diff.ExtractAndMoveOperationRefactoring;
import gr.uom.java.xmi.diff.ExtractOperationRefactoring;
import gr.uom.java.xmi.diff.InlineOperationRefactoring;

public class Tests {

	@Test
	public void refactoringMinerTest() throws Exception {
		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		ExtendedGitService gitService = new ExtendedGitServiceImpl();

		ArrayList<GithubProject> projects = new ArrayList<GithubProject>();

		for (String projectLink : projectLinks) {
			GithubProject project;
			try {
				project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);
				projects.add(project);
			} catch (Exception e) {
				e.printStackTrace();
				fail("Exception Thrown");
			}
		}
		for (GithubProject project : projects) {
			List<RefactoringData> allRefactoringData = new ArrayList<RefactoringData>();

			GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
			miner.detectAll(project.getRepository(), "master", new RefactoringHandler() {
				@Override
				public void handle(String commitId, List<Refactoring> refactorings) {
					for (Refactoring ref : refactorings) {
						LocationInfo astBeforeChange;
						LocationInfo astAfterChange;
						if (ref.getRefactoringType() == RefactoringType.INLINE_OPERATION) {
							astBeforeChange = ((InlineOperationRefactoring) ref).getInlinedOperation()
									.getLocationInfo();
							astAfterChange = ((InlineOperationRefactoring) ref).getTargetOperationAfterInline()
									.getLocationInfo();
						} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
							astBeforeChange = ((ExtractOperationRefactoring) ref).getSourceOperationBeforeExtraction()
									.getLocationInfo();
							astAfterChange = ((ExtractOperationRefactoring) ref).getExtractedOperation()
									.getLocationInfo();
						} else if (ref.getRefactoringType() == RefactoringType.EXTRACT_AND_MOVE_OPERATION) {
							astBeforeChange = ((ExtractAndMoveOperationRefactoring) ref)
									.getSourceOperationBeforeExtraction().getLocationInfo();
							astAfterChange = ((ExtractAndMoveOperationRefactoring) ref).getExtractedOperation()
									.getLocationInfo();
						} else {
							continue;
						}

						Code beforeCode = null;
						Code afterCode = null;
						try {
							Commit commit = new Commit(commitId);
							beforeCode = new Code(commit, project.getDirectory(), astBeforeChange, gitService,
									project.getRepository());
							commit = new Commit(commitId);
							afterCode = new Code(commit, project.getDirectory(), astAfterChange, gitService,
									project.getRepository());
						} catch (Exception e) {
							continue;
						}

						RefactoringData refactoringData = new RefactoringData(ref, afterCode, project.getName());

						boolean exists = false;
						for (RefactoringData existingRefactoring : allRefactoringData) {
							if (existingRefactoring.getRefactoredCode().equals(refactoringData.getRefactoredCode())) {
								exists = true;
								break;
							}
						}
						if (!exists)
							allRefactoringData.add(refactoringData);
					}
				}
			});
		}

	}

	@Test
	public void createProjects() {
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
						}
					}
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
	public void pdgTester() throws IOException {
		String wholeText = readFile("/RefactoringMatcher/src/ca/concordia/refactoringmatcher/TestClass.java",
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
				Groum groum = new Groum(pdg);
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
			"https://github.com/danilofes/refactoring-toy-example.git",
			// "https://github.com/elastic/elasticsearch.git",
			// "https://github.com/google/google-java-format.git",
			// "https://github.com/google/ExoPlayer.git",
			// "https://github.com/romuloceccon/jedit.git",
			// "https://github.com/jfree/jfreechart.git",
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
