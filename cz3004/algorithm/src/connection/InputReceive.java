package connection;

import explore.Explore;
import explore.LeftWallHug;
import fastestPath.FastestPath;
import io.FileRW;
import model.Map;
import model.Robot;
import model.RobotData;

/*
 * ID:	0 - Android
 * 		1 - Arduino
 * 		2 - PC
 * 
 * Every command start with 2 IDs : Sender + Receiver		
 * 
 *
 * 1. Start command character received from Android: "02e"
 * 
 * 2. Command string received from RPI : "12position#|position#|" ...
 * 	 -> position: left sensors:  TL (top Left)  , ML (middle Left) , BL (bottom Left)
 * 				  front sensors: LF (left Front), MF (middle Front), RF (right Front)
 * 				  right sensors: TR (top Right) , MR (middle Right), BR (bottom Right) 
 * 
 * 3. Move commands to send to RPI :	FORWARD = "21w"
 * 										TURNLEFT = "21a"
 * 										BACKWARD = "21s"
 * 										TURNRIGHT = "21d"
 * 										STOP = "21t"
 * */
public class InputReceive implements InputListener {

	private boolean startSignal; // Signal to start executing
	public Explore explore;

	public InputReceive() {
		this.startSignal = false;
	}

	public boolean getStartSignal() {
		return this.startSignal;
	}

	// receive data and return string to send
	@Override
	public void onReceive(String s) {
		System.out.println("Communicator Module: Message received: " + s);
		String command = "";
		Map map;
		int[][] fastestPathMap;

		if (s.contains("cd"))
			s = "cd";
		else if (s.contains("z"))
			s = "z";

		switch (s) {
		case "e":
			this.startSignal = true;
			map = view.Main.getMap();
			this.explore = new LeftWallHug(map);
			map.discoverInitial();
			CommunicatorRpi.getInstance().getConn().send(1, "e");
			view.Main.getTimer().attachExploreAlgo(explore);
			view.Main.getTimer().startTimer();
			break;
		case "se":
			try {
				this.explore.setKeepExploring(false);
				this.explore.endExplore(true);
				CommunicatorRpi.getInstance().getConn().send(1, RobotData.STOP);
				view.Main.getTimer().stopTimer();
				this.startSignal = false;
			} catch (NullPointerException e1) {
				System.out.println("Communicator Module: Error: Exploration not started!");
			}
			break;
		case "cd":
			// System.out.println("Communicator Module: Robot Calibration
			// Complete!");
			this.explore.setPauseFlag(false);
			CommunicatorRpi.getInstance().getConn().send(1, this.explore.makeDecision());
			break;
		case "z":
			this.startSignal = false;
			try {
				Thread.sleep(1000); // Pause for 1000ms
			} catch (InterruptedException e1) {
				System.out.println("Communicator Module: Case 'z' sleep(1000) Interrupted: " + e1.getMessage());
			}
			map = view.Main.getMap();
			// Robot to Start Zone
			FastestPath currentToStartFastestPath = new FastestPath();
			map.removeTravelPath();
			map.getRobot().resetOldIJ();
			currentToStartFastestPath.FindFastestPath(map.getRobot().getI() - 1, map.getRobot().getJ() - 1,
					map.getStart_I() - 1, map.getStart_J() - 1);
			fastestPathMap = FileRW.loadMap("fastest_path.txt");
			map.showTravelPath(fastestPathMap);
			// Animate robot to start point
			map.getRobot().moveToPosition(map, fastestPathMap, map.getStart_I(), map.getStart_J(), true);
			try {
				Thread.sleep(7000); // Pause for 7000ms
			} catch (InterruptedException e1) {
				System.out.println("Communicator Module: Case 'z' sleep(7000) Interrupted: " + e1.getMessage());
			}
			// Get the robot to face South
			CommunicatorRpi.getInstance().getConn().send(1, explore.faceSouth());
			// Calibrate to prepare for Prepare Challenge
			CommunicatorRpi.getInstance().getConn().send(1, RobotData.PREPCHALLENGE);
			map.getRobot().setOrientation(1);
			break;
		case "y":
			CommunicatorRpi.getInstance().getConn().send(1, "y");
			// On complete, initiate to goal point
			map = view.Main.getMap();
			// Robot to Goal Zone
			FastestPath fastestPath = new FastestPath();
			map.removeTravelPath();
			map.getRobot().resetOldIJ();
			fastestPath.FindFastestPath(map.getStart_I() - 1, map.getStart_J() - 1, map.getGoal_I() - 1,
					map.getGoal_J() - 1);
			fastestPathMap = FileRW.loadMap("fastest_path.txt");
			map.showTravelPath(fastestPathMap);
			// Animate robot to Goal Zone
			map.getRobot().moveToPosition(map, fastestPathMap, map.getGoal_I(), map.getGoal_J(), false);
			break;
		default:
			if (this.startSignal) {
				try {
					if (s.substring(0, 2).equals("LF"))
						command = explore.startExploration(s);
					if (command != "")
						CommunicatorRpi.getInstance().getConn().send(1, command);
				} catch (Exception e) {
					System.out.println("Communicator Module Exception: " + e.getMessage());
				}
			}
			break;
		}
	}

	public void setStartSignal(boolean value) {
		this.startSignal = value;
	}
}
