package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class provides some utility functions for use in a Hill-Climbing Agent.
 * 
 * @author Ethan Hill
 * 
 */
public class HillClimbUtils {

	/*
	 * private to prevent instantiation
	 */
	private HillClimbUtils() {
		// No code needed
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
		return attackingQueenPairCount;
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
	 * Based on a random restart Hill-Climbing Search Agent, this agent utility
	 * finds a solution to the n-queens problem given an arbitrary boardSize
	 * (though larger sizes will take much longer, possibly even too long to be
	 * worthwhile to attempt, sizes > 20). Restarts the search with a random new
	 * board state of size boardSize after encountering local minima or a
	 * perceived plateau.
	 * 
	 * @param boardSize
	 *            the size of the representation of a
	 * @return a solution state representation of a board with n-queens
	 */
	public static int[] hillClimbingAgent(int boardSize) {

		final int PLATEAU_THRESHOLD = 5;
		int plateauCount = 0;
		int iterationCount = 0;

		boolean done = false;
		boolean stuckFlag = false;

		// Create random initial board to begin with
		int[] initialState = HillClimbUtils.randomBoard(boardSize);
		int initialScore = HillClimbUtils.boardScore(initialState);
		Node currentNode = new Node(initialScore, initialState);

		while (!done) {

			if (stuckFlag || (plateauCount > PLATEAU_THRESHOLD)) {
				int[] restartState = HillClimbUtils.randomBoard(boardSize);
				int restartScore = HillClimbUtils.boardScore(restartState);
				currentNode = new Node(restartScore, restartState);
				plateauCount = 0;
				stuckFlag = false;

				// Alert that a random restart was needed
				System.out.println("!!-RANDOM RESTART NEEDED-!!");

			}

			List<Node> nodes = HillClimbUtils
					.successors(currentNode.getState());

			/*
			 * Sort the list according to score, and choose the best (lowest
			 * scoring) child node
			 */
			Collections.sort(nodes);
			Node nextNode = nodes.remove(0);

			// Output the current state of affairs
			System.out.println("Iteration " + iterationCount
					+ ": Current State: "
					+ Arrays.toString(currentNode.getState())
					+ " Current Score: " + currentNode.getScore()
					+ " Best Child Score: " + nextNode.getScore());

			// Case where we possibly have a solution, or we are stuck
			if (nextNode.getScore() > currentNode.getScore()) {
				if (currentNode.getScore() == 0) {
					System.out.println("SOLUTION FOUND");
					done = true;
				} else {
					// We have reached a local minima
					stuckFlag = true;
				}
			} else if (nextNode.getScore() < currentNode.getScore()) {
				// Assign currentNode to nextNode
				currentNode = new Node(nextNode.getScore(), nextNode.getState());
				// Plateau trend has been broken (if there was one)
				plateauCount = 0;
			} else {
				/*
				 * Here is where we could run into a plateau, currScore ==
				 * Nextscore. We will allow a plateau to continue on for a few
				 * iterations before forcing a random restart.
				 */

				// Assign currentNode to nextNode
				currentNode = new Node(nextNode.getScore(), nextNode.getState());
				plateauCount++;
			}

			iterationCount++;
		}

		return currentNode.getState();
	}
}
