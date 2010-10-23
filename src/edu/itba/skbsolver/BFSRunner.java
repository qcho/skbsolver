package edu.itba.skbsolver;

import java.util.PriorityQueue;

public class BFSRunner {
	public static void run(Level level){
		PriorityQueue<State> queue = new PriorityQueue<State>();
		PositionsTable posTable = new PositionsTable();
		StateSpawner stateSpawner = new StateSpawner(posTable, level);

		State winner = null;
		queue.add(level.getInitialState());
		
		while (!queue.isEmpty()){
			State s = queue.remove();
			for(State n : stateSpawner.childs(s)){
				if (level.playerWin(n)){
					winner = n;
					queue.clear();
					break;
				}
				queue.add(n);
			}
		}
		
		// TODO: Rebuild solution from winner

	}

}
