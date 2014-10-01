import Optimizables.*;
	
	class Optimizer {
	protected double lambda;
	protected Optimizable x;
	protected int current_fitness;
	protected int last_value;//candidate value just before the selection phase
	public int counter;
	public boolean verbose=false;
	
	Optimizer(Optimizable individu){
		x=individu;
		lambda=1.0;
		//current_fitness=0;
	}
		
	//Initialization phase
	public void init(){
		x.init();//supposed to update lastvalue
		current_fitness=x.lastvalue();
		if(verbose){System.out.println("Début");x.println(); }
	}//Optimizable
	
	public void one_iteration(){//universal process
		//Mutation phase
		Optimizable xprim=x.mutation(lambda);
		counter+=(int) lambda;
		if(verbose){
			System.out.println("Mutation");
			xprim.println();
		}
		//Crossover phase
		Optimizable y;
		if((int) lambda>1){//Omit crossover if lambda==1
			counter+=(int) lambda;
			y=x.crossover(xprim, lambda);//crossover is supposed to compute lastvalue
			last_value=y.lastvalue();//if the candidate before the crossover was better than after...
			if(last_value<xprim.lastvalue()){//mutation is supposed to compute lastvalue
				y=xprim;
				last_value=xprim.lastvalue();
			}
			if(verbose){
				System.out.println("Crossover");
				y.println();
			}
		} else {
			y=xprim;
			last_value=xprim.lastvalue();
		}
		//Selection
		selection(y);
		if(verbose){
			System.out.println("Selection");
			x.println();
		}
	}
	
	public void selection(Optimizable y){//can be inherited for self-adaptive methods
		int sy=last_value;
		if(sy>current_fitness){//according to the article, we make one fitness computation for each 
			counter++;
		}
		if(sy>=current_fitness){//Be careful : >= is really important
			x=y;
			current_fitness=sy;
		}
	}
	
	public int current_fitness(){
		return current_fitness;
	}

}
