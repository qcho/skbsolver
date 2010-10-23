package edu.itba.skbsolver;

import java.util.HashSet;
import java.util.Set;

public class PositionsTable {
	
	private Set<State> map;
	
	public PositionsTable(){
		map = new HashSet<State>(1<<20); // Create a hashset with 20 bits keys
	}

	public boolean has(State newState) {
		return map.contains(newState);
	}
	
	public void add(State newState){
		map.add(newState);
	}
	
}
