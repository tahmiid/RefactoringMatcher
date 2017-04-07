package ca.concordia.java.ast.decomposition.cfg;

import java.util.Set;

import ca.concordia.java.ast.VariableDeclarationObject;
import ca.concordia.java.ast.decomposition.StatementObject;

public class PDGStatementNode extends PDGNode {
	
	public PDGStatementNode(CFGNode cfgNode, Set<VariableDeclarationObject> variableDeclarationsInMethod) {
		super(cfgNode, variableDeclarationsInMethod);
		determineDefinedAndUsedVariables();
	}

	private void determineDefinedAndUsedVariables() {
		CFGNode cfgNode = getCFGNode();
		if(cfgNode.getStatement() instanceof StatementObject) {
			StatementObject statement = (StatementObject)cfgNode.getStatement();
			thrownExceptionTypes.addAll(statement.getExceptionsInThrowStatements());

			for(PlainVariable variable : statement.getDeclaredLocalVariables()) {
				declaredVariables.add(variable);
				definedVariables.add(variable);
			}
			for(PlainVariable variable : statement.getDefinedLocalVariables()) {
				definedVariables.add(variable);
			}
			for(PlainVariable variable : statement.getUsedLocalVariables()) {
				usedVariables.add(variable);
			}
		}
	}
}
