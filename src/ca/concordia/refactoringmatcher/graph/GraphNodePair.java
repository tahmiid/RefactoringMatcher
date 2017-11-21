package ca.concordia.refactoringmatcher.graph;

import ca.concordia.java.ast.decomposition.cfg.GraphNode;

public class GraphNodePair {
    public final GraphNode node1;
    public final GraphNode node2;

    public GraphNodePair(GraphNode v1, GraphNode v2) {
        this.node1 = v1;
        this.node2 = v2;
    }

    public boolean equals(Object o) {
        return (o != null) && (this.getClass() == o.getClass()) && node1 == ((GraphNodePair) o).node1 && node2 == ((GraphNodePair) o).node2;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + node1.hashCode();
        result = 37 * result + node2.hashCode();
        return result;
    }

    public String toString() {
        return String.format("(%s,%s)", node1, node2);
    }
}
