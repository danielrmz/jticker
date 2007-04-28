import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public boolean usefile = true;
	public boolean usestd = true;
	public boolean usevar = false;
	
	private File   varfile = new File("logged.log");
	private String varlog  = "";
	
	private boolean first = false;
	private String startup = "";
	
	public Logger(){
		
	}
	
	public Logger(String startupmessage){
		this.startup = startupmessage;
	}
	
	public Logger(File f){
		this.varfile = f;
	}
	
	public void clear(){
		this.varlog = "";
		try {
			PrintStream os = new PrintStream(new FileOutputStream(varfile));
			os.println("");
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getStartup(){
		return this.startup;
	}
	
	public void setStartup(String startup){
		this.startup = startup;
	}
	public String getVarLog(){
		return this.varlog;
	}
	
	public void setFile(File f){
		this.varfile = f;
	}
	
	public void logdate(String message){
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");
		String text = s.format(new Date())+ " > " + message;
		this.log(text);
	}
	
	public void log(String message){
		if(!first){
			this.first = true;
			log(this.startup);
		}
		if(usefile){
			this.logtofile(message);
		}
		if(usestd) {
			this.logtostd(message);
		}
		if(usevar) {
			this.logtovar(message);
		}
	}
	
	public void logtovar(String message) {
		varlog += message+"\n";
	}
	
	public void logtostd(String message) {
		System.out.println(message);
	}
	
	public void logtofile(String message){
		try {
			PrintStream os = new PrintStream(new FileOutputStream(varfile,true));
			os.println(message);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
