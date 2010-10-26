package edu.itba.skbsolver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Logger {
	
	public static Logger single;
	
	private OutputStreamWriter out;
	private boolean console;
	
	public Logger(boolean console) {
		if (!console){
			try {
				out = new OutputStreamWriter(new FileOutputStream("skbsolver.log"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.console = console;
	}
	
	public static Logger getLogger(boolean console){
		if (single == null){
			single = new Logger(console);			
		}
		return single;
	}
	
	public void log(String param){
		try {
			if (console){
				System.out.println(param);
			} else {
				out.write(param);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void info(String param){
		log("INFO: " + param);
	}
	public void debug(String param){
		log("DEBUG: " + param);
	}
	public void error(String param){
		log("ERROR: " + param);
	}
	public void error(String param, Exception e){
		log("ERROR: " + param + "\nException: " + e);
	}
}
