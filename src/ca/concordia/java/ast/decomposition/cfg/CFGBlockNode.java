package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBlockNode extends CFGNode  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1870756685073196928L;
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