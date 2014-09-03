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
	 * Sets the score corresponding to the fitness of an individual in a
	 * population and returns the previous value. How Fitness is defined will
	 * differ from problem to problem.
	 * 
	 * @param score
	 *            The fitness score value to replace the old one.
	 * @return The score corresponding to the fitness of an individual in a
	 *         population.
	 *
	 */
	public double setFitnessScore(double fitnessScore) {
		double previousValue = this.fitnessScore;
		this.fitnessScore = fitnessScore;
		return previousValue;
	}

	/**
	 * Retrieves the current fitnessScore value of {@code this}.
	 * 
	 * @return The fitness score of {@code this}.
	 */
	public double getFitnessScore() {
		return this.fitnessScore;
	}

	/**
	 * Applies a mutation to the genetic sequence of {@code this}, with the
	 * probability of mutation set to {@code mutationRate}. Whether mutation is
	 * applied to a single gene in the genetic sequence of {@code this} or to
	 * the entire genetic sequence is problem dependent and up to the
	 * implementer.
	 * 
	 * @param mutationRate
	 */
	public abstract void mutate(double mutationRate);

	/**
	 * An instance of {@code this} selects a point (or points depending on the
	 * choice of implementation) at which its genetic sequence is split and
	 * crossed over with the genetic sequence of {@code mate}.
	 * 
	 * @param mate
	 *            The {@code Evolvable} mate for {@code this} with which
	 *            {@code this} will exchange genes.
	 */
	public abstract void exchangeGenes(Evolvable mate);

	public abstract boolean equals(Object other);

	public abstract int hashCode();
}
