package ca.concordia.java.ast.decomposition.cfg;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBlockNode extends CFGNode {

	private CFGNode controlParent;

	public CFGBlockNode(AbstractStatement statement) {
		super(statement);
	}

	public CFGNode getControlParent() {
		return controlParent;
	}

	public void setControlParent(CFGNode controlParent) {
		this.controlParent = controlParent;
	}

}