import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.Timer;

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
	
	public int tiempo = -1;
	
	public static int g = 1;
	
	public int id = 0;
	
	public Timer timer = null;
	
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
	
	public void start(){
		timer = new Timer(1000, new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				tiempo++;
			} });
		timer.start();
	}
	
	public void stop(){
		timer.stop();
	}
	
	public String toTime(){
		int hora = tiempo/3600;
		int minutos = (tiempo%3600)/60;
		int segundos = (tiempo % 3600 % 60);
		String horastr = hora<10?"0"+hora:hora+"";
		String minustr = minutos<10?"0"+minutos:minutos+"";
		String segustr = segundos<10?"0"+segundos:segundos+"";
		if(hora == 0 && minutos == 0 && segundos == 0){
			return "Sin empezar";
		}
		return  horastr+":"+minustr+":"+segustr;
	}
}
