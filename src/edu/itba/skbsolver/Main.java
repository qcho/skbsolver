package edu.itba.skbsolver;

import java.io.File;
import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.itba.skbsolver.dot.DotPrinter;

public class Main {
	private static final String USAGE_HELP = "Usage: skbsolver.jar level-file method [tree] \n"
			+ "    level-file   The file of the level.\n"
			+ "    method       Either \"BFS\" or \"DFS\".\n"
			+ "    tree         Wheter to output .dot file or not.";

	final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String args[]) {
		
		long start = System.currentTimeMillis();

		Solution sol = null;
		DotPrinter dotPrinter = null;
		Level level = null;

		try {
			if (args.length < 2) {
				throw new Exception("Expected at least 2 arguments.\n");
			}

			level = new Level(new File(args[0]));

			if (args.length == 3 && "tree".equals(args[2])) {
				dotPrinter = DotPrinter.getInstance();
				dotPrinter.init(new File(args[0].replace(".level", ".dot")));
				dotPrinter.addState(level.initial);
			}

			if ("BFS".equals(args[1])) {
				System.out.println("Running BFS");
				sol = BFSRunner.run(level, dotPrinter);

			} else if ("DFS".equals(args[1])) {
				System.out.println("Running DFS");
				sol = DFSRunner.run(level, dotPrinter);
			} else {
				throw new InvalidParameterException(
						"Only BFS & DFS methods are provided.");
			}

			if (sol.movements != -1) {
				System.out.println("Best Solution: " + sol.movements);

				for (String s : sol.transitions) {
					System.out.println("    " + s);
				}
			} else {
				System.out.println("No solution found.");
			}
			System.out.println(level.BADCAPACITORS);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			System.out.println(USAGE_HELP);
		} finally {
			long end = System.currentTimeMillis();
			
			if (dotPrinter != null){
				String footer = "Execution time was "+(end-start)+" ms.";
				dotPrinter.close(footer);
				System.out.println(footer);
			}
			
		}

	}
}