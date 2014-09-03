package edu.ohio_state.cse.genequeens;

import java.util.Collection;

/**
 * Implementations of {@code MateSelector} are intended to select proper mates
 * for an individual from a population based on criteria specified by the
 * implementer. These criteria will be problem-dependent and can vary from
 * completely stochastic selection to deterministic selection based on an
 * {@link edu.ohio_state.cse.genequeens.Evolvable Evolvable}'s fitness.
 * 
 * @author EthanHill
 *
 */
public interface MateSelector<T> {

	/**
	 * Finds a mate for {@code individual}, given the population from which
	 * {@code individual} comes. For expected behavior, {@code population} must
	 * contain {@code individual}. If no mate can be found for
	 * {@code individual} in {@code population}, simply return
	 * {@code individual}.
	 * 
	 * @param individual
	 *            The individual for which a mate is being selected from
	 *            {@code population}.
	 * @param population
	 *            The population from which a mate is being selected for
	 *            {@code individual}.
	 * @return A suitable mate for {@code individual} selected from
	 *         {@code population}. If no suitable mate is found, the individual
	 *         is instead returned.
	 */
	Evolvable selectMate(Evolvable individual, Collection<Evolvable> population);
}
