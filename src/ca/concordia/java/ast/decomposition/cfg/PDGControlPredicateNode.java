package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import ca.concordia.java.ast.VariableDeclarationObject;
import ca.concordia.java.ast.decomposition.AbstractExpression;
import ca.concordia.java.ast.decomposition.CompositeStatementObject;

public class PDGControlPredicateNode extends PDGNode implements Serializable {
	
	public PDGControlPredicateNode(CFGNode cfgNode, Set<VariableDeclarationObject> variableDeclarationsInMethod) {
		super(cfgNode, variableDeclarationsInMethod);
		determineDefinedAndUsedVariables();
	}

	private void determineDefinedAndUsedVariables() {
		CFGNode cfgNode = getCFGNode();
		if(cfgNode.getStatement() instanceof CompositeStatementObject) {
			CompositeStatementObject composite = (CompositeStatementObject)cfgNode.getStatement();
			List<AbstractExpression> expressions = composite.getExpressions();
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
