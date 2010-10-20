package edu.itba.skbsolver;

import java.awt.Point;

/**
 * A scene stores the state of all objects in the map. It is related to a
 * , that stores the position of non-movable items. 
 * 
 * @author eordano
 */
public class Snap {
	
	private Point[] boxes;
	private Point player;
	private Scenario baseMap;
	
	/**
	 * Create a Snap from:
	 * 
	 * @param boxes     a vector of positions where boxes are
	 * @param player    the position of the player
	 * @param baseMap   a Scenario where the boxes are 
	 */
	Snap(Point[] boxes, Point player, Scenario baseMap){
		this.boxes = boxes;
		this.player = player;
		this.baseMap = baseMap;
	}

	public Point[] getBoxes(){
		return boxes;
	}

	public Point getPlayer() {
		return player;
	}

	public Scenario getBaseMap() {
		return baseMap;
	}
	
}
