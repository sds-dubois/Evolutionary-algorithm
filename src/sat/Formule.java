package sat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class Formule {
	HashSet<Clause> clauses=new HashSet<Clause>();
	
	public void addClause(Clause c){
		clauses.add(c);
	}
	
	public HashSet<Integer> variables(){//Return all the variables involved in the Formula
		int result=0;
		HashSet<Integer> variables=new HashSet<Integer>();
		for(Clause c:clauses){
			variables.addAll(c.variables());
		}
		return variables;
	}
	
	public boolean evaluate(Valuation v) throws Exception {
		boolean result=true;
		for(Clause c:clauses){
			result=result && c.evaluate(v);
		}
		return result;
	}
	
	public boolean evaluate(int[] v) throws Exception {
		boolean result=true;
		for(Clause c:clauses){
			result=result && c.evaluate(v);
		}
		return result;
	}
	
	public int numberOfSatisfiedClauses(int[] v) throws Exception{
		int result=0;
		for(Clause c:clauses){
			if(c.evaluate(v)){
				result++;
			}
		}
		return result;
	}
	
	public void println(){
		Iterator<Clause> cl=clauses.iterator();
		if(cl.hasNext()){
			System.out.print("(");
			cl.next().print();
			System.out.print(")");
		}
		while(cl.hasNext()){
			System.out.print("∧(");
			cl.next().print();
			System.out.print(")");
		}
		System.out.println();
	}
	
	public int numberOfClauses(){
		return clauses.size();
	}
	
	public static Formule generate(int numberOfLitterals,int sizeOfClauses,int numberOfClauses){//generate random formula
		Formule f=new Formule();
		for(int i=0;i<numberOfClauses;i++){
			Clause c=new Clause();
			for(int j=0;c.variables().size()<sizeOfClauses;j++){
				int index=(int) (Math.random()*numberOfLitterals);
				boolean neg=Math.random()>0.5;
				Litteral l=new Litteral(index,neg);
				if(!c.variables().contains(l.index)){
					c.addLitteral(l);
				}
			}
			f.addClause(c);
		}
		f.renameLitterals();
		return f;
	}
	
	public static Formule generate2(int numberOfLitterals,int sizeOfClauses,int numberOfClauses){//generate number formula with 
		//an ensured satisfiability
		Formule f=new Formule();
		for(int i=0;i<numberOfClauses;i++){
			Clause c=new Clause();
			c.addLitteral(new Litteral(i));//ensure there will be a satisfying valuation
			for(int j=0;c.variables().size()<sizeOfClauses;j++){
				int index=(int) (Math.random()*numberOfLitterals);
				boolean neg=Math.random()>0.5;
				Litteral l=new Litteral(index,neg);
				if(!c.variables().contains(l.index)){
					c.addLitteral(l);
				}
			}
			f.addClause(c);
		}
		f.renameLitterals();
		return f;
	}
	
	public void renameLitterals(){//rename all litterals from 0 to numberoflitterals-1 
		HashSet<Integer> variables=variables();
		LinkedHashMap<Integer,Integer> variablesMap=new LinkedHashMap<Integer,Integer>();
		for(Integer v:variables){
			variablesMap.put(v, variablesMap.size());
		}
		for(Clause c:clauses){
			for(Litteral l:c.litteraux){
				l.index=variablesMap.get(l.index);
			}
		}
	}
}
