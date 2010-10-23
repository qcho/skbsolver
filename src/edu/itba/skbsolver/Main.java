package edu.itba.skbsolver;

import java.io.File;
import java.security.InvalidParameterException;

import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

public class Main {
	private static final String USAGE_HELP = 
			"Usage: level-file method [tree] \n\n" + 
			"    level-file   The file of the level.\n" +
			"    method   Either \"BFS\" or \"DFS.\n" +
			"    tree   Wheter to output .dot file or not.";

	public static void main(String args[]) {

		Solution sol = null;
		boolean tree = false;
		Level level = null;

		try {
			if (args.length < 2 || args.length > 3) {
				throw new WrongNumberArgsException("Expected 2 or 3 parameters");
			}

			level = new Level(new File(args[0]));

			if ("tree".equals(args[2])) {
				tree = true;
			}

			if ("BFS".equals(args[1])) {
				System.out.println("Running BFS");
				sol = BFSRunner.run(level, tree);

			} else if ("DFS".equals(args[1])) {
				System.out.println("Running DFS");
				sol = DFSRunner.run(level, tree);
			} else {
				throw new InvalidParameterException("Only BFS & DFS methods are provided.");
			}

			System.out.println("Best Solution: " + sol.movements);
			
			for (String s : sol.transitions){
				System.out.println("    " + s);
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			System.out.println(USAGE_HELP);
		}

	}
}