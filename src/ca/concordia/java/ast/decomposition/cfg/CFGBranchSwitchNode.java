package ca.concordia.java.ast.decomposition.cfg;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBranchSwitchNode extends CFGBranchConditionalNode {

	public CFGBranchSwitchNode(AbstractStatement statement) {
		super(statement);
	}
}
