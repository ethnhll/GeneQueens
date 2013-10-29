package edu.ohio_state.cse.genequeens;

import java.util.ArrayList;
import java.util.List;

public class TestHarness {

	public static void main(String[] args) {

		int[] board = { 2, 0, 1 };

		List<Node> nodes = new ArrayList<Node>();

		System.out.println(Utils.boardScore(board));

		nodes = Utils.successors(board);

	}

}
