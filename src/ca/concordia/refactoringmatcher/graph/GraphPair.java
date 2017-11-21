package ca.concordia.refactoringmatcher.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ca.concordia.java.ast.decomposition.cfg.Graph;
import ca.concordia.java.ast.decomposition.cfg.GraphNode;

public class GraphPair {
	private Graph graph1;
	private Graph graph2;
	private int largestGraphSize;
	private boolean areIsomorph;
	private ArrayList<Set<GraphNodePair>> subgraphs;

	public GraphPair(Graph graph1, Graph graph2) {
		this.graph1 = graph1;
		this.graph2 = graph2;
		areIsomorph = false;
		largestGraphSize = graph1Size() > graph2Size() ? graph1Size() : graph2Size();
		subgraphs = new ArrayList<Set<GraphNodePair>>();

		calculateIsomorphism();

		if (graph1Size() <= graph2Size()) {
			calculateSubgraphIsomorphism(graph1, graph2);
		} else {
			calculateSubgraphIsomorphism(graph2, graph1);
		}
	}

	public int graph1Size() {
		return graph1.size();
	}

	public int graph2Size() {
		return graph2.size();
	}

	public boolean areIsomorph() {
		return areIsomorph;
	}
	
	public ArrayList<Set<GraphNodePair>> getSubGraphs()
	{
		return subgraphs;
	}

	public void calculateSubgraphIsomorphism(Graph model, Graph input) {
		assert model.size() <= input.size() : "model graph must have less or equal number of nodes as input";

		GraphNode[] modelNodes = model.getNodes().toArray(new GraphNode[0]);
		GraphNode[] inputNodes = input.getNodes().toArray(new GraphNode[0]);

		int[][] currentGraphMatrix = new int[model.getNodes().size()][input.getNodes().size()];
		for (int i = 0; i < model.getNodes().size(); i++) {
			for (int j = 0; j < input.getNodes().size(); j++) {
				if (modelNodes[i].isSuccessorOf(modelNodes[i]) && !inputNodes[j].isSuccessorOf(inputNodes[j])) {
					currentGraphMatrix[i][j] = 0;
				} else {
					currentGraphMatrix[i][j] = 1;
				}
			}
		}

		Set<GraphNodePair> nodePairs = new HashSet<GraphNodePair>();
		backtrackSGI(currentGraphMatrix, 0, nodePairs, modelNodes, inputNodes, model, input);
	}

	private void backtrackSGI(int[][] currentGraphMatrix, int index, Set<GraphNodePair> nodePairs,
			GraphNode[] modelNodes, GraphNode[] inputNodes, Graph model, Graph input) {
		if (index > model.size() - 1) {
			subgraphs.add(new HashSet<GraphNodePair>(nodePairs));
			return;
		}
		for (int j = 0; j < input.size(); j++) {
			if (currentGraphMatrix[index][j] == 1) {
				GraphNodePair pair = new GraphNodePair(modelNodes[index], inputNodes[j]);
				nodePairs.add(pair);
				int[][] newGraphMatrix = new int[model.size()][input.size()];
				for (int s = 0; s < model.size(); s++) {
					System.arraycopy(currentGraphMatrix[s], 0, newGraphMatrix[s], 0, input.size());
				}
				for (int k = index + 1; k < model.size(); k++) {
					newGraphMatrix[k][j] = 0;
				}
				if (forward_checkingSGI(newGraphMatrix, index, nodePairs, modelNodes, inputNodes, model, input)) {
					backtrackSGI(newGraphMatrix, index + 1, nodePairs, modelNodes, inputNodes, model, input);
				}
				nodePairs.remove(pair);
			}
		}
	}

