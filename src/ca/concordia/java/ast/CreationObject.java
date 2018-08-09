package ca.concordia.java.ast;

import java.io.Serializable;

public abstract class CreationObject  implements Serializable{
	private TypeObject type;
	protected ASTInformation creation;
	
	public CreationObject(TypeObject type) {
		this.type = type;
	}

	public TypeObject getType() {
		return type;
	}
}
