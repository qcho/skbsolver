package edu.itba.skbsolver;

import java.awt.Point;


/**
 * A scene stores the state of all objects in the map. It is related to a
 * , that stores the position of non-movable items. 
 * 
 * @author eordano
 */
public class State {
	
	public int[] boxes;
	public int moves;
	public int player;
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

	public Point playerPoint() {
		return new Point(px(), py());
	}

}
