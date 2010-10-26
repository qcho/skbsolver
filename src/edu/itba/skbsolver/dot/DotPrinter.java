package edu.itba.skbsolver.dot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import edu.itba.skbsolver.Logger;
import edu.itba.skbsolver.State;

public class DotPrinter {
	final static Logger logger = Logger.getLogger(false);

	// volatile is needed so that multiple thread can reconcile the instance
	// semantics for volatile changed in Java 5.
	private volatile static DotPrinter instance = null;

	private File dotFile = null;
	private BufferedWriter writter = null;
	
	private long counter = 0;

	private DotPrinter() {
	}

	// synchronized keyword has been removed from here
	public static DotPrinter getInstance() {
		// needed because once there is singleton available no need to acquire
		// monitor again & again as it is costly
		if (instance == null) {
			synchronized (DotPrinter.class) {
				// this is needed if two threads are waiting at the monitor at
				// the
				// time when singleton was getting instantiated
				if (instance == null) {
					instance = new DotPrinter();
				}
			}
		}
		return instance;
	}

	public void init(File dotFile) {
		
		if (this.writter != null){
			logger.error("Another instance is present");
			return;
		}
		
		this.dotFile = dotFile;
		this.deleteDot();

		try {
			// Construct the BufferedWriter object
			this.writter = new BufferedWriter(new FileWriter(dotFile, true));
			
			// Start writing headers
			this.writter.append("graph sokoban {");
			this.writter.newLine();
			this.writter.append("node [ shape=box, fontname=Courier, fontsize=6];");
			this.writter.append("edge [fontname=Courier, fontsize=8];");
			this.writter.newLine();
			this.writter.newLine();

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	
	public void addNode(String current, String label) throws IOException{
		this.writter.append(current + " [label=\" "+ ++counter + "\n" + label + "\"];");
		this.writter.newLine();	
	}
	
	public void addEdge(String parent, String current, String label) throws IOException{
		this.writter.append(parent + " -- " + current + " [label=\"" + label + "\"];");
		this.writter.newLine();
	}
	
	public void addAnnotation(State s, String noteType, String note){
		String parent = "s" + Integer.toHexString(s.hashCode());
		String current = "a" + noteType;
		try {
			addNode(current, note);
			addEdge(parent, current, "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addState(State s){
		try {
			String current = "s" + Integer.toHexString(s.hashCode());
			addNode(current, s.toString().replace("\n", "\\n"));
			if(s.parent != null){
				String parent = "s" + Integer.toHexString(s.parent.hashCode());
				addEdge(parent, current, "" + (s.moves - s.parent.moves));
			}
			this.writter.newLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void close(String footer) {
		try {
			this.writter.append('}');
			this.writter.newLine();
			this.writter.append("//" + footer);
			
			if (this.writter != null) {
				this.writter.flush();
				this.writter.close();
				this.writter = null;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void deleteDot() {
		// Attempt to delete it
		if (dotFile.exists()){
			
			if (!dotFile.canWrite())
				throw new IllegalArgumentException("Delete: write protected: " + dotFile.getName());

			// If it is a directory, make sure it is empty
			if (dotFile.isDirectory()) {
				String[] files = dotFile.list();
				if (files.length > 0)
					throw new IllegalArgumentException("Delete: directory not empty: " + dotFile.getName());
			}
			
			if (!dotFile.delete()){
				throw new IllegalArgumentException("Delete: deletion failed");				
			}
		}
		
	}

}
