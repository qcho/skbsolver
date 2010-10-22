package edu.itba.skbsolver;

import java.util.HashMap;
import java.util.Map;

public class PositionsTable {
	
	private Map<State, Boolean> map;
	
	public PositionsTable(){
		map = new HashMap<State, Boolean>();
	}

	public boolean has(State newState) {
		return map.containsKey(newState);
	}
	
	public void add(State newState){
		map.put(newState, true);
	}
	
}
