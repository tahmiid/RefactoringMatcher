package gr.uom.java.ast.decomposition.cfg;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class PlainVariable extends AbstractVariable {
	private volatile int hashCode = 0;
	
	public PlainVariable(VariableDeclaration variableName) {
		super(variableName);
	}

	public PlainVariable(String variableName, String variableType) {
		super(variableName, variableType);
	}

	public PlainVariable(String variableName) {
		super(variableName);
	}

	public boolean containsPlainVariable(PlainVariable variable) {
		return this.variableName.equals(variable.variableName);
	}

	public boolean startsWithVariable(AbstractVariable variable) {
		if(variable instanceof PlainVariable) {
			return this.variableName.equals( variable.variableName);
		}
		return false;
	}

	public PlainVariable getInitialVariable() {
		return this;
	}

	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o instanceof PlainVariable) {
			PlainVariable plain = (PlainVariable)o;
			return this.hashCode == plain.hashCode;
		}
		return false;
	}

	public int hashCode() {
		if(hashCode == 0) {
			int result = 17;
			result = 31*result + variableName.hashCode();
			hashCode = result;
		}
		return hashCode;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(variableName);
		return sb.toString();
	}
}
