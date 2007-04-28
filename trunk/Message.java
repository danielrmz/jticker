import java.io.*;

/**
 * Clase base para la transimision de comandos
 * e informacion
 * @since 1.0
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */

public class Message implements Serializable {
	
	public static int GET_GAME = 0;
	
	public static int FETCH_GAMELIST = 1;
	
	public static int FETCH_CATEGORIES = 2;
	
	public static int CLOSE_CONNECTION = 3;
	
	public static int CLOSE_GAME = 4;
	
	public static int GAME_UPDATE = 5;
	
	public static int GAME_REMOVE = 6;
	
	public static int CHECK_USER = 7;
	
	public static int GET_SPORT = 8;
	
	public static int REMOVE_SUSCRIBEDSPORT = 9;
	
	private static final long serialVersionUID = -2723363051271966964L;
	
	private Object data = null;
	
	private int type = 0;
	
	public Message(int type){
		this.type = type;
	}
	
	public Message(Object message){
		this.data = message;
	}
	
	public Message(Object message, int type){
		this.data = message;
		this.type = type;
	}
	
	public Object getData(){
		return this.data;
	}
	
	public void setData(Object data){
		this.data = data;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
	
}
