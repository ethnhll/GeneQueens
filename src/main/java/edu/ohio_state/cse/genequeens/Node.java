package edu.ohio_state.cse.genequeens;

import java.util.Arrays;

/**
 * This class is intended to provide a simple, immutable structure to hold the
 * score of a board of n-queens (the number of non-attacking queens) and the
 * board representation to which the score corresponds.
 * 
 * @author Ethan Hill
 * 
 */
public final class Node implements Comparable<Object> {

	/**
	 * the score associated with the board representation (how many attacking
	 * queen pairs are on the board)
	 */
	private final int score;

	/**
	 * the array representation of an n-queens board layout
	 */
	private final int[] state;

	/**
	 * Constructs an immutable structure for a successor of a particular state,
	 * holding the value of the passed in score and state of the n-queens board.
	 * 
	 * @param score
	 *            the number of attacking queen pairs on the n-queens board
	 * @param state
	 *            the array representation of the n-queens board
	 */
	public Node(int score, int[] state) {
		this.score = score;
		this.state = Arrays.copyOf(state, state.length);
	}

	/**
	 * Returns the score (number of attacking queen pairs) associated with the
	 * board representation of {@code this}
	 * 
	 * @return the number of non-attacking queens on the board of {@code this}
	 */
	public int getScore() {

		return this.score;
	}

	/**
	 * Returns an array representation of an n-queens board.
	 * 
	 * @return the state representation of a board of n-queens
	 */
	public int[] getState() {
		return Arrays.copyOf(this.state, this.state.length);
	}

	@Override
	public boolean equals(Object object) {

		boolean areEqual = true;

		if (object == null) {
			areEqual = false;
		}
		if (object != this) {
			areEqual = false;
		}
		if (!(object instanceof Node)) {
			areEqual = false;
		}

		Node nodeObject = (Node) object;

		if (this.score != nodeObject.getScore()) {
			areEqual = false;
		}
		if (!Arrays.equals(this.state, nodeObject.getState())) {
			areEqual = false;
		}

		areEqual = this.toString().equals(nodeObject.toString());

		return areEqual;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(this.score);
		builder.append(", ");
		builder.append(Arrays.toString(this.state));
		builder.append(")");
		return builder.toString();
	}
	
	/**
	 * Implementing {@Code Comparable}
	 */
	public int compareTo(Object object){
		assert object instanceof Node : "Violated assertion: comparing Node to another Node";
		Node node = (Node) object;

		/*
		 * We want to order the Nodes by their score, lowest to highest
		 */
		if (this.score > node.getScore()){
			return 1;
		}
		else if (this.score < node.getScore()){
			return -1;
		}
		else {
			return 0;
		}
	}
}
