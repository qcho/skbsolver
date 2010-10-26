package edu.itba.skbsolver;

import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import edu.itba.skbsolver.dot.DotPrinter;

public class DFSRunner {

	final static Logger logger = Logger.getLogger(false);
	
	public static Solution run(Level level, DotPrinter dotPrinter) {
		Deque<State> stack = new LinkedList<State>();
		PositionsTable posTable = new PositionsTable();
		StateSpawner stateSpawner = new StateSpawner(posTable, level);

		State winner = null;
		stack.addFirst(level.getInitialState());

		while (!stack.isEmpty()) {
			State s = stack.removeLast();
			List<State> newStates = stateSpawner.childs(s, false);

			for (State n : newStates) {
				if (level.playerWin(n)) {
					winner = n;
					logger.debug("Found a solution: \n" + n.toString());
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
				
				if(dotPrinter != null){
					dotPrinter.addState(s);
				}
				
				List<State> newStates = stateSpawner.childs(s, true);
	
				Collections.sort(newStates, new CompareState());

				if(dotPrinter != null){
					StringBuilder sb = new StringBuilder();
					sb.append("Cutting "+stateSpawner.countDeadlocks+" deadlocks,\\n");
					sb.append("Cutting "+stateSpawner.countCapacity+" capacity deadlocks.\\n");
					sb.append("Revisiting "+stateSpawner.countRevisited+" childs.\\n");
					sb.append("New "+stateSpawner.countNewFreeze+" capacitors.\\n");
					dotPrinter.addAnnotation(s, "#62B1D0", sb.toString());
				}
				
				for (State n : newStates) {
					if (level.playerWin(n)) {
						winner = n;
						logger.debug("Found a solution: \n" + n.toString());
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
			return -((a.moves - b.moves) + 4*(a.moves - a.parent.moves - b.moves + b.parent.moves));
		}
	}

}
