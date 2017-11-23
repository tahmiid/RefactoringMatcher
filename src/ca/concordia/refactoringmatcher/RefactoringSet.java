package ca.concordia.refactoringmatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.eclipse.jgit.lib.PersonIdent;

public class RefactoringSet extends HashSet<RefactoringData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long firstRefactoringDate;
	private Long lastRefactoringDate;
	private Long duration;
	private boolean sameCommit;
	private boolean sameDay;
//	private boolean sameDeveloper;
	private HashSet<Commit> commits;
	
	public boolean add(RefactoringData refactoringData) {
        
		if(super.add(refactoringData))
		{
			calculateProperties();
			return true;
		}
		else
		{	
			return false;
		}
    }
	
	public boolean remove(RefactoringData refactoringData) {
        
		if(super.remove(refactoringData))
		{
			calculateProperties();
			return true;
		}
		else
		{	
			return false;
		}
    }
	
    public boolean addAll(ArrayList<RefactoringData> refactoringData) {
		if(super.addAll(refactoringData))
		{
			calculateProperties();
			return true;
		}
		else
		{	
			return false;
		}
    }
    
    public void clear() {
    	super.clear();
    	calculateProperties();
    }
	

	private void calculateProperties() {
		lastRefactoringDate = 0L;
		firstRefactoringDate = new Date().getTime();
		sameCommit = true;
//		sameDeveloper = true;	
		commits = new HashSet<Commit>();
		
		if(super.size() == 0)
		{
			return;
		}
		Commit commit = new ArrayList<RefactoringData>(this).get(0).getCommit();
		
		for (RefactoringData refactoring : this) {
			commits.add(refactoring.getCommit());
//			if(sameDeveloper)
//			{
//				if(!commit.getCommiter().getEmailAddress().equals(refactoring.getCommit().getCommiter().getEmailAddress()))
//				{
//					sameDeveloper = false;
//				}
//			}
			
//			long time = refactoring.getCommitTime().getTime();
//			if(time >= lastRefactoringDate)
//				lastRefactoringDate = time;
//			if(time <= firstRefactoringDate)
//				firstRefactoringDate = time;
		}
		
		if(commits.size() > 1)
		{
			sameCommit = false;
		}
		duration = (lastRefactoringDate - firstRefactoringDate) / (1000*60*60*24);
		sameDay = duration==0 ? true : false;
	}

	public Long getFirstRefactoringDate() {
		return firstRefactoringDate;
	}

	public Long getLastRefactoringDate() {
		return lastRefactoringDate;
	}

	public Long getDuration() {
		return duration;
	}

	public boolean isSameProject()
	{
		HashSet<String> projectNames = new HashSet<String>();
		for (RefactoringData refactoringData : this) {
			projectNames.add(refactoringData.getProjectName());
		}
		
		if(projectNames.size()>1)
		{
			for (String string : projectNames) {

				System.out.println(string);
			}
		}
		
		return projectNames.size() == 1 ? true : false;
	}
	
	public HashSet<Commit> getCommits() {
		return commits;
	}

	public boolean isSameCommit() {
		return sameCommit;
	}

	public boolean isSameDay() {
		return sameDay;
	}

//	public boolean isSameDeveloper() {
//		return sameDeveloper;
//	}	
}
