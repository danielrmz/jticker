import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;
public class SportGamesPanel extends JSplitPane {

	private static final long serialVersionUID = -1782169527753675613L;
	private int id = -1;
	private JPanel gamepanel = new JPanel();
	private JPanel infopanel = new JPanel(new GridLayout(5,2));
	
	public SportGamesPanel(int sportid, JTabbedPane pane){
		JScrollPane gpane = this.getGamesPane(pane);
		JScrollPane ipane = this.getInfoPane();
		this.setTopComponent(gpane);
		this.setBottomComponent(ipane);
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.setOneTouchExpandable(true);
		this.setDividerLocation(300);
	
		Dimension minimumSize = new Dimension(600, 400);
		Dimension minimumSize2 = new Dimension(600, 200);
		gpane.setMinimumSize(minimumSize);
		ipane.setMinimumSize(minimumSize2);
		
		this.id = sportid;
		this.setSize(new Dimension(160,400));
		this.setLocation(new Point(0,0));
		this.setOpaque(true);
		
	}
	private class CloseButton extends JButton implements ActionListener { 
		private static final long serialVersionUID = 1L;
		JTabbedPane pane;
		SportGamesPanel sp;
		public CloseButton(JTabbedPane pane, SportGamesPanel sp){
			super("X");
			this.pane = pane;
			this.sp = sp;
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			this.pane.remove(this.sp);
			ClientInterface.openedPanels.remove(this.sp);
			ClientInterface.openedTabs.remove(""+id);
		}
	}
	public JScrollPane getGamesPane(JTabbedPane p){
		JPanel subpanel = new JPanel(new BorderLayout());
		JScrollPane pane = new JScrollPane(subpanel);
		CloseButton close = new CloseButton(p, this);
		JPanel subsubpanel = new JPanel(new FlowLayout());
		subsubpanel.add(close);
		subsubpanel.add(new JLabel("Juegos Disponibles:"));
		subpanel.add(subsubpanel, BorderLayout.NORTH);
		subpanel.add(gamepanel,BorderLayout.CENTER);
		gamepanel.setLayout(new FlowLayout());
		return pane;
	}
	
	public JScrollPane getInfoPane(){
		JScrollPane pane = new JScrollPane(infopanel);
		infopanel.setBackground(Color.white);
		return pane;
	}
	
	public void addGame(Game g){
		this.gamepanel.add(new GameButton(g,this.id));
	}
	
	public void addGames(LinkedList<Game> g){
		for(int i = 0; i < g.size(); i++){
			this.addGame(g.get(i));	
    	}
	}
	
	private class GameButton extends JButton implements ActionListener { 
		private static final long serialVersionUID = 5136284837909015359L;
		private int id = -1;
		private int sportid = -1;
		private Game g;
		public GameButton(Game g, int sportid){
			Team t1 = g.getLocal();
			Team t2 = g.getVisit();
			this.id = g.id;
			this.g = g;
			this.sportid = sportid;
			this.setText(""+t1.getName()+" \n vs. \n "+t2.getName());
			this.addActionListener(this);
			this.setPreferredSize(new Dimension(150,80));
			
		}

		public void actionPerformed(ActionEvent arg0) {
				infopanel.removeAll();
				infopanel.add(new JLabel("Partido:"+this.g.getLocal().getName() + " vs. " + this.g.getVisit().getName()));
				infopanel.add(new JLabel());
				infopanel.add(new JLabel("Fecha:"));
				infopanel.add(new JLabel(this.g.fecha));
				infopanel.add(new JLabel("Marcador (al conectarse):"));
				infopanel.add(new JLabel(this.g.getLocal().getName() +" "+ this.g.getLocalScore() +" <-> "+ this.g.getVisit().getName()+" "+this.g.getVisitScore()));
				JButton button = new JButton("Suscribirse");
				button.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						String request = sportid+" "+id;
						if(!ClientInterface.suscrtionsid.contains(request)){
							Message m = new Message(request, Message.GET_GAME);
							ClientInterface.suscrtionsid.add(request);
							ClientInterface.cliente.sendMessage(m);
							ClientInterface.sb.setCenterMessage("Suscripciones: "+ClientInterface.suscrtionsid.size());
							SuscriptionFrame s = new SuscriptionFrame(g, request);
							s.setVisible(true);
						}
					}});
				infopanel.add(button);
				infopanel.updateUI();
				
		}
		
	}
}
