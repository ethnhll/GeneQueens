package edu.ohio_state.cse.genequeens;

import java.util.Arrays;

/**
 * 
 * @author Ethan
 *
 */
public class Node {

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
	public int getScore(){
		return this.score;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getState(){
		return this.state;
	}


	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + score;
		result = prime * result + Arrays.hashCode(state);
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		
		boolean equal = true;
		
		if (this == obj) {
			equal = true;
		}
		if (obj == null) {
			equal = false;
		}
		if (this.getClass() != obj.getClass()) {
			equal = false;
		}
		
		Node otherNode = (Node) obj;
		
		if (this.score != otherNode.getScore()) {
			equal = false;
		}
		if (!Arrays.equals(this.state, otherNode.getState())) {
			equal = false;
		}
		
		return equal;
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
