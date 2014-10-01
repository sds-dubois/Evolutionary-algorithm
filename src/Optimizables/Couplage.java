package Optimizables;
import java.util.HashMap;

import graph.GraphWindow;
import graph.IntegerPair;
import graph.UndirectedGraph;



public class Couplage extends Optimizable {
  int nbV ;  //number of vertices
  private int nbE ;  // number of edges
  private int lastvalue;
  UndirectedGraph g;
  int[][] pairing ;
  
  public Couplage(UndirectedGraph graph) {
	g=graph;
    nbV = g.numberOfVertices();
    setNbE(g.numberOfEdges());
    pairing = new int[nbV][getNbE()] ;
    for (int j =0 ; j<nbV ; j++) {
      for (int k=0 ; k<getNbE() ; k++){
      pairing[j][k] = -1 ;
      }
    }
  }
  
  public Couplage copy() {
    Couplage c = new Couplage(g) ;
    for (int j =0 ; j<nbV ; j++) {
      for (int k=0; k<getNbE() ; k++){
      c.pairing[j][k] = this.pairing[j][k] ;
      }
    }
    c.lastvalue=lastvalue;
    c.g=g;
    return c ;
  }
  
  @Override
  public Optimizable mutation(double lambda){
	  int l=poisson(lambda)+1;
	  Couplage candidate = mutateCouplage(l);
	  int max=candidate.fitness();
	  for(int i=0;i<lambda-1;i++){
	    Couplage temp = mutateCouplage(l);
	    if(temp.fitness()>max){
	      candidate=temp;
	      max=temp.lastvalue;
	    }
	  }
	  candidate.lastvalue=max;
	  return candidate;
  }
  
  @Override
  public Optimizable crossover(Optimizable xpr, double lambda) {
	  double c=1.0/lambda;
	  Couplage candidate=new Couplage(g);
	    int max=-1;
	    for(int i=0;i<lambda;i++){
	      //System.out.println("x") ;
	      //x.println();
	      //System.out.println("xprim") ;
	      //xprim.println();
	      Couplage  temp= crossCouplage(c,(Couplage) xpr);//should be safe if good use
	      //temp.println();
	      //tirages++;
	      int f = temp.fitness() ;
	      if(f>max || max == -1){
	        candidate=temp;
	        max=f;
	      }
	    }
	    candidate.lastvalue=max;
	    //candidate.println() ;
	    return candidate;	  
  }
  
  public Couplage mutateCouplage(int l){
	    Couplage couplage2 = this.copy() ;
	    for (int j=0 ; j<l ; j++ ) {
	    int randVertex = (int) (Math.random()* (double) nbV ) ;
	    IntegerPair p = coupleRandEdges(randVertex) ;
	    int randEdge1 = p.a ;
	    int randEdge2 = p.b ;
	    if (randEdge1 != randEdge2) {
	      couplage2.setCouple(g, randVertex, randEdge1, randEdge2);
	    } }
	    return couplage2 ;
	  }
  
  public void setCouple(UndirectedGraph g, int vertex , int e1, int e2){
    int former1 = pairing[vertex][e1] ;
    int former2 = pairing[vertex][e2] ;
    pairing[vertex][e1] = e2 ;
    pairing[vertex][e2] = e1 ;
    //now : cleaning up the mess 
    if (former1 != e1) {
      if (former2 != e2) {
        pairing[vertex][former1] = former2 ;
        pairing[vertex][former2] = former1 ;
      }
      else {
        pairing[vertex][former1] = former1 ;
      }
    }
    else {
      if (former2 != e2) { pairing[vertex][former2] = former2 ;}
    }
  }
  
  public IntegerPair coupleRandEdges(int vertex){
    int a = -1 ; int b = -1 ;
    while (a == -1 ){
      int i = (int) (Math.random()* (double)getNbE()) ;
      if (pairing[vertex][i] != -1) { a=i; }
    }
    while (b == -1 ){
      int i = (int) (Math.random()* (double)getNbE()) ;
      if (i != a && pairing[vertex][i] != -1) { b=i; }
    }
    IntegerPair p = new IntegerPair(a,b) ;
    return p ;
  }
  
  public Couplage mutationUniqueCouplage(UndirectedGraph g){
    int randVertex = (int) (Math.random()* (double) nbV ) ;
    IntegerPair p = coupleRandEdges(randVertex) ;
    int randEdge1 = p.a ;
    //System.out.println(randVertex +" " + p.a + " " + p.b);
    int randEdge2 = p.b ;
    if (randEdge1 != randEdge2) {
      Couplage couplage2 = this.copy() ;
      couplage2.setCouple(g, randVertex, randEdge1, randEdge2);
      return couplage2 ;
    }
    else {return null ; }
  }
  
