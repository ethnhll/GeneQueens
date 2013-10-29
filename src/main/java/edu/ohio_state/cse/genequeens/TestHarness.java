package edu.ohio_state.cse.genequeens;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TestHarness {

	private static int[] randomBoard(int[] board){
	
		int[] temp = Arrays.copyOf(board, board.length);
		int boardSize = temp.length;
		
		for (int columnIndex = 0; columnIndex < boardSize; columnIndex++) {

			// Random number between 0 and boardSize-1

			Random rand = new Random();

			// Implicitly, rand.nextInt((MAX(=boardSize-1)-MIN(=0))+1) +
			// MIN(=0);

			int randomVal = rand.nextInt(boardSize);

			temp[columnIndex] = randomVal;
		}
		return temp;
		
	}
	
	
	public int[] hillClimbingAgent(int boardSize) {

		int[] temp = HillClimbUtils.randomBoard(boardSize);

		

		return null;

	}

	public static void main(String[] args) {

		int[] board = HillClimbUtils.randomBoard(4);

		Set<Node> nodes = new HashSet<Node>();

		nodes = HillClimbUtils.successors(board);
		int i = 1;
		for (Node node : nodes) {
			System.out.println(i + " " + node);
			i++;
		}

	}

}
