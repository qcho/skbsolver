package edu.itba.skbsolver;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


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

	// The entire list of capacitors in a map.
	private List<Capacitor> capacitors;
	
	// This should be List<Capacitor>[][] but Java doesn't allow it
	private Object[][] capacitorMap;
	
	// This map returns what tiles are "step-able" by boxes without triggering
	// a simple deadlock. Example: a box trapped in a corner
	private boolean[][] isDeadlock;
	
	// TODO: Calculate this when building level
	public int xsize;
	public int ysize;

	public int[][] playerZobrist;
	public int[][] boxZobrist;
	
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
		xsize = 0;
		ysize = 0;
		
		// Load files
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
					
				} else if (read == '@'){ // Player
					player = ((x&0xFFFF) << 16) | (y & 0xFFFF);
					line.append(' ');
				} else if (read == '+'){ // Player on a target
					player = ((x&0xFFFF) << 16) | (y & 0xFFFF);
					line.append('.');
				} else if (read == '$'){ // Box
					boxes.add(new Point(x, y));
					line.append(' ');
				} else if (read == '*'){ // Box on a target
					boxes.add(new Point(x, y));
					line.append('.');
				} else if (read == '#'){ // Wall
					line.append('#');
				} else if (read == ' '){ // Empty spaces
					line.append(' ');
				} else if (read == '.'){ // Target
					line.append('.');
				} else {                 // This shouldn't happen
					throw new RuntimeException("Unrecognized character");
				}
				
				// Move to the left
				y++;
				ysize = ysize < y ? y : ysize; // Update board size
			}
			xsize = x; // Update board size
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

		capacitors = new LinkedList<Capacitor>();
		
		capacitorMap = new Object[xsize][ysize];

		isDeadlock = new boolean[xsize][ysize];
		
		for(int i = 0; i < xsize; i++){
			for(int j = 0; j < ysize; j++){
				isDeadlock[i][j] = true;
				capacitorMap[i][j] = new LinkedList<Capacitor>();
			}
		}
		
		createZobristKeys();
		
		calculateDeadlocks();
		
		calculateHallwayCapacitors();
		calculateTwinsCapacitors();
		calculateCornerCapacitors();
		return;
	}
	
	public State getInitialState(){
		return initial;
	}
	
	
	/**
	 * Return the tile in a given position 
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
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

	/**
	 * Get all capacitors for the level
	 * 
	 * @return
	 */
	public List<Capacitor> getCapacitors() {
		return this.capacitors;
	}
	
	/**
	 * Return the list of capacitors generated for a random position.
	 * 
	 * This method retrieves a list of capacitors involved with this tile,
	 * so another method can check if he can "step into" this tile.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Capacitor> getCapacitorsByPos(int x, int y){
		return (List<Capacitor>) this.capacitorMap[x][y];
	}

	public boolean isBasicDeadlock(int x, int y) {
		return this.isDeadlock[x][y];
	}
	
	/**
	 * Create Zobrist hash random strings.
	 * 
	 * This works the following way:
	 * 
	 * For the entire game, each tile is asigned a random string generated at
	 * the beggining of the game. When a piece gets into that tile, the State key
	 * gets XORed with the Zobrist key of that tile. A piece in Sokoban can be
	 * either a player or a box, so we create two different zobrist keys.
	 * 
	 * This program asumes that if two Zobrist keys are the same, in a 16 bit key
	 * hash table, the states are the same (you have to be very unlucky to hit the
	 * same hash with two different States)
	 */
	private void createZobristKeys() {
		Random randGen = new Random(xsize + ysize);
		playerZobrist = new int[xsize][ysize];
		boxZobrist = new int[xsize][ysize];
		for (int i = 0; i < xsize; i++){
			for (int j = 0; j < ysize; j++){
				playerZobrist[i][j] = randGen.nextInt();
				boxZobrist[i][j] = randGen.nextInt();
			}
		}
	}


	/**
	 * Calculate simple Deadlocks.
	 * 
	 * This method figures out in what cells can a block be before going to a target.
	 * Given the 1 to 1 relationship that exists between boxes an targets, if a box is
	 * in, say, a corner, it will never be able to get to a target and the game is
	 * over.
	 * 
	 * This algorithm starts with a box in every target and does a BFS by placing a
	 * box in the target and after that, branches out placing a player in every
	 * direction, with roles swaped (the box pushes the player). If the player ends
	 * up in a legal position, the tile in which the box is gets marked as "step-able"
	 * by a box. 
	 */
	private void calculateDeadlocks() {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * This method scans the map for a "Hallway capacitor".
	 * 
	 * These are a set of tiles that, once a box gets into some tile, it can
	 * not escape, but there can be a target in that set.
	 * 
	 * Example:
	 * 		         ##########
	 *               #1 .  $ 2#
	 * 
	 * There, the sign "$" represents a box. The white spaces, and the point,
	 * and the tile under the '$', is a "Hallway Set" of capacity 1. If
	 * another box wants to get into the Set, it raises a Deadlock. 
	 * 
	 * This can be of this form, also (beware):
	 * 
	 *                  ###
	 * 	             #### #####
	 *               #1 .  $ 2#
	 */
	private void calculateHallwayCapacitors(){
		// TODO
	}
	
	/**
	 * This method scans the map for "Corner capacitors".
	 * 
	 * When the map contains a corner of this form:
	 * 
	 *       ###
	 *       #32
	 *       #1
	 * 
	 * And the tiles 1 and 2 are occupied by boxes, that is a deadlock right there,
	 * except in the case that the player is in the tile 3. Since that can be only 
	 * possible if that is the initial position, we'll be careful not to count it.
	 */
	private void calculateCornerCapacitors(){
		// TODO
	}
	
	/**
	 * This method scans the map for "Twin capacitors".
	 * 
	 * A set of tiles is said to be a "Twin capacitor set" if it is of this form:
	 * 
	 * ###
	 * # 1
	 * # 2
	 * # 3
	 * ###
	 * 
	 * In this case, moving any of these boxes triggers corner capacitors. But it
	 * may be the case that they're of this form:
	 * 
	 * ###
	 * #.3
	 * #.2
	 * #.1
	 * ###
	 * 
	 * So the amount of targets inside the area is also counted.
	 * 
	 * A Twin set can also be of these forms:
	 * 
	 * ####           ####    ######
	 * #  1           #  1    #  $     <- this one looks like a corner capacitor
	 * #  2           #  2    #  $     <- or a freeze deadlock
	 * ####           #  3    #$$
	 *                ####    #
	 */
	private void calculateTwinsCapacitors(){
		// TODO
	}
}
