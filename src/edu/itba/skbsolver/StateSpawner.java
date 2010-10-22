package edu.itba.skbsolver;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class StateSpawner{

	public static final int[] dx = {0,1,0,-1};
	public static final int[] dy = {1,0,-1,0};
	
	private PositionsTable posTable;
	private Level level;

	
	public StateSpawner(PositionsTable posTable, Level level){
		this.posTable = posTable;
	}

	public List<State> childs(State s) {
		List<State> newStates = new LinkedList<State>();
		Deque<Integer> queue = new LinkedList<Integer>();
		int[][] distance = new int[level.xsize][level.ysize];
		int[][] boxIndex = new int[level.xsize][level.ysize];

		int px, py, rx, ry, p, r, boxMoved;
		
		
		// Initialize auxiliar vectors
		for (int i = 0; i < level.xsize; i++){
			for (int j = 0; j < level.ysize; j++){
				boxIndex[i][j] = distance[i][j] = -1;
			}
		}
		for (int i = 0; i < s.boxes.length; i++){
			boxIndex[s.boxes[i] >> 16][s.boxes[i] & 0xFFFF] = i;
		}

		// Put first element on queue
		queue.addLast(s.player);
		distance[s.px()][s.py()] = 0;

		// BFS for pushes
		while (!queue.isEmpty()){
			p = queue.removeFirst();
			px = p >> 16;
			py = p & 0xFFFF;
			for (int d = 0; d < 4; d++){
				rx = px+dx[d];
				ry = py+dy[d];
				
				// TODO: esto es un placeholders
				boolean validMove = true;
				
				if (distance[rx][ry] == -1 && // Si no visité este tile y:
						
					level.get(rx,ry)!='#' &&  // no hay una pared, entro, pero solo sí:
					
					(boxIndex[rx][ry] == -1 || // <- No hay una caja, el tile está vacío
							
							// o hay una caja ahí y es movible:
					(level.get(rx+dx[d], ry+dy[d]) != '#' && boxIndex[rx+dx[d]][ry+dy[d]] == -1)
					
				)) {
					r = (rx<<16)+ry;
					
					boxMoved = boxIndex[rx][ry];
					
					if (boxMoved != -1) {
						// TODO: Check if no simple deadlocks
						
						State newState = new State(s, boxMoved, d, distance[rx][ry]);
						if (!posTable.has(newState)){
							// TODO: Check for further deadlocks
							newStates.add(newState);
						}
					}
					// Add the new position to the queue
					distance[rx][ry] = distance[px][py] + 1;
					queue.addLast(r);
				}
			}
		}
		
		return newStates;
	}

}
