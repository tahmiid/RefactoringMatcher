package ca.concordia.refactoringdata;

import java.io.Serializable;

import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.Refactoring;

public interface IRefactoringData extends Serializable {	
	
	public Refactoring getRefactoring();

	public String getCommitId();

	public String getProjectLink();
	
	public void prepareForSerialization();

	public void recoverAfterDeserialization();

	public void retrieveCode(Repository repository) throws Exception;
}
