package Optimizables;
import java.util.HashSet;

import sat.Formule;


public class BinaryNumberValuation extends BinaryNumber {
	private Formule f;
	
	public BinaryNumberValuation(Formule form) {
		super(form.variables().size());
		f=form;//f is assumed to have good order litterals -- use renameLitterals method
		//f.renameLitterals();//essential for having good ordered litterals
	}
	
	@Override
	public int fitness(){//Count the number of satisfied clauses
			try {
				lastvalue=f.numberOfSatisfiedClauses(x);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return lastvalue;
	}

	@Override
	public int lastvalue(){
		return lastvalue;
	}
	
	@Override
	public BinaryNumberValuation copy(){
		BinaryNumberValuation retour=new BinaryNumberValuation(f);
		retour.x=x.clone();
		retour.numberOfOnes=numberOfOnes;
		retour.lastvalue=lastvalue;
		return retour;
	}
}
