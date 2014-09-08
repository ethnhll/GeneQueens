package edu.ohio_state.cse.genequeens;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

/**
 * This class is intended to provide a simple structure to represent the chess
 * board of the n-queens problem.
 * 
 * @author Ethan Hill
 * 
 */
public class ChessBoard extends Evolvable implements Comparable<Evolvable> {

	/**
	 * The array representation of an n-queens board layout.
	 */
	private final int[] boardRep;

	/**
	 * A counter static across all instances, to track how many instances are
	 * initialized.
	 */
	private static int instanceCount = 0;

	/**
	 * The unique identifier for {@code this}.
	 */
	private final int id;

	/**
	 * The fixed initial size of {@code this}. Subsequent changes to
	 * {@code this} cannot change its internal size.
	 */
	private final int boardSize;

	/**
	 * Instantiates a new {@code ChessBoard} with an internal board
	 * representation of size {@code boardSize}. The internal representation is
	 * filled with random values to represent the positions of the queens on the
	 * board.
	 * 
	 * @param boardSize
	 *            The size of the internal board representation.
	 */
	public ChessBoard(int boardSize) {

		this.boardSize = boardSize;
		this.boardRep = new int[boardSize];
		this.id = instanceCount;
		instanceCount++;
			
		Random rand = new Random();
		for (int i = 0; i < boardSize; i++) {
			this.boardRep[i] = rand.nextInt(boardSize);
		}
	}

	/**
	 * Constructs an instance of {@code ChessBoard} with an internal
	 * representation of a board layout as that of {@code boardRep}.
	 * 
	 * @param boardRep
	 *            The representation of the chess board to construct a new
	 *            instance of a {@code ChessBoard}.
	 */
	public ChessBoard(int[] boardRep) {
		this.boardSize = boardRep.length;
		this.boardRep = Arrays.copyOf(boardRep, boardRep.length);
		this.id = instanceCount;
		instanceCount++;
	}

	/**
	 * Replaces the internal representation of {@code this} to another board
	 * representation. The new board representation must be the same size of the
	 * original board representation with which {@code this} was initialized.
	 * 
	 * @param boardRep
	 *            The array representation of the n-queens board.
	 * @return The original board representation being replaced.
	 */
	private int[] replaceBoard(int[] boardRep) {
		assert boardRep.length == this.boardSize : String.format(
				"boardRep is not the same size as %s was initialized with",
				this.toString());

		// Since the boardRep is final, we will manually copy in values
		for (int i = 0; i < boardRep.length; i++) {
			this.boardRep[i] = boardRep[i];
		}
		int[] oldRep = Arrays.copyOf(this.boardRep, this.boardSize);
		return oldRep;
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
	
	/**
	 * Retrieves the id unique to {@code this} instance.
	 * 
	 * @return A unique integer id.
	 */
	public int getId(){
		return this.id;
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
		if (this != object) {
			areEqual = false;
		}
		ChessBoard boardObject = (ChessBoard) object;
		if (this.getFitnessScore() != boardObject.getFitnessScore()) {
			areEqual = false;
		}
		if (!Arrays.equals(this.boardRep, boardObject.getBoardLayout())) {
			areEqual = false;
		}
		if (this.boardSize != boardObject.getBoardLayout().length) {
			areEqual = false;
		}
		if (this.id != boardObject.getId()){
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
		for (int columnA = 0; columnA < this.boardSize - 1; columnA++) {
			for (int columnB = columnA + 1; columnB < this.boardSize; columnB++) {

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
		int nChooseTwo = factorial(this.boardSize)
				/ (factorial(2) * factorial(this.boardSize - 2));
		this.fitnessScore = (nChooseTwo - attackingQueenPairCount);
		return this.fitnessScore;
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
		for (int i = 0; i < this.boardSize; i++) {
			double probability = new Random().nextDouble();
			if (probability <= mutationRate) {
				int mutatedGene = new Random().nextInt(this.boardSize);
				this.boardRep[i] = mutatedGene;
			}
		}
	}

	@Override
	public Evolvable exchangeGenes(Evolvable mate) {
		assert mate instanceof ChessBoard : String.format(
				"mate %s is of type %s, not of type %s", mate.toString(), mate
						.getClass().getName(), this.getClass().getName());

		ChessBoard mateBoard = (ChessBoard) mate;
		assert this.boardSize == mateBoard.getBoardLayout().length : String
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
		int[] childB = new int[mateBoard.getBoardLayout().length];

		// Fill the children arrays with the right side of crossover point
		for (int i = 0; i < crossOverIndex; i++) {
			int geneA = this.boardRep[i];
			childA[i] = geneA;
			int geneB = mateBoard.getBoardLayout()[i];
			childB[i] = geneB;
		}
		// Now fill with left side of crossover point
		for (int i = crossOverIndex; i < mateBoard.getBoardLayout().length; i++) {
			int gene = mateBoard.getBoardLayout()[i];
			childA[i] = gene;
			int geneB = this.boardRep[i];
			childB[i] = geneB;
		}
		this.replaceBoard(childA);
		Evolvable replacedMate = new ChessBoard(childB);
		return replacedMate;
	}

	@Override
	public int hashCode() {
		int result = 37 * this.id;
		return result + Arrays.hashCode(this.boardRep);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int columnIndex = 0; columnIndex < this.boardSize; columnIndex++) {
			builder.append("Queen" + (columnIndex + 1) + ": Row "
					+ this.boardRep[columnIndex] + " Column " + columnIndex
					+ "\n");
		}
		return builder.toString();
	}

	/**
	 * A genetic algorithm attempting to solve the n-queens problem should be
	 * minimizing an individual's fitness. That is, the number of attacking
	 * queens should approach zero.
	 * 
	 * @author Ethan Hill
	 *
	 */
	public static class QueensGoal implements EvolutionaryGoal {
		public boolean isSatisfied(Collection<Evolvable> population) {
			for (Evolvable individual : population) {
				ChessBoard board = (ChessBoard) individual;
				if (board.getFitnessScore() == 0) {
					return true;
				}
			}
			return false;
		}
	}
}