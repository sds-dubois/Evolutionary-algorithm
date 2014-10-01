import Optimizables.*;
import graph.UndirectedGraph;
import sat.*;

public class Projet {
	public static void main(String[] args) throws Exception {
		//nonselfadaptivetest();
		//selfadaptivetest();
		//randgraphtest();
		//ringtest();
		//eulerianCycleTest();
		//satTest();
		//hamiltonienCycleTest();
	}
	
	public static void nonselfadaptivetest(){
		int ntests=100;//number of tests of a same instance problem for the average
		for(int lambda=1;lambda<=1;lambda+=4){
			System.out.println("Lambda="+lambda);
			for(int n=300;n<=300;n+=50){
				//long startTime = System.currentTimeMillis();//uncomment for time evaluation
				BinaryNumber x=new BinaryNumber(n);
				GAOptimizer o=new GAOptimizer(x,lambda);
				o.counter=0;
				for(int i=0;i<ntests;i++){
					o.init();
					while(o.current_fitness()!=n){
						o.one_iteration();
					}
				}
				System.out.println((double) o.counter/(double) ntests);
				//long endTime   = System.currentTimeMillis();
				//long totalTime = endTime - startTime;
				//System.out.println((double) totalTime/(double) ntests);
			}
		}
	}
	
	public static void selfadaptivetest(){
		int ntests=1;
		for(int rule=6;rule<=6;rule+=3){//population is increased by F^(1/rule) at unsuccessful evaluation
			System.out.println("Rule="+rule);
			for(int n=300;n<=300;n+=50){
				//long startTime = System.currentTimeMillis();
				//BinaryNumber.counter=0;
				BinaryNumber x=new BinaryNumber(n);
				SAOptimizer o=new SAOptimizer(x,rule);
				o.counter=0;
				o.verbose=true;
				for(int i=0;i<ntests;i++){
					o.init();
					while(o.current_fitness()!=n){
						o.one_iteration();
					}
				}
				System.out.println((double) o.counter/(double) ntests);
				//long endTime   = System.currentTimeMillis();
				//long totalTime = endTime - startTime;
				//System.out.println((double) totalTime/(double) ntests);
			}
		}
	}

	public static void randgraphtest(){
		UndirectedGraph graph=UndirectedGraph.randUndirectedGraph(5);
		BinaryNumberMatching g=new BinaryNumberMatching(graph);
		SAOptimizer o=new SAOptimizer(g);
		o.verbose=true;
		o.init();
		for(int i=0;i<100;i++){
			o.one_iteration();
			System.out.println("fit="+o.current_fitness());
		}
		((BinaryNumberMatching) o.x).displayWindow();//Display the graph
	}
	
