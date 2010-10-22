package edu.itba.skbsolver;

import java.util.Deque;

public class BFSRunner {
	public static void run(Level level){
		PriorityQueue<State> queue;
		PositionsTable posTable;
		StateSpawner stateSpawner(posTable);
				
		queue.push(level.getInitialState(), 0);
		
		while (!queue.empty()){
			State s = queue.pop();
			for(State n : stateSpawner.childs(s)){
				
			}
		}
	}

}
