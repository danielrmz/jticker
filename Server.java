import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Server.java
 * Clase que maneja las conexiones al servidor asi como la informacion
 * de los juegos
 * @since 1.0
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */
public class Server {
	/**
	 * Servidor actual
	 */
	public static ServerSocket server = null;
	
	/**
	 * Clientes del servidor
	 */
	public static LinkedList<ServerThread> clients = new LinkedList<ServerThread>(); 
	
	/**
	 * Puerto de Conexion
	 */
	public static int port = 1298;
	
	/**
	 * Limite de Usuarios
	 */
	public static int user_limit = 100;
	
	/**
	 * Estado del Server
	 */
	public static boolean active = true;
	
	/**
	 * Pool de threads
	 */
	public static ExecutorService pool; 
	
	/**
	 * Logger
	 */
	public static Logger logger = new Logger(new File("logs/SportsTicker.log"));
	
	/**
	 * Categorias
	 * Nombres de las categorias
	 * se usara el subindice para representar el id de la categoria
	 */
	public static LinkedList<Sport> deportes = new LinkedList<Sport>();
	
	/**
	 * Constructor
	 * Se definen equipos por default
	 */
	public Server(){
		//-- Crear datos default
		Sport s = new Sport("Soccer","soccer.png");
		Sport b = new Sport("Basketball","basketball.png");
		Team t1 = s.addTeam("Tigres");
		Team t2 = s.addTeam("Rayados");
		s.addGame(t1,t2);
		
		deportes.add(s);
		deportes.add(b);
	}
	
	public static Sport getSport(int id){
		for(int i = 0; i < deportes.size(); i++){
			Sport s = deportes.get(i);
			if(s.getId() == id){
				return s;
			}
		}
		return null;
	}
	
	/**
	 * RunServer Crea los espacios de los sockets
	 */
	public void runServer(){
		Server.pool = Executors.newFixedThreadPool(Server.user_limit );
		
		try {
	         server = new ServerSocket(Server.port, Server.user_limit); 
	         logger.logdate("Servidor habilitado: Esperando conexiones...");
	        
	         while (Server.active) {
	        	try {
	            	Socket serversocket = server.accept();
	            	ServerThread connection = new ServerThread(serversocket);
	            	pool.execute(connection);
	            	Server.clients.addLast(connection);
	            	String hostname = connection.getConnection().getInetAddress().getHostName();
	            	logger.logdate(hostname+" entro al servidor");
	        	} catch ( EOFException eofException ) {
	        		logger.logdate( "Error al aceptar la conexión" );
	            } catch(SocketException e){
	           
	            } 
	            
	         }
	         
	    } catch ( IOException ioException ) {
	    	ioException.printStackTrace();
	    } 
	    
	}
	
	/**
	 * Cierra las conexiones
	 * @throws IOException 
	 */
	public void close() throws IOException {
		this.closeConnections();
		if(Server.server!= null){
			Server.server.close();
		}
		logger.logdate("Servidor OFFLINE");
		logger.log("----------------------------------------");
	}
	
	/**
	 * Cierra todas las conexiones existentes de forma remota, 
	 * por lo que no es necesario cerrarlas aqui, si se cerrara el programa como quiera.
	 * @throws IOException
	 */
	private void closeConnections() throws IOException {
		while(Server.clients != null && !Server.clients.isEmpty()){
			ServerThread client = (Server.clients.removeFirst());
			client.sendMessage(new Message(Message.CLOSE_CONNECTION));
			//client.close();
		}
	}
	
	/**
	 * Muestra las conexiones en consola
	 */
	public static void viewConnections(){
		for(int i=0; i<Server.clients.size();i++){
			ServerThread cliente = Server.clients.get(i);
			logger.logdate(cliente.getConnection().getInetAddress().getHostName());
		}
	}
	
	
	
	public static void main(String[] args) {
		Server server = new Server();
		if ( args.length != 0 ) {
			String arg = args[0].toLowerCase();
			if(arg.equals("-nofilelog")){
				logger.usefile = false;
			}
			
		}
		server.runServer();
	}

}
