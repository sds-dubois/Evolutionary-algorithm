package Optimizables;
public abstract class Optimizable {
	public Optimizable mutation(double lambda){return null;}//Perform mutation phase
	public Optimizable crossover(Optimizable xprim, double lambda){return null;};//Perform crossover phase
	public abstract int fitness();//Compute the fitness of the function
	abstract public int lastvalue();//return the last value computed by the fitness function
	abstract public void println();//display the Optimizable
	abstract public void init();//initialization 
}
