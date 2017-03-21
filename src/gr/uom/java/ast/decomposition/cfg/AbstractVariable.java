package gr.uom.java.ast.decomposition.cfg;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public abstract class AbstractVariable {
	protected String variableName;
	protected String variableType;
	
	public AbstractVariable(VariableDeclaration name) {
		this.variableName = name.getName().getFullyQualifiedName();
		this.variableType = name.getInitializer() != null ? name.getInitializer().toString() : null;
	}

	public AbstractVariable(String variableName, String variableType) {
		this.variableName = variableName;
		this.variableType = variableType;
	}
	
	public AbstractVariable(String variableName) {
		this.variableName = variableName;
	}


	public String getVariableName() {
		return variableName;
	}

	public String getVariableType() {
		return variableType;
	}

	public abstract boolean containsPlainVariable(PlainVariable variable);
	public abstract boolean startsWithVariable(AbstractVariable variable);
	public abstract PlainVariable getInitialVariable();
}
