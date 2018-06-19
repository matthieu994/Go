package go;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import components.TimerType;

public class Goban extends JPanel implements ComponentListener {
	public static int SIZE;
	public static int SKIPS;
	public static GameState gameState;
	public static TimerType timer;

	public static State player;
	private static ArrayList<Stone[][]> listeCoups;
	private static ArrayList<Stone> listeKo;
	private static ArrayList<Integer> listeSkips;
	private int listIndex;
	public static GobanGrid grid;
	private Infos infos;

	public Goban(int SIZE, TimerType timer) {
		addComponentListener(this);
		setBackground(new Color(155, 105, 50));
		gameState = GameState.START;
		SKIPS = 0;
		player = State.BLACK;
		listeCoups = new ArrayList<Stone[][]>();
		listeKo = new ArrayList<Stone>();
		listeSkips = new ArrayList<Integer>();
		Goban.SIZE = SIZE;
		Goban.timer = timer;
		listIndex = 1;

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		grid = new GobanGrid();
		infos = new Infos();
		add(grid);
		add(infos);
		if (Goban.timer != TimerType.NONE) {
			infos.p1Timer.start();
		}
		// listeCoups.add(new Stone[Goban.SIZE + 1][Goban.SIZE + 1]);
		// listeSkips.add(new Integer(0));
		addCoup();
	}

	public Goban(int size, GameState gamestate, TimerType timer, State player, int skips, int p1Timer, int p2Timer, Stone[][] stones) {
		addComponentListener(this);
		setBackground(new Color(155, 105, 50));
		Goban.gameState = gamestate;
		SKIPS = skips;
		Goban.player = player;
		listeCoups = new ArrayList<Stone[][]>();
		listeKo = new ArrayList<Stone>();
		listeSkips = new ArrayList<Integer>();
		Goban.SIZE = size;
		Goban.timer = timer;
		listIndex = 1;
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		grid = new GobanGrid(deepCopy(stones));
		infos = new Infos();
		Infos.setScore();
		add(grid);
		add(infos);
		if (Goban.timer != TimerType.NONE) {
			infos.p1Timer.setTime(p1Timer);
			infos.p2Timer.setTime(p2Timer);
			if (player == State.BLACK)
				infos.p1Timer.start();
			else
				infos.p2Timer.start();
		}
		addCoup();
		if(gameState != GameState.START) {
			finish();
		}
	}

	public static Stone[][] getStones() {
		return grid.stones;
	}

