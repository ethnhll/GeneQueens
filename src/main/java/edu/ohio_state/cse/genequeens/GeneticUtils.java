package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This utility class provides some functionality for the Genetic Search agent
 * as well as several helper methods.
 * 
 * @author Ethan Hill
 * 
 */
public class GeneticUtils {

	/**
	 * Reproduces children from two parent states, where there is a "genetic"
	 * crossover point between the two parents, determined randomly. The
	 * children replace the parents and the genetic material that the parents
	 * contained is not maintained.
	 * <p>
	 * Originally, this method returned an int array which represented a single
	 * child, however, I wanted to experiment with maintaining a population
	 * size, so I changed the method to alter the contents of the parent arrays.
	 * </p>
	 * 
	 * @param childA
	 *            the parent array that will be replaced with the genetic code
	 *            of its child
	 * @param childB
	 *            the parent array that will be replaced with the genetic code
	 *            if its child
	 */
	public static void reproduce(int[] childA, int[] childB) {

		assert (childA.length == childB.length) : "Violated assertion: Parents match in length";

		/*
		 * Here we are treating the children parameters as the parents, but they
		 * will get replaced by the child genes later.
		 */
		int[] parentA = Arrays.copyOf(childA, childA.length);
		int[] parentB = Arrays.copyOf(childB, childB.length);

		// Fill the child array with the right side of crossover point from
		// parentA
		for (int i = 0; i < crossOverIndex; i++) {
			int gene = parentA[i];
			childA[i] = gene;
		}
		// Now fill with left side of crossover point
		for (int i = crossOverIndex; i < parentB.length; i++) {
			int gene = parentB[i];
			childA[i] = gene;
		}

		// Fill the other child array with the right side of crossover point
		// from parentB
		for (int i = 0; i < crossOverIndex; i++) {
			int gene = parentB[i];
			childB[i] = gene;
		}
		// Now fill with left side of crossover point
		for (int i = crossOverIndex; i < parentB.length; i++) {
			int gene = parentA[i];
			childB[i] = gene;
		}

	}

	/**
	 * Mutates the genes of a child representation with a probability related to
	 * the mutation rate give to the genetic search agent.
	 * 
	 * @param child
	 *            the child to be mutated
	 * @param mutationRate
	 *            the rate/probability that the genes of the child state will be
	 *            mutated
	 * @return a mutated child with potentially mutated/altered genes
	 */
	public static int[] mutate(int[] child, double mutationRate) {

		int[] mutantChild = Arrays.copyOf(child, child.length);

		for (int i = 0; i < mutantChild.length; i++) {
			double probability = new Random().nextDouble();
			if (probability <= mutationRate) {
				int mutatedGene = new Random().nextInt(mutantChild.length);
				mutantChild[i] = mutatedGene;
			}
		}

		return Arrays.copyOf(mutantChild, mutantChild.length);
	}

	/**
	 * Returns a count of queen pairs that are able to be attacked (unsafe
	 * queens) on an n x n board, where a single queen occupies a column on the
	 * board.
	 * 
	 * @param board
	 *            an integer array of size n representing an n x n size board of
	 *            queens, with one queen per column
	 * @return the number of attacking queen pairs ton the board (these queens
	 *         are unsafe)
	 */
	

	/**
	 * Creates a full board with queens positioned randomly across the board.
	 * 
	 * @param boardSize
	 *            the size of the board representation of the n-queens problem
	 * @return a randomBoard filled with queens at random positions
	 */
	public static int[] randomBoard(int boardSize) {

		int[] temp = new int[boardSize];

		for (int columnIndex = 0; columnIndex < boardSize; columnIndex++) {

			Random rand = new Random();

			// Translates to....
			// rand.nextInt((MAX(=boardSize-1)-MIN(=0))+1) + MIN(=0);
			int randomVal = rand.nextInt(boardSize);

			temp[columnIndex] = randomVal;
		}
		return temp;
	}

