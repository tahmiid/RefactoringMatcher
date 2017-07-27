package ca.concordia.refactoringmatcher;

import java.util.Set;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitService;

public interface ExtendedGitService extends GitService{
	
	public String getNextTag(Repository repository, String commitId) throws Exception;
	
	public Set<Ref> getAllReleaseTags(Repository repository) throws Exception;
}