	public static void setStones(Stone[][] A) {
		grid.stones = A;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void nextPlayer() {
		gameState = (gameState == GameState.CAPTURE) ? GameState.END : GameState.START;
		if (player == State.BLACK) {
			player = State.WHITE;
			if (Goban.timer != TimerType.NONE) {
				infos.p1Timer.stop();
				infos.p2Timer.start();
			}
		} else {
			player = State.BLACK;
			if (Goban.timer != TimerType.NONE) {
				infos.p1Timer.start();
				infos.p2Timer.stop();
			}
		}
		grid.repaint();
	}

	public void addKo(Stone stone) {
		listeKo.add(stone);
	}

	public void removeKo() {
		if (listeKo.size() > 0) {
			listeKo.remove(listeKo.size() - 1);
		}
	}

	public boolean isKo(Stone stone) {
		for (int i = 0; i < listeKo.size(); i++) {
			if (stone.x == listeKo.get(i).x && stone.y == listeKo.get(i).y) {
				return true;
			}
		}
		return false;
	}

	public boolean isSameConfig(Stone[][] stones) {
		if (listeCoups.size() < 3)
			return false;
		int sameconfig = 0;
		for (int i = 0; i < listeCoups.size(); i++) {
			// System.out.println("Compare to: "+i);
			// GobanGrid.printStones(listeCoups.get(i));
			if (deepCompare(Goban.deepCopy(listeCoups.get(i)), Goban.deepCopy(stones))) {
				sameconfig++;
				// System.out.println("Same config "+sameconfig);
			}
		}
		if (sameconfig >= 3) {
			return true;
		}
		return false;
	}

	public boolean deepCompare(Stone[][] array1, Stone[][] array2) {
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (array1[x][y] == null && array2[x][y] != null) {
					// System.out.println("array1["+x+"]["+y+"] = null | array2["+x+"]["+y+"] =
					// "+array2[x][y].color);
					return false;
				}
				if (array1[x][y] != null && array2[x][y] == null) {
					// System.out.println("array2["+x+"]["+y+"] = null");
					return false;
				}
				if (array1[x][y] == null && array2[x][y] == null)
					continue;
				if (!array1[x][y].equals(array2[x][y])) {
					System.out.println("elements are different");
					return false;
				}
			}
		}
		return true;
	}

	public void addCoup() {
		listeCoups.add(Goban.deepCopy(grid.stones));
		listeSkips.add(new Integer(SKIPS));
		for (; listIndex > 1; listIndex--) {
			listeCoups.remove(listeCoups.size() - listIndex);
		}
		listIndex = 1;
		try {
			save();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// GobanGrid.printStones(getStones());
	}

	public void undoCoup() {
		if (listIndex >= listeCoups.size())
			return;
		grid.stones = deepCopy(listeCoups.get(listeCoups.size() - listIndex - 1));
		SKIPS = listeSkips.get(listeSkips.size() - listIndex - 1);
		grid.repaint();
		listIndex++;
		nextPlayer();
		Infos.setScore();
	}

	public void redoCoup() {
		if (listIndex > listeCoups.size() || listIndex == 1)
			return;
		grid.stones = deepCopy(listeCoups.get(listeCoups.size() - listIndex + 1));
		SKIPS = listeSkips.get(listeSkips.size() - listIndex + 1);
		grid.repaint();
		listIndex--;
		nextPlayer();
		Infos.setScore();
	}

	public void printArray() {
		System.out.println("------------------------------");
		for (int i = 0; i < listeCoups.size(); i++) {
			GobanGrid.printStones(listeCoups.get(i));
		}
		System.out.println("------------------------------");
	}

	public static Stone[][] deepCopy(Stone[][] A) {
		Stone[][] array = new Stone[SIZE][SIZE];
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				if (A[x][y] == null)
					array[x][y] = null;
				else
					array[x][y] = new Stone(A[x][y].color, A[x][y].x, A[x][y].y);
			}
		}
		return array;
	}

	public void reSize() {
		int width = getWidth(), height = getHeight();
		int gridSize = (width < height) ? width : height;
		if (width - gridSize < 200) {
			gridSize -= 200;
		}
		gridSize = (int) (gridSize / SIZE) * (SIZE);
		grid.setSize(gridSize);
		infos.setSize(width - gridSize, height);
		revalidate();
	}

	public void componentResized(ComponentEvent e) {
		reSize();
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void finish() {
		gameState = GameState.END;
		listeCoups = new ArrayList<Stone[][]>();
		listIndex = 1;
		addCoup();
		grid.finish();
		infos.finish();
	}

	public void save() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("resources/saves/game.go");
		writer.println(Goban.SIZE);
		writer.println(Goban.gameState);
		writer.println(Goban.timer);
		writer.println(Goban.player);
		writer.println(Goban.SKIPS);
		writer.println(infos.p1Timer);
		writer.println(infos.p2Timer);
		for (int y = 0; y < grid.stones[0].length; y++) {
			for (int x = 0; x < grid.stones.length; x++) {
				if (grid.stones[x][y] == null)
					writer.print("X");
				else if (grid.stones[x][y].color == State.BLACK)
					writer.print("B");
				else if (grid.stones[x][y].color == State.WHITE)
					writer.print("W");
			}
			writer.println();
		}
		writer.close();
	}
}

