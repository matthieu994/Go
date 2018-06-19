package components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JToggleButton;

public class ToggleButton extends JToggleButton implements ActionListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private Color backgroundColor;
	private ToggleButton TB1;
	private ToggleButton TB2;

	public ToggleButton(String text, int fontSize, Color backgroundColor) {
		super(text);
		setFont(new Font("Roboto", Font.PLAIN, fontSize), Color.WHITE);
		constructor(backgroundColor);
	}
	
	public void setFont(Font font, Color color) {
		setFont(font);
		setForeground(color);
	}

	public void constructor(Color backgroundColor) {
		addMouseMotionListener(this);
		setContentAreaFilled(false);
		addActionListener(this);
		setBorderPainted(false);
		setFocusPainted(false);
		setBackground(backgroundColor);
		this.backgroundColor = backgroundColor;
	}
	
	public void addButton(ToggleButton TB1, ToggleButton TB2) {
		this.TB1 = TB1;
		this.TB2 = TB2;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isSelected()) {
			g.setColor(getBackground().darker());
		} else {
			g.setColor(backgroundColor);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(getModel().isSelected()) {
			TB1.setSelected(false);
			TB2.setSelected(false);
		}
		if(!getModel().isSelected()) {
			setSelected(true);
		}
	}
}