	private boolean forward_checkingSGI(int[][] currentGraphMatrix, int index, Set<GraphNodePair> nodePairs,
			GraphNode[] modelNodes, GraphNode[] inputNodes, Graph model, Graph input) {
		for (int k = index + 1; k < model.size(); k++) {
			for (int l = 0; l < input.size(); l++) {
				if (currentGraphMatrix[k][l] == 1) {
					for (GraphNodePair pair : nodePairs) {
						GraphNode v = pair.node1;
						GraphNode w = pair.node2;
						if (v.isSuccessorOf(modelNodes[k])) {
							if (!w.isSuccessorOf(inputNodes[l])) {
								currentGraphMatrix[k][l] = 0;
								break;
							}
						}
						if (v.isPredecessorOf(modelNodes[k])) {
							if (!w.isPredecessorOf(inputNodes[l])) {
								currentGraphMatrix[k][l] = 0;
								break;
							}
						}
					}
				}
			}
		}

		for (int k = 0; k < model.size(); k++) {
			if (sum(currentGraphMatrix[k]) == 0) {
				return false;
			}
		}
		return true;
	}

	private void calculateIsomorphism() {
		if ((graph1Size() != graph2Size()) || (graph1.getEdges().size() != graph2.getEdges().size())) {
			return;
		}
		int[][] currentGraphMatrix = new int[graph1Size()][graph2Size()];
		GraphNode[] graph1Nodes = graph1.getNodes().toArray(new GraphNode[0]);
		GraphNode[] graph2Nodes = graph2.getNodes().toArray(new GraphNode[0]);

		for (int i = 0; i < graph1Size(); i++) {
			for (int j = 0; j < graph2Size(); j++) {
				if (graph1Nodes[i].getOutDegree() == graph2Nodes[j].getOutDegree()
						&& graph1Nodes[i].getInDegree() == graph2Nodes[j].getInDegree()) {
					if (graph1Nodes[i].isSuccessorOf(graph1Nodes[i]) && graph2Nodes[j].isSuccessorOf(graph2Nodes[j])) {
						currentGraphMatrix[i][j] = 1;
					} else if (!graph1Nodes[i].isSuccessorOf(graph1Nodes[i])
							&& !graph2Nodes[j].isSuccessorOf(graph2Nodes[j])) {
						currentGraphMatrix[i][j] = 1;
					} else {
						currentGraphMatrix[i][j] = 0;
					}
				} else {
					currentGraphMatrix[i][j] = 0;
				}
			}
		}
		HashSet<GraphNodePair> nodePairs = new HashSet<GraphNodePair>();
		backtrack(currentGraphMatrix, 0, nodePairs, graph1Nodes, graph2Nodes);
	}

	private void backtrack(int[][] currentGraphMatrix, int index, Set<GraphNodePair> nodePairs, GraphNode[] graph1Nodes,
			GraphNode[] graph2Nodes) {
		if (areIsomorph)
			return;

		if (index > graph1Size() - 1) {
			if (nodePairs.size() == largestGraphSize)
				areIsomorph = true;
			return;
		}

		for (int j = 0; j < graph2Size(); j++) {
			if (currentGraphMatrix[index][j] == 1) {
				GraphNodePair pair = new GraphNodePair(graph1Nodes[index], graph2Nodes[j]);
				nodePairs.add(pair);

				int[][] newGraphMatrix = new int[graph1Size()][graph2Size()];

				for (int s = 0; s < graph1Size(); s++) {
					System.arraycopy(currentGraphMatrix[s], 0, newGraphMatrix[s], 0, graph2Size());
				}

				for (int k = index + 1; k < graph1Size(); k++) {
					newGraphMatrix[k][j] = 0;
				}

				if (forward_checking(newGraphMatrix)) {
					backtrack(newGraphMatrix, index + 1, nodePairs, graph1Nodes, graph2Nodes);
				}

				nodePairs.remove(pair);
			}
		}
	}

	private boolean forward_checking(int[][] currentGraphMatrix) {
		for (int i = 0; i < graph1Size(); i++) {
			if (sum(currentGraphMatrix[i]) == 0) {
				return false;
			}
		}
		return true;
	}

	private int sum(int[] integers) {
		int sum = 0;
		for (int i : integers) {
			sum += i;
		}
		return sum;
	}
}
