package sat;

public class Litteral {
	int index;
	boolean negation=false;
	
	Litteral(int i){
		index=i;
	}
	
	public Litteral(int i,boolean b){
		index=i;
		negation=b;
	}
	
	public boolean evaluate(Valuation v) throws Exception{
		return (negation && !v.get(index)) || (!negation && v.get(index));
	}
	
	public boolean evaluate(int[] v) throws Exception{
		if(v.length<=index){
			throw new Exception("Erreur : la valuation est partielle.");
		}
		return (negation && !(v[index]==1)) || (!negation && (v[index]==1));
	}
	
	public void print(){
		if(negation){
			System.out.print("¬");
		}
		System.out.print("x"+index);
	}
}
