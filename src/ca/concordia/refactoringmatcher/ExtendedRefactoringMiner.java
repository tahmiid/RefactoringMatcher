package ca.concordia.refactoringmatcher;

import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.RefactoringHandler;

public interface ExtendedRefactoringMiner extends GitHistoryRefactoringMiner{

	public void detectAllWithTimeOut(Repository repository, String branch, RefactoringHandler handler) throws Exception;
}
