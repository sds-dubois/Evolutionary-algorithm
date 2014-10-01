import Optimizables.Optimizable;

public class GAOptimizer extends Optimizer {
	GAOptimizer(Optimizable individu) {
		super(individu);
	}
	
	GAOptimizer(Optimizable individu, double l){
		super(individu);
		assert l>=1;
		this.lambda=l;
	}
	
}
