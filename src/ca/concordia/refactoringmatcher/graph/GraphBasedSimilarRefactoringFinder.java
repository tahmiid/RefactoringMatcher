package ca.concordia.refactoringmatcher.graph;

import java.util.ArrayList;
import java.util.List;

import org.refactoringminer.api.RefactoringType;

import ca.concordia.refactoringdata.ExtractMethod;
import ca.concordia.refactoringdata.IRefactoringData;
import ca.concordia.refactoringmatcher.RefactoringPair;
import ca.concordia.refactoringmatcher.SimilarRefactoringFinder;

public class GraphBasedSimilarRefactoringFinder implements SimilarRefactoringFinder {

	public List<RefactoringPair> getSimilarRefactoringPairs(List<IRefactoringData> refactorings) {
		List<RefactoringPair> refactoringPairs = new ArrayList<RefactoringPair>();
		for (int i = 0; i < refactorings.size() - 1; i++) {
			for (int j = i + 1; j < refactorings.size(); j++) {
				if (refactorings.get(i).getRefactoring().getRefactoringType() == RefactoringType.EXTRACT_OPERATION
						&& refactorings.get(j).getRefactoring()
								.getRefactoringType() == RefactoringType.EXTRACT_OPERATION) {
					ExtractMethod em1 = (ExtractMethod) refactorings.get(i);
					ExtractMethod em2 = (ExtractMethod) refactorings.get(j);

					if (em1.isPartofSameRefactoring(em2))
						continue;
					
					if (em1.getExtractionSize() < 2)
						continue;

					try {
						GraphPair pair = new GraphPair(em1.getExtractedOperationPDG(), em2.getExtractedOperationPDG());
						if (pair.areIsomorph()) {
							RefactoringPair refPair = new RefactoringPair(em1, em2);
							refactoringPairs.add(refPair);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return refactoringPairs;
	}
}
