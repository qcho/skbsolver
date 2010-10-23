package edu.itba.skbsolver;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


/**
 * A Scenario is a data structure to keep a map in screen. A map is a set of
 * walls and targets, in this case represented by chars in a array.
 * 
 * @author eordano
 */
public class Level {
	
	// Stores the map
	private String[] map;
	
	// A representation of the initial status
	private State initial;

	private List<Capacitor> capacitors;
	
	private List<Capacitor>[][] capacitorMap;
	
	// TODO: Calculate this when building level
	public int xsize;
	public int ysize;
	
	/**
	 * Create a Scenario from a filename. The initial position of things
	 * is stored locally and can be accessed with getInitialSnap(). 
	 * 
	 * @param fileName
	 */
	Level(String fileName){
		List<Point> boxes = new LinkedList<Point>();
		List<String> lines = new LinkedList<String>();
		int player = 0;
		
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
					player = ((x&0xFFFF) << 16) | (y & 0xFFFF);
					line.append(' ');
				} else if (read == '+'){
					player = ((x&0xFFFF) << 16) | (y & 0xFFFF);
					line.append('.');
				} else if (read == '$'){
					boxes.add(new Point(x, y));
					line.append(' ');
				} else if (read == '*'){
					boxes.add(new Point(x, y));
					line.append('.');
				} else if (read == '#'){
					line.append('#');
				} else if (read == ' '){
					line.append(' ');
				} else if (read == '.'){
					line.append('.');
				} else {
					throw new RuntimeException("Unrecognized character");
				}
				
				// Move to the left
				y++;
			}
			
			int[] finalBoxes = new int[boxes.size()];
			int i = 0;
			for (Point box : boxes){
				finalBoxes[i++] = (box.x << 16) | (box.y & 0xFFFF);
			}
			initial = new State(finalBoxes, player, this, 0);
			
			map = new String[lines.size()];
			i = 0;
			for (String aLine : lines){
				map[i++] = aLine;
			}
		} catch (IOException e) {
			throw new RuntimeException("Error reading file");
		}
		return;
	}
	
	public State getInitialState(){
		return initial;
	}
	
	public char get(int x, int y){
		return map[x].charAt(y);
	}
	
	/**
	 * Is the game over if I'm in a given status?
	 */
	public boolean playerWin(State snap){
		for(int box : snap.boxes){
			if (map[box>>16].charAt(box&0xFFFF) != '.'){
				return false;
			}
		}
		return true;
	}

	public List<Capacitor> getCapacitors() {
		return this.capacitors;
	}
	
	public List<Capacitor> getCapatitorsByPos(int x, int y){
		return this.capacitorMap[x][y];
	}
}
