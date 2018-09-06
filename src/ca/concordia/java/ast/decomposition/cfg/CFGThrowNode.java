package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGThrowNode extends CFGNode  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3849137072173463024L;

	public CFGThrowNode(AbstractStatement statement) {
		super(statement);
	}

}
