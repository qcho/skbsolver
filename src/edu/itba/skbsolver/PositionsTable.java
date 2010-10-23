package edu.itba.skbsolver;

public class PositionsTable {
	
	private boolean[] map;
	
	public PositionsTable(){
		map = new boolean[1<<20]; // Create a hashset with 20 bits keys
		for(int i = 0; i < 1<<20; i++){
			map[i] = false;
		}
	}

	public boolean has(int hash) {
		return map[hash & ((1<<20) - 1)];
	}
	
	public void add(int hash){
		map[hash & ((1<<20) - 1)] = true;
	}
	
}
