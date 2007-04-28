import java.io.*;
import java.util.*;

import javax.swing.ImageIcon;

public class Team implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String name = "";
	
	protected int played = 0;
	
	protected int wins = 0;
	
	protected int loses = 0;
	
	protected int ties = 0;
	
	protected LinkedList<Game> games = new LinkedList<Game>();
	
	protected ImageIcon logo = null;
	
	
	public Team(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public int getLoses() {
		return loses;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}

	public int getPlayed() {
		return played;
	}

	public void setPlayed(int played) {
		this.played = played;
	}

	public int getTies() {
		return ties;
	}

	public void setTies(int ties) {
		this.ties = ties;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public void setGame(Game g){
		this.games.add(g);
	}
	
	public void setImage(String image){
		this.logo = new ImageIcon("img/"+image);
	}
	
	public ImageIcon getImage(){
		return this.logo;
	}
}
