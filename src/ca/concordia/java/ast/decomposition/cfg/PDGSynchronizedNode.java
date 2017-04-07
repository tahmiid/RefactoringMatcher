package ca.concordia.java.ast.decomposition.cfg;

import java.util.Set;

import ca.concordia.java.ast.FieldObject;
import ca.concordia.java.ast.VariableDeclarationObject;

public class PDGSynchronizedNode extends PDGBlockNode {
	public PDGSynchronizedNode(CFGSynchronizedNode cfgSynchronizedNode, Set<VariableDeclarationObject> variableDeclarationsInMethod/*,
			Set<FieldObject> fieldsAccessedInMethod*/) {
		super(cfgSynchronizedNode, variableDeclarationsInMethod/*, fieldsAccessedInMethod*/);
		this.controlParent = cfgSynchronizedNode.getControlParent();
		determineDefinedAndUsedVariables();
	}
}