package edu.itba.skbsolver;

import java.io.File;
import java.security.InvalidParameterException;

import edu.itba.skbsolver.dot.DotPrinter;

public class Main {
	
	private static final String USAGE_HELP = "Usage: skbsolver.jar level-file method [tree] \n"
			+ "    level-file   The file of the level.\n"
			+ "    method       Either \"BFS\" or \"DFS\".\n"
			+ "    tree         Wheter to output .dot file or not.";

	final static Logger logger = Logger.getLogger(false);

	public static void main(String args[]) {
		
		long start = System.currentTimeMillis();
		logger.setStatus(false);

		Solution sol = null;
		DotPrinter dotPrinter = null;
		Level level = null;

		try {
			if (args.length < 2) {
				throw new Exception("Expecting at least 2 arguments.\n");
			}

			level = new Level(new File(args[0]));

			if (args.length >= 3 && "tree".equals(args[2])) {
				dotPrinter = DotPrinter.getInstance();
				dotPrinter.init(new File(args[0]+".dot"));
				dotPrinter.addState(level.initial);
			
			}
			System.out.println("Searching for best solution, sit tight!");
			if ("BFS".equals(args[1])) {
				//logger.info("Running BFS");
				sol = BFSRunner.run(level, dotPrinter);

			} else if ("DFS".equals(args[1])) {
				//logger.info("Running DFS");
				sol = DFSRunner.run(level, dotPrinter);
			} else {
				throw new InvalidParameterException(
						"Only BFS & DFS methods are provided.");
			}

			if (sol.movements != -1) {
				StringBuilder sb = new StringBuilder();
				for (String s : sol.transitions) {
					sb.append(s);
				}
				System.out.println("Best solution found in "+sol.movements+" movements:");
				System.out.println(sb);
			} else {
				System.out.println("No solution found.");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(Logger.getStackTrace(e));
			logger.error(e.getMessage(), e);
			System.out.println(USAGE_HELP);
		} finally {
			long end = System.currentTimeMillis();
			String footer = "Execution time was "+(end-start)+" ms.";
			if (dotPrinter != null){
				dotPrinter.close(footer);
			}
			//logger.info(footer);
			System.out.println(footer);
			
		}

	}
}
