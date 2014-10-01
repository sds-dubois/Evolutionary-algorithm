package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class UndirectedGraph implements GraphInterface {
	private LinkedHashMap<Integer,IntegerPair> edges;
	private LinkedHashMap<Integer, LinkedList<Integer>> edgesMap;
	private LinkedList<Integer> vertices;
	private LinkedHashMap<Integer, LinkedList<Integer>> successorsMap;

	public UndirectedGraph(){
		this.edgesMap=new LinkedHashMap<Integer,LinkedList<Integer>>();	
		this.vertices=new LinkedList<Integer>();
		this.successorsMap=new LinkedHashMap<Integer,LinkedList<Integer>>();
		this.edges=new LinkedHashMap<Integer,IntegerPair>();
	}

	public void addVertex(int vertex) { 
		vertices.addLast(vertex);
		successorsMap.put(vertex,new LinkedList<Integer>());
		edgesMap.put(vertex, new LinkedList<Integer>());
	}
	
	public void addMultipleSuccessor(int vertex, int successor) {//Authorize multiple edges
		IntegerPair a;
		if(vertex<successor){//By convention edges are saved with first argument lower than the second
			a=new IntegerPair(vertex,successor);
		} else {
			a=new IntegerPair(successor,vertex);
		}
		//if(!edges.containsValue(a)){//Multiple edges forbidden
			edgesMap.get(vertex).add(edges.size());
			edgesMap.get(successor).add(edges.size());
			successorsMap.get(vertex).add(successor);
			successorsMap.get(successor).add(vertex);
			edges.put(edges.size(), a);			
	//	}
	}
	
	public void addSuccessor(int vertex, int successor) {//Forbid multiple edges
		IntegerPair a;
		if(vertex<successor){//By convention edges are saved with first argument lower than the second
			a=new IntegerPair(vertex,successor);
		} else {
			a=new IntegerPair(successor,vertex);
		}
		if(!edges.containsValue(a)){//Multiple edges forbidden
			edgesMap.get(vertex).add(edges.size());
			edgesMap.get(successor).add(edges.size());
			successorsMap.get(vertex).add(successor);
			successorsMap.get(successor).add(vertex);
			edges.put(edges.size(), a);			
		}
	}
	
	public LinkedList<Integer> getEdgeNumbers(int vertex){
		return edgesMap.get(vertex);
	}
	
	public IntegerPair getEdge(int index){
		return edges.get(index);
	}
	
	public void showEdges(){
		for(int i=0;i<edges.size();i++){
			System.out.println("("+edges.get(i).a+","+edges.get(i).b+")");
		}
	}
	
	public int numberOfEdges(){
		return edges.size();
	}
	
	public int numberOfVertices(){
		return vertices.size();
	}
	
	public boolean hasSuccessor(int vertex, int successor) { 
		return (successorsMap.get(vertex)!=null)&&(successorsMap.get(vertex).contains(successor));
	}
	
	//Generate random graph
	public static UndirectedGraph randUndirectedGraph(int n){
		UndirectedGraph g=new UndirectedGraph();
		for(int i=0;i<n;i++){
			g.addVertex(i);
		}
		for(int i=0;i<n;i++){
			HashSet<Integer> friends=new HashSet<Integer>();
			while(friends.size()!=3){
				int candidate=(int) (Math.random()*(double) n);
				if(candidate!=i && !friends.contains(candidate)){
					friends.add(candidate);
					g.addSuccessor(i, candidate);
				}
			}
		}
		return g;
	}
	
	//Generate the ring with n edges
	public static UndirectedGraph ring(int n){
		UndirectedGraph g=new UndirectedGraph();
		for(int i=0;i<n;i++){
			g.addVertex(i);
		}
		for(int i=0;i<n-1;i++){
			g.addSuccessor(i, i+1);
		}
		g.addSuccessor(n-1, 0);
		return g;
	}
	
	@Override
	public Iterable<Integer> allVertices() {
		return vertices;
	}

	@Override
	public Iterable<Integer> successors(int vertex) {
		return successorsMap.get(vertex);
	}
	
	//Attempt to make eulerian an arbitrary graph. Can create multiple edges
	public void makeEulerian()  {
		LinkedList<Integer> OddDegrees = new LinkedList<Integer>();
		for(Integer i:this.allVertices()){
			if(successorsMap.get(i).size()%2!=0){
				OddDegrees.add(i);
			}
		}
		if(OddDegrees.size() % 2 != 0){
			//throw new Exception("Erreur : il est impossible de rendre ce graphe Eulerien");
		}
		//System.out.println(OddDegrees.toString());
		while(!OddDegrees.isEmpty()){
			int a=OddDegrees.pop(),b=OddDegrees.pop();
			addMultipleSuccessor(a,b);
		}
	}
	public static UndirectedGraph hamiltonienGraph(int n, int k){//Generate a graph with a hamiltonien cycle
		  UndirectedGraph g=new UndirectedGraph();
	    for(int i=0;i<n;i++){
	      g.addVertex(i);
	    }
	    for(int i=0;i<n-1;i++){
	      g.addSuccessor(i, i+1);
	    }
	    g.addSuccessor(n-1, 0);
	    for(int i=0;i<n;i++){
	      HashSet<Integer> friends=new HashSet<Integer>();
	      while(friends.size()!=k){
	        int candidate=(int) (Math.random()*(double) n);
	        if(candidate!=i && !friends.contains(candidate)){
	          friends.add(candidate);
	          g.addSuccessor(i, candidate);
	        }
	      }
	    }
	    return g ;
		}
	

}
