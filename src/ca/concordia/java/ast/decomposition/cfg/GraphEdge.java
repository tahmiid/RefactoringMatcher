package ca.concordia.java.ast.decomposition.cfg;

import java.io.Serializable;

public class GraphEdge  implements Serializable{
	protected GraphNode src;
	protected GraphNode dst;
	
	public GraphEdge(GraphNode src, GraphNode dst) {
		this.src = src;
		this.dst = dst;
	}

	public GraphNode getSrc() {
		return src;
	}

	public GraphNode getDst() {
		return dst;
	}
	
	public String toString() {
		return src + "->" + dst;
	}
}
