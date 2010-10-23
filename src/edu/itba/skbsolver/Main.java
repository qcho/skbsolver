package edu.itba.skbsolver;

import java.io.File;
import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final String USAGE_HELP = 
			"Usage: skbsolver.jar level-file method [tree] \n" + 
			"    level-file   The file of the level.\n" +
			"    method       Either \"BFS\" or \"DFS\".\n" +
			"    tree         Wheter to output .dot file or not.";
	
	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String args[]) {

		Solution sol = null;
		boolean tree = false;
		Level level = null;

		try {
			if (args.length < 2) {
				throw new Exception("Expected at least 2 arguments.\n");
			}

			level = new Level(new File(args[0]));

			if (args.length == 3 &&	"tree".equals(args[2])) {
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

			if (sol != null){
				System.out.println("Best Solution: " + sol.movements);
				
				for (String s : sol.transitions){
					System.out.println("    " + s);
				}
			} else {
				System.out.println("No solution found.");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.out.println(USAGE_HELP);
		}

	}
}