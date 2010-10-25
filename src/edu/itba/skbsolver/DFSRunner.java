package edu.itba.skbsolver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import edu.itba.skbsolver.dot.DotPrinter;

public class DFSRunner {
	public static Solution run(Level level, DotPrinter dotPrinter) {
		Deque<State> stack = new LinkedList<State>();
		PositionsTable posTable = new PositionsTable();
		StateSpawner stateSpawner = new StateSpawner(posTable, level);

		State winner = null;
		stack.addFirst(level.getInitialState());

		while (!stack.isEmpty()) {
			State s = stack.removeLast();
			List<State> newStates = stateSpawner.childs(s, false);

			//sort((ArrayList<State>) newStates, new CompareState());

			for (State n : newStates) {
				if (level.playerWin(n)) {
					winner = n;
					level.logger.info("Found a solution: \n" + n.toString());
				}
				if (winner == null || n.moves < winner.moves) {
					stack.addLast(n);
				}
			}
		}
		
		if (winner != null){
			posTable = new PositionsTable();
			stateSpawner = new StateSpawner(posTable, level);
			stack.addFirst(level.getInitialState());
			
			while (!stack.isEmpty()) {
				State s = stack.removeLast();
				List<State> newStates = stateSpawner.childs(s, true);
	
				//sort((ArrayList<State>) newStates, new CompareState());
	
				for (State n : newStates) {
					if (level.playerWin(n)) {
						winner = n;
						level.logger.info("Found a solution: \n" + n.toString());
					}
					if (winner == null || n.moves < winner.moves) {
						stack.addLast(n);
					}
				}
			}
		}
		
		return new Solution(winner);
	}

	private static class CompareState implements Comparator<State> {
		public int compare(State a, State b){
			return -((a.moves - b.moves) + (a.moves - a.parent.moves - b.moves + b.parent.moves));
		}
	}

}
