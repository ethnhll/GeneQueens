package edu.ohio_state.cse.genequeens;

import java.util.Arrays;
import java.util.Random;

/**
 * This class is intended to provide a simple, immutable structure to represent
 * the chess board of the n-queens problem.
 * 
 * @author Ethan Hill
 * 
 */
public class ChessBoard extends Evolvable implements Comparable<Evolvable> {

	/**
	 * the array representation of an n-queens board layout
	 */
	private final int[] boardRep;

	/**
	 * Constructs an immutable structure for a successor of a particular state,
	 * holding the value of the passed in score and state of the n-queens board.
	 * 
	 * @param score
	 *            score related to the number of attacking queen pairs on the
	 *            n-queens board
	 * @param state
	 *            the array representation of the n-queens board
	 */
	public ChessBoard(int score, int[] state) {
		this.boardRep = Arrays.copyOf(state, state.length);
	}

	/**
	 * Simple utility method that calculates a factorial.
	 * 
	 * @param n
	 *            The number to which factorial will be applied.
	 * @return The result factorial (@code n!)
	 */
	private static int factorial(int n) {
		int result = 1;
		for (int i = 1; i <= n; i++) {
			result = result * i;
		}
		return result;
	}

	/**
	 * Returns an array representation of an n-queens board.
	 * 
	 * @return The state representation of a board of n-queens
	 */
	public int[] getBoardLayout() {
		return Arrays.copyOf(this.boardRep, this.boardRep.length);
	}

	@Override
	public boolean equals(Object object) {
		boolean areEqual = true;
		if (object == null) {
			areEqual = false;
		}
		if (!(object instanceof ChessBoard)) {
			areEqual = false;
		}

		ChessBoard nodeObject = (ChessBoard) object;
		if (this.getFitnessScore() != nodeObject.getFitnessScore()) {
			areEqual = false;
		}
		if (!Arrays.equals(this.boardRep, nodeObject.getBoardLayout())) {
			areEqual = false;
		}
		return areEqual;
	}

	public int compareTo(Evolvable object) {
		assert object instanceof ChessBoard : String.format(
				"object %s is of type %s, not of type %s", object.toString(),
				object.getClass().getName(), this.getClass().toString());

		ChessBoard node = (ChessBoard) object;
		if (this.fitnessScore > node.getFitnessScore()) {
			return 1;
		} else if (this.fitnessScore < node.getFitnessScore()) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public double evaluateFitness() {
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
		for (int columnA = 0; columnA < this.boardRep.length - 1; columnA++) {
			for (int columnB = columnA + 1; columnB < this.boardRep.length; columnB++) {

				int rowA = this.boardRep[columnA];
				int rowB = this.boardRep[columnB];

				float slope = ((float) (rowA - rowB))
						/ ((float) (columnA - columnB));

				if ((Math.abs(slope) == 1.0F) || slope == 0.0F) {
					attackingQueenPairCount++;
				}
			}
		}
		/*
		 * We need to now subtract this number of attacking pairs from the total
		 * number of queen pairs, which is n choose 2. Or n!/(2!*(n-2)!)
		 */
		int nChooseTwo = factorial(this.boardRep.length) / factorial(2)
				* factorial(this.boardRep.length - 2);

		return (double) (nChooseTwo - attackingQueenPairCount);
	}

	/**
	 * Applies mutation with some probability equal to {@code mutationRate} to
	 * the entirety of the genetic sequence of {@code this}.
	 * 
	 * @param mutationRate
	 *            The probability that a mutation occurs in the genetic sequence
	 *            of {@code this}.
	 * @see Evolvable
	 */
	@Override
	public void mutate(double mutationRate) {
		for (int i = 0; i < this.boardRep.length; i++) {
			double probability = new Random().nextDouble();
			if (probability <= mutationRate) {
				int mutatedGene = new Random().nextInt(this.boardRep.length);
				this.boardRep[i] = mutatedGene;
			}
		}
	}

	@Override
	public void exchangeGenes(Evolvable mate) {
		assert mate instanceof ChessBoard : String.format(
				"mate %s is of type %s, not of type %s", mate.toString(), mate
						.getClass().getName(), this.getClass().getName());
		ChessBoard mateBoard = (ChessBoard) mate;
		assert this.boardRep.length == mateBoard.getBoardLayout().length : String
				.format("individual %s and selected mate %s do not match "
						+ "length of genetic sequences.\n"
						+ "individual length: %d\n" + "mate length: %d",
						this.toString(), mateBoard.toString());
		/*
		 * We are going to deterministically say that the left side of the
		 * crossover point will come from {@code this} and the right side of the
		 * crossover point will come from {@code mate}.
		 * 
		 * Max is the length of a parent-1, however nextInt is also exclusive of
		 * top bound, so closer to parent-2. This guarantees that at least one
		 * "gene" will be transferred from a parent to the child.
		 */
		int crossOverIndex = new Random().nextInt(this.boardRep.length - 1);
		int[] childA = new int[this.boardRep.length];
		int[] childB = new int[mateBoard.boardRep.length];

		// Fill the child array with the right side of crossover point from
		// parentA
		for (int i = 0; i < crossOverIndex; i++) {
			int gene = this.boardRep[i];
			childA[i] = gene;
		}
		// Now fill with left side of crossover point
		for (int i = crossOverIndex; i < mateBoard.getBoardLayout().length; i++) {
			int gene = mateBoard.getBoardLayout()[i];
			childA[i] = gene;
		}
		// Fill the other child array with the right side of crossover point
		// from parentB
		for (int i = 0; i < crossOverIndex; i++) {
			int gene = mateBoard.getBoardLayout()[i];
			childB[i] = gene;
		}
		// Now fill with left side of crossover point
		for (int i = crossOverIndex; i < mateBoard.getBoardLayout().length; i++) {
			int gene = this.boardRep[i];
			childB[i] = gene;
		}
		
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
}
