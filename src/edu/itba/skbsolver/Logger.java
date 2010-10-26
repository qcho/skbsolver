package edu.itba.skbsolver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {
	
	public static Logger single;
	
	private OutputStreamWriter out;
	private boolean console;
	private boolean activated;
	
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
	
	public void setStatus(boolean activate){
		single.activated = activate;
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
		if (activated){
			log("INFO: " + param);
		}
	}
	public void debug(String param){
		if (activated){
			log("DEBUG: " + param);
		}
	}
	public void error(String param){
		if (activated){
			log("ERROR: " + param);
		}
	}
	public void error(String param, Exception e){
		if (activated){
			log("ERROR: " + param + "\nException: " + getStackTrace(e));
		}
	}
	
	public static String getStackTrace(Throwable t){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
