package edu.itba.skbsolver;

import java.util.List;
import java.util.PriorityQueue;

import edu.itba.skbsolver.dot.DotPrinter;

public class BFSRunner {
	public static Solution run(Level level, DotPrinter dotPrinter) {
		PriorityQueue<State> queue = new PriorityQueue<State>();
		PositionsTable posTable = new PositionsTable();
		StateSpawner stateSpawner = new StateSpawner(posTable, level);

		State winner = null;
		queue.add(level.getInitialState());

		while (!queue.isEmpty()) {
			State s = queue.remove();

			if (level.playerWin(s)){
				winner = s;
				queue.clear();
				break;
			}
				
			List<State> newStates = stateSpawner.childs(s, true);

			// TODO: reorder states with a Heuristic

			for (State n : newStates) {
				if(dotPrinter != null){
					dotPrinter.addState(n);
				}
				queue.add(n);
			}
		}

		return new Solution(winner);
	}

}
