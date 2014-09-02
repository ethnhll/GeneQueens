package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AnnealingUtils {

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
		return attackingQueenPairCount;
	}

	/**
	 * Creates a full board with queens positioned randomly across the board.
	 * 
	 * @param boardSize
	 *            the size of the board representation of the n-queens problem
	 * @return a randomBoard filled with queens at random positions
	 */
	public static int[] randomBoard(int boardSize) {

		int[] randBoard = new int[boardSize];

		for (int columnIndex = 0; columnIndex < boardSize; columnIndex++) {

			Random rand = new Random();

			// Translates to....
			// rand.nextInt((MAX(=boardSize-1)-MIN(=0))+1) + MIN(=0);
			int randomVal = rand.nextInt(boardSize);

			randBoard[columnIndex] = randomVal;
		}
		return randBoard;
	}

	/**
	 * Generates a set of possible successors to a given state. Successors are
	 * integer arrays that arise from moving a single queen on the board to any
	 * other possible location on the board.
	 * <p>
	 * IMPLEMENTATION SPECIFIC: Because the board of queens is represented by an
	 * array of integers, it is safe to assume queens cannot occupy the same
	 * column and so queen movement is restricted to moving to an open spot
	 * within the queen's current column.
	 * </p>
	 * 
	 * @param state
	 *            the initial state to generate successors from
	 * @return a list of successor nodes generated from the initial state
	 */
	public static List<Node> successors(int[] state) {

		int[] stateCopy = Arrays.copyOf(state, state.length);
		int parentScore = HillClimbUtils.boardScore(stateCopy);

		/*
		 * Add the parent state to the list of successors to remove after all
		 * successors are generated. This is to ensure that if a possible
		 * successor matches the parent state, that it is not added to the list
		 * already.
		 */
		List<Node> successors = new ArrayList<Node>();
		Node parent = new Node(parentScore, stateCopy);
		successors.add(parent);

		for (int columnIndex = 0; columnIndex < stateCopy.length; columnIndex++) {

			int[] tempState = Arrays.copyOf(stateCopy, stateCopy.length);

			for (int rowVal = 0; rowVal < tempState.length; rowVal++) {

				tempState[columnIndex] = rowVal;

				int stateScore = boardScore(tempState);

				Node child = new Node(stateScore, tempState);
				if (!successors.contains(child)) {
					successors.add(child);
				}
			}
		}
		successors.remove(parent);
		return successors;
	}

	/**
	 * 
	 * @param boardSize
	 * @param temperature
	 * @return
	 */
	public static int[] simulatedAnnealingAgent(int boardSize, int temperature) {

		// Create a random initial parent Node
		int[] initialState = randomBoard(boardSize);
		int initialScore = boardScore(initialState);
		Node current = new Node(initialScore, initialState);

		double currentTemperature = (double) (temperature);

		while (boardScore(current.getState()) != 0) {
			/*
			 * No definition for what is a good temperature. I use 100 simply
			 * because that is the boiling point of water (an arbitrary choice).
			 * I also use this because I implement the schedule as geometric
			 * cooling.
			 */

			boolean done = false;
			int iterations = 1;

			while (!done) {
				// Using Newton's Law of Cooling
				currentTemperature = currentTemperature
						* Math.exp(-((double) boardSize * iterations));
				// Output the current state of affairs
				System.out.println("Iteration " + iterations
						+ ": Current State: "
						+ Arrays.toString(current.getState())
						+ " Current Score: " + current.getScore()
						+ " Current Temperature: " + currentTemperature);

				if (currentTemperature < 0.0000001) {
					done = true;
				}
				List<Node> successors = successors(current.getState());
				int randomChildPos = new Random().nextInt(successors.size());
				Node next = successors.remove(randomChildPos);

				int deltaE = next.getScore() - current.getScore();
				// We are using less than 0 because a lower score is better
				if (deltaE <= 0) {
					current = next;
				} else {
					double rand = new Random().nextDouble();
					if (rand < Math
							.exp(-((double) deltaE / currentTemperature))) {
						current = next;
					}
				}
				iterations++;
			}

		}
		System.out.println("SOLUTION FOUND");
		return current.getState();
	}
}
