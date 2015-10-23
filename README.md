# Evolutionary Algorithms (INF431 Project)

This project deals with Evolutionary Algorithms which are a kind of optimization algorithms inspired by
biological evolution. The potential solutions are here individuals that evolve: they go through
mutations, crossovers and even a selection step. The selection is done by maximizing a fitness function that
determinates the quality of the potential solutions.

The goal of this project is to implement Evolutionary algorithms in Java, and to test/compare different methods on specific optimization problems (see the [PDF report](https://github.com/sds-dubois/Evolutionary-algorithm/blob/master/projetDuboisFeppon.pdf) for more details).
The algorithmic problems tackled with these algorithms are :
- Maximum matchings
- Eulerian cycles
- Hamiltonian cycles
- SAT

A general API is implemented to support different variant of evolutionary algorithms. The code also contains a graph structure as well as simple visualization tool to see those algorithms in action.
