package ca.concordia.refactoringmatcher;

import gr.uom.java.xmi.decomposition.ASTInformation;
import org.refactoringminer.api.Refactoring;

public class CodeChange {
	
	private Refactoring refactoring;
	private ASTInformation from;
	private ASTInformation to;
	
	
	public CodeChange(Refactoring ref, ASTInformation from, ASTInformation to) {
		this.refactoring = ref;
		this.from = from;
		this.to = to;
	}
	public Refactoring getRefactoring() {
		return refactoring;
	}
	public void setRefactoring(Refactoring refactoring) {
		this.refactoring = refactoring;
	}
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
