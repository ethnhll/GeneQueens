package edu.ohio_state.cse.genequeens;

public class TestHarness {

	public static void main(String[] args) {

		int[] solution = HillClimbUtils.hillClimbingAgent(4);

		for (int columnIndex = 0; columnIndex < solution.length; columnIndex++) {
			System.out.println("Queen" + (columnIndex + 1) + ": Row "
					+ solution[columnIndex] + " Column " + columnIndex);
		}

	}

}
