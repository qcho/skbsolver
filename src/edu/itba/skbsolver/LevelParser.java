package edu.itba.skbsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelParser {

	final static Logger logger = LoggerFactory.getLogger(LevelParser.class);

	public int xsize = 0;
	public int ysize = 0;

	// Stores the map
	public char[][] map;

	public int[][] playerZobrist;
	public int[][] boxZobrist;
	
	public List<Integer> boxesBuffer;
	public List<Integer> playerBuffer;
	
	public LevelParser(File level) {
		xsize = 0;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(level));
			
			List<String> lines = new LinkedList<String>();
			boxesBuffer = new LinkedList<Integer>();
			playerBuffer = new LinkedList<Integer>();
			
			String str;

			while ((str = in.readLine()) != null) {
				lines.add(processLine(boxesBuffer, playerBuffer, xsize, str));
				
				xsize++;
				ysize = Math.max(ysize, str.length());
			}
			
			map = new char[xsize][ysize];
			int j = 0;
			
			for (String line : lines){
				char[] myLine = new char[ysize];
				int i;
				for(i = 0; i < line.length(); i++){
					myLine[i] = line.charAt(i);
				}
				for(; i < ysize; i++){
					myLine[i] = ' ';
				}
				map[j++] = myLine;
			}
			
			in.close();
		} catch (IOException e) {
			logger.error("Error parsing level", e);
		}

	}

	private String processLine(List<Integer> boxesBuffer, List<Integer> playerBuffer, int x, String line) {
		logger.info("line " + line);
		StringBuffer myLine = new StringBuffer();
		
		int y = 0;
		for (char c : line.toCharArray()) {
			
			switch (c) {
				case '#':
					myLine.append('#');
					break;
				case '@':
					myLine.append(' ');
					playerBuffer.add(((x & 0xFFFF) << 16) | (y & 0xFFFF));
					break;
				case '+':
					myLine.append('.');
					playerBuffer.add(((x & 0xFFFF) << 16) | (y & 0xFFFF));
					break;
				case '$':
					myLine.append(' ');
					boxesBuffer.add(((x & 0xFFFF) << 16) | (y & 0xFFFF));
					break;
				case '*':
					myLine.append('.');
					boxesBuffer.add(((x & 0xFFFF) << 16) | (y & 0xFFFF));
					break;
				case '.':
					myLine.append('.');
					break;
				case ' ':
					myLine.append(' ');
					break;
				default:
					break;
			}
			
			y++;
		}

		return myLine.toString();
	}
}
