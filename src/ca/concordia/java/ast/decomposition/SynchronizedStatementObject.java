package ca.concordia.java.ast.decomposition;

import java.io.Serializable;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

import ca.concordia.java.ast.ParameterObject;

public class SynchronizedStatementObject extends CompositeStatementObject  implements Serializable{

	public SynchronizedStatementObject(Statement statement, List<ParameterObject> parameters, AbstractMethodFragment parent) {
		super(statement, parameters, StatementType.SYNCHRONIZED, parent);
		AbstractExpression abstractExpression = new AbstractExpression(
				((SynchronizedStatement)statement).getExpression(), parameters, this);
		this.addExpression(abstractExpression);
	}

}