	/**
	 * Given a boardSize, mutationRate, and PopulationSize, this utility method
	 * is designed to find a solution to the n-queens problem. Individuals are
	 * selected for crossover to produce another individual, which is added to
	 * the population. The parents of the individual are removed from the
	 * population, so, eventually, a population will "run dry/go extinct" and
	 * require a restart.
	 * 
	 * @param boardsize
	 *            the size of the n-queens board
	 * @param mutationRate
	 *            the rate at which child states can possibly mutate
	 * @param populationSize
	 * @return a solution to the n-queens problem
	 */
	public static int[] geneticAlgorithmAgent(int boardSize,
			double mutationRate, int populationSize) {

		// Calculate the max fitness
		int maxFitness = (int) ((factorial(boardSize)) / ((factorial(2) * factorial(boardSize - 2))));
		List<ChessBoard> population = new ArrayList<ChessBoard>();

		int noImprovementCounter = 0;
		final int NO_IMPROVEMENT_PLATEAU = 10 * populationSize;
		boolean restartFlag = false;
		boolean done = false;

		// We use this to determine if the population is improving
		int prevMostFitRank = 0;

		int[] solution = new int[boardSize];
		int iterations = 0;

		while (!done) {

			int populationFitness = 0;

			/*
			 * Create random population of individuals (restarts with new
			 * population if the old population showed no improvement over many
			 * generations).
			 */
			if (restartFlag) {
				System.out
						.println("\n!!-INSUFFICIENT IMPROVEMENT: RESTART NEEDED-!!\n");
			}

			for (int i = 0; i < populationSize; i++) {
				int[] randBoard = GeneticUtils.randomBoard(boardSize);
				int individualScore = GeneticUtils.boardScore(randBoard);

				ChessBoard individual = new ChessBoard(individualScore, randBoard);
				population.add(individual);
				populationFitness += individualScore;
				restartFlag = false;
			}

			// While there are individuals to breed
			while ((population.size() > 1) && (!restartFlag) && (!done)) {

				// Now sort the list by fitness
				Collections.sort(population);
				// Examine the population's most fit individual, the last in the
				// list
				ChessBoard mostFit = population.get(populationSize - 1);
				int mostFitRank = (mostFit.getScore() / populationFitness) * 100;
				// Now we examine this rank to see if the population is
				// improving over time
				if (mostFitRank <= prevMostFitRank) {
					noImprovementCounter++;
				} else {
					noImprovementCounter = 0;
				}

				prevMostFitRank = mostFitRank;

				if (noImprovementCounter > NO_IMPROVEMENT_PLATEAU) {
					restartFlag = true;
					noImprovementCounter = 0;

				}

				// Output the current state of affairs
				System.out.println("Iteration " + iterations
						+ ": Most Fit Individual: "
						+ Arrays.toString(mostFit.getBoardLayout()) + " Score: "
						+ mostFit.getScore());

				int parentIndexA = new Random().nextInt(population.size());
				int parentIndexB = new Random().nextInt(population.size() - 1);

				ChessBoard parentA = population.remove(parentIndexA);
				ChessBoard parentB = population.remove(parentIndexB);

				// Update the population fitness
				populationFitness -= (parentA.getScore() - parentB.getScore());

				int[] parentStateA = Arrays.copyOf(parentA.getBoardLayout(),
						parentA.getBoardLayout().length);
				int[] parentStateB = Arrays.copyOf(parentB.getBoardLayout(),
						parentB.getBoardLayout().length);

				reproduce(parentStateA, parentStateB);
				/*
				 * Because we let reproduce alter the contents of the
				 * parentState arrays, mutate is really mutating the children,
				 * not the parents.
				 */
				int[] mutatedChildA = mutate(parentStateA, mutationRate);
				int childScoreA = boardScore(mutatedChildA);

				int[] mutatedChildB = mutate(parentStateB, mutationRate);
				int childScoreB = boardScore(mutatedChildB);

				if (childScoreA == maxFitness) {
					done = true;
					solution = Arrays.copyOf(mutatedChildA,
							mutatedChildA.length);
					System.out.println("SOLUTION FOUND");
				} else if (childScoreB == maxFitness) {
					done = true;
					solution = Arrays.copyOf(mutatedChildB,
							mutatedChildB.length);
					System.out.println("SOLUTION FOUND");
				}

				ChessBoard childA = new ChessBoard(childScoreA, mutatedChildA);
				ChessBoard childB = new ChessBoard(childScoreB, mutatedChildB);
				population.add(childA);
				population.add(childB);
				// Update the population fitness

				populationFitness += (childA.getScore() + childB.getScore());

				iterations++;
			}

		}
		return Arrays.copyOf(solution, solution.length);
	}
}
