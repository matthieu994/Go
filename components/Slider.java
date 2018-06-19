package components;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider extends JSlider implements ChangeListener {
	public enum SliderType {
		HANDICAP, TIME, BYO_YOMI, BYO_YOMI_TIME
	}

	private static final long serialVersionUID = 1L;
	private SliderType type;
	private Label output;

	public Slider(int min, int max, int value, Label output, SliderType type) {
		super(min, max, value);
		this.output = output;
		this.type = type;
		addChangeListener(this);
		setOpaque(false);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (type == SliderType.HANDICAP) {
			output.setText("Handicap: " + this.getValue());
		}
		if (type == SliderType.TIME) {
			output.setText("Temps: " + this.getValue() + "min.");
		}
		if (type == SliderType.BYO_YOMI) {
			output.setText("P\u00e9riodes de Byo-Yomi: " + this.getValue());
		}
		if (type == SliderType.BYO_YOMI_TIME) {
			output.setText("Temps de Byo-Yomi: " + this.getValue() + "sec.");
		}
	}

	public void toggle(boolean visible) {
		setVisible(visible);
		output.setVisible(visible);
	}
}
