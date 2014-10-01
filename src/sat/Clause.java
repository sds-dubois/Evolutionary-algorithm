package sat;

import java.util.HashSet;
import java.util.Iterator;

public class Clause {
	HashSet<Litteral> litteraux=new HashSet<Litteral>();
	
	public void addLitteral(Litteral l){
		litteraux.add(l);
	}
	
	public boolean evaluate(Valuation v) throws Exception{
		boolean result=false;
		Iterator<Litteral> litts=litteraux.iterator();
		while(!result && litts.hasNext()){
			result=litts.next().evaluate(v);
		}
		return result;
	}
	
	public boolean evaluate(int[] v) throws Exception{
		boolean result=false;
		Iterator<Litteral> litts=litteraux.iterator();
		while(!result && litts.hasNext()){
			result=litts.next().evaluate(v);
		}
		return result;
	}
	
	
	public void print(){
		Iterator<Litteral> litts=litteraux.iterator();
		if(litts.hasNext()){
			litts.next().print();
		}
		while(litts.hasNext()){
			System.out.print("∨");
			litts.next().print();
		}
	}
	
	public HashSet<Integer> variables(){
		HashSet<Integer> litts=new HashSet<Integer>();//necessary to count the number of variables and not the number of litterals 
		for(Litteral litt:litteraux){
				litts.add(litt.index);
		}
		return litts;
	}
}



