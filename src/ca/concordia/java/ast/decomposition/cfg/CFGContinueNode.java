package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import org.eclipse.jdt.core.dom.ContinueStatement;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGContinueNode extends CFGNode  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9214233871066669750L;
	private String label;
	private CFGNode innerMostLoopNode;
	
	public CFGContinueNode(AbstractStatement statement) {
		super(statement);
		ContinueStatement continueStatement = (ContinueStatement)statement.getStatement();
		if(continueStatement.getLabel() != null)
			label = continueStatement.getLabel().getIdentifier();
	}

	public String getLabel() {
		return label;
	}

	public boolean isLabeled() {
		return label != null;
	}

	public CFGNode getInnerMostLoopNode() {
		return innerMostLoopNode;
	}

	public void setInnerMostLoopNode(CFGNode innerMostLoopNode) {
		this.innerMostLoopNode = innerMostLoopNode;
	}
}
