package ca.concordia.java.ast.decomposition.cfg;

import java.util.LinkedHashSet;
import java.util.Set;

public class GraphNode {
	private static int nodeNum = 0;
	protected int id;
	protected Set<GraphEdge> incomingEdges;
	protected Set<GraphEdge> outgoingEdges;

	public GraphNode() {
		nodeNum++;
		this.id = nodeNum;
		this.incomingEdges = new LinkedHashSet<GraphEdge>();
		this.outgoingEdges = new LinkedHashSet<GraphEdge>();
	}

	public int getId() {
		return id;
	}

	public void addIncomingEdge(GraphEdge edge) {
		incomingEdges.add(edge);
	}

	public void addOutgoingEdge(GraphEdge edge) {
		outgoingEdges.add(edge);
	}

	public static void resetNodeNum() {
		nodeNum = 0;
	}

	public int getInDegree() {
		return incomingEdges.size();
	}

	public int getOutDegree() {
		return outgoingEdges.size();
	}

	public boolean isSuccessorOf(GraphNode graphNode) {
		boolean isSuccessorOf = false;
		for (GraphEdge graphEdge : incomingEdges) {
			if (graphEdge.src.id == graphNode.id) {
				isSuccessorOf = true;
				break;
			}
		}
		return isSuccessorOf;
	}

	public boolean isPredecessorOf(GraphNode graphNode) {
		boolean isPredecessorOf = false;
		for (GraphEdge graphEdge : outgoingEdges) {
			if (graphEdge.dst.id == graphNode.id) {
				isPredecessorOf = true;
				break;
			}
		}
		return isPredecessorOf;
	}
}
