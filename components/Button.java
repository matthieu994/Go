package components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import go.GameState;
import go.Go;
import go.Goban;
import go.Infos;
import go.State;

public class Button extends JButton implements ActionListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private Color backgroundColor;

	public Button(ImageIcon icon, int iconSize, Color backgroundColor) {
		super(new ImageIcon(icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_DEFAULT)));
		constructor(backgroundColor);
	}

	public Button(String text, int fontSize, Color backgroundColor) {
		super(text);
		setFont(new Font("Roboto", Font.PLAIN, fontSize), Color.WHITE);
		constructor(backgroundColor);
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

	public Button(String text, int fontSize, Color fontColor, Color backgroundColor) {
		this(text, fontSize, backgroundColor);
		setForeground(fontColor);
	}

	public void setSize(int width, int height) {
		setMinimumSize(new Dimension(width, height));
		setPreferredSize(new Dimension(width, height));
		setMaximumSize(new Dimension(width, height));
	}

	public void setFont(Font font, Color color) {
		setFont(font);
		setForeground(color);
	}

	public void setFontSize(int fontSize) {
		setFont(getFont().deriveFont((float) fontSize));
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isPressed()) {
			g.setColor(getBackground().darker());
		} else if (getModel().isRollover()) {
			g.setColor(getBackground().brighter());
		} else {
			g.setColor(backgroundColor);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "9" || e.getActionCommand() == "13" || e.getActionCommand() == "19") {
			Go.mainWindow.chooseTimer(Integer.parseInt(e.getActionCommand()));
		}
		if (e.getActionCommand() == "Abandonner") {
//			Goban.gameState = GameState.END;
			if(Goban.player == State.BLACK) Infos.scoreBlack.setText("-2");
			else Infos.scoreWhite.setText("-2");
			Go.mainWindow.displayWinner();
		}
		if (e.getActionCommand() == "Quitter") {
			Goban.gameState = GameState.END;
			Go.mainWindow.displayMenu();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
}
