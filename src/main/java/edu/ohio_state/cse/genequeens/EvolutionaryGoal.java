package edu.ohio_state.cse.genequeens;

import java.util.Collection;

/**
 * An {@code EvolutionaryGoal} is defined as a monitor of a population (of
 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvables} and its current
 * progress toward a particular goal in a
 * {@link edu.ohio_state.cse.genequeens.GeneticAlgorithms GeneticAlgorithm}. A
 * goal should be defined per problem in the implementation of {@code this}, but
 * generally will involve evaluating the fitness of a population and determining
 * if this meets satisfactory conditions for the problem at hand.
 * <p>
 * It should be noted that not every problem being solved by a
 * {@code GeneticAlgorithm} will have an applicable {@code EvolutionaryGoal}.
 * </p>
 * 
 * @author Ethan Hill
 *
 */
public interface EvolutionaryGoal {

	/**
	 * Determines whether an individual in {@code population}, or
	 * {@code population} itself, satisfies the implemented Goal.
	 * 
	 * @param population
	 *            The population of
	 *            {@link edu.ohio_state.cse.genequeens.Evolvable Evolvables} to
	 *            verify.
	 * @return {@code true} if {@code population} satisfies the requirements of
	 *         {@code this}, and {@code false} otherwise.
	 */
	boolean isSatisfied(Collection<Evolvable> population);
}
