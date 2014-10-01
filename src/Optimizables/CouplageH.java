package Optimizables;

import graph.GraphWindow;
import graph.IntegerPair;
import graph.UndirectedGraph;

import java.util.HashMap;


public class CouplageH extends Optimizable {
  UndirectedGraph g ;
  private int nbV ;  //number of vertices
  int nbE ;  // number of edges
  private int lastvalue ;
  int[][] structure ;
  IntegerPair[] pairing ;
  
  public CouplageH(UndirectedGraph graphe ) {
    g = graphe ;
    setNbV(g.numberOfVertices()) ;
    nbE = g.numberOfEdges() ;
    pairing = new IntegerPair[getNbV()] ;
    for (int j =0 ; j<getNbV() ; j++) {
      pairing[j] = new IntegerPair(-1,-1) ;
    }
  }
  
  public void init(){    //init pairing 
    for (int j =0 ; j<getNbV() ; j++) {
      pairing[j] = new IntegerPair(-1,-1) ;
      }
    lastvalue = fitness() ;
  }
  
  public Optimizable mutation(double lambda){
    int l=poisson(lambda)+1;
    //System.out.println("L : " + l + " lambda : " + lambda) ;
    CouplageH candidate = mutateCouplage(l);
    //x.println() ;
    int max=candidate.fitness();
    for(int i=0;i<lambda-1;i++){
      CouplageH temp = mutateCouplage(l);
      //tirages++;
      //temp.println();
      if(temp.fitness()>max){
        candidate=temp;
        max=temp.lastvalue;
      }
    }
    candidate.lastvalue = max ;
    //candidate.println();
    return candidate;
  }
  
  /* public static int poisson(double lambda){
    double t=Math.exp(-lambda);
    int x=0;
    double prod=Math.random();
    while(prod>=t){
      x++;
      prod*=Math.random();
    }
    return x;
  } */
  
