import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * ClientInterface.java
 * Interfaz directa con el usuario
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */
public class ClientInterface extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 2367013873241421563L;
	
	public static Client cliente = null;
	
	public Timer timer = null;
	
	public LinkedList <Sport> deportes = null;
	
	public static LinkedList <Game> suscriptions = new LinkedList<Game>();
	
	public static LinkedList <Sport> sportsuscriptions = new LinkedList<Sport>();
	
	public static LinkedList <String>  suscrtionsid = new LinkedList<String>();
	
	public JMenuItem conectar = new JMenuItem("Conectar");
	
	public JMenuItem desconectar = new JMenuItem("Desconectar");
	
	public JMenuItem sobre = new JMenuItem("Sobre");
	
	public JMenuItem cerrar = new JMenuItem("Cerrar");
	
	public static JStatusBar sb = new JStatusBar("Desconectado","Suscripciones: 0 ","Ultima actualización: - ");
	
	public JTabbedPane desktop;
	
	public static JList lstSports = null;
	
	private DefaultListModel sports = new DefaultListModel();
	
	private JPanel tbJuegos = null;
	
	/**
	 * Constructor
	 */
	public ClientInterface(){
		super("Sports Ticketer");
		this.setSize(788, 570);
		this.setResizable(false);
		this.setJMenuBar(getJMenubar());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().add(sb, BorderLayout.SOUTH);
		this.getContentPane().add(this.getJDesktop(), BorderLayout.CENTER);
	}
	
	/**
	 * Establece la conexion con el servidor
	 * @param String server
	 * @return boolean Si fue aceptado o hubo un error
	 */
	public boolean start(String server){
		cliente = new Client(server);
		cliente.runClient();
		if(cliente.getStatus()){
			//-- Pedir request de categorias
			cliente.sendMessage(new Message(Message.FETCH_CATEGORIES));
			//-- Empezar con el timer de recibir mensajes
			this.startTimer();
		}
		return cliente.getStatus();
	}
	
	/**
	 * Detiene las conexiones y cambia las opciones del menu
	 */
	private void stop(){
		//-- Si no ha hecho la conexion aun simplemente cambiar el statusbar
		if(cliente != null){
			cliente.receiver.stop(); 
			cliente.closeConnection();
		}
		
		if(this.timer != null){
			this.timer.stop();
		}
		
		//-- Limpia la lista de deportes recibidos
		this.sports.clear();
		this.desktop.removeAll();
		this.openedPanels.clear();
		this.openedTabs.clear();
		desconectar.setVisible(false);
		conectar.setVisible(true);
		sb.setCenterMessage("Suscripciones: 0");
		sb.setMessage("Desconectado");
	}
	
	/**
	 * Crea el timer que verifica los mensajes recibidos
	 */
	private void startTimer(){

		timer = new Timer(10,new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				//-- Si esta conectado checar el mensaje
				if(cliente.getStatus()){
					Message m = cliente.receiver.message;
					if(m != null){
						int type = m.getType();
						if(type == Message.FETCH_CATEGORIES){
							try {
								sports.clear();
							} catch (Exception e){}
							deportes = (LinkedList<Sport>)m.getData();
							Object aux[] = sort(deportes);
							
							for(int i = 0; i < deportes.size(); i++){
								sports.add(i, (Sport)aux[i]);
							}
						} else if(type == Message.GET_GAME){
							Game g = (Game)m.getData();
							suscriptions.add(g);
						} else if(type == Message.GAME_UPDATE){
							Game g = (Game)m.getData();
							suscriptions.remove(g);
							suscriptions.add(g);
						} else if(type == Message.GAME_REMOVE){
							Game g = (Game)m.getData();
							suscriptions.remove(g);
						} else if(type == Message.GET_SPORT){
							Sport s = (Sport)m.getData();
							System.out.println(s.getGames().getLast().toString());
							try {
								int i = 0;
								while(sportsuscriptions.size() > i && sportsuscriptions.get(i).getId() != s.getId()){ i++; }
								if(sportsuscriptions.size() > 0){
									sportsuscriptions.remove(i);
									sportsuscriptions.add(i,s);
								} else {
									sportsuscriptions.addLast(s);
								}
							} catch (IndexOutOfBoundsException e){
								e.printStackTrace();
								System.exit(0);
							}
							
						} else if(type == Message.REMOVE_SUSCRIBEDSPORT){
							Sport s = (Sport)m.getData();
							sportsuscriptions.remove(s);
						}
						cliente.receiver.message = null;
						sb.setRightMessage(new Date().toString());
					}
				} else {
					stop();
				}
			} 
			
		});
		timer.start();
	}
	
	/**
	 * Crea el ambiente de la aplicacion
	 * @return JSplitPane contenedor general
	 */
	private JSplitPane getJDesktop(){
		desktop = new JTabbedPane();
		desktop.setBackground(new Color(232,232,232));
		JScrollPane sportspane = this.getSportsList();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sportspane, desktop);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerLocation(180);
	
		Dimension minimumSize = new Dimension(180, 560);
		Dimension minimumSize2 = new Dimension(600, 560);
		sportspane.setMinimumSize(minimumSize);
		desktop.setMinimumSize(minimumSize2);
		
		return splitPane;
	}
	
	private void openJuegosTab(){
		if(this.tbJuegos == null){
			this.tbJuegos = new JPanel();
			desktop.addTab("Juegos",this.tbJuegos);
		}
	}
	
	private JMenuBar getJMenubar() {
		JMenuBar menubar = new JMenuBar();
		JMenu archivo = new JMenu("Archivo");
		JMenu ayuda = new JMenu("Ayuda");
		conectar.addActionListener(this);
		desconectar.addActionListener(this);
		desconectar.setVisible(false);
		sobre.addActionListener(this);
		cerrar.addActionListener(this);
		archivo.add(conectar);
		archivo.add(desconectar);
		archivo.addSeparator();
		archivo.add(cerrar);
		ayuda.add(sobre);
		menubar.add(archivo);
		menubar.add(ayuda);
		
		return menubar;
	}
	
	/**
	 * Regresa un arreglo ordenado de objetos
	 * @param usuarios
	 * @return Arreglo ordenado de usuarios
	 */
	private Object[] sort(LinkedList sports){
		Object[] u = sports.toArray();
		
		for(int i=0;i<sports.size();i++){
			for(int j=i+1;j<sports.size();j++){
				if(((Sport)u[i]).getName().hashCode()>((Sport)u[j]).getName().hashCode()){
					String aux = (String)u[j];
					u[j] = u[i];
					u[i] = aux;
				}
			}
		}
		return u;
	}
	
	
	public static LinkedList <String>openedTabs = new LinkedList<String>();
	public static LinkedList <SportGamesPanel>openedPanels = new LinkedList<SportGamesPanel>();
	
	/**
	 * Regresa la lista de usuarios	
	 * @return JScrollPane Contenedor de los usuarios	
	 */
	private JScrollPane getSportsList() {
		
		lstSports = new JList(this.sports);
		lstSports.setSize(new Dimension(165,400));
		lstSports.setFixedCellHeight(25);
		lstSports.setCellRenderer(new SportCellRenderer());
		
		JScrollPane usp = new JScrollPane(lstSports,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		usp.setSize(new Dimension(160,400));
		usp.setLocation(new Point(612,31));
		usp.setOpaque(false);
		usp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
			
		lstSports.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					int index = lstSports.locationToIndex(e.getPoint());
				    ListModel dlm = lstSports.getModel();
				    Sport s = (Sport)dlm.getElementAt(index);
				    lstSports.ensureIndexIsVisible(index);
				    
				    if(!openedTabs.contains(s.getId()+"")){
				    	openedTabs.add(s.getId()+"");
				    	SportGamesPanel panel = new SportGamesPanel(s,desktop);
				    	panel.addGames(s.getGames());
				    	openedPanels.add(panel);
				    	desktop.addTab(s.getName(),s.getImage(), panel);
				    	
				    }
					
				}
			}

			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
        	
        });
		return usp;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(conectar)){
			String server = JOptionPane.showInputDialog(this, 
														"Especifique la ip/nombre del servidor", 
														"localhost");
			if(server != null && !server.equals("")){
				if(this.start(server)){
					this.conectar.setVisible(false);
					this.desconectar.setVisible(true);
				}
			} else {
				//JOptionPane.showMessageDialog(this, "Especifique la direccion o de cancelar");
			}
		} else if(e.getSource().equals(desconectar)){
			this.stop();
		} else if(e.getSource().equals(cerrar)){
			this.stop();
			System.exit(0);
		}
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		this.stop();
	}

	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
