package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class provides a set of utilities for use with Genetic Algorithms as
 * applied to populations of {@link edu.ohio_state.cse.genequeens.Evolvable
 * Evolvable} individuals.
 * 
 * @author EthanHill
 *
 */
public class GeneticAlgorithms {
	/**
	 * The default probability that a random gene in the genetic sequence of an
	 * individual gets changed to another gene. Based on optimal parameters
	 * found by De Jong and Spears in "An Analysis of the Interacting Roles of
	 * Population Size and Crossover in Genetic Algorithms".
	 */
	private static final double MUTATION_RATE_DEJONG = 0.001d;

	private static final int CONVERGENCE_THRESHOLD = 10;

	/**
	 * Totals the fitnessScores of all individuals that appear in
	 * {@code population}.
	 * 
	 * @param population
	 *            The population whose total fitness score is to be calculated.
	 * @return The total fitness score of {@code population}.
	 */
	public static double evaluatePopulation(Collection<Evolvable> population) {
		assert !population.isEmpty() : "population is empty";

		double populationFitness = 0.0d;
		for (Evolvable member : population) {
			populationFitness += member.evaluateFitness();
		}
		return populationFitness;
	}

	/**
	 * Retrieves the most fit individual found in a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals.
	 * The definition of "most fit" is dependent on the problem and how an
	 * {@code Evolvable} implements {@link java.lang.Comparable Comparable}.
	 * 
	 * @param population
	 *            The population from which the most fit individual is selected.
	 * @return An instance of the most fit individual from {@code population}.
	 */
	public static Evolvable findMostFitIndividual(
			Collection<Evolvable> population) {
		assert !population.isEmpty() : "population is empty";

		List<Evolvable> list = new ArrayList<Evolvable>(population);
		Collections.sort(list);
		// Get the topmost individual
		Evolvable mostFitIndividual = list.get(0);
		return mostFitIndividual;
	}

	/**
	 * Creates the next generation of individuals by selecting mates for,
	 * exchanging genes between, and mutating the result of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals
	 * found in {@code population}. Mates are selected from {@code population}
	 * using an instance of a {@link edu.ohio_state.cse.genequeens.MateSelector
	 * MateSelector} implementation. Mutation is applied with a probability of
	 * {@code mutationRate} to the genetic sequences of the resulting
	 * individuals of genetic exchange.
	 * 
	 * @param population
	 *            The population from which a new generation is formed.
	 * @param mateSelector
	 *            An implementation of {@code MateSelector} that chooses a mate
	 *            for an individual from {@code population}.
	 * @param mutationRate
	 *            The probability at which a mutation is applied to the genes of
	 *            individuals in {@code population} after genetic exchange.
	 *            Behavior is not well defined for mutation rates of zero or
	 *            less.
	 * @return A collection of {@code Evovlable}s generated from
	 *         {@code population}.
	 */
	public static Collection<Evolvable> createNextGeneration(
			Collection<Evolvable> population,
			MateSelector<Evolvable> mateSelector, double mutationRate) {
		assert !population.isEmpty() : "population is empty";

		Collection<Evolvable> nextGeneration = new ArrayList<Evolvable>();
		List<Evolvable> listCopy = new ArrayList<Evolvable>(population);

		while (!listCopy.isEmpty()) {
			// Just pick the top individual from the list
			Evolvable individual = listCopy.remove(0);
			Evolvable mate = mateSelector.selectMate(individual, population);
			if (individual.equals(mate)) {
				// Add and do nothing, there is no mate for individual
				nextGeneration.add(individual);
			} else {
				// Remove the mate from the list
				listCopy.remove(mate);

				// Perform gene crossover on the two individuals
				individual.exchangeGenes(mate);
				// Mutate the two resulting individuals
				individual.mutate(mutationRate);
				mate.mutate(mutationRate);

				// Add the new individuals to the new population
				nextGeneration.add(individual);
				nextGeneration.add(mate);
			}
		}
		return nextGeneration;
	}

	/**
	 * SemiStochasticMostFitSelector is intended to provide a MateSelector
	 * implementation in which mates are greedily selected by fitness, with some
	 * probability that a random mate is selected instead. An individual in a
	 * population has a mate selected whose fitness is closest to the
	 * individual's own fitness.
	 * 
	 * @author EthanHill
	 *
	 */
	public static class SemiStochasticMostFitSelector implements
			MateSelector<Evolvable> {
		private static final double STOCHASTIC_SELECTION_PROBABILITY = 0.10d;

		public Evolvable selectMate(Evolvable individual,
				Collection<Evolvable> population) {
			assert population.contains(individual) : String.format(
					"population does not contain individual %s",
					individual.toString());
			assert !population.isEmpty() : "population is empty";
			if (population.size() == 1) {
				// No mate exists for the individual
				return individual;

			} else {
				/*
				 * Here we try to simulate sexual selection. Individuals will
				 * tend to pick partners closest to their fitness, but with some
				 * probability, they could pick a random individual as a mate.
				 */
				List<Evolvable> list = new ArrayList<Evolvable>(population);
				Random rand = new Random();
				int mateIndex;
				if (rand.nextDouble() <= STOCHASTIC_SELECTION_PROBABILITY) {
					int individualIndex = list.indexOf(individual);
					mateIndex = rand.nextInt() * (population.size());
					while (mateIndex == individualIndex) {
						mateIndex = rand.nextInt() * (population.size());
					}
				} else {
					Collections.sort(list);
					int individualIndex = list.indexOf(individual);
					// If individual is last element in list of size > 1,
					// select mate just before individual
					if (individualIndex == list.size() - 1) {
						mateIndex = individualIndex--;
					} else {
						mateIndex = individualIndex++;
					}
				}
				return list.get(mateIndex);
			}
		}
	}

