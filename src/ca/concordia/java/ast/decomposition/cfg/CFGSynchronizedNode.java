package ca.concordia.java.ast.decomposition.cfg;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGSynchronizedNode extends CFGBlockNode {
	public CFGSynchronizedNode(AbstractStatement statement) {
		super(statement);
	}
}