class GobanGrid extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	public Stone[][] stones;
	public boolean[][] deadStones;
	private boolean[][] hoshis;
	public static Point hoverStone;

	public GobanGrid(Stone[][] stones) {
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(new Color(155, 105, 50));
		this.stones = stones;
		deadStones = new boolean[Goban.SIZE][Goban.SIZE];
		hoshis = new boolean[Goban.SIZE][Goban.SIZE];
		if (Goban.SIZE == 9 || Goban.SIZE == 13) {
			hoshis[(int) Goban.SIZE / 4][(int) Goban.SIZE
					/ 4] = hoshis[Goban.SIZE - (int) Goban.SIZE / 4 - 1][(int) Goban.SIZE
							/ 4] = hoshis[(int) Goban.SIZE / 4][Goban.SIZE - (int) Goban.SIZE / 4
									- 1] = hoshis[Goban.SIZE - (int) Goban.SIZE / 4 - 1][Goban.SIZE
											- (int) Goban.SIZE / 4
											- 1] = hoshis[(int) Goban.SIZE / 2][(int) Goban.SIZE / 2] = true;
		}
		if (Goban.SIZE == 19) {
			hoshis[3][3] = hoshis[9][3] = hoshis[15][3] = hoshis[3][9] = hoshis[9][9] = hoshis[15][9] = hoshis[3][15] = hoshis[9][15] = hoshis[15][15] = true;
		}
	}

	public GobanGrid() {
		addMouseListener(this);
		addMouseMotionListener(this);
		setBackground(new Color(155, 105, 50));
		stones = new Stone[Goban.SIZE][Goban.SIZE];
		deadStones = new boolean[Goban.SIZE][Goban.SIZE];

		int handicap = 0;
		if (myWindow.range != null) {
			handicap = myWindow.range.getValue();
			if(handicap > 1) Goban.player = State.WHITE;
		}
		if (handicap % 2 == 1 && handicap != 1) {
			stones[Goban.SIZE / 2][Goban.SIZE / 2] = new Stone(State.BLACK, Goban.SIZE / 2, Goban.SIZE / 2);
		}
		if (handicap >= 2 && handicap <= 9) {
			if (Goban.SIZE == 9 || Goban.SIZE == 13) {
				stones[Goban.SIZE - Goban.SIZE / 4 - 1][Goban.SIZE / 4] = new Stone(State.BLACK,
						Goban.SIZE - Goban.SIZE / 4 - 1, Goban.SIZE / 4);
				stones[Goban.SIZE / 4][Goban.SIZE - Goban.SIZE / 4 - 1] = new Stone(State.BLACK, Goban.SIZE / 4,
						Goban.SIZE - Goban.SIZE / 4 - 1);
			} else {
				stones[3][15] = new Stone(State.BLACK, 3, 15);
				stones[15][3] = new Stone(State.BLACK, 15, 3);
			}
		}
		if (handicap >= 4 && handicap <= 9) {
			if (Goban.SIZE == 9 || Goban.SIZE == 13) {
				stones[Goban.SIZE / 4][Goban.SIZE / 4] = new Stone(State.BLACK, Goban.SIZE / 4, Goban.SIZE / 4);
				stones[Goban.SIZE - Goban.SIZE / 4 - 1][Goban.SIZE - Goban.SIZE / 4 - 1] = new Stone(State.BLACK,
						Goban.SIZE - Goban.SIZE / 4 - 1, Goban.SIZE - Goban.SIZE / 4 - 1);
			} else {
				stones[3][3] = new Stone(State.BLACK, 3, 3);
				stones[15][15] = new Stone(State.BLACK, 15, 15);
			}
		}
		if (handicap >= 6 && handicap <= 9) {
			stones[9][3] = new Stone(State.BLACK, 9, 3);
			stones[9][15] = new Stone(State.BLACK, 9, 15);
		}
		if (handicap >= 8 && handicap <= 9) {
			stones[3][9] = new Stone(State.BLACK, 3, 9);
			stones[15][9] = new Stone(State.BLACK, 15, 9);
		}

		hoshis = new boolean[Goban.SIZE][Goban.SIZE];
		if (Goban.SIZE == 9 || Goban.SIZE == 13) {
			hoshis[(int) Goban.SIZE / 4][(int) Goban.SIZE
					/ 4] = hoshis[Goban.SIZE - (int) Goban.SIZE / 4 - 1][(int) Goban.SIZE
							/ 4] = hoshis[(int) Goban.SIZE / 4][Goban.SIZE - (int) Goban.SIZE / 4
									- 1] = hoshis[Goban.SIZE - (int) Goban.SIZE / 4 - 1][Goban.SIZE
											- (int) Goban.SIZE / 4
											- 1] = hoshis[(int) Goban.SIZE / 2][(int) Goban.SIZE / 2] = true;
		}
		if (Goban.SIZE == 19) {
			hoshis[3][3] = hoshis[9][3] = hoshis[15][3] = hoshis[3][9] = hoshis[9][9] = hoshis[15][9] = hoshis[3][15] = hoshis[9][15] = hoshis[15][15] = true;
		}
	}

	public void setSize(int gridSize) {
		setMinimumSize(new Dimension(gridSize, gridSize));
		setPreferredSize(new Dimension(gridSize, gridSize));
		setMaximumSize(new Dimension(gridSize, gridSize));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawGrid(g);
	}

	public void drawGrid(Graphics g) {
		int gridSize = getWidth();
		int cellSize = gridSize / (Goban.SIZE);
		int padding = cellSize;
		gridSize -= padding;
		padding /= 2;

		g.setColor(new Color(30, 30, 30));
		setHoshi(g, cellSize, padding);

		for (int x = 0; x < Goban.SIZE; x++) {
			g.drawLine(x * cellSize + padding, padding, x * cellSize + padding, gridSize + padding);
		}
		for (int y = 0; y < Goban.SIZE; y++) {
			g.drawLine(padding, y * cellSize + padding, gridSize + padding, y * cellSize + padding);
			drawLineStones(g, y, cellSize, padding);
		}
		drawHoverStone(g, cellSize, padding);
	}

	public void drawLineStones(Graphics g, int y, int cellSize, int padding) {
		double width = cellSize * 0.9;
		Image stone = null;
		Graphics2D g2d = (Graphics2D) g;
		for (int x = 0; x < Goban.SIZE; x++) {
			if (stones[x][y] == null)
				continue;
			if (stones[x][y].color == State.EMPTY)
				continue;
			if (stones[x][y].color == State.BLACK)
				stone = getImage("/resources/img/black.png");
			if (stones[x][y].color == State.BLACK_T)
				stone = getImage("/resources/img/black_T.png");
			if (stones[x][y].color == State.WHITE)
				stone = getImage("/resources/img/white.png");
			if (stones[x][y].color == State.WHITE_T)
				stone = getImage("/resources/img/white_T.png");

			if (stones[x][y].color == State.BLACK_T || stones[x][y].color == State.WHITE_T)
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
			else if ((Goban.gameState == GameState.END || Goban.gameState == GameState.CAPTURE) && deadStones[x][y] == true)
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			else
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			g.drawImage(stone, x * cellSize + (int) width / 14, y * cellSize + (int) width / 14, (int) width,
					(int) width, this);
		}
		g.setColor(new Color(0, 0, 0));
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	public void drawHoverStone(Graphics g, int cellSize, int padding) {
		if (hoverStone == null)
			return;
		double width = cellSize * 0.9;
		Image stone = null;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
		if (Goban.player == State.BLACK)
			stone = getImage("/resources/img/black.png");
		if (Goban.player == State.WHITE)
			stone = getImage("/resources/img/white.png");
		if (Goban.gameState == GameState.END) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			stone = deadStones[(int) hoverStone.getX()][(int) hoverStone.getY()] ? getImage("/resources/img/tick.png")
					: getImage("/resources/img/cancel.png");
		}
		g.drawImage(stone, (int) hoverStone.getX() * cellSize + (int) width / 14,
				(int) hoverStone.getY() * cellSize + (int) width / 14, (int) width, (int) width, this);
	}

	public Image getImage(String url) {
		return new ImageIcon(this.getClass().getResource(url)).getImage();
	}

	public void addStone(MouseEvent e, boolean hover) {
		int gridSize = getWidth();
		int cellSize = gridSize / (Goban.SIZE);
		int padding = cellSize;
		gridSize -= padding;
		padding /= 2;
		int posX = (e.getX() - padding) / cellSize;
		int posY = (e.getY() - padding) / cellSize;

		double dist = distance(e.getX(), e.getY(), (posX * cellSize) + padding, (posY * cellSize) + padding);
		Point cell = new Point(posX, posY);
		if (distance(e.getX(), e.getY(), (posX * cellSize) + padding + cellSize, (posY * cellSize) + padding) < dist) {
			dist = distance(e.getX(), e.getY(), (posX * cellSize) + padding + cellSize, (posY * cellSize) + padding);
			cell = new Point(posX + 1, posY);
		}
		if (distance(e.getX(), e.getY(), (posX * cellSize) + padding, (posY * cellSize) + padding + cellSize) < dist) {
			dist = distance(e.getX(), e.getY(), (posX * cellSize) + padding, (posY * cellSize) + padding + cellSize);
			cell = new Point(posX, posY + 1);
		}
		if (distance(e.getX(), e.getY(), (posX * cellSize) + padding + cellSize,
				(posY * cellSize) + padding + cellSize) < dist) {
			dist = distance(e.getX(), e.getY(), (posX * cellSize) + padding + cellSize,
					(posY * cellSize) + padding + cellSize);
			cell = new Point(posX + 1, posY + 1);
		}
		posX = (int) cell.getX();
		posY = (int) cell.getY();

		if (stones[posX][posY] == null && (Goban.gameState != GameState.END || Goban.gameState == GameState.CAPTURE)
				&& Goban.gameState != GameState.TERRITORY) {
			if (hover) {
				hoverStone = new Point(posX, posY);
			} else if (addStone(posX, posY)) {
				hoverStone = null;
				Goban.SKIPS = 0;
				Infos.setScore();
				Go.getGoban().addCoup();
				Go.getGoban().nextPlayer();
			}
		} else if (Goban.gameState == GameState.END && stones[posX][posY] != null) {
			if (hover) {
				hoverStone = new Point(posX, posY);
			} else {
				stones[posX][posY].removeGroup(!deadStones[posX][posY]);
			}
		}
		repaint();
	}

	public boolean addStone(int x, int y) {
		stones[x][y] = new Stone(Goban.player, x, y);
		if (Go.getGoban().isKo(stones[x][y])) {
			stones[x][y] = null;
			return false;
		}
		Stone killedStone = null;
		Stone[] neighbors = new Stone[4];
		boolean ko = (stones[x][y].getLiberties() < 1) ? true : false;
		Stone koStone = null;
		if (x > 0) {
			neighbors[0] = stones[x - 1][y];
		}
		if (x + 1 < Goban.SIZE) {
			neighbors[1] = stones[x + 1][y];
		}
		if (y > 0) {
			neighbors[2] = stones[x][y - 1];
		}
		if (y + 1 < Goban.SIZE) {
			neighbors[3] = stones[x][y + 1];
		}

		for (Stone neighbor : neighbors) {
			if (neighbor == null) {
				continue;
			}
			if (neighbor.color != stones[x][y].color) {
				if (neighbor.getLiberties() < 1) {
					if (!neighbor.isAlone())
						ko = false;
					else if (ko)
						koStone = new Stone(State.KO, neighbor.x, neighbor.y);
					killedStone = neighbor;
					neighbor.removeGroup();
					if (Go.getGoban().isSameConfig(stones)) {
						stones[killedStone.x][killedStone.y] = killedStone;
						stones[x][y] = null;
						return false;
					}
				}
			}
		}
		if (stones[x][y].getLiberties() < 1) {
			stones[x][y] = null;
			ko = false;
			return false;
		}
		Go.getGoban().removeKo();
		if (ko) {
			Go.getGoban().addKo(koStone);
		}
		return true;
	}

	public double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public void setHoshi(Graphics g, int cellSize, int padding) {
		for (int y = 0; y < hoshis[0].length; y++) {
			for (int x = 0; x < hoshis.length; x++) {
				if (hoshis[x][y])
					g.fillOval(x * cellSize + padding - 3, y * cellSize + padding - 3, 6, 6);
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		addStone(e, false);
	}

	public void mouseMoved(MouseEvent e) {
		addStone(e, true);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public static void printStones(Stone[][] array) {
		for (int y = 0; y < Goban.SIZE; y++) {
			for (int x = 0; x < Goban.SIZE; x++) {
				if (array[x][y] == null)
					System.out.print(" ");
				// else if (Go.getGoban().isKo(array[x][y]))
				// System.out.print('k');
				else
					System.out.print(array[x][y]);
			}
			System.out.println();
		}
		for (int x = 0; x < Goban.SIZE; x++) {
			System.out.print("-");
		}
		System.out.println();
	}

	public void fill(State color, boolean[][] array) { // Remplissage territoires
		for (int y = 0; y < Goban.SIZE; y++) {
			for (int x = 0; x < Goban.SIZE; x++) {
				if (array[x][y])
					stones[x][y] = new Stone(color, x, y);
			}
		}
		repaint();
	}

	public void finish() {
		hoverStone = null;
		repaint();
	}
}