	/**
	 * Applies the genetic algorithm to a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals,
	 * producing an approximation to the problem at hand (dependent on the
	 * implementation of {@code Evolvable}.
	 * 
	 * This implementation makes use of default mutation rates and continues
	 * until population fitness does not improve over a fixed number of
	 * generations {@link #CONVERGENCE_THRESHOLD generations}.
	 * 
	 * @param population
	 *            The population from which a solution is drawn by this method.
	 * @return A collection of of n-best individuals that result from this
	 *         implementation of a genetic algorithm.
	 */
	public static Collection<Evolvable> evolve(Collection<Evolvable> population) {
		// Snapshot of best seen population
		Collection<Evolvable> populationSnapshot = population;

		// Initial book-keeping
		double bestFitness = evaluatePopulation(population);
		Evolvable bestIndividual = findMostFitIndividual(population);

		int convergenceCount = 0;

		while (convergenceCount < CONVERGENCE_THRESHOLD) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector(), MUTATION_RATE_DEJONG);

			// Evaluate
			double totalFitness = evaluatePopulation(population);
			Evolvable mostFitIndividual = findMostFitIndividual(population);

			// Check for convergence
			if (mostFitIndividual.getFitnessScore() > bestIndividual
					.getFitnessScore()) {
				// Saw a better individual, reset convergence counter
				convergenceCount = 0;
				// We aren't interested in the best unless population is better
				if (totalFitness > bestFitness) {
					bestIndividual = mostFitIndividual;
					bestFitness = totalFitness;
					populationSnapshot = population;
				}
			} else {
				convergenceCount++;
			}
		}
		return populationSnapshot;
	}

	/**
	 * Applies the genetic algorithm to a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals,
	 * producing an approximation to the problem at hand (dependent on the
	 * implementation of {@code Evolvable}. After {@code maxGenerations} pass,
	 * whether this algorithm has found a solution or converged or neither, the
	 * algorithm "gives up" and returns an n-best list.
	 * 
	 * @param population
	 *            The population from which a solution is drawn by this method.
	 * @param maxGenerations
	 *            The maximum number of iterations before the genetic algorithm
	 *            "gives up" and returns an n-best list of individuals.
	 * @return A collection of of n-best individuals that result from this
	 *         implementation of a genetic algorithm.
	 */
	public static Collection<Evolvable> evolve(
			Collection<Evolvable> population, int maxGenerations) {
		// Snapshot of best seen population
		Collection<Evolvable> populationSnapshot = population;

		// Initial book-keeping
		double bestFitness = evaluatePopulation(population);
		Evolvable bestIndividual = findMostFitIndividual(population);

		int generationCount = 0;

		while (generationCount < maxGenerations) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector(), MUTATION_RATE_DEJONG);

			// Evaluate
			double totalFitness = evaluatePopulation(population);
			Evolvable mostFitIndividual = findMostFitIndividual(population);

			// Check for convergence
			if (mostFitIndividual.getFitnessScore() > bestIndividual
					.getFitnessScore()) {
				// We aren't interested in the best unless population is better
				if (totalFitness > bestFitness) {
					bestIndividual = mostFitIndividual;
					bestFitness = totalFitness;
					populationSnapshot = population;
				}
			}
			generationCount++;
		}
		return populationSnapshot;
	}

	/**
	 * Applies the genetic algorithm to a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals,
	 * producing an approximation to the problem at hand (dependent on the
	 * implementation of {@code Evolvable}. There is also a specified mutation
	 * rate, a probability that the genetic sequence of an individual is mutated
	 * after genetic exchange.
	 * 
	 * This implementation makes use of default mutation rates and continues
	 * until population fitness does not improve over a fixed number of
	 * generations {@link #CONVERGENCE_THRESHOLD generations}.
	 * 
	 * @param population
	 *            The population from which a solution is drawn by this method.
	 * @param mutationRate
	 *            The probability that the genetic sequence of an
	 *            {@code Evolvable} individual gets mutated after genetic
	 *            exchange with a mate.
	 * @return A collection of of n-best individuals that result from this
	 *         implementation of a genetic algorithm.
	 */
	public static Collection<Evolvable> evolve(
			Collection<Evolvable> population, double mutationRate) {
		// Snapshot of best seen population
		Collection<Evolvable> populationSnapshot = population;

		// Initial book-keeping
		double bestFitness = evaluatePopulation(population);
		Evolvable bestIndividual = findMostFitIndividual(population);

		int convergenceCount = 0;

		while (convergenceCount < CONVERGENCE_THRESHOLD) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector(), mutationRate);

			// Evaluate
			double totalFitness = evaluatePopulation(population);
			Evolvable mostFitIndividual = findMostFitIndividual(population);

			// Check for convergence
			if (mostFitIndividual.getFitnessScore() > bestIndividual
					.getFitnessScore()) {
				// Saw a better individual, reset convergence counter
				convergenceCount = 0;
				// We aren't interested in the best unless population is better
				if (totalFitness > bestFitness) {
					bestIndividual = mostFitIndividual;
					bestFitness = totalFitness;
					populationSnapshot = population;
				}
			} else {
				convergenceCount++;
			}
		}
		return populationSnapshot;
	}

	/**
	 * Applies the genetic algorithm to a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals,
	 * producing an approximation to the problem at hand (dependent on the
	 * implementation of {@code Evolvable}. After {@code maxGenerations} pass,
	 * whether this algorithm has found a solution or converged or neither, the
	 * algorithm "gives up" and returns an n-best list. There is also a
	 * specified mutation rate, a probability that the genetic sequence of an
	 * individual is mutated after genetic exchange.
	 * 
	 * @param population
	 *            The population from which a solution is drawn by this method.
	 * @param maxGenerations
	 *            The maximum number of iterations before the genetic algorithm
	 *            "gives up" and returns an n-best list of individuals.
	 * @param mutationRate
	 *            The probability that the genetic sequence of an
	 *            {@code Evolvable} individual gets mutated after genetic
	 *            exchange with a mate.
	 * @return A collection of of n-best individuals that result from this
	 *         implementation of a genetic algorithm.
	 */
	public static Collection<Evolvable> evolve(
			Collection<Evolvable> population, int maxGenerations,
			double mutationRate) {
		// Snapshot of best seen population
		Collection<Evolvable> populationSnapshot = population;

		// Initial book-keeping
		double bestFitness = evaluatePopulation(population);
		Evolvable bestIndividual = findMostFitIndividual(population);

		int generationCount = 0;

		while (generationCount < maxGenerations) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector(), mutationRate);

			// Evaluate
			double totalFitness = evaluatePopulation(population);
			Evolvable mostFitIndividual = findMostFitIndividual(population);

			// Check for convergence
			if (mostFitIndividual.getFitnessScore() > bestIndividual
					.getFitnessScore()) {
				// We aren't interested in the best unless population is better
				if (totalFitness > bestFitness) {
					bestIndividual = mostFitIndividual;
					bestFitness = totalFitness;
					populationSnapshot = population;
				}
			}
			generationCount++;
		}
		return populationSnapshot;
	}

	/**
	 * Applies the genetic algorithm to a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals,
	 * producing a solution to the problem at hand (dependent on the
	 * implementation of the
	 * {@link edu.ohio_state.cse.genequeens.EvolutionaryGoal goal}).
	 * 
	 * @param population
	 *            The population from which a solution is drawn by this method.
	 * @param goal
	 *            The problem-specific implementation of an
	 *            {@code EvolutionaryGoal} which verifies whether the population
	 *            satisfies some requirements to be considered a solution to a
	 *            problem.
	 * @return A collection of of n-best individuals that result from this
	 *         implementation of a genetic algorithm.
	 */
	public static Collection<Evolvable> evolve(
			Collection<Evolvable> population, EvolutionaryGoal goal) {
		evaluatePopulation(population);
		while (!goal.isSatisfied(population)) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector(), MUTATION_RATE_DEJONG);
			evaluatePopulation(population);
		}
		return population;
	}

	/**
	 * Applies the genetic algorithm to a population of
	 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable} individuals,
	 * producing a solution to the problem at hand (dependent on the
	 * implementation of the
	 * {@link edu.ohio_state.cse.genequeens.EvolutionaryGoal goal}). There is a
	 * specified mutation rate, a probability that the genetic sequence of an
	 * individual is mutated after genetic exchange.
	 * 
	 * @param population
	 *            The population from which a solution is drawn by this method.
	 * @param mutationRate
	 *            The probability that the genetic sequence of an
	 *            {@code Evolvable} individual gets mutated after genetic
	 *            exchange with a mate.
	 * @param goal
	 *            The problem-specific implementation of an
	 *            {@code EvolutionaryGoal} which verifies whether the population
	 *            satisfies some requirements to be considered a solution to a
	 *            problem.
	 * @return A collection of of n-best individuals that result from this
	 *         implementation of a genetic algorithm.
	 */
	public static Collection<Evolvable> evolve(
			Collection<Evolvable> population, double mutationRate,
			EvolutionaryGoal goal) {
		evaluatePopulation(population);
		while (!goal.isSatisfied(population)) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector(), mutationRate);
			evaluatePopulation(population);
		}
		return population;
	}
}
