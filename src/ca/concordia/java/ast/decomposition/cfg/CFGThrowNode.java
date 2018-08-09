package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGThrowNode extends CFGNode  implements Serializable{

	public CFGThrowNode(AbstractStatement statement) {
		super(statement);
	}

}
