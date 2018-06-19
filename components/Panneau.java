package components;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Panneau extends JPanel {
	private static final long serialVersionUID = 1L;

	public void setSize(int width, int height) {
		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
	}
}