  public void copyPartFrom(int vertex, Couplage c){  //copy a matching for a vertex
    for (int i =0 ; i< getNbE(); i++){
      pairing[vertex][i] = c.pairing[vertex][i] ;
    }
  }
  
  public Couplage crossCouplage(double c, Couplage xprim){
    Couplage offspring = new Couplage(g) ;
    for (int v= 0 ; v < nbV ; v++){
      if(Math.random() < (1-c)) {
        offspring.copyPartFrom(v,this) ;
      }
      else {
        offspring.copyPartFrom(v,xprim) ;
      }
    }
    return offspring ;
  }
  
  
  public int numberOfEdgesAlone(){
    int nb = 0 ;
    for (int j = 0 ; j < getNbE() ; j++ ){
      IntegerPair e = g.getEdge(j) ;
      if (pairing[e.a][j] == j) { nb ++ ; }
      if (pairing[e.b][j] == j) { nb ++ ; }
    }
    return nb ;
  }
  
  
  public void println(){
    for(int i=0 ; i<getNbE();i++){
      System.out.println();
      for (int j=0 ; j< nbV ; j++) {
        System.out.print(pairing[j][i]+ " - ") ;
      }
    }
    System.out.println();
  }

  
public int fitness() {
	int f = 0 ;
    int nbAlone = numberOfEdgesAlone() ;
    //System.out.println(nbAlone) ;
    f = getNbE() - nbAlone ;
    if(nbAlone ==0){
      f += lengthCycles() ;  //lengthCycles => sum of squares // lengthCycles2 => max 
    }
    lastvalue=f;
    return f ;
}

@Override
public int lastvalue() {
	return lastvalue;
}

@Override
public void init() {
	for (int i =0 ; i <getNbE() ; i++){
		IntegerPair e = g.getEdge(i) ;
		pairing[e.a][i] = i ;
		pairing[e.b][i] = i ;
	} 
	lastvalue=fitness();
}
  
public int mesureCycle(int edge, boolean[] visites){
	  int l = 1 ;
	  int vertex = g.getEdge(edge).a ;
	  int suivant = pairing[vertex][edge] ;
	  //boolean b = false ;
	 //System.out.println(suivant);
	 while(suivant != edge && !visites[suivant]){
	    visites[suivant] = true ;
	    //System.out.println(l + " " + suivant +" " + vertex + " " + edge);
	    //couplage.println();
	    IntegerPair p = g.getEdge(suivant) ;
	    //System.out.println(p.a + " " + p.b);
	    if (p.a == vertex ) { vertex = p.b ; }
	    else { vertex = p.a ; }
	    suivant = pairing[vertex][suivant] ;
	    l++ ;
	    //if (visites[suivant]) { b =true ;}
	  }
	  //System.out.println(l) ;
	  return l ;
	}
	
	public int lengthCycles(){  //return the sum of the (cycles' length)�
	  // we assume that numberOfEdgesAlone == 0 && couplage is a matching for graph g
	  int l = 0 ;
	  int nbE = g.numberOfEdges() ;
	  boolean[] visites = new boolean[nbE] ;
	  for( int e =0 ; e < nbE ; e++) {
	    if(!visites[e]){
	      visites[e] = true ;
	      int temp = mesureCycle(e,visites) ;
	      l += temp * temp ;
	    }
	  }
	  return l ;
	}
	
	 public int lengthCycles2(){  //return the sum of the max cycles' length
	    // we assume that numberOfEdgesAlone == 0 && couplage is a matching for graph g
	    int l = 0 ;
	    int nbE = g.numberOfEdges() ;
	    boolean[] visites = new boolean[nbE] ;
	    for( int e =0 ; e < nbE ; e++) {
	      if(!visites[e]){
	        visites[e] = true ;
	        int temp = mesureCycle(e,visites) ;
	        if(l < temp) {l = temp ; }
	      }
	    }
	    return l ;
	  }
	
	 public int lengthCyclesPrint(){  //return the sum of the (cycles' length)� 
	    // we assume that numberOfEdgesAlone == 0 && couplage is a matching for graph g
	    int l = 0 ;
	    int nbE = g.numberOfEdges() ;
	    boolean[] visites = new boolean[nbE] ;
	    for( int e =0 ; e < nbE ; e++) {
	      if(!visites[e]){
	        visites[e] = true ;
	        int temp = mesureCyclePrint(e,visites) ;
	        l += temp * temp ;
	      }
	    }
	    return l ;
	  }
	
