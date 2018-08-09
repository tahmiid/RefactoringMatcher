package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import org.eclipse.jdt.core.dom.BreakStatement;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBreakNode extends CFGNode  implements Serializable{
	private String label;
	private CFGNode innerMostLoopNode;
	
	public CFGBreakNode(AbstractStatement statement) {
		super(statement);
		BreakStatement breakStatement = (BreakStatement)statement.getStatement();
		if(breakStatement.getLabel() != null)
			label = breakStatement.getLabel().getIdentifier();
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
