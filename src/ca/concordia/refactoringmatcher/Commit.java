package ca.concordia.refactoringmatcher;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jgit.lib.PersonIdent;

public class Commit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Commit(String name) throws ParseException {
		this.id = name;
//		this.message = fullMessage;
//		this.time = new Date( commitTime * 1000L);
//		this.commiter = committerIdent;
	}
	
	public String getId() {
		return id;
	}
//	public String getMessage() {
//		return message;
//	}
//	public Date getTime() {
//		return time;
//	}
//	public PersonIdent getCommiter() {
//		return commiter;
//	}
	
	public boolean equals(Commit commit)
	{
		return id.equals(commit.getId()) ? true : false;
	}



	private String id;
//	private String message;
//	private Date time;
//	private PersonIdent commiter;

}
