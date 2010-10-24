package edu.itba.skbsolver;

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
			State s = stack.removeFirst();
			List<State> newStates = stateSpawner.childs(s);

			// TODO: reorder states with a Heuristic

			for (State n : newStates) {
				if (level.playerWin(n)) {
					winner = n;
					level.logger.info("Found a solution: \n" + n.toString());
				}
				if (winner == null || n.moves < winner.moves) {
					stack.addFirst(n);
				}
			}
		}

		return new Solution(winner);
	}
}
