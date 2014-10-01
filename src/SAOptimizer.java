import Optimizables.Optimizable;

public class SAOptimizer extends Optimizer {
	private double mul;
	private double F;
	private boolean infinite_authorized=true;
	private int maxlambda;
	
	SAOptimizer(Optimizable individu){
		super(individu);
		F=1.5;
		mul=Math.pow(F, 1.0/4);
	}
	
	SAOptimizer(Optimizable individu,int rule){
		super(individu);
		F=1.5;
		mul=Math.pow(F,1.0/(double) rule);
	}
	
	SAOptimizer(Optimizable individu,int rule, int maxlamb){
		super(individu);
		F=1.5;
		mul=Math.pow(F,1.0/(double) rule);
		maxlambda=maxlamb;
		infinite_authorized=false;
	}
	
	@Override
	public void selection(Optimizable y){
		int sy=last_value;
		if(sy>current_fitness){//Be careful : >= is really important
			x=y;
			counter++;
			current_fitness=sy;
			lambda/=F;//Self adaptive behavior
		} else {
			if(sy==current_fitness){
				x=y;//important to explore more behaviors
			} //else {//lambda doesn't change if the current_fitness hasn't changed
			lambda*=mul;//Self adaptive behavior. Important to widen lambda if current_value hasn't changed
			//Remark : this can make the number of tests explode
			//}
		}
		if(lambda<1){
			lambda=1;
		}
		if(!infinite_authorized && lambda>maxlambda){
			lambda=maxlambda;
		}
		
		if(verbose){
			System.out.println("Lambda="+lambda);
		}
	}
}