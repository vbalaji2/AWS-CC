package DataPersistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import Handler.Constants;

public class Logger {

	private static Logger LOGGER;
	
	private Logger() {
	}
	
	public static Logger getLogger() {
		if(LOGGER == null) {
			LOGGER = new Logger();
		}
		return LOGGER;
	}
	
	public void log(String message) {
		try {
			
	       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	       PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.LOGGERFILE, true)));
	       out.println(timestamp.getTime() + " : " + message);
	       out.close();
		}catch(IOException ex) {
			System.out.println("EXCEPTION IN LOGGER");
		}
	}
	
	public String getLoggerMsg() {
		String result = "Logger details : \n" ;
		try {
			FileReader reader = new FileReader(new File(Constants.LOGGERFILE));
			BufferedReader br = new BufferedReader(reader);
			String s;
			
			while((s=br.readLine()) != null) {
				result += s + "\n";
			}
			br.close();
		}catch(IOException ex) {
			result += "Unable to read logger file";
		}
		return result;
	}
}
