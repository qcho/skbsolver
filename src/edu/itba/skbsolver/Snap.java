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
	
	/**
	 * Create a Snap from:
	 * a vector of points, a position for the player, and a Scene
	 * 
	 * @param pathToMap the path to a file with the map
	 */
	Snap(Point[] boxes, Point playerPosition, Scenario baseMap){
		
	}

	public Point[] getBoxes(){
		return this.boxes;
	}
	
	
}
