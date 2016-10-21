package ca.concordia.refactoringmatcher;

import gr.uom.java.xmi.decomposition.ASTInformation;
import org.refactoringminer.api.Refactoring;

public abstract class AppliedRefactoring implements Refactoring {
	
	private ASTInformation from;
	private ASTInformation to;
	
	public ASTInformation getFrom() {
		return from;
	}
	public void setFrom(ASTInformation from) {
		this.from = from;
	}
	public ASTInformation getTo() {
		return to;
	}
	public void setTo(ASTInformation to) {
		this.to = to;
	}

}
