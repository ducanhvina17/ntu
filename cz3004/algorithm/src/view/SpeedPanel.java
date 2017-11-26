package view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Sets up a drop down list for the user to select the speed of the robot
 */
public class SpeedPanel extends JPanel {
	private static final long serialVersionUID = 6797289428220696656L;
	int multiplier;
	JLabel speedLabel;
	DefaultComboBoxModel<String> speed;
	JComboBox<String> speedComboBox;

	public SpeedPanel(int multiplier) {
		super();
		this.multiplier = multiplier;
		this.speedLabel = new JLabel("Steps per second", JLabel.LEFT);
		this.speed = new DefaultComboBoxModel<String>();
		for (int i = 1; i <= 10; i++) {
			speed.addElement("x" + (multiplier * i));
		}
		this.speedComboBox = new JComboBox<String>(speed);
		speedComboBox.setSelectedIndex(0); // Default is the multiplier value
		speedComboBox.addActionListener(speedComboBox);

		this.add(speedLabel);
		this.add(speedComboBox);
	}

	/**
	 * Multiplies the index position by the multiplier specified
	 *
	 * @return the value of the speed indicated by user
	 */
	public int getSpeed() {
		return (speedComboBox.getSelectedIndex() + 1) * this.multiplier;
	}
}
