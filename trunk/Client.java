import java.io.*;
import java.net.*;

/**
 * Client.java
 * manejador para el cliente
 * 
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */
public class Client {
	
	/**
	 * IP del servidor
	 */
	private String serverip = "";
	
	/**
	 * Stream de output al servidor
	 */
	private ObjectOutputStream output; 
	
	/**
	 * Stream de input al servidor
	 */
	private ObjectInputStream input; 
	
	/**
	 * Socket de conexion
	 */
	public Socket client; 
	
	/**
	 * Status
	 */
	private boolean connected = false;
	
	/**
	 * Puerto
	 */
	public static int port = 1298;
	
	/**
	 * Receiver
	 */
	Receiver receiver = null;
	
	/**
	 * Constructor
	 * @param ip IP del servidor
	 */
	public Client(String ip) {
		this.serverip = ip;
	}
	
	/**
	 * Empieza la conexion con el servidor
	 */
	public boolean runClient() {
		boolean connected = false;
		try {
			ClientInterface.sb.setMessage( "Estableciendo la conexión con el servidor..." );
			client = new Socket(InetAddress.getByName(this.serverip) , port );
			ClientInterface.sb.setMessage( "Conectado a: " + client.getInetAddress().getHostName() );
			
			output = new ObjectOutputStream( client.getOutputStream() );
			output.flush();
			receiver = new Receiver();
			receiver.start();
			connected = true;
		} catch (UnknownHostException uh){
			connected = false;
		} catch (ConnectException e){
			ClientInterface.sb.setMessage("Falló la conexión con el servidor.");
			connected = false;
		} catch ( EOFException eofException ) {
			ClientInterface.sb.setMessage("Desconectado");
			connected = false;
		} catch ( IOException ioException ) {
			ClientInterface.sb.setMessage("Error de E/S");
			connected = false;
		} 
		
		this.connected = connected;
		return connected;
	} 
	
	/**
	 * Cierra las conexiones
	 */
	public void closeConnection() {
		try {
			if(client != null && output != null && input != null && this.connected)
			 output.close(); 
	         input.close(); 
	         client.close(); 
	         this.connected = false;
	    } catch (SocketException e) {
		} catch (IOException ioException) {
		} catch (NullPointerException nul){}
		
	}

	/**
	 * Manda el mensaje
	 * @param message
	 */
	public void sendMessage( Object message ) {
		 try {
			 
			 output.writeObject(message);
			 output.flush();
		 } catch (SocketException se){
		 } catch ( IOException ioException ) {
			 ioException.printStackTrace();
	         System.out.println( "\nError de escritura del objeto" );
		 } 
	 }

	/**
	 * @param serverip The serverip to set.
	 */
	public void setServerip(String serverip) {
		this.serverip = serverip;
		this.closeConnection();
		this.runClient();
	}

	/**
	 * Regresa el estado de la conexion
	 * @return boolean el estado de la conexion
	 */
	public boolean getStatus(){
		return this.connected;
	}
	/**
	 * Clase que checa la recepcion de los mensajes
	 */
	public class Receiver extends Thread {
		public Message message = null;
		
		public Receiver(){
			try {
				input = new ObjectInputStream( client.getInputStream() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		public void run() {
			while(client != null && connected){
				
				try {
					message = (Message)input.readObject();
				} catch (EOFException eof){
					
				} catch (SocketException se) {
					ClientInterface.sb.setMessage("El servidor se cerro");
					closeConnection();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		ClientInterface c = new ClientInterface();
		c.setVisible(true);
	}

}
