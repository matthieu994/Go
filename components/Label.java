package components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class Label extends JLabel {
	private static final long serialVersionUID = 1L;

	public Label(String text, int fontSize, Color textColor) {
		super(text);
		setFont(new Font("Roboto", Font.PLAIN, fontSize));
		setForeground(textColor);
	}

	public void setFontSize(int fontSize) {
		setFont(this.getFont().deriveFont((float) fontSize));
	}
}
