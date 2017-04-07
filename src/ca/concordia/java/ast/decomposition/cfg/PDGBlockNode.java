package ca.concordia.java.ast.decomposition.cfg;

import java.util.List;
import java.util.Set;

import ca.concordia.java.ast.VariableDeclarationObject;
import ca.concordia.java.ast.decomposition.AbstractExpression;
import ca.concordia.java.ast.decomposition.CompositeStatementObject;

public class PDGBlockNode extends PDGNode {
	protected CFGNode controlParent;

	public PDGBlockNode(CFGNode cfgNode, Set<VariableDeclarationObject> variableDeclarationsInMethod/*,
			Set<FieldObject> fieldsAccessedInMethod*/) {
		super(cfgNode, variableDeclarationsInMethod/*, fieldsAccessedInMethod*/);
	}

	public PDGNode getControlDependenceParent() {
		if(controlParent != null) {
			if(controlParent.getPDGNode() != null)
				return controlParent.getPDGNode();
		}
		return super.getControlDependenceParent();
	}

	protected void determineDefinedAndUsedVariables() {
		CFGNode cfgNode = getCFGNode();
		if(cfgNode.getStatement() instanceof CompositeStatementObject) {
			CompositeStatementObject compositeStatement = (CompositeStatementObject)cfgNode.getStatement();
			List<AbstractExpression> expressions = compositeStatement.getExpressions();
			for(AbstractExpression expression : expressions) {
				for(PlainVariable variable : expression.getDeclaredLocalVariables()) {
					declaredVariables.add(variable);
					definedVariables.add(variable);
				}
				for(PlainVariable variable : expression.getDefinedLocalVariables()) {
					definedVariables.add(variable);
				}
				for(PlainVariable variable : expression.getUsedLocalVariables()) {
					usedVariables.add(variable);
				}
			}
		}
	}
}