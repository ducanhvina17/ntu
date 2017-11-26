/**
 * 
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import explore.Explore;

/**
 * A JPanel that implements a timer UI panel
 * 
 * @author Leroy Lim
 *
 */
public class TimerPanel extends JPanel {
	private static final long serialVersionUID = -4057749747345897634L;
	private static int DEFAULT_TIME = (5 * 60 * 1000) + (15 * 1000); // Timer is
																		// set
																		// in
																		// this
	// format min::sec::ms
	private JLabel timerLabel, timeLabel;
	private Timer timer;
	private int timeLeft;
	private SimpleDateFormat df;
	private Explore expAlgo;

	ActionListener countDown = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			timeLeft -= 1000;
			timeLabel.setText(df.format(timeLeft));
			if (timeLeft <= 0) {
				expAlgo.timeup();
				timer.stop();
			}
		}
	};

	public TimerPanel() {
		super();
		this.timerLabel = new JLabel("Time remaining: ");
		this.timeLeft = DEFAULT_TIME;
		this.df = new SimpleDateFormat("m:ss");
		this.df.setTimeZone(TimeZone.getTimeZone("GMT"));
		timeLabel = new JLabel(df.format(timeLeft));
		timer = new Timer(1000, countDown);
		timer.setCoalesce(true);
		timer.setRepeats(true);
		timer.setInitialDelay(0);
		this.add(timerLabel);
		this.add(timeLabel);
	}

	public void attachExploreAlgo(Explore exp) {
		this.expAlgo = exp;
	}

	public void resetTimer() {
		this.timeLeft = DEFAULT_TIME;
		timeLabel.setText(df.format(timeLeft));
	}

	public void setTimer(int minutes) {
		this.timeLeft = minutes * 60 * 1000;
		timeLabel.setText(df.format(timeLeft));
		System.out.println("Timer: Time set to " + minutes + " minutes.");
	}

	public void startTimer() {
		this.timer.start();
	}

	public void stopTimer() {
		this.timer.stop();
	}

}
