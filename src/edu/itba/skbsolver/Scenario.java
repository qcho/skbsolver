package edu.itba.skbsolver;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


public class Scenario {
	
	// Stores the map
	private char[][] map;
	
	// A representation of the initial status
	private Snap initial;
	
	/**
	 * Create a Scenario from a filename. The initial position of things
	 * is stored locally and can be accessed with getInitialSnap(). 
	 * 
	 * @param fileName
	 */
	Scenario(String fileName){
		List<Point> boxes = new LinkedList<Point>();
		List<String> lines = new LinkedList<String>();
		Point player = null;
		
		InputStream istream = null;
		try {
			istream = new FileInputStream(fileName);
		} catch (Exception e){
			throw new RuntimeException("Could not open file");
		}
		
		int read = 0;
		try {
			StringBuffer line = new StringBuffer();
			int x = 0;
			int y = 0;
			while((read = istream.read()) != -1){
				
				if (read == '\n'){
					// Reset positions
					x++;
					y = 0;
					
					// Add line to list
					lines.add(line.toString());
					
					// Reset line
					line = new StringBuffer();
					
				} else if (read == '@'){
					player = new Point(x, y);
					line.append(' ');
				}
				
				// Move to the left
				y++;
			}
			
			Point[] finalBoxes = new Point[boxes.size()];
			int i = 0;
			for (Point box : boxes){
				finalBoxes[i++] = box;
			}
			initial = new Snap(finalBoxes, player, this);
		} catch (IOException e) {
			throw new RuntimeException("Error reading file");
		}
		return;
	}
	
	/**
	 * Is the game over if I'm in a given status?
	 */
	public boolean playerWin(Snap snap){
		for(Point box : snap.getBoxes()){
			if (map[box.x][box.y] != '.'){
				return false;
			}
		}
		return true;
	}
}
