package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;
import java.util.Set;

import ca.concordia.java.ast.FieldObject;
import ca.concordia.java.ast.VariableDeclarationObject;

public class PDGExitNode extends PDGStatementNode  implements Serializable{
	private AbstractVariable returnedVariable;
	
	public PDGExitNode(CFGNode cfgNode, Set<VariableDeclarationObject> variableDeclarationsInMethod/*,
			Set<FieldObject> fieldsAccessedInMethod*/) {
		super(cfgNode, variableDeclarationsInMethod/*, fieldsAccessedInMethod*/);
		if(cfgNode instanceof CFGExitNode) {
			CFGExitNode exitNode = (CFGExitNode)cfgNode;
			returnedVariable = exitNode.getReturnedVariable();
		}
	}

	public AbstractVariable getReturnedVariable() {
		return returnedVariable;
	}
}
