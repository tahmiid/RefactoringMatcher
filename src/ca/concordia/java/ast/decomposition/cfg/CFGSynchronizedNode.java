package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGSynchronizedNode extends CFGBlockNode  implements Serializable{
	public CFGSynchronizedNode(AbstractStatement statement) {
		super(statement);
	}
}
