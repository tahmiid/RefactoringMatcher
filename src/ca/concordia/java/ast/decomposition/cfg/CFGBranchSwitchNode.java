package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGBranchSwitchNode extends CFGBranchConditionalNode  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6216893628423008740L;

	public CFGBranchSwitchNode(AbstractStatement statement) {
		super(statement);
	}
}
