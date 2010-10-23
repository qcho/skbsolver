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
	public static final int hashMagic = 5;
	
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
		this.hashCalculated = player;
		for (int i = 0; i < boxes.length; i++){
			hashCalculated ^= (boxes[i] >>> hashMagic*i);
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

		// TODO: Review Zobrist key, this works but is not quite good
		this.hashCalculated = s.hashCalculated ^
			s.player ^
			(s.boxes[boxMoved] >>> hashMagic*boxMoved) ^
			this.player ^
			(this.boxes[boxMoved] >>> hashMagic*boxMoved);
		
		int newBox = boxMoved;
		
		while (newBox > 0 && this.boxes[newBox] < this.boxes[newBox-1]){
			this.hashCalculated ^= (this.boxes[newBox] >>> hashMagic*newBox);
			this.hashCalculated ^= (this.boxes[newBox-1] >>> hashMagic*(newBox-1));
			swap(this.boxes, newBox, newBox - 1);
			this.hashCalculated ^= (this.boxes[newBox] >>> hashMagic*(newBox));
			this.hashCalculated ^= (this.boxes[newBox-1] >>> hashMagic*(newBox-1));
			newBox--;
		}
		while (newBox != boxes.length-1 && this.boxes[newBox] > this.boxes[newBox+1]){
			this.hashCalculated ^= (this.boxes[newBox] >>> hashMagic*newBox);
			this.hashCalculated ^= (this.boxes[newBox+1] >>> hashMagic*(newBox+1));
			swap(this.boxes, newBox, newBox + 1);
			this.hashCalculated ^= (this.boxes[newBox] >>> hashMagic*newBox);
			this.hashCalculated ^= (this.boxes[newBox+1] >>> hashMagic*(newBox+1));
			newBox++;
		}
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

	public boolean equals(State other){
		if (this.player != other.player){
			return false;
		}
		for(int i = 0; i < boxes.length; i++){
			if (this.boxes[i] != other.boxes[i]){
				return false;
			}
		}
		return true;
	}
	
	public int hashCode(){
		return this.hashCalculated;
	}
	
	private void swap(int[] v, int a, int b){
		int c = v[a];
		v[a] = b;
		v[b] = c;
	}
	
	public int compareTo(State b){
		if (moves == b.moves){
			return player - b.player;
		}
		return moves - b.moves;
	}
}
