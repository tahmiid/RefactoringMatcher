package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

public abstract class PDGDependence extends GraphEdge  implements Serializable{
	private PDGDependenceType type;
	
	public PDGDependence(PDGNode src, PDGNode dst, PDGDependenceType type) {
		super(src, dst);
		this.type = type;
	}

	public GraphNode getSrc() {
		return src;
	}

	public GraphNode getDst() {
		return dst;
	}

	public PDGDependenceType getType() {
		return type;
	}
}
