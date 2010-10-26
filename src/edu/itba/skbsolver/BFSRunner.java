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
				dotPrinter.addAnnotation(s, "#62B1D0", "Cutting "+stateSpawner.countDeadlocks+" deadlocks.");
				dotPrinter.addAnnotation(s, "#FF8B73", "Cutting "+stateSpawner.countCapacity+" capacity deadlocks.");
				dotPrinter.addAnnotation(s, "#FFC640", "Revisiting "+stateSpawner.countRevisited+" childs.");
				dotPrinter.addAnnotation(s, "#6EE768", "New "+stateSpawner.countNewFreeze+" capacitors.");
			}

			for (State n : newStates) {
				queue.add(n);
			}
		}

		return new Solution(winner);
	}

}
