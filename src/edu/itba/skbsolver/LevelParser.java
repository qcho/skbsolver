package edu.itba.skbsolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelParser {

	final static Logger logger = LoggerFactory.getLogger(LevelParser.class);

	public LevelParser(File level) {

		try {
			BufferedReader in = new BufferedReader(new FileReader(level));
			String str;
			while ((str = in.readLine()) != null) {
				processLine(str);
			}
			in.close();
		} catch (IOException e) {
			logger.error("Error parsing level", e);
		}

	}
	
	private void processLine(String line){
		logger.info("line " + line);
		for(char c : line.toCharArray()){
			System.out.print(c);
		}
		
	}

}
