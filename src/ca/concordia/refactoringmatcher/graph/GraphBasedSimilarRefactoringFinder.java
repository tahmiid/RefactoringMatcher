package ca.concordia.refactoringmatcher.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jgit.lib.Repository;

import ca.concordia.java.ast.ConstructorObject;
import ca.concordia.java.ast.MethodObject;
import ca.concordia.java.ast.decomposition.cfg.CFG;
import ca.concordia.java.ast.decomposition.cfg.PDG;
import ca.concordia.refactoringmatcher.ExtendedGitService;
import ca.concordia.refactoringmatcher.RefactoringData;
import ca.concordia.refactoringmatcher.RefactoringPair;

public class GraphBasedSimilarRefactoringFinder {// implements
													// SimilarRefactoringFinder
													// {

	public List<RefactoringPair> getSimilarRefactoringPairs(List<RefactoringData> refactorings, Repository repository,
			ExtendedGitService gitService) throws IOException, Exception {
		List<RefactoringPair> refactoringPairs = new ArrayList<RefactoringPair>();
		List<PDG> pdgs = new ArrayList<PDG>();
		HashMap<PDG, RefactoringData> pdgToRefactoringMap = new HashMap<PDG, RefactoringData>();

		for (RefactoringData refactoringData : refactorings) {
			try {
				Repository repo = repository;

				MethodDeclaration methodDeclaration = refactoringData.getRefactoredCode()
						.getMethodDeclaration(gitService, repo);
				if (methodDeclaration != null) {
					System.out.println(methodDeclaration.toString());

					MethodObject methodObject = createMethodObject(methodDeclaration);
					PDG pdg = getPDG(methodObject);
					pdgs.add(pdg);
					pdgToRefactoringMap.put(pdg, refactoringData);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		for (

		PDG pdg1 : pdgs) {
			for (PDG pdg2 : pdgs) {
				if (pdg1.equals(pdg2))
					continue;

				try {
					GraphPair pair = new GraphPair(pdg1, pdg2);
					if (pair.areIsomorph()) {
						RefactoringPair refPair = new RefactoringPair(pdgToRefactoringMap.get(pair.getGraph1()),
								pdgToRefactoringMap.get(pair.getGraph2()));
						refactoringPairs.add(refPair);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return refactoringPairs;
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
}
