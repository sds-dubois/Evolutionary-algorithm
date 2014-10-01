package Optimizables;
import java.util.HashMap;

import graph.GraphWindow;
import graph.IntegerPair;
import graph.UndirectedGraph;

public class BinaryNumberMatching extends BinaryNumber {

	UndirectedGraph g;
	protected int lastvalue;
	public BinaryNumberMatching(UndirectedGraph graph) {
		super(graph.numberOfEdges());
		g=graph;
	}
	
	@Override
	public int fitness() {
		int val=0;
		for(Integer v:g.allVertices()){
			int temp=degree(v);
			if(temp>=1){
				val+=temp-1;
			}
		}
		lastvalue=numberOfOnes-x.length*val;
		return lastvalue;
	}
	
	@Override
	public int lastvalue(){
		return lastvalue;
	}
	
	@Override
	public BinaryNumberMatching copy(){
		BinaryNumberMatching retour=new BinaryNumberMatching(g);
		retour.x=x.clone();
		retour.numberOfOnes=numberOfOnes;
		retour.lastvalue=lastvalue;
		return retour;
	}
	
	private int degree(Integer vertex){//compute degree of a vertex inside the subgraph induced by this BinaryNumber
		int deg=0;
		for(Integer i:g.getEdgeNumbers(vertex)){
			if(x[i]==1)
				deg++;
		}
		return deg;
	}
	
	public void displayWindow(){
		HashMap<IntegerPair, Integer> couleurs=new HashMap<IntegerPair, Integer>();
		for(int i=0;i<g.numberOfEdges();i++){
			if(x[i]==1){
				couleurs.put(g.getEdge(i), 3);
				couleurs.put(new IntegerPair(g.getEdge(i).b,g.getEdge(i).a),3);
			}
		}
		GraphWindow graphWindow=new GraphWindow(800, 800, g,new HashMap<Integer,Integer>(), couleurs, new HashMap<IntegerPair,Float>());
	}

}
