package edu.itba.skbsolver;

import java.awt.Point;


/**
 * A scene stores the state of all objects in the map. It is related to a
 * , that stores the position of non-movable items. 
 * 
 * @author eordano
 */
public class State implements Comparable<State>{

	public static final int[] dx = {0,1,0,-1};
	public static final int[] dy = {1,0,-1,0};
	
	public int[] boxes;
	public int moves;
	public int player;
	public int hashCalculated = 0;
	public State parent = null;
	public Level map;
	
	/**
	 * Create a Snap from:
	 * 
	 * @param boxes     a vector of positions where boxes are
	 * @param player    the position of the player
	 * @param baseMap   a Scenario where the boxes are 
	 */
	State(int[] boxes, int player, Level map, int moves){
		this.boxes = boxes;
		this.player = player;
		this.map = map;
		this.moves = moves;
		this.hashCalculated = map.playerZobrist[player>>16][player&0xFFFF];
		for (int i = 0; i < boxes.length; i++){
			hashCalculated ^= map.boxZobrist[boxes[i]>>16][boxes[i]&0xFFFF];
		}
	}
	
	/**
	 * Create a State from a previous state and a box movement 
	 * 
	 * @param s
	 * @param boxMoved
	 * @param d
	 */
	public State(State s, int boxMoved, int direction, int playerMoves) {
		this.parent = s;
		this.boxes = s.boxes.clone();
		this.moves = s.moves + playerMoves;
		this.map = s.map;
		this.player = s.boxes[boxMoved];
		
		this.boxes[boxMoved] += (dx[direction]<<16) + dy[direction];

		// Update the Zobrist hash
		this.hashCalculated = s.hashCalculated ^
			map.playerZobrist[s.player>>16][s.player&0xFFFF] ^
			map.boxZobrist[s.boxes[boxMoved]>>16][s.boxes[boxMoved]&0xFFFF]^
  			map.playerZobrist[this.player>>16][this.player&0xFFFF] ^
			map.boxZobrist[this.boxes[boxMoved]>>16][this.boxes[boxMoved]&0xFFFF];
	}

	/**
	 * @return player's X position
	 */
	public int px(){
		return (player >> 16) & 0xFFFF;
	}
	/**
	 * @return players' Y position
	 */
	public int py(){
		return (player & 0xFFFF);
	}

	/** 
	 * @param i the index of the box
	 * @return the x coordinate of the box
	 */
	public int bx(int i){
		return (boxes[i] >> 16) & 0xFFFF;
	}
	
	/** 
	 * @param i the index of the box
	 * @return the y coordinate of the box
	 */
	public int by(int i){
		return (boxes[i] & 0xFFFF);
	}

	/**
	 * Retrieve the player's position as a Point
	 * @return
	 */
	public Point playerPoint() {
		return new Point(px(), py());
	}

	/**
	 * We are considering that two states are equal iff they have the same 
	 * Zobrist hash. This may not be true!!! but is very likely (if it's
	 * completely random, the chances are 1/4 billion) 
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(State other){
		return this.hashCalculated == other.hashCalculated;
	}
	
	/**
	 * The hash is calculated in the constructor
	 */
	public int hashCode(){
		return this.hashCalculated;
	}
	
	/**
	 * A comparison between states. This is required to push states into
	 * a priority queue.
	 */
	public int compareTo(State b){
		if (moves == b.moves){
			return player - b.player;
		}
		return moves - b.moves;
	}
}
