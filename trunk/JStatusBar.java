import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * StatusBar para el JFrame
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 * @since 1.0
 */
public class JStatusBar extends JPanel {
	private static final long serialVersionUID = 5578387481446278467L;
	private String message = "";
	private JLabel left = new JLabel();
	private JLabel middle = new JLabel();
	private JLabel right = new JLabel();
  
	public JStatusBar() { 
    	this.init();
        this.setMessage(" ");
    }
    
    public JStatusBar(String message){
    	this.init();
    	this.setMessage(message);
    }
    
    public JStatusBar(String left, String right){
    	this.init();
    	this.setLeftMessage(left);
    	this.setRightMessage(right);
    }
    
    public JStatusBar(String left, String middle, String right){
    	this.init();
    	this.setLeftMessage(left);
    	this.setCenterMessage(middle);
    	this.setRightMessage(right);
    }
    
    public void init(){
    	this.setLayout(new GridLayout(1,3));
    	this.add(left);
    	this.add(middle);
    	this.add(right);
    	middle.setHorizontalAlignment(JLabel.CENTER);
    	right.setHorizontalAlignment(JLabel.RIGHT);
    	Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        this.setBorder(loweredbevel);
        this.setPreferredSize(new Dimension(100, 20));
    }
    
    public void setMessage(String message) {
    	this.message = message;
        this.left.setText(" "+message);        
    }
    
    public void setCenterMessage(String message){
    	this.middle.setText(message);
    }
    
    public void setRightMessage(String message){
    	this.right.setText(message+" ");
    }
    
    public void setLeftMessage(String message){
    	this.left.setText(" "+message);
    }
    
    public String getMessage(){
    	return this.message;
    }
}