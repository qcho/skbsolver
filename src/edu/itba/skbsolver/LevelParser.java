package edu.itba.skbsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelParser {

	final static Logger logger = LoggerFactory.getLogger(LevelParser.class);

	int xsize = 0;
	int ysize = 0;

	public LevelParser(File level) {

		try {
			BufferedReader in = new BufferedReader(new FileReader(level));
			String str;

			while ((str = in.readLine()) != null) {

				xsize = Math.max(xsize, str.length());
				ysize++;

				processLine(str);
			}

			in.close();
		} catch (IOException e) {
			logger.error("Error parsing level", e);
		}

	}

	private void processLine(String line) {
		logger.info("line " + line);
		int x = 0;
		for (char c : line.toCharArray()) {
			
			switch (c) {
			case '#':
				break;
			case '@':
				break;
			case '+':
				break;
			case '$':
				break;
			case '*':
				break;
			case '.':
				break;
			case ' ':
				break;
			default:
				break;
			}
			
			
			System.out.print(c);
		}

	}

}
