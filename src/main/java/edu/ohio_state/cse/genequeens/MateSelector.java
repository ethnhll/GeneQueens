package edu.ohio_state.cse.genequeens;

import java.util.Collection;

/**
 * 
 * @author EthanHill
 *
 */
public interface MateSelector<T> {

	/**
	 * 
	 * @param individual
	 * @param population
	 * @return A suitable mate for {@code individual} selected from
	 *         {@code population}. If no suitable mate is found, the individual
	 *         is instead returned.
	 */
	Evolvable selectMate(Evolvable individual, Collection<Evolvable> population);
}
