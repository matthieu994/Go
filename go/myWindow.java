package go;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import components.Button;
import components.Label;
import components.Panneau;
import components.Slider;
import components.Slider.SliderType;
import components.TimerType;
import components.ToggleButton;

//import mySQL.DemoSQL;

public class myWindow extends JFrame implements ComponentListener {
	private static final long serialVersionUID = 1L;
	private Panneau contentPane = new Panneau();
	public Goban goban;
	private Label title;
	private Button start;

	public static Dimension MenuWindow = new Dimension(800, 600);
	public static Dimension GobanWindow = new Dimension(650, 500);

	public static Slider timer;
	public static Slider range;
	public static Slider byoPeriods;
	public static Slider byoTime;

	public myWindow() {
		addComponentListener(this);
		try {
			setIconImage(ImageIO.read(getClass().getResource("/resources/img/black.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		setContentPane(contentPane);
	}

	public void displayMenu() {
		setTitle("Go");
		setSize((int) MenuWindow.getWidth(), (int) MenuWindow.getHeight());
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(600, 580));
		contentPane.removeAll();
		contentPane.setLayout(new GridBagLayout());
		contentPane.setBackground(new Color(20, 120, 130));

		title = new Label("Jeu de Go", getWidth() / 8, Color.WHITE);
		start = new Button("Jouer", getWidth() / 17, new Color(20, 100, 120));
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseSize();
			}
		});
		Button quick = new Button("Partie Rapide", getWidth() / 24, new Color(20, 140, 120));
		quick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayGame(19, TimerType.ABSOLUTE);
			}
		});
		Button load = new Button("Continuer", getWidth() / 24, new Color(20, 110, 120));
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					load();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0; gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 0, 15, 0);
		contentPane.add(title, gbc);
		start.setSize(getWidth() / 4, getHeight() / 8);
		load.setSize(getWidth() / 4, getHeight() / 8);
		gbc.gridy = 1; gbc.gridwidth = 2; gbc.insets.bottom = 40;
		if(new File("resources/saves/game.go").exists()) {
			gbc.gridwidth = 1;
			contentPane.add(start, gbc);
			gbc.gridx++;
			contentPane.add(load, gbc);
		} else {
			contentPane.add(start, gbc);
		}
		gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
		contentPane.add(quick, gbc);

		contentPane.validate();
		contentPane.revalidate();
		contentPane.repaint();
	}

	public void chooseSize() { // Choisir taille goban
		contentPane.removeAll();
		contentPane.repaint();
		contentPane.setLayout(new GridBagLayout());
		contentPane.setBackground(new Color(10, 90, 90));

		Label size = new Label("Taille du goban ?", getWidth() / 14, Color.WHITE);
		Button size9 = new Button("9", 30, new Color(36, 120, 122));
		Button size13 = new Button("13", 30, new Color(36, 120, 122));
		Button size19 = new Button("19", 30, new Color(36, 120, 122));
		Panneau buttonContainer = new Panneau();
		buttonContainer.setLayout(new GridBagLayout());
		buttonContainer.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 10, 0, 10);
		buttonContainer.add(size9, gbc);
		buttonContainer.add(size13, gbc);
		buttonContainer.add(size19, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 20, 0);
		contentPane.add(size, gbc);
		gbc.gridy = 1;
		contentPane.add(buttonContainer, gbc);
		contentPane.validate();
	}

	public void chooseTimer(int SIZE) { // Choisir type horloge
		contentPane.removeAll();
		contentPane.repaint();
		contentPane.setLayout(new GridBagLayout());

		Label timerTitle = new Label("Type d'horloge ?", 60, Color.WHITE);
		ToggleButton none = new ToggleButton("Aucune", 30, new Color(36, 120, 122));
		ToggleButton absolute = new ToggleButton("Absolue", 30, new Color(36, 120, 122));
		ToggleButton byo = new ToggleButton("Byo-Yomi", 30, new Color(36, 120, 122));
		none.addButton(absolute, byo);
		none.setSelected(true);
		absolute.addButton(none, byo);
		byo.addButton(absolute, none);
		none.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.toggle(false);
				byoPeriods.toggle(false);
				byoTime.toggle(false);
			}
		});
		absolute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// displayGame(SIZE, TimerType.ABSOLUTE);
				timer.toggle(true);
				byoPeriods.toggle(false);
				byoTime.toggle(false);
			}
		});
		byo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// displayGame(SIZE, TimerType.BYO);
				timer.toggle(true);
				byoPeriods.toggle(true);
				byoTime.toggle(true);
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0, 10, 20, 10);
		contentPane.add(timerTitle, gbc);

		gbc.insets = new Insets(0, 10, 40, 10);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		contentPane.add(none, gbc);
		gbc.gridx = 1;
		contentPane.add(absolute, gbc);
		gbc.gridx = 2;
		contentPane.add(byo, gbc);

		gbc.insets = new Insets(0, 15, 20, 15);
		gbc.gridx = 1;
		gbc.gridy++;
		Label outputTimer = new Label("Temps: 30min.", 30, Color.WHITE);
		timer = new Slider(1, 60, 30, outputTimer, SliderType.TIME);
		contentPane.add(timer, gbc);
		gbc.gridy++;
		contentPane.add(outputTimer, gbc);
		timer.toggle(false);

		gbc.gridx = 0;
		gbc.gridy--;
		Label outputByo = new Label("P\u00e9riodes de Byo-Yomi: 3", 20, Color.WHITE);
		byoPeriods = new Slider(1, 5, 3, outputByo, SliderType.BYO_YOMI);
		contentPane.add(byoPeriods, gbc);
		gbc.gridy++;
		contentPane.add(outputByo, gbc);
		byoPeriods.toggle(false);

		gbc.gridx = 2;
		gbc.gridy--;
		Label outputByoTime = new Label("Temps de Byo-Yomi: 10sec.", 20, Color.WHITE);
		byoTime = new Slider(5, 30, 10, outputByoTime, SliderType.BYO_YOMI_TIME);
		contentPane.add(byoTime, gbc);
		gbc.gridy++;
		contentPane.add(outputByoTime, gbc);
		byoTime.toggle(false);

		gbc.gridy++;
		gbc.gridx = 1;
		gbc.insets = new Insets(20, 0, 0, 0);
		Label outputRange = new Label("Handicap: 0", 25, Color.WHITE);
		if (SIZE == 9 || SIZE == 13) {
			range = new Slider(0, 5, 0, outputRange, SliderType.HANDICAP);
		} else {
			range = new Slider(0, 9, 0, outputRange, SliderType.HANDICAP);
		}
		contentPane.add(range, gbc);
		gbc.gridy++;
		contentPane.add(outputRange, gbc);

		Button retour = new Button("Retour", 35, new Color(120, 50, 20));
		retour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseSize();
			}
		});
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		contentPane.add(retour, gbc);

		Button start = new Button("Jouer!", 35, new Color(20, 100, 120));
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (none.isSelected())
					displayGame(SIZE, TimerType.NONE);
				if (absolute.isSelected())
					displayGame(SIZE, TimerType.ABSOLUTE);
				if (byo.isSelected())
					displayGame(SIZE, TimerType.BYO);
			}
		});
		gbc.gridx = 1;
		contentPane.add(start, gbc);

		contentPane.validate();
	}

	public void displayGame(int size, TimerType timer) {
		contentPane.setLayout(new BorderLayout());
		contentPane.removeAll();
		goban = new Goban(size, timer);
		contentPane.add(goban);
		contentPane.validate();
	}

	public void displayWinner() {
		Goban.gameState = GameState.END;
		contentPane.removeAll();
		contentPane.repaint();
		contentPane.setLayout(new GridBagLayout());

		File save = new File("resources/saves/game.go");
		save.delete();

		Label scoreB = new Label(Infos.scoreBlack.getText(), getWidth() / 17, Color.WHITE);
		Label scoreW = new Label(Infos.scoreWhite.getText(), getWidth() / 17, Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 4;
		gbc.gridx = gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 20, 0);
		contentPane.add(new Label("Score: ", getWidth() / 12, Color.WHITE), gbc);

		gbc.insets = new Insets(0, 0, 0, getWidth() / 16);
		gbc.gridy = gbc.gridx = gbc.gridwidth = 1;
		contentPane.add(scoreB, gbc);
		gbc.insets = new Insets(0, getWidth() / 16, 0, 0);
		gbc.gridx = 2;
		contentPane.add(scoreW, gbc);

		gbc.insets = new Insets(0, 8, 0, 8);
		gbc.gridx = 0;
		ImageIcon blackIcon = new ImageIcon(getClass().getResource("/resources/img/black.png"));
		contentPane.add(
				new JLabel(new ImageIcon(
						blackIcon.getImage().getScaledInstance(getWidth() / 16, getWidth() / 16, Image.SCALE_DEFAULT))),
				gbc);
		gbc.gridx = 3;
		ImageIcon whiteIcon = new ImageIcon(getClass().getResource("/resources/img/white.png"));
		contentPane.add(
				new JLabel(new ImageIcon(
						whiteIcon.getImage().getScaledInstance(getWidth() / 16, getWidth() / 16, Image.SCALE_DEFAULT))),
				gbc);

		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(30, 0, 0, 0);
		double scoreBlack = Double.parseDouble(scoreB.getText());
		double scoreWhite = Double.parseDouble(scoreW.getText());
//		if (scoreBlack > 0 && scoreWhite > 0)
//			DemoSQL.insertInto(scoreBlack, scoreWhite);
		if (scoreBlack > scoreWhite) {
			if (scoreWhite == -1)
				scoreW.setText("Temps \u00e9coul\u00e9");
			if (scoreWhite == -2)
				scoreW.setText("Abandon");
			scoreB.setForeground(new Color(20, 200, 50));
			scoreW.setForeground(new Color(200, 20, 50));
			contentPane.add(new Label("Joueur 1 gagne !", getWidth() / 18, Color.WHITE), gbc);
		} else if (scoreBlack < scoreWhite) {
			if (scoreBlack == -1)
				scoreB.setText("Temps \u00e9coul\u00e9");
			if (scoreBlack == -2)
				scoreB.setText("Abandon");
			scoreB.setForeground(new Color(200, 20, 50));
			scoreW.setForeground(new Color(20, 200, 50));
			contentPane.add(new Label("Joueur 2 gagne !", getWidth() / 18, Color.WHITE), gbc);
		} else {
			scoreB.setForeground(new Color(20, 110, 170));
			scoreW.setForeground(new Color(20, 110, 170));
			contentPane.add(new Label("\u00c9galit\u00e9 !", getWidth() / 18, Color.WHITE), gbc);
		}

		Button play = new Button("Jouer", 20, Color.WHITE, new Color(50, 100, 120));
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Goban.gameState = GameState.END;
				Go.mainWindow.displayMenu();
			}
		});
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 3;
		contentPane.add(play, gbc);
		Button scoreBoard = new Button("Afficher scores", 20, Color.WHITE, new Color(70, 150, 150));
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy++;
		contentPane.add(scoreBoard, gbc);
		scoreBoard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				displayScoreBoard();
			}
		});

		contentPane.validate();
	}

