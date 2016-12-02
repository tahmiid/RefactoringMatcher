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
	
	private String similarCode;
	private Long firstRefactoringDate;
	private Long lastRefactoringDate;
	private Long duration;
	private boolean sameCommit;
	private boolean sameDay;
	private boolean sameDeveloper;
	
	public String getSimilarCode()
	{
		return similarCode;
	}
	
	public void setSimilarCode(String similarCode)
	{
		this.similarCode = similarCode;
	}
	
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
		sameDeveloper = true;	
		if(super.size() == 0)
		{
			return;
		}
		Commit commit = new ArrayList<RefactoringData>(this).get(0).getCommit();
		
		for (RefactoringData refactoring : this) {
			if(sameDeveloper)
			{
				if(!commit.getCommiter().getEmailAddress().equals(refactoring.getCommit().getCommiter().getEmailAddress()))
				{
					sameDeveloper = false;
				}
			}
			
			if(sameCommit)
			{
				if(!commit.equals(refactoring.getCommit()))
				{
					sameCommit = false;
				}
			}
			
			long time = refactoring.getCommitTime().getTime();
			if(time >= lastRefactoringDate)
				lastRefactoringDate = time;
			if(time <= firstRefactoringDate)
				firstRefactoringDate = time;
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

	public boolean isSameCommit() {
		return sameCommit;
	}

	public boolean isSameDay() {
		return sameDay;
	}

	public boolean isSameDeveloper() {
		return sameDeveloper;
	}	
}
