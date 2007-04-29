import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;

public class SuscriptionFrame extends JDialog implements WindowListener {
	private static final long serialVersionUID = 1L;
	private Team local = null;
	private Team visitor = null;
	private JPanel content = new JPanel(new GridLayout(2,2));
	private String id = "";
	public SuscriptionFrame(Game g, String id){
		local = g.getLocal();
		visitor = g.getVisit();
		this.id = id;
		this.setTitle(local.getName()+ " vs. " + visitor.getName());
		content.add(new JLabel(local.getName()));
		content.add(new JLabel(visitor.getName()));
		content.add(new JLabel(g.getLocalScore()+""));
		content.add(new JLabel(g.getVisitScore()+""));
		this.setSize(new Dimension(200,100));
		this.add(content);
		this.addWindowListener(this);
	}
	
	public void updateGame(Game g){
		content.removeAll();
		content.add(new JLabel(local.getName()));
		content.add(new JLabel(visitor.getName()));
		content.add(new JLabel(g.getLocalScore()+""));
		content.add(new JLabel(g.getVisitScore()+""));
	}

	public void windowActivated(WindowEvent arg0) {}

	public void windowClosed(WindowEvent arg0) {}

	public void windowClosing(WindowEvent arg0) {
		ClientInterface.suscrtionsid.remove(this.id);
		ClientInterface.cliente.sendMessage(new Message(this.id, Message.GAME_REMOVE));
		ClientInterface.sb.setCenterMessage("Suscripciones: "+ClientInterface.suscrtionsid.size());
	}

	public void windowDeactivated(WindowEvent arg0) {}

	public void windowDeiconified(WindowEvent arg0) {}

	public void windowIconified(WindowEvent arg0) {}

	public void windowOpened(WindowEvent arg0) {}
}
