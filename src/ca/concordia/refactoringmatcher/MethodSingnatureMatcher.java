package ca.concordia.refactoringmatcher;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jgit.lib.Repository;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.PDG;


public class MethodSingnatureMatcher {

	static String[] projectLinks = { 
//			"https://github.com/alibaba/fastjson.git", 
			"https://github.com/jfree/jfreechart.git",
//			"https://github.com/apache/commons-lang.git", 
							};

	public static void main(String[] args) throws Exception {
		Path outputDirectory = Files.createDirectories(Paths.get("output"));
		Path projectsDirectory = Files.createDirectories(Paths.get("projects"));
		ExtendedGitService gitService = new ExtendedGitServiceImpl();

		ArrayList<Project> projects = new ArrayList<Project>();

		for (String projectLink : projectLinks) {
			Project project = new Project(projectLink, projectsDirectory, outputDirectory, gitService);
			project.printReport();
			projects.add(project);
			
			for (RefactoringData refactoringData : project.getRefactorings()) {
				Repository repo = project.getRepository();

				MethodDeclaration methodDeclaration = refactoringData.getAfterCode().getMethodDeclaration(gitService, repo);
				if (methodDeclaration != null) {
					System.out.println(methodDeclaration.toString());

					PDG pdg = getPDG(methodDeclaration);
					Release  release = project.getRelease(refactoringData.getAfterCode().getCommit().getId());
				}
			}
		}
	}
	
	public static PDG getPDG(MethodDeclaration methodDeclaration) {
		MethodObject methodObject = createMethodObject(methodDeclaration);
		CFG cfg = new CFG(methodObject);
		PDG pdg = new PDG(cfg);
		System.out.println(pdg.getNodes().size());
		System.out.println("Done");	
		return pdg;
	}

	private static MethodObject createMethodObject(MethodDeclaration methodDeclaration) {
		final ConstructorObject constructorObject = new ConstructorObject(methodDeclaration);
		MethodObject methodObject = new MethodObject(constructorObject);
		return methodObject;
	}

}
