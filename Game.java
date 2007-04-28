import java.io.*;

/**
 * Clase Game para encapsular los datos del juego y transmitirse a traves de la conexion
 * @version 1.0
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected Team local = null;
	
	protected Team visitante = null;
	
	protected int sclocal = 0;
	
	protected int scvisitante = 0;
	
	protected String place = "No definido";
	
	public String fecha = "No definida";
	

	public static int g = 1;
	
	public int id = 0;
	
	public Game(Team local, Team visitante){
		this.local = local;
		this.visitante = visitante;
		this.id = g;
		g++;
	}
	
	public Game(Team local, Team visitante, int sclocal, int scvisit){
		this.local = local;
		this.visitante = visitante;
		this.sclocal = sclocal;
		this.scvisitante= scvisit;
		this.id = g;
		g++;
	}
	
	public void setLScore(int local){
		this.sclocal = local;
	}
	
	public void setVScore(int visit){
		this.scvisitante = visit;
	}
	
	public Team getLocal(){
		return this.local;
	}
	
	public Team getVisit(){
		return this.visitante;
	}
	
	public int getLocalScore(){
		return this.sclocal;
	}
	
	public int getVisitScore(){
		return this.scvisitante;
	}
	

}
