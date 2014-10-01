package sat;

import java.util.LinkedHashMap;

public class Valuation {//The use of this class can be avoided
	private LinkedHashMap<Integer,Boolean> v;
	
	public Valuation(){
		v=new LinkedHashMap<Integer,Boolean>();
	}
	
	public void addEntry(int index,boolean value){
		v.put(index, value);
	}
	
	public boolean get(int index) throws Exception{
		if(v.containsKey(index)){
			return v.get(index);
		} else {
			throw new Exception("Erreur : la valuation est partielle.");
		}
	}
	
	
}
