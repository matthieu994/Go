package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import go.GameState;
import go.Go;
import go.Goban;
import go.Infos;
import go.State;
import go.myWindow;

public class Countdown extends JLabel {
	private static final long serialVersionUID = 1L;
	public static int TIME;
	private int interval;
	private Timer timer;
	private int BYO_PERIODS;
	private int periods;
	private int BYO_TIME;

	public Countdown() {
		super(Integer.toString(TIME));
		if(Goban.timer == TimerType.NONE) return;
		if (myWindow.timer != null) {
			TIME = myWindow.timer.getValue() * 60;
			BYO_PERIODS = myWindow.byoPeriods.getValue();
			periods = BYO_PERIODS;
			BYO_TIME = myWindow.byoTime.getValue();
		} else {
			TIME = 30 * 60;
			BYO_PERIODS = 3;
			periods = BYO_PERIODS;
			BYO_TIME = 10;
		}

		setFont(new Font("Roboto", Font.PLAIN, 25));
		setForeground(Color.WHITE);
		interval = TIME * 1000;
		int delay = 1000; // milliseconds
		setText(interval / 1000 / 60 + ":00");
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (Goban.gameState == GameState.END) {
					timer.stop();
					return;
				}
				if (interval <= 0 && Goban.timer != TimerType.NONE) {
					if (Goban.timer == TimerType.BYO && periods > 0) {
						interval = BYO_TIME * 1000;
						periods--;
						setForeground(new Color(230, 90, 90));
					} else {
						setForeground(new Color(200, 20, 20));
						if (Goban.player == State.BLACK) {
							Infos.scoreBlack.setText("-1");
						} else {
							Infos.scoreWhite.setText("-1");
						}
						Go.mainWindow.displayWinner();
						return;
					}
				}
				interval -= delay;
				if (Goban.timer == TimerType.BYO && periods < BYO_PERIODS)
					setText(Integer.toString(interval / 1000 % 60));
				else
					setText(interval / 1000 / 60 + ":" + interval / 1000 % 60);
			}
		};
		timer = new Timer(delay, taskPerformer);
	}
	
	public void setTime(int interval) {
		this.interval = interval;
		if (Goban.timer == TimerType.BYO && periods < BYO_PERIODS) {
			setText(Integer.toString(interval / 1000 % 60));			
		}
		else
			setText(interval / 1000 / 60 + ":" + interval / 1000 % 60);
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		if (Goban.timer == TimerType.BYO && periods < BYO_PERIODS) {
			interval = BYO_TIME * 1000;
			setText(Integer.toString(interval / 1000 % 60));
		}
		timer.stop();
	}

	@Override
	public String toString() {
		return Integer.toString(interval);
	}
}
