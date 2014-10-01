package graph;

public interface GraphInterface {

	// This method allows iterating over all vertices of the graph.
	Iterable<Integer> allVertices();

	// This method allows iterating over all successors of a vertex.
	Iterable<Integer> successors(int vertex);

}
