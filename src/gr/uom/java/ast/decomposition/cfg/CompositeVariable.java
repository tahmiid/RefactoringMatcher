package gr.uom.java.ast.decomposition.cfg;

import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class CompositeVariable extends AbstractVariable {
	private AbstractVariable rightPart;
	private volatile int hashCode = 0;
	
	public CompositeVariable(VariableDeclaration referenceName, AbstractVariable rightPart) {
		super(referenceName);
		this.rightPart = rightPart;
	}

	public CompositeVariable(AbstractVariable argument, AbstractVariable rightPart) {
		this(argument.getVariableName(), argument.getVariableType(), rightPart);
	}

	private CompositeVariable(String variableName, String variableType, AbstractVariable rightPart) {
		super(variableName, variableType);
		this.rightPart = rightPart;
	}

	//if composite variable is "one.two.three" then right part is "two.three"
	public AbstractVariable getRightPart() {
		return rightPart;
	}

	//if composite variable is "one.two.three" then left part is "one.two"
	public AbstractVariable getLeftPart() {
		if(rightPart instanceof PlainVariable) {
			return new PlainVariable(variableName, variableType);
		}
		else {
			CompositeVariable compositeVariable = (CompositeVariable)rightPart;
			return new CompositeVariable(variableName, variableType, compositeVariable.getLeftPart());
		}
	}

	//if composite variable is "one.two.three" then final variable is "three"
	public PlainVariable getFinalVariable() {
		if(rightPart instanceof PlainVariable) {
			return (PlainVariable)rightPart;
		}
		else {
			return ((CompositeVariable)rightPart).getFinalVariable();
		}
	}

	//if composite variable is "one.two.three" then initial variable is "one"
	public PlainVariable getInitialVariable() {
		return new PlainVariable(variableName, variableType);
	}

	public boolean containsPlainVariable(PlainVariable variable) {
		if(this.equals(variable))
			return true;
		return rightPart.containsPlainVariable(variable);
	}

	public boolean startsWithVariable(AbstractVariable variable) {
		if(variable instanceof PlainVariable) {
			return this.getInitialVariable().equals((PlainVariable)variable);
		}
		else {
			CompositeVariable composite = (CompositeVariable)variable;
			if(this.getInitialVariable().equals(composite.getInitialVariable())) {
				return this.getRightPart().startsWithVariable(composite.getRightPart());
			}
			return false;
		}
	}

	public AbstractVariable getRightPartAfterPrefix(AbstractVariable variable) {
		if(variable instanceof PlainVariable) {
			if(this.getInitialVariable().equals((PlainVariable)variable))
				return this.getRightPart();
		}
		else {
			CompositeVariable composite = (CompositeVariable)variable;
			if(this.getInitialVariable().equals(composite.getInitialVariable())) {
				if(this.getRightPart() instanceof CompositeVariable) {
					return ((CompositeVariable)this.getRightPart()).getRightPartAfterPrefix(composite.getRightPart());
				}
			}
		}
		return null;
	}

	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o instanceof CompositeVariable) {
			CompositeVariable composite = (CompositeVariable)o;
			return this.equals(composite) &&
			this.rightPart.equals(composite.rightPart);
		}
		return false;
	}

	public int hashCode() {
		if(hashCode == 0) {
			int result = 17;
			result = 31*result + rightPart.hashCode();
			hashCode = result;
		}
		return hashCode;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(variableName);
		sb.append(".");
		sb.append(rightPart.toString());
		return sb.toString();
	}
}
