package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGSynchronizedNode extends CFGBlockNode  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CFGSynchronizedNode(AbstractStatement statement) {
		super(statement);
	}
}
