import java.awt.*;
import javax.swing.*;

/**
 * Clase para darle formato a las celdas de la lista de usuarios
 * @author Revolution Software Developers
 */
public class SportCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public SportCellRenderer(){
		this.setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, final Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, 
                value, 
                index, 
                isSelected, 
                cellHasFocus);
		
		Sport s = (Sport)value;
		super.setIcon(s.getImage());
		super.setText(s.getName());
        super.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(242,242,242)));
       
        return this;
	}
	
}