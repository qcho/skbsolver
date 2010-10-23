package edu.itba.skbsolver;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class DFSRunner {
	public static Solution run(Level level){
		Deque<State> stack = new LinkedList<State>();
		PositionsTable posTable = new PositionsTable();
		StateSpawner stateSpawner = new StateSpawner(posTable, level);

		State winner = null;
		stack.addFirst(level.getInitialState());
		
		while (!stack.isEmpty()){
			State s = stack.removeFirst();
			List<State> newStates = stateSpawner.childs(s);
			
			// TODO: reorder states with a Heuristic
			
			for(State n : newStates){
				if (level.playerWin(n)){
					winner = n;
					stack.clear();
					break;
				}
				stack.add(n);
			}
		}
		
		// TODO: Rebuild solution from winner
		return new Solution(winner);
	}
}
