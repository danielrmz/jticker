import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import javax.swing.Timer;

/**
 * Clase que maneja la conexion con el cliente
 * @since 1.0
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */
public class ServerThread implements Runnable {
	/**
	 * Objeto de Salida para el cliente
	 */
	private ObjectOutputStream output; 
	
	/**
	 * Objeto de Entrada para el cliente
	 */
	private ObjectInputStream input; // input stream from client
		
	/**
	 * Socket de conexion del cliente
	 */
	private Socket connection; 
	
	/**
	 * Suscripciones de juegos
	 */
	private LinkedList<Game> suscriptions = new LinkedList<Game>();
	
	/**
	 * Suscripciones a deportes
	 */
	private LinkedList<Sport> sportsuscriptions = new LinkedList<Sport>();
	
	/**
	 * Timer que manda las actualizaciones de las suscripciones a los usuarios
	 */
	private Timer suscription_timer;
	
	/**
	 * Constructor de la conexion
	 * @param connection
	 */
	public ServerThread(Socket connection) throws IOException {
		this.connection = connection;
		this.output = new ObjectOutputStream(connection.getOutputStream());
		this.output.flush();
	    this.input = new ObjectInputStream(connection.getInputStream());
	    this.suscription_timer = new Timer(10, new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				
				for(int i = 0; i<suscriptions.size();i++) {
					Message m = new Message(suscriptions.get(i), Message.GAME_UPDATE);
					sendMessage(m);
				}
				
				for(int i = 0; i < sportsuscriptions.size();i++){
					Sport s = sportsuscriptions.get(i);
					int score = s.getGames().getLast().getLocalScore() + 1;
					s.getGames().getLast().setLScore(score);
					System.out.println(s.getGames().getLast().toString());
					Sport saux = new Sport(s);
					Message m = new Message(saux, Message.GET_SPORT);
					sendMessage(m);
				}
			}
	    	
	    });
	    this.suscription_timer.start();
	}
	

	/**
	 * Cierra la conexion actual
	 * @throws IOException
	 */
	public void close() {
		if(this.connection != null){
			try {
				Server.logger.logdate(getHostName()+" salio del servidor");
				
				this.suscription_timer.stop();
				
				this.output.close();
				this.input.close();
				this.connection.close();
				this.connection = null;
				Server.clients.remove(this);
			} catch (SocketException e2){
				this.connection = null;
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * Manda un mensaje al host de la conexion
	 * @param msg
	 */
	public void sendMessage(Object msg) {
		
		try {
			output.writeObject(msg);
			output.flush();
		} catch ( IOException ioException ) {
			
		} catch (Exception e) {
			this.close();
		}
		
	}

	/**
	 * Trae el mensaje proporcionado por el cliente
	 * @return Message Mensaje
	 * @throws IOException
	 */
	public Message getMessage() throws SocketException, IOException {
		 Message message = null;
		 
		 if(this.connection != null){
			 try {
				message = (Message) input.readObject();
	         } catch(SocketException e) {
	        	Server.clients.remove(this);
	 			this.close();
	 		 } catch ( ClassNotFoundException classNotFoundException ) {
	        	 Server.logger.logdate( "\nTipo de Objeto no reconocido" );
	         } catch (EOFException eof){
	        	Server.clients.remove(this);
	  			this.close();
	         }
		 }
         return message;
	}
	
	/**
	 * Trae la conexion
	 * @return connection
	 */
	public Socket getConnection(){
		return this.connection;
	}
	
	/**
	 * Trae el hostname dependiendo de un ip
	 * @return String hostname de la conexion
	 */
	public String getHostName(){
		return this.connection.getInetAddress().getHostName();
	}

	/**
	 * Run, checa por nuevos mensajes
	 */
	public void run() {
		
		while(this.connection != null){
			
			try {
				
				Message message = getMessage();
				
				if(message != null){
					int type = message.getType();
					if(type == Message.FETCH_CATEGORIES){
						Server.logger.logdate("Mandando lista de deportes a "+getHostName());
						this.sendMessage(new Message(Server.deportes, Message.FETCH_CATEGORIES));
						
					} else if(type == Message.FETCH_GAMELIST){
						Server.logger.logdate("Mandando lista de partidos a "+getHostName());
						int id = Integer.parseInt(message.getData()+"");
						this.sendMessage(new Message(Server.getSport(id), Message.FETCH_GAMELIST));
							
					} else if(type == Message.GET_GAME){
						String ids[] = (message.getData()+"").split(" ");
						int sportid = Integer.parseInt(ids[0]);
						Sport s = Server.getSport(sportid);
						
						if(ids.length == 2){
							int gameid  = Integer.parseInt(ids[1]);
							Game g = s.getGame(gameid);
							this.sendMessage(new Message(g, Message.GET_GAME));
							this.suscriptions.add(g);
							Server.logger.logdate("El usuario: "+getHostName()+" se suscribió al juego "+g.getLocal().getName() + " vs. "+g.getVisit().getName());
						} else {
							this.sendMessage(new Message(s, Message.GET_SPORT));
							this.sportsuscriptions.add(s);
							Server.logger.logdate("El usuario: "+getHostName()+" se suscribió a los partidos de :"+s.getName());							
						}
						
					} else if(type == Message.GET_SPORT){
						String id = (message.getData()+"");
						Sport s = Server.getSport(Integer.parseInt(id));
						this.sendMessage(new Message(s, Message.GET_SPORT));
						
					} else if(type == Message.GAME_REMOVE){
						String ids[] = (message.getData()+"").split(" ");
						int sportid = Integer.parseInt(ids[0]);
						int gameid  = Integer.parseInt(ids[1]);
						Sport s = Server.getSport(sportid);
						Game g = s.getGame(gameid);
						this.suscriptions.remove(g);
						this.sendMessage(new Message(g, Message.GAME_REMOVE));
					} else if(type == Message.REMOVE_SUSCRIBEDSPORT){
						String id = (message.getData()+"");
						Sport s = Server.getSport(Integer.parseInt(id));
						this.sportsuscriptions.remove(s);
						this.sendMessage(new Message(s, Message.REMOVE_SUSCRIBEDSPORT));
					} else if(type == Message.CLOSE_GAME){
						
					} else if(type == Message.CLOSE_CONNECTION){
						this.close();
					}
					
				}	
				Thread.sleep(10);
			
			} catch (SocketException ee){ 
				this.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
				this.close();
			} 
		}
	}
}