	 public int mesureCyclePrint(int edge, boolean[] visites){
	   HashMap<IntegerPair, Integer> couleurs=new HashMap<IntegerPair, Integer>();
	   //System.out.println("---D�but---") ;
	   //System.out.println("Edge : " + edge) ;
	   couleurs.put(g.getEdge(edge), 3);
	   couleurs.put(new IntegerPair(g.getEdge(edge).b,g.getEdge(edge).a),3);
	   GraphWindow graphWindow=new GraphWindow(600, 600, g,new HashMap<Integer,Integer>(), couleurs, new HashMap<IntegerPair,Float>());
	   int l = 1 ;
	    int vertex = g.getEdge(edge).a ;
	    int v = g.getEdge(edge).a ;
	    //System.out.println("Vertex : " + vertex) ;
	    int suivant = pairing[vertex][edge] ;
	    boolean b = false ;
	    while(suivant != edge && !visites[suivant]){
	      couleurs.put(g.getEdge(suivant), 3);
	      couleurs.put(new IntegerPair(g.getEdge(suivant).b,g.getEdge(suivant).a),3);
	      //System.out.println(suivant) ;
	      visites[suivant] = true ;
	      //System.out.println(l + " " + suivant +" " + vertex + " " + edge);
	      //couplage.println();
	      IntegerPair p = g.getEdge(suivant) ;
	      //System.out.println(p.a + " " + p.b);
	      if (p.a == vertex ) { vertex = p.b ; }
	      else { vertex = p.a ; }
	      //System.out.println("Edge : " + suivant + " " +vertex + " " + p.a + " " + p.b ) ;
	      suivant = pairing[vertex][suivant] ;
	      //System.out.print(" - " + suivant ) ; 
	      l++ ;
	      //if (visites[suivant]) { b =true ; }
	    }
	    //System.out.println(l) ;
	    return l ;
	  } 
	 
		public static int binom(int n,double p){
			int r=0;
			for(int j=0;j<n;j++){
				if(Math.random()<=p){
					r++;
				}
			}
			return r;
		}
		
		public static int poisson(double lambda){
		  double t=Math.exp(-lambda);
			int x=0;
			double prod=Math.random();
			while(prod>=t){
				x++;
				prod*=Math.random();
			}
			return x;
		}

		public int getNbE() {
			return nbE;
		}

		public void setNbE(int nbE) {
			this.nbE = nbE;
		}
		
}