//	public void displayScoreBoard() {
//		contentPane.removeAll();
//		contentPane.repaint();
//		contentPane.setLayout(new GridBagLayout());
//
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.gridx = gbc.gridy = 0;
//		gbc.insets = new Insets(0, 10, 20, 10);
//		contentPane.add(new Label("Joueur 1", 20, Color.WHITE), gbc);
//		gbc.gridx++;
//		contentPane.add(new Label("Joueur 2", 20, Color.WHITE), gbc);
//		gbc.gridy++;
//		gbc.insets.bottom = 10;
//		HashMap<Integer, Integer> map = DemoSQL.getScores(50);
//		for (Integer scoreBlack : map.keySet()) {
//			gbc.gridx = 0;
//			contentPane.add(new Label(Integer.toString(scoreBlack), 20, Color.WHITE), gbc);
//			gbc.gridx++;
//			contentPane.add(new Label(Integer.toString(map.get(scoreBlack)), 20, Color.WHITE), gbc);
//			gbc.gridy++;
//		}
//
//		contentPane.validate();
//	}

	public void load() throws IOException {
		BufferedReader save = new BufferedReader(new FileReader("resources/saves/game.go"));
		int size = Integer.parseInt(save.readLine());
		GameState gamestate;
		if(save.readLine().equals("START")) gamestate = GameState.START;
		else gamestate = GameState.END;
		String timerString = save.readLine();
		TimerType timer;
		if(timerString.equals("NONE")) timer = TimerType.NONE;
		else if(timerString.equals("ABSOLUTE")) timer = TimerType.ABSOLUTE;
		else timer = TimerType.BYO;
		State player = (save.readLine().equals("BLACK")) ? State.BLACK : State.WHITE;
		int skips = Integer.parseInt(save.readLine());
		int p1Timer, p2Timer;
		if(timer != TimerType.NONE) {
			p1Timer = Integer.parseInt(save.readLine());
			p2Timer = Integer.parseInt(save.readLine());
		} else {
			p1Timer = 0;
			p2Timer = 0;
			save.readLine(); save.readLine();
		}
		Stone[][] stones = new Stone[size][size];
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				char c = (char)save.read();
				if (c == 'X') continue;
				if (c == 'B') stones[x][y] = new Stone(State.BLACK, x, y);
				if (c == 'W') stones[x][y] = new Stone(State.WHITE, x, y);
			}
			save.read(); save.read();
		}
		save.close();

		contentPane.setLayout(new BorderLayout());
		contentPane.removeAll();
		goban = new Goban(size, gamestate, timer, player, skips, p1Timer, p2Timer, stones);
		contentPane.add(goban);
		contentPane.validate();
		goban.nextPlayer();
	}

	public void componentResized(ComponentEvent e) {
		if (title == null || start == null)
			return;
		title.setFontSize(getWidth() / 8);
		start.setFontSize(getWidth() / 17);
		start.setSize(getWidth() / 3, getHeight() / 8);
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}
}
