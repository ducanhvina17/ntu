package view;
/**
 * Main Application for View
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import connection.CommunicatorRpi;
import explore.Explore;
import explore.LeftWallHug;
import fastestPath.FastestPath;
import io.FileRW;
import model.Map;
import model.MapParam;
import model.Robot;
import model.RobotSensor;

/**
 * @author Leroy Lim
 *
 */
public class Main {
	private static JFrame frame;
	private static Map map;
	private static FastestPath fastestPath;

	/*
	 * Mode: 0 - Editing 1 - Exploring 2 - Fastest Path
	 */
	private static int mode;

	private static TimerPanel timerPanel;

	public static Map getMap() {
		return map;
	}

	public static int getMode() {
		return mode;
	}

	public static TimerPanel getTimer() {
		return timerPanel;
	}

	public static void main(String[] args) {
		// Start listening
		portListen();

		// Initiates the Interactive Map
		map = new Map();
		fastestPath = new FastestPath();

		// Setting up GUI objects
		frame = new JFrame("Algorithm Simulator");
		JPanel textPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel resultPanel = new JPanel();

		// Text UI
		textPanel.add(new JLabel("<html>" + "Instructions:<br/>" + "Click Explore to start Exploration." + "</html>"));

		// Set up RightPanel
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

		// Speed Combo Box
		SpeedPanel speedPanel = new SpeedPanel(5);

		// Timer Panel
		timerPanel = new TimerPanel();

		// Percentage Panel
		TextFieldPanel percentPanel = new TextFieldPanel("Coverage (%): ", 100);

		// Set Start and End Coordinates of Robot
		TextFieldPanel startJPanel = new TextFieldPanel("X: ", map.getStart_J());
		TextFieldPanel startIPanel = new TextFieldPanel("Y: ", map.getStart_I());

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 3));

		// Set time in timer
		JButton setTimeButton = new JButton("Set timer");
		setTimeButton.addActionListener(e -> {
			String time = JOptionPane.showInputDialog(new JFrame(),
					"Indicate how many minutes for the exploration to run", "5");
			try {
				timerPanel.setTimer(Integer.parseInt(time));
			} catch (NumberFormatException e1) {
				System.out.println("Input Error: NumberFormatException. No changes were made.");
			}
		});

		// Set Starting coordinates of Robot Button
		JButton setStartButton = new JButton("Set Starting Coordinates");
		setStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Check if Robot already exists
				if (!map.robotAssigned()) {
					RobotSensor sensor = new RobotSensor(model.RobotData.F_SHORTEST, model.RobotData.F_LONGEST,
							model.RobotData.L_SHORTEST, model.RobotData.L_LONGEST, model.RobotData.R_SHORTEST,
							model.RobotData.R_LONGEST);
					Robot robot = new Robot(MapParam.NUM_ROWS - 3, 2, model.RobotData.INITIAL_DIRECTION, sensor);
					map.addRobot(robot);
					map.updateRobotPosition();
				}
				map.getRobot().setI(startIPanel.getTextValue());
				map.getRobot().setJ(startJPanel.getTextValue());
				map.updateRobotPosition();
				map.getRobot().resetOldIJ();
			}
		});

		// Save Map Button
		JButton saveMapButton = new JButton("Save Map");
		saveMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileRW.storeExploredMap(map.getMapCell(), "./sample_map/stored_map.txt");
				System.out.println("Current map stored");
			}
		});

		// Load Map Button
		JButton loadMapButton = new JButton("Load Map");
		loadMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				map.ResetMap();
				JFileChooser fc = new JFileChooser(new File("./sample_map/"));
				int returnVal = fc.showDialog(loadMapButton, "Open Map");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileName = "./sample_map/" + fc.getSelectedFile().getName();
					System.out.println(fileName);
					FileRW.loadMap(map, fileName);
					System.out.println("Sample map loaded");
				}
			}
		});

		// Explore button
		JButton exploreButton = new JButton("Explore");
		exploreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mode = 1;
				Explore exploreAlgo = new LeftWallHug(map, speedPanel.getSpeed());
				map.setRealRun(false); // Is simulated
				Thread expThread = new Thread(new Runnable() {
					@Override
					public void run() {
						// Set % of coverage
						exploreAlgo.setPercent(percentPanel.getTextValue());
						// Start timer
						timerPanel.attachExploreAlgo(exploreAlgo);
						timerPanel.startTimer();
						// Start exploration
						exploreAlgo.startExploration();
						// Robot to Start Zone
						int[][] fastestPathMap;
						map.removeTravelPath();
						map.getRobot().resetOldIJ();
						fastestPath.FindFastestPath(map.getRobot().getI() - 1, map.getRobot().getJ() - 1,
								map.getStart_I() - 1, map.getStart_J() - 1);
						fastestPathMap = FileRW.loadMap("fastest_path.txt");
						map.showTravelPath(fastestPathMap);
						// Animate robot to start point
						map.getRobot().moveToPosition(map, fastestPathMap, map.getStart_I(), map.getStart_J(),
								speedPanel.getSpeed(), "", true);
						timerPanel.stopTimer();
					}
				});
				expThread.setPriority(Thread.NORM_PRIORITY);
				expThread.start();
			}

		});

		// Fastest Path button
		JButton fastestPathButton = new JButton("Fastest Path");
		fastestPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FastestPath fastestPath = new FastestPath();
				// Note: -1 to remove walls
				Thread fastestPathThread = new Thread(new Runnable() {
					@Override
					public void run() {
						int[][] fastestPathMap;
						// Robot to Goal Zone
						map.removeTravelPath();
						map.getRobot().resetOldIJ();
						fastestPath.FindFastestPath(map.getStart_I() - 1, map.getStart_J() - 1, map.getGoal_I() - 1,
								map.getGoal_J() - 1);
						fastestPathMap = FileRW.loadMap("fastest_path.txt");
						map.showTravelPath(fastestPathMap);
						// Animate robot to Goal Zone
						map.getRobot().moveToPosition(map, fastestPathMap, map.getGoal_I(), map.getGoal_J(),
								speedPanel.getSpeed(), "", false);
					}
				});
				fastestPathThread.setPriority(Thread.NORM_PRIORITY);
				fastestPathThread.start();
			}

		});

		// Print String A button
		JButton stringAButton = new JButton("Get String A");
		stringAButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultPanel.removeAll();
				resultPanel.add(new JLabel("<html>String A:<br />" + map.getStringA() + "</html>"));
				System.out.println("String A: " + map.getStringA());
				resultPanel.revalidate();
				resultPanel.repaint();
				frame.pack();
			}
		});

		// Print String B button
		JButton stringBButton = new JButton("Get String B");
		stringBButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultPanel.removeAll();
				resultPanel.add(new JLabel("<html>String B:<br />" + map.getStringB() + "</html>"));
				System.out.println("String B: " + map.getStringB());
				resultPanel.revalidate();
				resultPanel.repaint();
				frame.pack();
			}
		});

		// Reset Button
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> {
			mode = 0; // Return to edit mode
			timerPanel.resetTimer(); // Reset timer
			map.ResetMap(); // Reset map
		});

		// Add buttons to button Panel
		buttonPanel.add(setTimeButton);
		buttonPanel.add(setStartButton);
		buttonPanel.add(saveMapButton);
		buttonPanel.add(loadMapButton);
		buttonPanel.add(exploreButton);
		buttonPanel.add(fastestPathButton);
		buttonPanel.add(stringAButton);
		buttonPanel.add(stringBButton);
		buttonPanel.add(resetButton);

		// Add buttons to right Panel
		rightPanel.add(speedPanel, BorderLayout.NORTH);
		rightPanel.add(timerPanel);
		rightPanel.add(percentPanel);
		rightPanel.add(startJPanel);
		rightPanel.add(startIPanel);
		rightPanel.add(buttonPanel);

		// Append GUI objects to JFrame
		frame.add(map, BorderLayout.CENTER);
		frame.add(rightPanel, BorderLayout.EAST);
		frame.add(textPanel, BorderLayout.SOUTH);
		frame.add(resultPanel, BorderLayout.SOUTH);

		// Set GUI to visible
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private static void portListen() {
		Thread commRpiThread = null;
		try {
			commRpiThread = new Thread(new Runnable() {
				@Override
				public void run() {
					CommunicatorRpi.getInstance().getConn().receive();
				}
			});
			commRpiThread.start();
		} catch (Exception e) {
			System.out.println("Main Console: Communication failed with message: " + e.getMessage());
			commRpiThread.start();
		}
	}

	public static void setMode(int mode) {
		Main.mode = mode;
	}
}
