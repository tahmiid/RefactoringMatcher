package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

import org.eclipse.jdt.core.dom.SwitchCase;

import ca.concordia.java.ast.decomposition.AbstractStatement;

public class CFGSwitchCaseNode extends CFGNode  implements Serializable{
	private boolean isDefault;
	
	public CFGSwitchCaseNode(AbstractStatement statement) {
		super(statement);
		SwitchCase switchCase = (SwitchCase)statement.getStatement();
		if(switchCase.isDefault())
			isDefault = true;
		else
			isDefault = false;
	}

	public boolean isDefault() {
		return isDefault;
	}
}
