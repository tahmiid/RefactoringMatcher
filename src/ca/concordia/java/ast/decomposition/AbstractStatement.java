package ca.concordia.java.ast.decomposition;

import java.io.Serializable;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;

import ca.concordia.java.ast.ASTInformation;
import ca.concordia.java.ast.ASTInformationGenerator;
import ca.concordia.java.ast.ParameterObject;

public abstract class AbstractStatement extends AbstractMethodFragment implements Serializable{

	private ASTInformation statement;
	private StatementType type;
	
    public AbstractStatement(Statement statement, List<ParameterObject> parameters, StatementType type, AbstractMethodFragment parent) {
    	super(parent, parameters);
    	this.type = type;
    	this.statement = ASTInformationGenerator.generateASTInformation(statement);
    }

    public Statement getStatement() {
    	return (Statement)this.statement.recoverASTNode();
    }

	public StatementType getType() {
		return type;
	}

	public int getNestingDepth() {
		AbstractStatement parent = (AbstractStatement) this.getParent();
		int depth = 0;
		while (parent != null) {
			if (!parent.getType().equals(StatementType.BLOCK)) {
				depth++;
			}
			parent = (AbstractStatement) parent.getParent();
		}
		return depth;
	}

	public abstract List<String> stringRepresentation();
}
