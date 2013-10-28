package edu.ohio_state.cse.genequeens;

import java.util.HashSet;
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
		int safeQueens = board.length - attackingQueens.size();
		return safeQueens;
	}
	
	
	
	
	
	
	
	
	private Node successors(int[] state){
		
		
	}
	
	public int[] hillClimbingAgent(int boardsize){
		
	}

	public static void main(String[] args) {

		int[] board = { 2, 1, 3, 3 };

		System.out.println(boardScore(board));

	}

}
