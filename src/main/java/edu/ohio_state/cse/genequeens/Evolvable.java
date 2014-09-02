package edu.ohio_state.cse.genequeens;

/**
 * This interface dictates the expected behavior of classes that will be used in
 * {@link edu.ohio_state.cse.genequeens.GeneticAlgorithms GeneticAlgorithms}.
 * Every implementation of these methods will be very problem-dependent and may
 * require further extension in the event that the behavior noted here is not
 * sufficient to meet some use-case.
 * 
 * @author Ethan Hill
 * @see GeneticAlgorithms
 */
public abstract class Evolvable implements Comparable<Evolvable> {

	protected double fitnessScore;

	/**
	 * Calculates and returns the score corresponding to the fitness of an
	 * individual in a population. How Fitness is defined will differ from
	 * problem to problem.
	 * 
	 * @return The score corresponding to the fitness of an individual in a
	 *         population.
	 */
	public abstract double evaluateFitness();

	/**
	 * Sets (and returns) the score corresponding to the fitness of an
	 * individual in a population. How Fitness is defined will differ from
	 * problem to problem.
	 * 
	 * @param score
	 * @return The score corresponding to the fitness of an individual in a
	 *         population.
	 *
	 */
	public double setFitnessScore(double fitnessScore) {
		this.fitnessScore = fitnessScore;
		return fitnessScore;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getFitnessScore(){
		return this.fitnessScore;
	}

	/**
	 * 
	 * @param mutationRate
	 */
	public abstract void mutate(double mutationRate);

	/**
	 * 
	 * @param mate
	 */
	public abstract void exchangeGenes(Evolvable mate);
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean equals();

	/**
	 * @return 
	 */
	public abstract int hashCode();
}
