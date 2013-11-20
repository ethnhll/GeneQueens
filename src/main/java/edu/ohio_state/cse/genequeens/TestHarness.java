package edu.ohio_state.cse.genequeens;

/**
 * The Main driver class for the Hill Climbing and Genetic search agents,
 * solving the n-queens problem.
 * 
 * @author Ethan Hill
 * 
 */
public class TestHarness {

	public static void main(String[] args) {

		if (args.length == 1) {

			int commandBoardSize = Integer.parseInt(args[0]);

			// Check if input is valid
			if (commandBoardSize >= 4) {

				System.out
						.println("--------------------------------------------------------------------------------");
				System.out.println("\t\tHILL CLIMBING SEARCH");
				System.out
						.println("--------------------------------------------------------------------------------");
				int[] solution = HillClimbUtils
						.hillClimbingAgent(commandBoardSize);

				for (int columnIndex = 0; columnIndex < solution.length; columnIndex++) {
					System.out.println("Queen" + (columnIndex + 1) + ": Row "
							+ solution[columnIndex] + " Column " + columnIndex);
				}
				System.out
						.println("--------------------------------------------------------------------------------");
			} else {
				System.out
						.println("Please restart the program and enter a boardSize greater than or equal to 4");
				System.out
						.println("--------------------------------------------------------------------------------");
				System.exit(0);
			}
		}

		else if (args.length == 3) {

			int commandBoardSize = Integer.parseInt(args[0]);
			double commandMutationRate = Double.parseDouble(args[1]);
			int commandPopulationSize = Integer.parseInt(args[2]);

			// Check if input is valid
			if ((commandBoardSize >= 4)
					&& ((0F < commandMutationRate) && (commandMutationRate < 1F))
					&& (commandPopulationSize > 0)) {
				System.out
						.println("--------------------------------------------------------------------------------");
				System.out.println("\t\tGENETIC SEARCH");
				System.out
						.println("--------------------------------------------------------------------------------");
				int[] solution = GeneticUtils.geneticAlgorithmAgent(
						commandBoardSize, commandMutationRate,
						commandPopulationSize);

				for (int columnIndex = 0; columnIndex < solution.length; columnIndex++) {
					System.out.println("Queen" + (columnIndex + 1) + ": Row "
							+ solution[columnIndex] + " Column " + columnIndex);
				}
				System.out
						.println("--------------------------------------------------------------------------------");
			} else {
				System.out
						.println("Please restart the program and enter valid boardSize (greater than or equal to 4),"
								+ "\nmutationRate (double between 0 and 1), and populationSize (greater than 0)");
				System.out
						.println("--------------------------------------------------------------------------------");
				System.exit(0);
			}

		}

		else if (args.length == 2) {
			int commandBoardSize = Integer.parseInt(args[0]);
			int commandTemperature = Integer.parseInt(args[1]);

			// Check if input is valid
			if ((commandBoardSize >= 4) && (commandTemperature > 0)) {
				System.out
						.println("--------------------------------------------------------------------------------");
				System.out.println("\t\tSIMULATED ANNEALING SEARCH");
				System.out
						.println("--------------------------------------------------------------------------------");
				int[] solution = AnnealingUtils.simulatedAnnealingAgent(
						commandBoardSize, commandTemperature);

				for (int columnIndex = 0; columnIndex < solution.length; columnIndex++) {
					System.out.println("Queen" + (columnIndex + 1) + ": Row "
							+ solution[columnIndex] + " Column " + columnIndex);
				}
				System.out
						.println("--------------------------------------------------------------------------------");
			} else {
				System.out
						.println("Please restart the program and enter valid boardSize (greater than or equal to 4),"
								+ "\nand temperature (greater than 0)");
				System.out
						.println("--------------------------------------------------------------------------------");
				System.exit(0);
			}
		} else {
			System.out
					.println("Please restart the program and enter valid input");
			System.out
					.println("--------------------------------------------------------------------------------");
			System.exit(0);
		}
	}
}
