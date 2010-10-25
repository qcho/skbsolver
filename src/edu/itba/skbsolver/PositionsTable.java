package edu.itba.skbsolver;

public class PositionsTable {

	private State[] map;

	public PositionsTable() {
		map = new State[1 << 24]; // Create a hashset with 24 bits keys
		for (int i = 0; i < 1 << 24; i++){
			map[i] = null;
		}
	}

	public boolean has(int hash) {
		return map[hash & ((1 << 24) - 1)] != null;
	}

	public void add(int hash, State state) {
		map[hash & ((1 << 24) - 1)] = state;
	}

	public State get(int newHash) {
		return map[newHash & ((1 << 24) - 1)];
	}

}
