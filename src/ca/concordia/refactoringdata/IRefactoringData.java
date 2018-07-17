package ca.concordia.refactoringdata;

import java.io.Serializable;
import org.refactoringminer.api.Refactoring;

public interface IRefactoringData extends Serializable {	
	
	public Refactoring getRefactoring();

	public String getCommitId();

	public String getProjectLink();
}
