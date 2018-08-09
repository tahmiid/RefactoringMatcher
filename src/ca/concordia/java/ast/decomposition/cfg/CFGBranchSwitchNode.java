package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBranchSwitchNode extends CFGBranchConditionalNode  implements Serializable{

	public CFGBranchSwitchNode(AbstractStatement statement) {
		super(statement);
	}
}
