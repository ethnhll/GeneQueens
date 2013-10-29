package edu.ohio_state.cse.genequeens;

import java.util.Arrays;

/**
 * 
 * @author Ethan
 * 
 */
public final class Node {

	/**
	 * 
	 */
	private final int score;

	/**
	 * 
	 */
	private final int[] state;

	/**
	 * 
	 * @param score
	 * @param state
	 */
	public Node(int score, int[] state) {
		this.score = score;
		this.state = state;
	}

	/**
	 * 
	 * @return
	 */
	public int getScore() {

		return this.score;
	}

	/**
	 * 
	 * @return
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
		
		if(this.score != nodeObject.getScore()){
			areEqual = false;
		}
		if(!Arrays.equals(this.state, nodeObject.getState())){
			areEqual = false;
		}

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

}