	public static void ringtest(){
		int ntests=1;
		for(int n=14;n<=14;n+=2){
			//long startTime = System.currentTimeMillis();
			UndirectedGraph graph=UndirectedGraph.ring(n);
			BinaryNumberMatching g=new BinaryNumberMatching(graph);
			GAOptimizer o=new GAOptimizer(g,1);
			o.verbose=true;
			//BinaryNumberMatching.counter=0;
			o.counter=0;
			for(int i=0;i<ntests;i++){
				o.init();
				while(o.current_fitness<n/2){
					o.one_iteration();
					//System.out.println(o.current_fitness);
					//System.out.println(o.lambda);
					//o.x.println();
				}
				//System.out.println((double) i/(double) ntests);
			}
			//((BinaryNumberMatching) o.x).displayWindow();
			//long endTime   = System.currentTimeMillis();
			//long totalTime = endTime - startTime;
			//System.out.println(totalTime/(double) ntests+" ms");
			//System.out.println(o.counter/(double) ntests);
		}
	}
	
public static void eulerianCycleTest(){
		int ntests=10;
		int nbGraphs = 10 ;
		for(int n=50;n<51;n+=20){
		  int counter =0 ;
		  //long startTime = System.currentTimeMillis();
		  for (int k =0 ; k< nbGraphs ; k++) {
  			UndirectedGraph g=UndirectedGraph.randUndirectedGraph(n);
  			g.makeEulerian();
  			//GraphWindow graphWindow=new GraphWindow(600, 600, g,new HashMap<Integer,Integer>(),new HashMap<IntegerPair, Integer>(), new HashMap<IntegerPair,Float>());
  			Couplage x=new Couplage(g);
  			SAOptimizer o=new SAOptimizer(x,2,n);
  			o.counter=0;
			int fitMax = x.getNbE() * (1 + x.getNbE()) ;
  			//int fitMax = 2* x.nbE ;  //because lengthCycles2 in fitness
  			//o.verbose=true;
  			for(int i=0;i<ntests;i++){
  				o.init();
  				//int j = 0 ;
  				while(o.current_fitness != fitMax){
  				  //System.out.println("Iteration : " +j) ;
  				  //j++ ;
  					o.one_iteration();
  				//System.out.println("Fitness : " + o.current_fitness) ;
  				}
  			}
  			counter += o.counter ;
		  }
		//long endTime   = System.currentTimeMillis();
		//long totalTime = endTime - startTime;
		//System.out.println(totalTime/(double) (10*ntests)+" ms");
		System.out.println((double) counter/(double) (ntests*10));

		}
	} 
	
	public static void satTest() throws Exception{
		for(int n=300;n<=300;n+=50){
			double ntests=1000;
			Formule f=Formule.generate2(n,3,n);//Instance of the problem
			BinaryNumberValuation b=new BinaryNumberValuation(f);
			GAOptimizer o=new GAOptimizer(b,12);
			//System.out.println("Fitmax="+fitmax);
			//o.verbose=true;
			o.counter=0;
			for(int i=0;i<ntests;i++){
				o.init();
				f=Formule.generate2(n, 3, n);
			//	f.println();
				//o.one_iteration();
				//System.out.println("Fitness="+o.current_fitness);
				while(o.current_fitness<(int) ((8.0/8.0)*f.numberOfClauses())){
					o.one_iteration();
				//	System.out.println("Fitness="+o.current_fitness);
				}
				//o.x.println();
				System.out.println("Test "+i+"/"+ntests+" litts="+f.variables().size());
			}
			System.out.println((double) (o.counter)/ntests);
		}		
	}
	
	
	public static void hamiltonienCycleTest() {
	  int ntest = 10;
	  int nbGraphs = 10 ;
	  for (int n=5; n<21; n+=5) {
	    int counter = 0 ;
	    //long startTime = System.currentTimeMillis();
	    //System.out.println("-- n = " + n + " --") ;
	  for (int k=0 ; k< nbGraphs ; k++) {
	     UndirectedGraph g = UndirectedGraph.hamiltonienGraph(n,3) ;
	     CouplageH x = new CouplageH(g) ;
	     GAOptimizer o = new GAOptimizer(x,1) ;
	     //x.printGraph() ;
	     o.counter = 0 ;
	     for (int i =0; i<ntest ;i++){
	       o.init();
	       //o.verbose = true ;
	       //int fitMax = (x.nbV +1)*(x.nbV+1) ; //fit1
	       //int fitMax = x.nbE ; //fit2
	       int fitMax = x.getNbV()+1 ;  //fit3
	       //int j = 0 ;
	       while(o.current_fitness != fitMax ) {
	         //System.out.println("Iteration : " +j) ;
	         //j++ ;
	         o.one_iteration();
	         //System.out.println("Fitness : " + o.current_fitness) ;
	       }
	       //((CouplageH) o.x).printGraph();
	     }
	     counter += o.counter ;
	     //System.out.println((double) o.counter/(double) (ntest));
	  }
	//long endTime   = System.currentTimeMillis();
    //long totalTime = endTime - startTime;
    //System.out.println(totalTime/(double) (10*ntest)+" ms");
    System.out.println((double) counter/(double) (10*ntest));  
	  }
	}
	
}
