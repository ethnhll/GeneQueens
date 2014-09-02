package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
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
	private static final int MAX_GENERATIONS_DEJONG = 1000;
	private static final int POPULATION_SIZE_DEJONG = 50;

	private static final int CONVERGENCE_THRESHOLD = 10;

	/**
	 * 
	 * @param population
	 * @return
	 */
	private double evaluatePopulation(Collection<Evolvable> population) {
		assert !population.isEmpty() : "population is empty";

		double populationFitness = 0.0d;
		for (Evolvable member : population) {
			populationFitness += member.evaluateFitness();
		}
		return populationFitness;
	}

	/**
	 * 
	 * @param population
	 * @return
	 */
	private Evolvable findMostFitIndividual(Collection<Evolvable> population) {
		assert !population.isEmpty() : "population is empty";

		List<Evolvable> list = new ArrayList<Evolvable>(population);
		Collections.sort(list);
		// Get the topmost individual
		Evolvable mostFitIndividual = list.get(0);
		return mostFitIndividual;
	}

	/**
	 * 
	 * @param population
	 * @param mateSelector
	 * @return
	 */
	private Collection<Evolvable> createNextGeneration(
			Collection<Evolvable> population,
			MateSelector<Evolvable> mateSelector) {
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
				// Mutate the two resulting individuals using the default rate
				individual.mutate(MUTATION_RATE_DEJONG);
				mate.mutate(MUTATION_RATE_DEJONG);

				// Add the new individuals to the new population
				nextGeneration.add(individual);
				nextGeneration.add(mate);
			}
		}
		return nextGeneration;
	}

	/**
	 * 
	 * @author EthanHill
	 *
	 */
	public class SemiStochasticMostFitSelector implements
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
	 * 
	 * @param population
	 * @return
	 */
	public Collection<Evolvable> evolve(Collection<Evolvable> population) {
		// Snapshot of best seen population
		Collection<Evolvable> populationSnapshot = population;

		// Initial book-keeping
		double bestFitness = evaluatePopulation(population);
		Evolvable bestIndividual = findMostFitIndividual(population);

		int convergenceCount = 0;

		while (convergenceCount < CONVERGENCE_THRESHOLD) {
			population = createNextGeneration(population,
					new SemiStochasticMostFitSelector());

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
}
