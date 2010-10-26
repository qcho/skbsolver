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

			if(dotPrinter != null){
				dotPrinter.addState(s);
			}
			
			if (level.playerWin(s)){
				winner = s;
				queue.clear();
				break;
			}
				
			List<State> newStates = stateSpawner.childs(s, true);

			if(dotPrinter != null){
				StringBuilder sb = new StringBuilder();
				sb.append("Cutting "+stateSpawner.countDeadlocks+" deadlocks,\\n");
				sb.append("Cutting "+stateSpawner.countCapacity+" capacity deadlocks.\\n");
				sb.append("Revisiting "+stateSpawner.countRevisited+" childs.\\n");
				sb.append("New "+stateSpawner.countNewFreeze+" capacitors.\\n");
				dotPrinter.addAnnotation(s, "#62B1D0", sb.toString());
			}

			for (State n : newStates) {
				queue.add(n);
			}
		}

		return new Solution(winner);
	}

}
