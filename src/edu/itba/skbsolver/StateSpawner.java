package edu.itba.skbsolver;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import edu.itba.skbsolver.exception.TileSetCapacityExceeded;

public class StateSpawner{

	public static final int[] dx = {0,1,0,-1};
	public static final int[] dy = {1,0,-1,0};
	
	private PositionsTable posTable;
	private Level level;

	
	public StateSpawner(PositionsTable posTable, Level level){
		this.posTable = posTable;
		this.level = level;
	}

	public List<State> childs(State s) {
		List<State> newStates = new LinkedList<State>();
		Deque<Integer> queue = new LinkedList<Integer>();
		int[][] distance = new int[level.xsize][level.ysize];
		int[][] boxIndex = new int[level.xsize][level.ysize];

		int px, py, rx, ry, tx, ty, p, r, boxMoved;
		boolean noDeadlock;
		
		
		// Initialize auxiliar vectors
		for (int i = 0; i < level.xsize; i++){
			for (int j = 0; j < level.ysize; j++){
				boxIndex[i][j] = distance[i][j] = -1;
			}
		}
		for (Capacitor cap : level.getCapacitors()){
			cap.reset();
		}
		try{
			for (int i = 0; i < s.boxes.length; i++){
				boxIndex[s.boxes[i] >> 16][s.boxes[i] & 0xFFFF] = i;
				for (Capacitor cap : level.getCapacitorsByPos(s.boxes[i]>>16, s.boxes[i] & 0xFFFF)){
					cap.countPlus();
				}
			}
		} catch (TileSetCapacityExceeded tS){ // This should not happen :)
			return new LinkedList<State>();
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
				
				tx = rx+dx[d];
				ty = ry+dy[d];
				
				if (distance[rx][ry] == -1 && // Si no visité este tile, y además:
						
					level.get(rx,ry)!='#' &&  // si no hay una pared, entro, pero solo sí:
					(   
						boxIndex[rx][ry] == -1 || // <- No hay una caja, entonces el tile está vacío	
						(
								// O hay una caja ahí
								level.get(tx, ty) != '#'
								
								// y es movible (no se choca con nada después):
								&& boxIndex[tx][ty] == -1
						
								// and is a "step-able" tile
								&& !level.isBasicDeadlock(tx, ty)
								
						)
					)
				) {
					noDeadlock = true;
										
					r = (rx<<16)+ry;
					
					boxMoved = boxIndex[rx][ry];
					
					if (boxMoved != -1) {
						
						int newHash = s.hashIfMove(d, boxMoved);
						
						if (posTable.has(newHash)){
							noDeadlock = false;
						}

						// Si no dispara un Capacitor Deadlock
						// This is kind of a easy check, let's do this before the freeze deadlock
						if (noDeadlock){
							for (Capacitor cap : level.getCapacitorsByPos(tx, ty)){
								if(!cap.canIstepInto()){
									noDeadlock = false;
								}
							}
						}

						if (noDeadlock){
							if (!s.triggersFreezeDeadlock(boxMoved, d)){

								posTable.add(newHash);
								
								State newState = new State(s, boxMoved, d, distance[rx][ry], newHash);
	
								// TODO: Bipartite deadlock?
								// I think bipartite deadlocks should be checked
								// only if we just inserted a box into a target :D
								
								// TODO: Other deadlock???
								
								newStates.add(newState);
								
							}
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