  public Optimizable crossover(Optimizable xprim, double lambda){
    CouplageH candidate=new CouplageH(g);
    double c = 1.0/lambda ;
    int max = candidate.lastvalue() ;
    for(int i=0;i<lambda;i++){
      CouplageH temp= CouplageH.crossCouplage(c,this,(CouplageH) xprim);
      //temp.println();
      //tirages++;
      int f = temp.fitness() ;
      if(f>max){
        candidate=temp;
        max=f;
      }
      else {candidate = this ; max = this.lastvalue() ;}
    }
    candidate.lastvalue = max ;
    return candidate;
  }
    
public CouplageH copy() {
  CouplageH c = new CouplageH(g) ;
  c.init();
  for (int j =0 ; j<getNbV() ; j++) {
    c.pairing[j] = this.pairing[j] ;
    // #A_supprimer
    /*for (int k=0; k<nbE ; k++){
    c.structure[j][k] = this.structure[j][k] ;
    } */
  }
  c.g = g ;
  c.lastvalue = lastvalue ;
  return c ;
}  

public IntegerPair coupleRandEdges(int vertex){
  int a = -1 ; int b = -1 ;
  while (a == -1 ){
    int i = (int) (Math.random()* (double)nbE) ;
    if (structure[vertex][i] != -1) { a=i; }
  }
  while (b == -1 ){
    int i = (int) (Math.random()* (double)nbE) ;
    if (i != a && structure[vertex][i] != -1) { b=i; }
  }
  IntegerPair p = new IntegerPair(a,b) ;
  return p ;
}

public void setEdge(int e , int v){
  IntegerPair p = pairing[v] ;
  if (p.a == -1) {
    pairing[v] = new IntegerPair(e,p.b) ;
  }
  else if (p.b == -1) {
    pairing[v] = new IntegerPair(p.a,e) ;
  }
  else if (Math.random()* (double) 2 < 1) {
    if (e != p.b) {pairing[v] = new IntegerPair(e,p.b) ;} //edges have to be different for the pairing
  }
  else {
    if (e != p.a) {pairing[v] = new IntegerPair(p.a,e) ;}  //edges have to be different for the pairing
  }
}

public CouplageH mutateCouplage(int l){
  CouplageH couplage2 = this.copy() ;
  for (int i=0 ; i < l ; i++) {
    int randEdge = (int) (Math.random() * (double) nbE ) ;
    //System.out.println("randE : " + randEdge) ;
    if (Math.random()* (double) 2 >= 1) {
      couplage2.setEdge(randEdge,g.getEdge(randEdge).a) ;
      //System.out.println("1 - sommet : " + g.getEdge(randEdge).a) ;
    }
    else {
      couplage2.setEdge(randEdge,g.getEdge(randEdge).b) ;
      //System.out.println("2 - sommet : " + g.getEdge(randEdge).b) ;
    }
  }
  return couplage2 ;
}
 
public static CouplageH crossCouplage(double c, CouplageH x, CouplageH xprim){
  CouplageH offspring = x.copy() ;
  for (int v= 0 ; v < x.getNbV() ; v++){
    if(Math.random() < c) {
      offspring.pairing[v] = xprim.pairing[v] ;
    }
  }
  return offspring ;
}

public void println() {
  //GraphWindow graphWindow=new GraphWindow(600, 600, g,new HashMap<Integer,Integer>(), new HashMap<IntegerPair, Integer>() , new HashMap<IntegerPair,Float>());
  for(int i = 0; i < getNbV(); i++) {
    System.out.print(pairing[i].a + "," + pairing[i]. b + " - ");
  }
  System.out.println();
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



public int fitness() {
  return fitness3() ;
}

int fitness1() {
  lastvalue = lengthPaths();
  return lastvalue ;
}

int fitness3() {
  int l = lengthPaths2() ;
  lastvalue = l;
  return lastvalue ;
}

int fitness2() {
  int f = 0 ;
  int[] t = new int[nbE] ;
  for (int i=0; i< getNbV() ; i++) {
    IntegerPair p = pairing[i] ;
    if (p.a != -1 && p.b != -1) {
      if (t[p.a] != -1) {t[p.a] = -1 ; f ++ ; 
      //System.out.println("int : " + i + " p.a =" + p.a) ; 
      }
      if (t[p.b] != -1) {t[p.b] = -1 ; f ++ ; 
      //System.out.println("int : " + i + " p.b =" + p.b) ;
      }
    }
    else { f += getNbV() ;}
  }
  f = nbE - f ;
  if (f == nbE -getNbV()) { 
    if  (mesureChemin(0,new boolean[getNbV()])==(getNbV()+1)) { f+= getNbV(); }
    else {
      f = f -1;
    }
  }
  lastvalue = f ;
  return f ;
}

@Override
public int lastvalue() {
  return lastvalue;
}


public int lengthPaths(){  // return the sum of the (paths' length)� (in terms of vertices)
  int l = 0 ;
  int k = 0 ;
  boolean[] visites = new boolean[getNbV()] ;
  for( int v =0; v < getNbV(); v++) {
    if(!visites[v]){
      int temp = mesureChemin(v,visites) ;
      //System.out.println("length path : " + temp) ;
      if (temp == 0) { k++ ; }
      l += temp*temp ;
    }
  }
  return (l - (k*getNbV())) ;
}


public int mesureChemin(int v, boolean[] visites){
  int l = 0 ;
  int e =0;
  //System.out.println("---D�but1---") ;
  //System.out.println("D�part: " + v) ;
  int suivant;
  Boolean b = true;
  int edge = pairing[v].a ;
  int edge2 = pairing[v].b ;
  if (edge != -1 && edge2 != -1) {
    IntegerPair p = g.getEdge(edge) ;            
    //l++ ;
    if (p.a == v) {suivant = p.b ;}
    else { suivant = p.a ; }
   while(!visites[suivant] && b && suivant != v){    //on explore d'un c�t�
      //System.out.print(suivant) ;
      visites[suivant] = true ;
      IntegerPair q = pairing[suivant] ;
      b = false ;
      if (q.a == edge) {e = q.b ; b = true ;}
      if (q.b == edge) {e = q.a ; b = true ;}
      if (q.a == -1 || q.b == -1) {b = false ;}
      if (b) {
        edge = e ;
        p= g.getEdge(edge) ;
        if (p.a == suivant) {suivant = p.b ;}
        else { suivant = p.a ; }
        l++ ;
      }
      //System.out.println(" - " + suivant) ;
    } 
   if (suivant == v && (pairing[v].a == edge || pairing[v].b == edge )) { l+= 2 ; }
      //System.out.println("D�part2: ") ;
      b = true ;
      edge = edge2 ;                 //on explore l'autre c�t�
      p = g.getEdge(edge) ;
      if (p.a == v) {suivant = p.b ;}
      else { suivant = p.a ; }
      while(suivant != v && !visites[suivant] && b){  
        //System.out.print(suivant) ;
        visites[suivant] = true ;
        IntegerPair q = pairing[suivant] ;
        b = false ;
        if (q.a == edge) {e = q.b ; b = true ; }
        if (q.b == edge) {e = q.a ; b = true ;}
        if (q.a == -1 || q.b == -1) {b = false ;}
        if (b) {
          edge = e ;
          p= g.getEdge(edge) ;
          if (p.a == v) {suivant = p.b ;}
          else { suivant = p.a ; }
          l++ ;
        }
        //System.out.println(" - " + suivant) ;
      }
  }
  visites[v] = true ;
  //System.out.println(l) ;
  return l ;
}




public int lengthPaths2(){  // return the sum of the (paths' length)� (in terms of vertices)
  int l = 0 ;
  int k = 0 ;
  boolean[] visites = new boolean[getNbV()] ;
  for( int v =0; v < getNbV(); v++) {
    if (!visites[v]) {
      int temp = mesureChemin(v,visites) ;
      //System.out.println("length path : " + temp) ;
      if (temp ==0) { k++ ;}
      if (l< temp) { l = temp ; }
    } 
  }
  return (l - (k*getNbV())) ;
}


public void printGraph() {
  HashMap<IntegerPair, Integer> couleurs=new HashMap<IntegerPair, Integer>();
  GraphWindow graphWindow=new GraphWindow(600, 600, g,new HashMap<Integer,Integer>(), couleurs, new HashMap<IntegerPair,Float>());
  for (int v = 0 ; v < getNbV() ; v ++) {
    IntegerPair p = pairing[v] ;
    if(p.a != -1){
    couleurs.put(new IntegerPair(g.getEdge(p.a).a,g.getEdge(p.a).b),5);
    couleurs.put(new IntegerPair(g.getEdge(p.b).a,g.getEdge(p.b).b),5);
    couleurs.put(new IntegerPair(g.getEdge(p.a).b,g.getEdge(p.a).a),5);
    couleurs.put(new IntegerPair(g.getEdge(p.b).b,g.getEdge(p.b).a),5);
    }
  }
}

public int getNbV() {
	return nbV;
}

public void setNbV(int nbV) {
	this.nbV = nbV;
}
}