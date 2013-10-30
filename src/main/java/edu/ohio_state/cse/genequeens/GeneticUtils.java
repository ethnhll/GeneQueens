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

		/*
		 * Generate a random number to determine the crossover point. We are
		 * going to deterministically say that the left side of the crossover
		 * point will come from parentA and the right side of the crossover
		 * point will come from parentB.
		 * 
		 * Max is the length of a parent-1, however nextInt is also exclusive of
		 * top bound, so closer to parent-2. This guarantees that at least one
		 * "gene" will be transferred from a parent to the child.
		 */
		int crossOverIndex = new Random().nextInt(parentA.length - 1);

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
	 * Simple utility method that calculates a factorial.
	 * 
	 * @param n
	 *            the number to take the factorial of
	 * @return the result factorial (@Code n!)
	 */
	public static double factorial(int n) {
		double result = 1d;
		for (int i = 1; i <= n; i++) {
			result = result * i;
		}
		return result;
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
	public static int boardScore(int[] board) {

		/*
		 * Because the code below is not entirely intuitive, here is a
		 * (hopefully) helpful pre-amble to what is going on.
		 * 
		 * A queen may be attacked by another queen occupying the same diagonal,
		 * vertical or horizontal positions on the board, however, because the
		 * board is represented as an integer array, no two queens will occupy
		 * the same column, therefore we no longer need to consider a queen
		 * being attacked vertically. To consider the other cases where a queen
		 * may be attacked, we examine two queens on a board (iteratively, we
		 * are examining every queen against every other queen) by calculating
		 * the "slope" between the two queens.
		 * 
		 * Treating the positions of the queens on the board as coordinates in
		 * an xy plane (ex. queen1 exists in (0,1) or the first column, second
		 * row), we can deduce that two queens having a slope of 1, -1, or 0 are
		 * attacking one another. We then simply count how many attacking queen
		 * pairs we observe.
		 */

		int attackingQueenPairCount = 0;
		int[] tempBoard = Arrays.copyOf(board, board.length);

		for (int columnA = 0; columnA < tempBoard.length - 1; columnA++) {
			for (int columnB = columnA + 1; columnB < tempBoard.length; columnB++) {

				int rowA = tempBoard[columnA];
				int rowB = tempBoard[columnB];

				float slope = ((float) rowA - rowB)
						/ ((float) columnA - columnB);

				if ((Math.abs(slope) == 1F) || slope == 0F) {
					attackingQueenPairCount++;
				}
			}
		}

		/*
		 * We need to now subtract this number of attacking pairs from the total
		 * number of queen pairs, which is n choose 2. Or n!/(2!*(n-2)!)
		 */
		int nChooseTwo = (int) ((factorial(tempBoard.length)) / (factorial(2) * factorial(tempBoard.length - 2)));

		return nChooseTwo - attackingQueenPairCount;

	}

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
		List<Node> population = new ArrayList<Node>();

		int noImprovementCounter = 0;
		final int NO_IMPROVEMENT_PLATEAU = 10*populationSize;
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

				Node individual = new Node(individualScore, randBoard);
				population.add(individual);
				populationFitness += individualScore;
				restartFlag = false;
			}

			// While there are individuals to breed
			while ((population.size() > 1) && (!restartFlag) && (!done)) {

				// Now sort the list by fitness
				Collections.sort(population);
				// Examine the population's most fit individual, the last in the list
				Node mostFit = population.get(populationSize -1);
				int mostFitRank = (mostFit.getScore() / populationFitness) * 100;
				// Now we examine this rank to see if the population is
				// improving
				// over time
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
						+ Arrays.toString(mostFit.getState()) + " Score: "
						+ mostFit.getScore());

				int parentIndexA = new Random().nextInt(population.size());
				int parentIndexB = new Random().nextInt(population.size() - 1);

				Node parentA = population.remove(parentIndexA);
				Node parentB = population.remove(parentIndexB);

				// Update the population fitness
				populationFitness -= (parentA.getScore() - parentB.getScore());

				int[] parentStateA = Arrays.copyOf(parentA.getState(),
						parentA.getState().length);
				int[] parentStateB = Arrays.copyOf(parentB.getState(),
						parentB.getState().length);

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

				Node childA = new Node(childScoreA, mutatedChildA);
				Node childB = new Node(childScoreB, mutatedChildB);
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
