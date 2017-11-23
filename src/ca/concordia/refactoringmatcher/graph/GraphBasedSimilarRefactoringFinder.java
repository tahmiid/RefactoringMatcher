package ca.concordia.refactoringmatcher.graph;

import java.util.List;

import ca.concordia.refactoringmatcher.RefactoringData;
import ca.concordia.refactoringmatcher.RefactoringPair;
import ca.concordia.refactoringmatcher.SimilarRefactoringFinder;

public class GraphBasedSimilarRefactoringFinder implements SimilarRefactoringFinder {

	@Override
	public List<RefactoringPair> getSimilarRefactoringPairs(List<RefactoringData> refactorings) {
//		for (RefactoringData refactoringData : project.getRefactorings()) {
//			Repository repo = project.getRepository();
//
//			MethodDeclaration methodDeclaration = refactoringData.getRefactoredCode()
//					.getMethodDeclaration(gitService, repo);
//			if (methodDeclaration != null) {
//				System.out.println(methodDeclaration.toString());
//
//				MethodObject methodObject = createMethodObject(methodDeclaration);
//				PDG pdg = getPDG(methodObject);
//				pdgs.add(pdg);
//			}
//		}
		return null;
	}
}
