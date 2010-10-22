package edu.itba.skbsolver;

import java.awt.Point;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class StateSpawner {
	private PositionsTable posTable;
	private Level level;

	public final int[] dx = {0,1,0,-1};
	public final int[] dy = {1,0,-1,0};
	
	public StateSpawner(PositionsTable posTable, Level level){
		this.posTable = posTable;
	}

	public List<State> childs(State s) {
		List<State> newStates = new LinkedList<State>();
		Deque<Point> queue = new Dequeue<Point>();
		boolean[][] visited = new boolean[level.xsize][level.ysize];
		
		queue.addLast(s.playerPoint());
		while (!queue.isEmpty()){
			Point p = queue.removeFirst();
			for (int d = 0; d < 4; d++){
				Point r = new Point(p.x+dx[d], p.y+dy[d]);
				boolean validMove = true;
				boolean movesBox = true;
				if (validMove) { // TODO: if valid move
					if (movesBox) { // TODO: if moves a box
						int boxMoved = 1; // TODO: calculate what box has been moved
						// TODO: Check if no simple deadlocks
						// TODO: Check if not exists in PositionsTable
						// TODO: Create new State
						State newState = new State(s, boxMoved, d); 
						// TODO: Check for further deadlocks
						newStates.add(newState);
					}
					// Add the new position to the queue
					queue.add(r);
				}
			}
		}
		
		return newStates;
	}

}
