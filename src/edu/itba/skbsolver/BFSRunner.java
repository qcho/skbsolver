package edu.itba.skbsolver;

import java.util.Comparator;
import java.util.Deque;
import java.util.PriorityQueue;

public class BFSRunner {
	public static void run(Level level){
		Comparator<State> stateComparator = new Comparator<State>(){
			@Override
			public int compare(State a, State b) {
				if (a.moves == b.moves){
					return a.player - b.player;
				}
				return a.moves - b.moves;
			}
		};
		PriorityQueue<State> queue(stateComparator);
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
