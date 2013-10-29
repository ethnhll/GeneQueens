package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Ethan
 * 
 */
public class Utils {

	/*
	 * Make the constructor private to prevent instantiation
	 */
	private Utils() {
		// No code needed
	}

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
		 * attacking one another.
		 * 
		 * In order to track how many queens are attacking one another (and to
		 * avoid double counting queens that can be attacked by more than one
		 * other queen), we add the two attacking queens to a set. The number of
		 * safe queens then is the number of attacking queens subtracted from
		 * the total number of queens (n, or the size of the board).
		 */
		Set<Integer> attackingQueens = new HashSet<Integer>();

		int[] tempBoard = Arrays.copyOf(board, board.length);

		for (int columnA = 0; columnA < tempBoard.length - 1; columnA++) {
			for (int columnB = columnA + 1; columnB < tempBoard.length; columnB++) {

				int rowA = tempBoard[columnA];
				int rowB = tempBoard[columnB];

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
		int safeQueens = tempBoard.length - attackingQueens.size();
		return safeQueens;
	}

	/**
	 * 
	 * @param state
	 * @return
	 */
	public static List<Node> successors(int[] state) {

		/*
		 * Add the parent state to the list of successors to remove after all
		 * successors are generated. This is to ensure that if a possible
		 * successor matches the parent state, that it is not added to the list
		 * already.
		 */
		List<Node> successors = new ArrayList<Node>();
		int[] stateCopy = Arrays.copyOf(state, state.length);
		int parentScore = Utils.boardScore(stateCopy);
		Node parent = new Node(parentScore, stateCopy);
		successors.add(parent);

		// TODO CLEANUP
		System.out.println(successors.toString() + '\n');

		for (int columnIndex = 0; columnIndex < stateCopy.length; columnIndex++) {

			int[] tempState = Arrays.copyOf(stateCopy, stateCopy.length);
			System.out.println("Initial State = " + Arrays.toString(tempState));

			for (int rowVal = 0; rowVal < tempState.length; rowVal++) {
				// TODO CLEANUP
				System.out.println("ColumnIndex = " + columnIndex);
				System.out.println("\tRowValue = " + rowVal);

				tempState[columnIndex] = rowVal;
				System.out.println("\tNew State = "
						+ Arrays.toString(tempState));

				int stateScore = boardScore(tempState);
				System.out.println("\tScore = " + stateScore);

				Node child = new Node(stateScore, tempState);
				System.out.println("Child Rep = " + child.toString() + '\n');

				/*
				 * Don't add the child if another child with the same state and
				 * score already is in the arrayList
				 */
				if (!successors.contains(child)) {
					successors.add(child);
				}
				System.out.println("\t\t" + successors.toString());
			}
		}
		successors.remove(parent);
		return successors;
	}

}
