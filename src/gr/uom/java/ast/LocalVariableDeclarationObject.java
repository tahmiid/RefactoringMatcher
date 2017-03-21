package gr.uom.java.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class LocalVariableDeclarationObject extends VariableDeclarationObject {
	private TypeObject type;
    private String name;
    private int scopeStartPosition;
    private ASTInformation variableDeclaration;
    private volatile int hashCode = 0;

    public LocalVariableDeclarationObject(TypeObject type, String name, ASTNode scope) {
        this.type = type;
        this.name = name;
        this.scopeStartPosition = scope.getStartPosition();
    }

    public TypeObject getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public VariableDeclaration getVariableDeclaration() {
    	ASTNode node = this.variableDeclaration.recoverASTNode();
    	if(node instanceof SimpleName) {
    		return (VariableDeclaration)node.getParent();
    	}
    	else {
    		return (VariableDeclaration)node;
    	}
	}

	public void setVariableDeclaration(VariableDeclaration variableDeclaration) {
		this.variableDeclaration = ASTInformationGenerator.generateASTInformation(variableDeclaration);
	}

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if (o instanceof LocalVariableDeclarationObject) {
        	LocalVariableDeclarationObject lvdo = (LocalVariableDeclarationObject)o;
            return this.name.equals(lvdo.name)/* && this.type.equals(lvdo.type)*/ /*&& this.variableBindingKey.equals(lvdo.variableBindingKey)*/;
        }
        return false;
    }

    public boolean equals(LocalVariableInstructionObject lvio) {
    	return this.name.equals(lvio.getName()) /*&& this.type.equals(lvio.getType())*/ /*&& this.variableBindingKey.equals(lvio.getVariableBindingKey())*/;
    }

    public int hashCode() {
    	if(hashCode == 0) {
    		int result = 17;
    		result = 37*result + scopeStartPosition;
    		result = 37*result + type.hashCode();
    		result = 37*result + name.hashCode();
    		hashCode = result;
    	}
    	return hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ");
        sb.append(name);
        return sb.toString();
    }
}
