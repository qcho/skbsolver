package edu.itba.skbsolver;

public class PositionsTable {

	private State[] map;
	public static int SIZE = 1<<24;
	public static int MASK = SIZE-1;

	public PositionsTable() {
		map = new State[SIZE]; // Create a with 24 bits keys
		for (int i = 0; i < SIZE; i++){
			map[i] = null;
		}
	}

	public boolean has(State state) {
		return map[find(state)] != null;
	}

	public void add(State state) {
		map[find(state)] = state;
	}
	
	public State get(State state){
		return map[find(state)];
	}
	
	private int find(State state){
		int hash = state.hashCode() & MASK;
		while (map[hash] != null && !map[hash].equals(state)){
			hash += 1;
			if (hash == SIZE){
				hash = 0;
			}
		}
		return hash;
	}

}
