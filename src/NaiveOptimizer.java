import Optimizables.Optimizable;

//Algorithm which just try random candidates.
public class NaiveOptimizer extends Optimizer {

	NaiveOptimizer(Optimizable individu) {
		super(individu);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void one_iteration(){
		x.init();
		if(x.fitness()>current_fitness){
			current_fitness=x.lastvalue();
		}
		x.println();
		counter++;
	}
}
