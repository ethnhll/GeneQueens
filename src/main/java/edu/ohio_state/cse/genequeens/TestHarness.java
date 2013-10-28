package edu.ohio_state.cse.genequeens;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestHarness {

	/**
	 * Returns a count of queens that are unable to be attacked (safe queens) on
	 * an n x n board, where a single queen occupies a column on the board.
	 * 
	 * @param board
	 *            an integer array of size n representing an n x n size board of
	 *            queens, with one queen per column
	 * @return the number of queens that are unable to be attacked by another
	 *         queen on the board (these queens are safe)
	 */
	private static int boardScore(int[] board) {

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
		 * attacking one another.
		 * 
		 * In order to track how many queens are attacking one another (and to
		 * avoid double counting queens that can be attacked by more than one
		 * other queen), we add the two attacking queens to a set. The number of
		 * safe queens then is the number of attacking queens subtracted from
		 * the total number of queens (n, or the size of the board).
		 */
		Set<Integer> attackingQueens = new HashSet<Integer>();

		for (int columnA = 0; columnA < board.length - 1; columnA++) {
			for (int columnB = columnA + 1; columnB < board.length; columnB++) {

				int rowA = board[columnA];
				int rowB = board[columnB];

				float slope = ((float) rowA - rowB)
						/ ((float) columnA - columnB);

				if ((Math.abs(slope) == 1F) || slope == 0F) {
					/*
					 * If the set already contains an element, then the set
					 * remains unchanged after an attempt to add that element.
					 */
					attackingQueens.add(Integer.valueOf(columnA));
					attackingQueens.add(Integer.valueOf(columnB));
				}
			}
		}
		int safeQueens =  attackingQueens.size();
		return safeQueens;
	}

	/**
	 * 
	 * @param state
	 * @return
	 */
	private static Set<Node> successors(int[] state) {

		Set<Node> successors = new HashSet<Node>();

		/*
		 * Add the parent state to the set of successors to remove after all
		 * successors are generated. This is to ensure that if a possible
		 * successor matches the parent state, that it is not added to the set.
		 */
		int parentScore = boardScore(state);
		Node parent = new Node(parentScore, state);
		successors.add(parent);

		for (int columnIndex = 0; columnIndex < state.length; columnIndex++) {

			int[] tempState = Arrays.copyOf(state, state.length);

			for (int rowVal = 0; rowVal < state.length; rowVal++) {

				tempState[columnIndex] = rowVal;
				int stateScore = boardScore(tempState);
				Node child = new Node(stateScore, tempState);

				successors.add(child);
			}
		}
		successors.remove(parent);
		return successors;
	}

	
	public int[] hillClimbingAgent(int boardSize) {
		
		int[] initialState = new int[boardSize];
		
		//Generate random numbers to fill an array of length boardSize
		for(int columnIndex = 0; columnIndex < initialState.length; columnIndex++){
			//Random number between 0 and boardSize-1
			Random rand = new Random();
			//Implicitly, rand.nextInt((MAX(=boardSize-1)-MIN(=0))+1) + MIN(=0);
			int randomVal = rand.nextInt(boardSize);
			initialState[columnIndex] = randomVal;
		}
		
		return null;
	}

	public static void main(String[] args) {

		int[] board = { 1, 0, 1};

		Set<Node> nodes = successors(board);
		
		for (Node node : nodes){
			System.out.println(node.toString());
		}
		
		
		

	}

}