/*
public class Couplage extends Optimizable {
  int nbV ;  //number of vertices
  public int nbE ;  // number of edges
  private int lastvalue;
  UndirectedGraph g;
  int[][] pairing ;
  
  public Couplage(UndirectedGraph graph) {
	g=graph;
    nbV = g.numberOfVertices();
    nbE = g.numberOfEdges();
    pairing = new int[nbV][nbE] ;
    for (int j =0 ; j<nbV ; j++) {
      for (int k=0 ; k<nbE ; k++){
      pairing[j][k] = -1 ;
      }
    }
  }
  
  public Couplage copy() {
    Couplage c = new Couplage(g) ;
    for (int j =0 ; j<nbV ; j++) {
      for (int k=0; k<nbE ; k++){
      c.pairing[j][k] = this.pairing[j][k] ;
      }
    }
    c.lastvalue=lastvalue;
    c.g=g;
    return c ;
  }
  
  @Override
  public Optimizable mutation(double lambda){
	  int n=g.numberOfEdges();
	  //double p=lambda/n;
	  //int l=binom(n,p)+1;
	  int l=poisson(lambda)+1;
	  Couplage candidate = mutateCouplage(l);
	  int max=candidate.fitness();
	  for(int i=0;i<lambda-1;i++){
	    Couplage temp = mutateCouplage(l);
	    if(temp.fitness()>max){
	      candidate=temp;
	      max=temp.lastvalue;
	    }
	  }
	  candidate.lastvalue=max;
	  return candidate;
  }
  
  @Override
  public Optimizable crossover(Optimizable xpr, double lambda) {
	  double c=1.0/lambda;
	  Couplage candidate=new Couplage(g);
	    int max=-1;
	    for(int i=0;i<lambda;i++){
	      //System.out.println("x") ;
	      //x.println();
	      //System.out.println("xprim") ;
	      //xprim.println();
	      Couplage  temp= crossCouplage(c,(Couplage) xpr);//should be safe if good use
	      //temp.println();
	      //tirages++;
	      int f = temp.fitness() ;
	      if(f>max || max == -1){
	        candidate=temp;
	        max=f;
	      }
	    }
	    candidate.lastvalue=max;
	    //candidate.println() ;
	    return candidate;	  
  }
  
  public Couplage mutateCouplage(int l){
	    Couplage couplage2 = this.copy() ;
	    for (int j=0 ; j<l ; j++ ) {
	    int randVertex = (int) (Math.random()* (double) nbV ) ;
	    IntegerPair p = coupleRandEdges(randVertex) ;
	    int randEdge1 = p.a ;
	    int randEdge2 = p.b ;
	    if (randEdge1 != randEdge2) {
	      couplage2.setCouple(g, randVertex, randEdge1, randEdge2);
	    } }
	    return couplage2 ;
	  }
  
  public void setCouple(UndirectedGraph g, int vertex , int e1, int e2){
    int former1 = pairing[vertex][e1] ;
    int former2 = pairing[vertex][e2] ;
    //System.out.println( former1 + " " + former2 + " " + e1 + " " + e2 );
    pairing[vertex][e1] = e2 ;
    pairing[vertex][e2] = e1 ;
    //now : cleaning up the mess 
    if (former1 != e1) {
      if (former2 != e2) {
        pairing[vertex][former1] = former2 ;
        pairing[vertex][former2] = former1 ;
      }
      else {
        pairing[vertex][former1] = former1 ;
      }
    }
    else {
      if (former2 != e2) { pairing[vertex][former2] = former2 ;}
    }
    //println() ;
  }
  
  public IntegerPair coupleRandEdges(int vertex){
    int a = -1 ; int b = -1 ;
    while (a == -1 ){
      int i = (int) (Math.random()* (double)nbE) ;
      if (pairing[vertex][i] != -1) { a=i; }
    }
    while (b == -1 ){
      int i = (int) (Math.random()* (double)nbE) ;
      if (i != a && pairing[vertex][i] != -1) { b=i; }
    }
    IntegerPair p = new IntegerPair(a,b) ;
    return p ;
  }
  
  public Couplage mutationUniqueCouplage(UndirectedGraph g){
    int randVertex = (int) (Math.random()* (double) nbV ) ;
    IntegerPair p = coupleRandEdges(randVertex) ;
    int randEdge1 = p.a ;
    //System.out.println(randVertex +" " + p.a + " " + p.b);
    int randEdge2 = p.b ;
    if (randEdge1 != randEdge2) {
      Couplage couplage2 = this.copy() ;
      couplage2.setCouple(g, randVertex, randEdge1, randEdge2);
      return couplage2 ;
    }
    else {return null ; }
  }
  
  public void copyPartFrom(int vertex, Couplage c){  //copy a matching for a vertex
    for (int i =0 ; i< nbE; i++){
      pairing[vertex][i] = c.pairing[vertex][i] ;
    }
  }
  
  public Couplage crossCouplage(double c, Couplage xprim){
    Couplage offspring = new Couplage(g) ;
    for (int v= 0 ; v < nbV ; v++){
      if(Math.random() < c) {
        offspring.copyPartFrom(v,this) ;
      }
      else {
        offspring.copyPartFrom(v,xprim) ;
      }
    }
    return offspring ;
  }
  
  
  public int numberOfEdgesAlone(UndirectedGraph g){
    int nb = 0 ;
    for (int j = 0 ; j < nbE ; j++ ){
      IntegerPair e = g.getEdge(j) ;
      if (pairing[e.a][j] == j) { nb ++ ; }
      if (pairing[e.b][j] == j) { nb ++ ; }
    }
    return nb ;
  }
  
  
  public void println(){
    for(int i=0 ; i<nbE;i++){
      System.out.println();
      for (int j=0 ; j< nbV ; j++) {
        System.out.print(pairing[j][i]+ " - ") ;
      }
    }
    System.out.println();
  }

@Override
public int fitness() {
	int f = 0 ;
    int nbAlone = numberOfEdgesAlone(g) ;
    //System.out.println(nbAlone) ;
    f = nbE - nbAlone ;
    if(nbAlone ==0){
      f += lengthCycles() ;
    }
    lastvalue=f;
    return f ;
}

@Override
public int lastvalue() {
	return lastvalue;
}

@Override
public void init() {
	for (int i =0 ; i <nbE ; i++){
		IntegerPair e = g.getEdge(i) ;
		pairing[e.a][i] = i ;
		pairing[e.b][i] = i ;
	} 
	lastvalue=fitness();
}
  
public int mesureCycle(int edge, boolean[] visites){
	  int l = 1 ;
	  int vertex = g.getEdge(edge).a ;
	  int suivant = pairing[vertex][edge] ;
	  //boolean b = false ;
	 // System.out.println(suivant);
	 while(suivant != edge && !visites[suivant]){
	    visites[suivant] = true ;
	    //System.out.println(l + " " + suivant +" " + vertex + " " + edge);
	    //couplage.println();
	    IntegerPair p = g.getEdge(suivant) ;
	    //System.out.println(p.a + " " + p.b);
	    if (p.a == vertex ) { vertex = p.b ; }
	    else { vertex = p.a ; }
	    suivant = pairing[vertex][suivant] ;
	    l++ ;
	    //if (visites[suivant]) { b =true ;}
	  }
	  //System.out.println(l) ;
	  return l ;
	}
	
	public int lengthCycles(){  //return the sum of the (cycles' length)� 
	  // we assume that numberOfEdgesAlone == 0 && couplage is a matching for graph g
	  int l = 0 ;
	  int nbE = g.numberOfEdges() ;
	  boolean[] visites = new boolean[nbE] ;
	  for( int e =0 ; e < nbE ; e++) {
	    if(!visites[e]){
	      visites[e] = true ;
	      int temp = mesureCycle(e,visites) ;
	      l += temp * temp ;
	    }
	  }
	  return l ;
	}
	
	 public int lengthCyclesPrint(){  //return the sum of the (cycles' length)� 
	    // we assume that numberOfEdgesAlone == 0 && couplage is a matching for graph g
	    int l = 0 ;
	    int nbE = g.numberOfEdges() ;
	    boolean[] visites = new boolean[nbE] ;
	    for( int e =0 ; e < nbE ; e++) {
	      if(!visites[e]){
	        visites[e] = true ;
	        int temp = mesureCyclePrint(e,visites) ;
	        l += temp * temp ;
	      }
	    }
	    return l ;
	  }
	
	 public int mesureCyclePrint(int edge, boolean[] visites){
	   HashMap<IntegerPair, Integer> couleurs=new HashMap<IntegerPair, Integer>();
	   //System.out.println("---D�but---") ;
	   //System.out.println("Edge : " + edge) ;
	   couleurs.put(g.getEdge(edge), 3);
	   couleurs.put(new IntegerPair(g.getEdge(edge).b,g.getEdge(edge).a),3);
	   GraphWindow graphWindow=new GraphWindow(600, 600, g,new HashMap<Integer,Integer>(), couleurs, new HashMap<IntegerPair,Float>());
	   int l = 1 ;
	    int vertex = g.getEdge(edge).a ;
	    int v = g.getEdge(edge).a ;
	    //System.out.println("Vertex : " + vertex) ;
	    int suivant = pairing[vertex][edge] ;
	    boolean b = false ;
	    while(suivant != edge && !visites[suivant]){
	      couleurs.put(g.getEdge(suivant), 3);
	      couleurs.put(new IntegerPair(g.getEdge(suivant).b,g.getEdge(suivant).a),3);
	      //System.out.println(suivant) ;
	      visites[suivant] = true ;
	      //System.out.println(l + " " + suivant +" " + vertex + " " + edge);
	      //couplage.println();
	      IntegerPair p = g.getEdge(suivant) ;
	      //System.out.println(p.a + " " + p.b);
	      if (p.a == vertex ) { vertex = p.b ; }
	      else { vertex = p.a ; }
	      //System.out.println("Edge : " + suivant + " " +vertex + " " + p.a + " " + p.b ) ;
	      suivant = pairing[vertex][suivant] ;
	      //System.out.print(" - " + suivant ) ; 
	      l++ ;
	      //if (visites[suivant]) { b =true ; }
	    }
	    //System.out.println(l) ;
	    return l ;
	  } 
	 
		public static int binom(int n,double p){
			int r=0;
			for(int j=0;j<n;j++){
				if(Math.random()<=p){
					r++;
				}
			}
			return r;
		}
		
		public static int poisson(double lambda){
			double t=Math.exp(-lambda);
			int x=0;
			double prod=Math.random();
			while(prod>=t){
				x++;
				prod*=Math.random();
			}
			return x;
		}
}
*/

