import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameSuscriptionFrame extends JFrame implements WindowListener, ActionListener {
	Sport sport;
	String id = "";
	Table marcadores = new Table(480,400);
	JButton cerrar = new JButton("Cerrar");
	SportGamesPanel sp = null;
	
	public GameSuscriptionFrame(Sport s, SportGamesPanel sp){
		this.sport = s;
		this.sp = sp;
		this.id = s.getName();
		this.setSize(new Dimension(500,300));
		this.addWindowListener(this);
		this.setTitle("Juegos para: "+s.getName());
		this.setLayout(new GridLayout(2,1));
		this.add(marcadores);
		this.updateStats(s);
		this.add(cerrar);
		cerrar.addActionListener(this);
	}
	
	public void updateStats(Sport s){
		int size = s.getGames().size();
		Object[] games = (Object[])s.getGames().toArray();
		Object[][] data = new Object[games.length][3];
		
		for(int i = 0; i < size;i++){
			Game j = (Game)games[i];
			data[i][0] = j.toTime();
			data[i][1] = j.getLocal().getName() + " vs. " + j.getVisit().getName();
			data[i][2] = j.getLocalScore() +" - "+ j.getVisitScore();
		}
		marcadores.setData(data);
	}
	
	private static final long serialVersionUID = 1L;
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) { this.close();}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}

	public void actionPerformed(ActionEvent arg0) {
		this.close();
		this.dispose();
	}
	
	public void close(){
		//for(int i = 0; i<)
		ClientInterface.suscrtionsid.remove(this.id);
		ClientInterface.cliente.sendMessage(new Message(this.id, Message.REMOVE_SUSCRIBEDSPORT));
		ClientInterface.openedPanels.remove(this.sp);
		ClientInterface.sb.setCenterMessage("Suscripciones: "+ClientInterface.suscrtionsid.size());
		
	}
}
