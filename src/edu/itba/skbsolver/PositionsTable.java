package edu.itba.skbsolver;

public class PositionsTable {

	private boolean[] map;

	public PositionsTable() {
		map = new boolean[1 << 24]; // Create a hashset with 24 bits keys
		for (int i = 0; i < 1 << 24; i++) {
			map[i] = false;
		}
	}

	public boolean has(int hash) {
		return map[hash & ((1 << 24) - 1)];
	}

	public void add(int hash) {
		map[hash & ((1 << 24) - 1)] = true;
	}

}
