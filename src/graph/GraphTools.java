package graph;

import java.util.Random;

public class GraphTools {

	public static Graph createRandomGraph1(int numberOfVertices, int seed) {

		Random r=new Random();
		r.setSeed(seed);

		Graph graph=new Graph();

		// create vertices

		for (int i=0;i<numberOfVertices;i++) graph.addVertex(i);

		// create edges

		for (int i=0;i<numberOfVertices;i++) {

			int numberOfEdges=r.nextInt((int)(numberOfVertices/3.5));

			for (int j=0;j<numberOfEdges;j++) {

				int vertexIndex=r.nextInt(numberOfVertices);
				if ((vertexIndex!=i)&&(!graph.hasSuccessor(i,vertexIndex))) graph.addSuccessor(i,vertexIndex);

			}

		}

		// return the graph

		return graph;

	}

	public static Graph createRandomGraph2(int numberOfVertices, int numberOfComponents, int seed) {

		Random r=new Random();
		r.setSeed(seed);

		Graph graph=new Graph();

		// create vertices

		for (int i=0;i<numberOfVertices;i++) graph.addVertex(i);

		// create edges inside components

		for (int c=0;c<numberOfComponents;c++) {

			for (int i=c*numberOfVertices/numberOfComponents;i<(c+1)*numberOfVertices/numberOfComponents;i++) {

				int numberOfEdges=r.nextInt((int)(numberOfVertices/1.5));

				for (int j=0;j<numberOfEdges;j++) {

					int vertexIndex=c*numberOfVertices/numberOfComponents+r.nextInt(numberOfVertices/numberOfComponents);
					if ((vertexIndex!=i)&&(!graph.hasSuccessor(i,vertexIndex))) graph.addSuccessor(i,vertexIndex);

				}

			}

		}

		// create edges between components

		for (int c=0;c<numberOfComponents;c++) {

			for (int i=c*numberOfVertices/numberOfComponents;i<1+c*numberOfVertices/numberOfComponents;i++) {

				int numberOfEdges=r.nextInt(4);

				for (int j=0;j<numberOfEdges;j++) {

					int d=r.nextInt(numberOfComponents);
					int vertexIndex=d*numberOfVertices/numberOfComponents+r.nextInt(numberOfVertices/numberOfComponents);
					if ((vertexIndex!=i)&&(!graph.hasSuccessor(i,vertexIndex))) graph.addSuccessor(i,vertexIndex);

				}

			}

		}


		// return the graph

		return graph;

	}

	public static Graph createTestGraph1() {

		Graph graph=new Graph();

		// create vertices

		for (int i=0;i<10;i++) graph.addVertex(i);

		// create edges

		graph.addSuccessor(0,7);
		graph.addSuccessor(0,6);
		graph.addSuccessor(1,2);
		graph.addSuccessor(2,8);
		graph.addSuccessor(3,9);
		graph.addSuccessor(4,7);
		graph.addSuccessor(4,0);
		graph.addSuccessor(1,4);
		graph.addSuccessor(5,8);
		graph.addSuccessor(7,1);
		graph.addSuccessor(5,3);
		graph.addSuccessor(9,5);
		graph.addSuccessor(9,2);
		graph.addSuccessor(2,9);
		graph.addSuccessor(3,2);
		graph.addSuccessor(3,8);
		graph.addSuccessor(8,9);
		graph.addSuccessor(7,8);

		// return the graph

		return graph;

	}

}
