import javax.swing.*;
/**
 * Clase individual para el ticker de cada partido
 * @since 1.0
 * @author Daniel Ramirez <daniel.rmz@gmail.com>
 */
public class Ticker extends JFrame {

	private static final long serialVersionUID = -4441577818401868030L;
	public int sport;
	public int game;
	public Ticker(int sport, int game){
		this.sport = sport;
		this.game = game;
	}
}
