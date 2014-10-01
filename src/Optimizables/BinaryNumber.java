package Optimizables;
import java.util.HashSet;

public class BinaryNumber extends Optimizable {
	public int[] x;
	protected int lastvalue;
	protected int numberOfOnes=0;
	
	public BinaryNumber(int n){
		x=new int[n];
		lastvalue=0;
	}
	
	//BinaryNumber subclass need to overload this method
	public BinaryNumber copy(){
		BinaryNumber retour=new BinaryNumber(x.length);
		retour.x=x.clone();
		retour.numberOfOnes=numberOfOnes;
		retour.lastvalue=lastvalue;
		return retour;
	}
	
	@Override
	public int fitness() {//for onemax function
		lastvalue=numberOfOnes;
		return numberOfOnes;
	}
	
	@Override 
	public int lastvalue(){
		return numberOfOnes;
	}

	@Override
	public void println() {
		String toPrint=new String();
		for(int i=0;i<x.length;i++){
			toPrint+=Integer.toString(x[i]);
		}
		System.out.println(toPrint+" Fitness="+this.fitness()+" "+this.getClass());
	}
	
	@Override
	public void init(){
		numberOfOnes=0;
		x=new int[x.length];//to be sure numberOfOnes=0
		for(int i=0;i<x.length;i++){//generate x randomly
			if(Math.random()>0.5){
				x[i]=1;
				numberOfOnes++;
			} else {
				x[i]=0;
			}
		}
		lastvalue = fitness();
	}
	
	@Override
	public Optimizable mutation(double lambda){//Mutation phase
		int n=x.length; double p=(double) ((int) lambda)/(double) n;
		int l=binom(n,p);
		BinaryNumber candidate = mutate(l);
		int max=candidate.fitness();
		for(int i=0;i<lambda-1;i++){
			BinaryNumber temp=mutate(l);
			if(temp.fitness()>max){
				candidate=temp;
				max=temp.lastvalue;
			}
		}
		candidate.lastvalue=max;
		return candidate;
	}
	
	@Override
	public Optimizable crossover(Optimizable xpr, double lambda) {//Crossover phase
			BinaryNumber xprim=(BinaryNumber) xpr;//Safe if correct use
			double c=1.0/((int) lambda);
			BinaryNumber candidate=this.cross(c, xprim);
			int max=candidate.fitness();
			for(int i=0;i<(int) lambda-1;i++){
				BinaryNumber temp=this.cross(c, xprim);
				if(temp.fitness()>max){
					candidate=temp;
					max=temp.lastvalue;
				}
			}
			candidate.lastvalue=max;
			return candidate;
	}
	
	public void invertBit(int l){
		assert(x.length>l);
		if(x[l]==0){
			numberOfOnes++;
		} else {
			numberOfOnes--;
		}
		x[l]=1-x[l];
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
	
	public BinaryNumber cross(double c, BinaryNumber xprim) {//Elementary crossover
		BinaryNumber retour=copy();
		retour.numberOfOnes=0;
		for(int i=0;i<x.length;i++){
			if(Math.random()<=c){
				retour.x[i]=xprim.x[i];
			}
			if(retour.x[i]==1){
				retour.numberOfOnes++;
			}
		}
		return retour;
	}
	
	public BinaryNumber mutate(int l) {//Elementary mutation
		BinaryNumber retour=copy();
		HashSet<Integer> set=new HashSet<Integer>();//for contains in O(1)
		while(set.size()!=l){
			int n=(int) Math.floor(Math.random()*x.length);//heuristic. Not working if x.length too large
			if(!set.contains(n)){
				set.add(n);
			}
		}
		for(Integer i:set){
			retour.invertBit(i);
		}
		return retour;
	}
	
}
