import java.io.*;
import java.util.*;
import javax.swing.*;

public class Sport implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String name = "";

	protected LinkedList<Game> juegos = new LinkedList<Game>();
	
	protected LinkedList<Team> equipos = new LinkedList<Team>();
	
	protected ImageIcon image = null;
	
	public static int c = 1;
	
	private int id = 0;
	
	public Sport(String name){
		 this.name = name;
		 this.id = c;
		 c++;
	}
	
	public Sport(Sport s){
		this.name = s.getName();
		this.id = s.getId();
		this.juegos = s.getGames();
		this.equipos = s.getTeams();
		this.image = s.getImage();
	}
	
	public Sport(String name, String image){
		 this.name = name;
		 this.setImage(image);
		 this.id = c;
		 c++;	 
	}
	
	public void addGame(Team local, Team visitante){
		Game g = new Game(local,visitante);
		local.setGame(g);
		visitante.setGame(g);
		juegos.add(g);
	}
	
	public Team addTeam(String name){
		Team t = new Team(name);
		this.equipos.add(t);
		return t;
	}
	
	public void removeTeam(Team team){
		equipos.remove(team);
	}
	
	public void removeGame(Game game){
		juegos.remove(game);
	}
	
	public void removeGame(int id){
		for(int i = 0; i < juegos.size(); i++){
			Game g = juegos.get(i);
			if(g.id == id) {
				juegos.remove(i);
				break;
			}
		}
	}
	
	public Game getGame(int id){
		for(int i = 0; i < juegos.size(); i++){
			Game g = juegos.get(i);
			if(g.id == id) {
				return g;
			}
		}
		return null;
	}

	public LinkedList<Game> getGames(){
		return this.juegos;
	}
	
	public LinkedList<Team> getTeams(){
		return this.equipos;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setImage(String image){
		this.image = new ImageIcon("img/"+image);
		if(this.image.getImageLoadStatus()==4) this.image = null;
	}
	public ImageIcon getImage(){
		return this.image;
	}
	
	public int getId(){
		return this.id;
	}
}
