package graph;

import java.util.LinkedList;
import java.util.LinkedHashMap;


public class Graph implements GraphInterface {

	public Graph() {

		this.vertices=new LinkedList<Integer>();
		this.successorsMap=new LinkedHashMap<Integer,LinkedList<Integer>>();
		
	}

	public void addVertex(int vertex) { 
		
		vertices.addLast(vertex);
		successorsMap.put(vertex,new LinkedList<Integer>());
		
	}
	
	public void addSuccessor(int vertex, int successor) { 
		
		successorsMap.get(vertex).addLast(successor);
		
	}
	
	public boolean hasSuccessor(int vertex, int successor) { 
		
		return (successorsMap.get(vertex)!=null)&&(successorsMap.get(vertex).contains(successor));
		
	}
	
	public Iterable<Integer> allVertices() { return vertices; }
	public Iterable<Integer> successors(int i) { return successorsMap.get(i); }

	private	final LinkedList<Integer> vertices;
	private	final LinkedHashMap<Integer,LinkedList<Integer>> successorsMap;

}
