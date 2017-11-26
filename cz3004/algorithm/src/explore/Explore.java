package explore;

import connection.CommunicatorRpi;
import io.FileRW;
import model.Map;
import model.MapCell;
import model.MapParam;
import model.Robot;
import model.RobotData;
import model.RobotSensor;

/**
 * @author Leroy
 * @author Nhat
 *
 */
public class Explore implements RobotData {
	protected static final int MOVESBEFORECALIBRATE = 6;
	protected Robot robot = null;
	protected Map map = null;
	protected boolean reachedGoal = false;
	protected int speed;
	private int time = 400;
	protected int percent = 100;
	protected int countMove = 0;
	protected int moveLimit = 400;
	protected boolean keepExploring = true;
	private boolean percentFlag = false;
	private boolean stopFlag = true;
	protected boolean interrupted;
	protected boolean pauseFlag = false;

	private String lastCommand;

	int moveSinceCalibrate = 0; // For checking how many moves since calibration

	// For real
	public Explore(Map coveredMap) {
		this.map = coveredMap;
		// Check if Robot already exists
		if (!map.robotAssigned()) {
			RobotSensor sensor = new RobotSensor(F_SHORTEST, F_LONGEST, L_SHORTEST, L_LONGEST, R_SHORTEST, R_LONGEST);
			Robot robot = new Robot(MapParam.NUM_ROWS - 3, 2, INITIAL_DIRECTION, sensor);
			map.addRobot(robot);
			map.updateRobotPosition();
		}
		this.robot = map.getRobot();
	}

	// For simulation
	public Explore(Map coveredMap, int speed) {
		this(coveredMap);
		this.speed = speed;
	}

	private double calcPriority(int direction) {
		int x = this.robot.getI();
		int y = this.robot.getJ();
		double priority = 0;
		switch (direction) {
		case 0:
			// North
			priority = map.getMapCell()[x - 2][y].getPriority() + map.getMapCell()[x - 2][y + 1].getPriority()
					+ map.getMapCell()[x - 2][y - 1].getPriority();
			break;
		case 1:
			// South
			priority = map.getMapCell()[x + 2][y].getPriority() + map.getMapCell()[x + 2][y - 1].getPriority()
					+ map.getMapCell()[x + 2][y + 1].getPriority();
			break;
		case 2:
			// West
			priority = map.getMapCell()[x][y - 2].getPriority() + map.getMapCell()[x + 1][y - 2].getPriority()
					+ map.getMapCell()[x - 1][y - 2].getPriority();
			break;
		case 3:
			// East
			priority = map.getMapCell()[x][y + 2].getPriority() + map.getMapCell()[x + 1][y + 2].getPriority()
					+ map.getMapCell()[x - 1][y + 2].getPriority();
			break;
		default:
			// System.out.println("Explore Algorithm Error: No robot orientation
			// given);");
			priority = 0;
			break;
		}
		return priority;
	}

	/**
	 * Check whether to calibrate the robot
	 * 
	 * @return String command to calibrate, if able
	 */
	protected String checkCalibrate() {
		String lastCommand = null;
		moveSinceCalibrate++;
		// Check if there is any wall to calibrate
		String calibrateMsg = robot.calibrate(map, moveSinceCalibrate, MOVESBEFORECALIBRATE);
		// System.out.println("Calibration Check: Direction to
		// Move:"+calibrateMsg);
		if (calibrateMsg != null) {
			// yes
			if (calibrateMsg != "")
				CommunicatorRpi.getInstance().getConn().send(1, calibrateMsg);
			moveSinceCalibrate = 0;
			pauseFlag = true;
			lastCommand = CALIBRATE;
			// System.out.println("Exploration Algorithm: Robot calibrating.");
			return lastCommand;
		}
		// Else, return null
		return lastCommand;
	}

	/**
	 * Checks if reached goal point. Checks if criteria hit. Checks if any flags
	 * triggered.
	 * 
	 * @return String command, if applicable
	 */
	protected String checkConditions() {
		String lastCommand = null;
		// Detect if the robot has reached goal
		if (this.robot.getI() == 2 && this.robot.getJ() == 14) {
			reachedGoal = true;
			// System.out.println("Reached goal point!");
		}

		// if the robot still haven't explored the map fully after the moveLimit
		// it will stop exploring and try to get back to Start
		if (keepExploring) {
			if (this.map.calPercent() >= percent || interrupted) {
				keepExploring = false;
			}
		}

		// Do nothing if paused
		if (pauseFlag) {
			lastCommand = "";
			return lastCommand;
		}

		// when the robot get back to start point and stop exploring
		// The STOP command will be sent
		if (!keepExploring) {
			// System.out.println("Exploration Algorithm: Ended due to flag");
			lastCommand = STOP;
			this.endExplore(true);
			return lastCommand;
		}
		return lastCommand;
	}

	protected void checkDeadEnd() {
		int x = this.robot.getI();
		int y = this.robot.getJ();
		switch (this.robot.getOrientation()) {
		case NORTH:
			setDeadEnd(x + 1, y - 1);
			setDeadEnd(x, y - 1);
			setDeadEnd(x - 1, y - 1);
			break;
		case SOUTH:
			setDeadEnd(x + 1, y + 1);
			setDeadEnd(x, y + 1);
			setDeadEnd(x - 1, y + 1);
			break;
		case WEST:
			setDeadEnd(x - 1, y - 1);
			setDeadEnd(x - 1, y);
			setDeadEnd(x - 1, y + 1);
			break;
		case EAST:
			setDeadEnd(x + 1, y - 1);
			setDeadEnd(x + 1, y);
			setDeadEnd(x + 1, y + 1);
			break;
		}
	}

	/**
	 * Updates priority and checks for dead ends
	 * 
	 * @param sensorData
	 *            String received from arduino
	 */
	protected void checkDeadEnd(String sensorData) {
		this.robot.getSensorDataReal(sensorData, this.map);

		// deal with dead end
		if (this.leftWall() && this.rightWall() && this.frontWall()) {
			switch (this.robot.getOrientation()) {
			case NORTH:
				setDeadEnd(robot.getI() - 1, robot.getJ() + 1);
				setDeadEnd(robot.getI() - 1, robot.getJ());
				setDeadEnd(robot.getI() - 1, robot.getJ() - 1);
				break;
			case SOUTH:
				setDeadEnd(robot.getI() + 1, robot.getJ() + 1);
				setDeadEnd(robot.getI() + 1, robot.getJ());
				setDeadEnd(robot.getI() + 1, robot.getJ() - 1);
				break;
			case WEST:
				setDeadEnd(robot.getI() - 1, robot.getJ() - 1);
				setDeadEnd(robot.getI(), robot.getJ() - 1);
				setDeadEnd(robot.getI() + 1, robot.getJ() - 1);
				break;
			case EAST:
				setDeadEnd(robot.getI() - 1, robot.getJ() + 1);
				setDeadEnd(robot.getI(), robot.getJ() + 1);
				setDeadEnd(robot.getI() + 1, robot.getJ() + 1);
				break;
			}
		}

		// deal with dead end
		if (this.leftWall() && this.rightWall() && this.frontWall()) {
			switch (this.robot.getOrientation()) {
			case NORTH:
				setDeadEnd(robot.getI() - 1, robot.getJ() + 1);
				setDeadEnd(robot.getI() - 1, robot.getJ());
				setDeadEnd(robot.getI() - 1, robot.getJ() - 1);
				break;
			case SOUTH:
				setDeadEnd(robot.getI() + 1, robot.getJ() + 1);
				setDeadEnd(robot.getI() + 1, robot.getJ());
				setDeadEnd(robot.getI() + 1, robot.getJ() - 1);
				break;
			case WEST:
				setDeadEnd(robot.getI() - 1, robot.getJ() - 1);
				setDeadEnd(robot.getI(), robot.getJ() - 1);
				setDeadEnd(robot.getI() + 1, robot.getJ() - 1);
				break;
			case EAST:
				setDeadEnd(robot.getI() - 1, robot.getJ() + 1);
				setDeadEnd(robot.getI(), robot.getJ() + 1);
				setDeadEnd(robot.getI() + 1, robot.getJ() + 1);
				break;
			}
		}
	}

	/**
	 * Is called when the Exploration ends.
	 * 
	 * Stores the explored map into a txt file. Stores the respective Map
	 * Descriptor into txt files Sends to Android the Map Descriptors
	 * 
	 * @param liveSimulation
	 *            True if it is the actual run
	 */
	public void endExplore(boolean liveSimulation) {
		// System.out.println("Exploration Aglorithm: Exploration complete!");
		String strA = this.map.getStringA();
		String strB = this.map.getStringB();
		FileRW.storeExploredMap(map.getMapCell(), "explored_map.txt");
		FileRW.storeStringText("stringA.txt", strA);
		FileRW.storeStringText("stringB.txt", strB);
		// Format for Android
		String androidMsg = this.robot.androidStringEncode("mdf_string " + strA + " " + strB, map);
		// System.out.println("Exploration Algorithm: Packing string message to
		// android: " + androidMsg);
		if (liveSimulation) {
			// System.out.println("Connection status: " +
			// CommunicatorRpi.getInstance().isConnected());
			if (CommunicatorRpi.getInstance().isConnected())
				CommunicatorRpi.getInstance().getConn().send(0, androidMsg);
			// else
			// System.out.println("Exploration Algorithm: Message not send: Not
			// connected.");
		} else {
			// System.out.println("Exploration Algorithm: Message not send: Only
			// a simulation.");
		}
	}

	/**
	 * Determines how to make the robot to face south
	 * 
	 * @return String command to Arduino
	 */
	public String faceSouth() {
		String command = "";
		switch (this.robot.getOrientation()) {
		case WEST:
			command = this.robot.turnLeft();
			break;
		case NORTH:
			command = this.robot.turnBack();
			break;
		case EAST:
			command = this.robot.turnLeft();
			break;
		default:
			// System.out.println("Face South Mechanism: Already facing South!
			// No action required.");
			break;
		}
		return command;
	}

	// looking for wall in front of the robot
	protected boolean frontWall() {
		int y = this.robot.getI();
		int x = this.robot.getJ();
		int direction = this.robot.getOrientation();
		MapCell newCell[][] = this.map.getMapCell();
		switch (direction) {
		case NORTH:
			if (newCell[y - 2][x].isObstacle() || newCell[y - 2][x - 1].isObstacle()
					|| newCell[y - 2][x + 1].isObstacle())
				return true;
			else
				return false;
		case SOUTH:
			if (newCell[y + 2][x].isObstacle() || newCell[y + 2][x - 1].isObstacle()
					|| newCell[y + 2][x + 1].isObstacle())
				return true;
			else
				return false;
		case EAST:
			if (newCell[y][x + 2].isObstacle() || newCell[y - 1][x + 2].isObstacle()
					|| newCell[y + 1][x + 2].isObstacle())
				return true;
			else
				return false;
		case WEST:
			if (newCell[y][x - 2].isObstacle() || newCell[y - 1][x - 2].isObstacle()
					|| newCell[y + 1][x - 2].isObstacle())
				return true;
			else
				return false;
		}
		return false;
	}

	public double[] getWeights() {
		double[] weights = new double[4];
		double total = 0;

		for (int i = 0; i < 4; i++) {
			weights[i] = 1;
		}

		for (int i = 1; i < this.robot.getI() - 1; i++) {
			for (int j = 1; j < MapParam.NUM_COLS - 1; j++) {
				if (map.getMapCell()[i][j].isDiscovered()) {
					weights[RobotData.NORTH] += 1;
				}
			}
		}

		for (int i = this.robot.getI() + 2; i < MapParam.NUM_ROWS - 1; i++) {
			for (int j = 1; j < MapParam.NUM_COLS; j++) {
				if (map.getMapCell()[i][j].isDiscovered()) {
					weights[RobotData.SOUTH] += 1;
				}
			}
		}

		for (int i = 1; i < MapParam.NUM_ROWS - 1; i++) {
			for (int j = this.robot.getJ() + 2; j < MapParam.NUM_COLS - 1; j++) {
				if (map.getMapCell()[i][j].isDiscovered()) {
					weights[RobotData.EAST] += 1;
				}
			}
		}

		for (int i = 1; i < MapParam.NUM_ROWS - 1; i++) {
			for (int j = 1; j < this.robot.getJ() - 1; j++) {
				if (map.getMapCell()[i][j].isDiscovered()) {
					weights[RobotData.WEST] += 1;
				}
			}
		}

		total = MapParam.NUM_ROWS * MapParam.NUM_COLS;
		weights[RobotData.NORTH] = (weights[RobotData.NORTH] + 1) / (total + 1);
		weights[RobotData.SOUTH] = (weights[RobotData.SOUTH] + 1) / (total + 1);
		weights[RobotData.EAST] = (weights[RobotData.EAST] + 1) / (total + 1);
		weights[RobotData.WEST] = (weights[RobotData.WEST] + 1) / (total + 1);

		return weights;
	}

	// looking for walls to the left of the robot
	protected boolean leftWall() {
		int y = this.robot.getI();
		int x = this.robot.getJ();
		int direction = this.robot.getOrientation();
		MapCell newCell[][] = this.map.getMapCell();
		switch (direction) {
		case NORTH:
			if (newCell[y][x - 2].isObstacle() || newCell[y - 1][x - 2].isObstacle()
					|| newCell[y + 1][x - 2].isObstacle())
				return true;
			else
				return false;
		case SOUTH:
			if (newCell[y][x + 2].isObstacle() || newCell[y - 1][x + 2].isObstacle()
					|| newCell[y + 1][x + 2].isObstacle())
				return true;
			else
				return false;
		case EAST:
			if (newCell[y - 2][x].isObstacle() || newCell[y - 2][x - 1].isObstacle()
					|| newCell[y - 2][x + 1].isObstacle())
				return true;
			else
				return false;
		case WEST:
			if (newCell[y + 2][x].isObstacle() || newCell[y + 2][x - 1].isObstacle()
					|| newCell[y + 2][x + 1].isObstacle())
				return true;
			else
				return false;
		}
		return false;
	}

	/**
	 * Makes decision as to where to move the robot towards to when exploring
	 * 
	 * @return String command to Arduino
	 */
	public String makeDecision() {
		// System.out.println("Robot Position: (x: "+this.robot.getJ()+",
		// y:"+this.robot.getI()+") i:"+this.robot.getI()+"
		// j:"+this.robot.getJ() + " Direction: " + robot.getOrientation());
		double northPriority, southPriority, westPriority, eastPriority, minpriority;
		double[] weights = getWeights();

		northPriority = calcPriority(0);
		southPriority = calcPriority(1);
		westPriority = calcPriority(2);
		eastPriority = calcPriority(3);

		double minPriority_preweighted = Math.min(Math.min(Math.min(northPriority, southPriority), westPriority),
				eastPriority);

		northPriority += Math.abs(northPriority) * weights[NORTH];

		southPriority += Math.abs(southPriority) * weights[SOUTH];

		eastPriority += Math.abs(eastPriority) * weights[EAST];

		westPriority += Math.abs(westPriority) * weights[WEST];

		minpriority = Math.min(Math.min(Math.min(northPriority, southPriority), westPriority), eastPriority);

		// System.out.println("N:" + northPriority + ",S:" + southPriority +
		// ",E:" + eastPriority + ",W:" + westPriority);
		// Move robot towards cell with lowest priority and set priority for
		// original robot position
		if (minPriority_preweighted >= 9999) {
			// Give up
			// System.out.println("Exploration Algorithm: Given up");
			lastCommand = RobotData.STOP;
			this.endExplore(true);
		} else if (minpriority > 0) {
			if (minpriority == northPriority) {
				switch (this.robot.getOrientation()) {
				case NORTH:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() + 2, robot.getJ() - 1);
					setPriority(robot.getI() + 2, robot.getJ() + 1);
					setPriority(robot.getI() + 2, robot.getJ());
					return lastCommand;
				case EAST:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case SOUTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				case WEST:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			} else if (minpriority == eastPriority) {
				switch (this.robot.getOrientation()) {
				case EAST:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() + 1, robot.getJ() - 2);
					setPriority(robot.getI() - 1, robot.getJ() - 2);
					setPriority(robot.getI(), robot.getJ() - 2);
					return lastCommand;
				case SOUTH:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case WEST:
					lastCommand = robot.turnRight();
					return lastCommand;
				case NORTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			} else if (minpriority == southPriority) {
				switch (this.robot.getOrientation()) {
				case SOUTH:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() - 2, robot.getJ() + 1);
					setPriority(robot.getI() - 2, robot.getJ() - 1);
					setPriority(robot.getI() - 2, robot.getJ());
					return lastCommand;
				case WEST:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case NORTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				case EAST:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			} else if (minpriority == westPriority) {
				switch (this.robot.getOrientation()) {
				case WEST:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() - 1, robot.getJ() + 2);
					setPriority(robot.getI() + 1, robot.getJ() + 2);
					setPriority(robot.getI(), robot.getJ() + 2);
					return lastCommand;
				case NORTH:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case EAST:
					lastCommand = robot.turnRight();
					return lastCommand;
				case SOUTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			}
		} else {
			if (minpriority == westPriority) {
				switch (this.robot.getOrientation()) {
				case WEST:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() - 1, robot.getJ() + 2);
					setPriority(robot.getI() + 1, robot.getJ() + 2);
					setPriority(robot.getI(), robot.getJ() + 2);
					return lastCommand;
				case NORTH:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case EAST:
					lastCommand = robot.turnRight();
					return lastCommand;
				case SOUTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			} else if (minpriority == southPriority) {
				switch (this.robot.getOrientation()) {
				case SOUTH:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() - 2, robot.getJ() + 1);
					setPriority(robot.getI() - 2, robot.getJ() - 1);
					setPriority(robot.getI() - 2, robot.getJ());
					return lastCommand;
				case WEST:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case NORTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				case EAST:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			} else if (minpriority == eastPriority) {
				switch (this.robot.getOrientation()) {
				case EAST:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() + 1, robot.getJ() - 2);
					setPriority(robot.getI() - 1, robot.getJ() - 2);
					setPriority(robot.getI(), robot.getJ() - 2);
					return lastCommand;
				case SOUTH:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case WEST:
					lastCommand = robot.turnRight();
					return lastCommand;
				case NORTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			} else if (minpriority == northPriority) {
				switch (this.robot.getOrientation()) {
				case NORTH:
					lastCommand = robot.moveFoward();
					setPriority(robot.getI() + 2, robot.getJ() + 1);
					setPriority(robot.getI() + 2, robot.getJ() - 1);
					setPriority(robot.getI() + 2, robot.getJ());
					return lastCommand;
				case EAST:
					lastCommand = robot.turnLeft();
					return lastCommand;
				case SOUTH:
					lastCommand = robot.turnRight();
					return lastCommand;
				case WEST:
					lastCommand = robot.turnRight();
					return lastCommand;
				}
			}
		}

		// default
		/*
		 * System.out.println("Exploration Algorithm: Ended due to default case"
		 * ); this.endExplore(true); lastCommand = STOP;
		 */
		return lastCommand;
	}

	// looking for walls to the right of the robot
	protected boolean rightWall() {
		int y = this.robot.getI();
		int x = this.robot.getJ();
		int direction = this.robot.getOrientation();
		MapCell newCell[][] = this.map.getMapCell();
		switch (direction) {
		case NORTH:
			if (newCell[y][x + 2].isObstacle() || newCell[y - 1][x + 2].isObstacle()
					|| newCell[y + 1][x + 2].isObstacle())
				return true;
			else
				return false;
		case SOUTH:
			if (newCell[y][x - 2].isObstacle() || newCell[y - 1][x - 2].isObstacle()
					|| newCell[y + 1][x - 2].isObstacle())
				return true;
			else
				return false;
		case EAST:
			if (newCell[y + 2][x].isObstacle() || newCell[y + 2][x - 1].isObstacle()
					|| newCell[y + 2][x + 1].isObstacle())
				return true;
			else
				return false;
		case WEST:
			if (newCell[y - 2][x].isObstacle() || newCell[y - 2][x - 1].isObstacle()
					|| newCell[y - 2][x + 1].isObstacle())
				return true;
			else
				return false;
		}
		return false;
	}

	protected void setDeadEnd(int x, int y) {
		map.getMapCell()[x][y].setPriority(map.getMapCell()[x][y].getPriority() + 100);
	}

	public void setKeepExploring(boolean value) {
		this.keepExploring = value;
	}

	public void setPauseFlag(boolean value) {
		this.pauseFlag = value;
	}

	/**
	 * Sets percent of the map to be covered by the robot before stopping the
	 * exploration
	 * 
	 * @param percent
	 *            percentage of map to cover
	 */
	public void setPercent(int percent) {
		this.percent = percent;
		this.percentFlag = true;
	}

	private void setPriority(int x, int y) {
		int a = map.getMapCell()[x + 1][y].getPriority() % 9980;
		int b = map.getMapCell()[x - 1][y].getPriority() % 9980;
		int c = map.getMapCell()[x][y + 1].getPriority() % 9980;
		int d = map.getMapCell()[x][y - 1].getPriority() % 9980;
		map.getMapCell()[x][y].setPriority(map.getMapCell()[x][y].getPriority() + 1
				+ (int) Math.sqrt((a * b + a * c + a * d + b * c + b * d + c * d) / 2));
	}

	/**
	 * Starts the simulated exploration of the robot
	 */
	public void startExploration() {
		double i = 0.0;
		interrupted = false;

		// Update robot position
		this.map.updateRobotPosition();

		// Discover initial position
		this.map.discoverInitial();

		// sense current position
		this.robot.getSensorData(this.map);
		do {
			i++;
			// System.out.println(i + " th move !");
			// System.out.println("current position of robot : " +
			// this.robot.getI() + " - " + this.robot.getJ()
			// + " direction " + this.robot.getOrientation());

			// decide on next move
			// System.out.println("left side got wall: " + this.leftWall());
			// System.out.println("front side got wall: " + this.frontWall());
			// System.out.println("right side got wall: " + this.rightWall());

			// Checks if there are dead ends and update priority accordingly
			if (this.leftWall() && this.rightWall() && this.frontWall()) {
				checkDeadEnd();
			}

			double northPriority, southPriority, westPriority, eastPriority, minpriority;
			double[] weights = getWeights();

			northPriority = calcPriority(0);
			southPriority = calcPriority(1);
			westPriority = calcPriority(2);
			eastPriority = calcPriority(3);

			double minPriority_preweighted;

			northPriority += Math.abs(northPriority) * weights[NORTH];

			southPriority += Math.abs(southPriority) * weights[SOUTH];

			eastPriority += Math.abs(eastPriority) * weights[EAST];

			westPriority += Math.abs(westPriority) * weights[WEST];

			// System.out.println(
			// "Min Priority : " + northPriority + " " + southPriority + " " +
			// eastPriority + " " + westPriority);
			minpriority = Math.min(Math.min(Math.min(northPriority, southPriority), westPriority), eastPriority);

			// System.out.println(
			// "N:" + northPriority + ",S:" + southPriority + ",E:" +
			// eastPriority + ",W:" + westPriority);
			// Move robot towards cell with lowest priority and set priority for
			// original robot position
			if (minpriority > 0) {
				if (minpriority == northPriority) {
					switch (this.robot.getOrientation()) {
					case NORTH:
						robot.moveFoward();
						setPriority(robot.getI() + 2, robot.getJ() - 1);
						setPriority(robot.getI() + 2, robot.getJ() + 1);
						setPriority(robot.getI() + 2, robot.getJ());
						break;
					case EAST:
						robot.turnLeft();
						break;
					case SOUTH:
						robot.turnLeft();
						break;
					case WEST:
						robot.turnRight();
						break;
					}
				} else if (minpriority == eastPriority) {
					switch (this.robot.getOrientation()) {
					case EAST:
						robot.moveFoward();
						setPriority(robot.getI() + 1, robot.getJ() - 2);
						setPriority(robot.getI() - 1, robot.getJ() - 2);
						setPriority(robot.getI(), robot.getJ() - 2);
						break;
					case SOUTH:
						robot.turnLeft();
						break;
					case WEST:
						robot.turnLeft();
						break;
					case NORTH:
						robot.turnRight();
						break;
					}
				} else if (minpriority == southPriority) {
					switch (this.robot.getOrientation()) {
					case SOUTH:
						robot.moveFoward();
						setPriority(robot.getI() - 2, robot.getJ() + 1);
						setPriority(robot.getI() - 2, robot.getJ() - 1);
						setPriority(robot.getI() - 2, robot.getJ());
						break;
					case WEST:
						robot.turnLeft();
						break;
					case NORTH:
						robot.turnLeft();
						break;
					case EAST:
						robot.turnRight();
						break;
					}
				} else if (minpriority == westPriority) {
					switch (this.robot.getOrientation()) {
					case WEST:
						robot.moveFoward();
						setPriority(robot.getI() - 1, robot.getJ() + 2);
						setPriority(robot.getI() + 1, robot.getJ() + 2);
						setPriority(robot.getI(), robot.getJ() + 2);
						break;
					case NORTH:
						robot.turnLeft();
						break;
					case EAST:
						robot.turnLeft();
						break;
					case SOUTH:
						robot.turnRight();
						break;
					}
				}
			} else {
				if (minpriority == westPriority) {
					switch (this.robot.getOrientation()) {
					case WEST:
						robot.moveFoward();
						setPriority(robot.getI() - 1, robot.getJ() + 2);
						setPriority(robot.getI() + 1, robot.getJ() + 2);
						setPriority(robot.getI(), robot.getJ() + 2);
						break;
					case NORTH:
						robot.turnLeft();
						break;
					case EAST:
						robot.turnLeft();
						break;
					case SOUTH:
						robot.turnRight();
						break;
					}
				} else if (minpriority == southPriority) {
					switch (this.robot.getOrientation()) {
					case SOUTH:
						robot.moveFoward();
						setPriority(robot.getI() - 2, robot.getJ() + 1);
						setPriority(robot.getI() - 2, robot.getJ() - 1);
						setPriority(robot.getI() - 2, robot.getJ());
						break;
					case WEST:
						robot.turnLeft();
						break;
					case NORTH:
						robot.turnLeft();
					case EAST:
						robot.turnRight();
						break;
					}
				} else if (minpriority == eastPriority) {
					switch (this.robot.getOrientation()) {
					case EAST:
						robot.moveFoward();
						setPriority(robot.getI() + 1, robot.getJ() - 2);
						setPriority(robot.getI() - 1, robot.getJ() - 2);
						setPriority(robot.getI(), robot.getJ() - 2);
						break;
					case SOUTH:
						robot.turnLeft();
						break;
					case WEST:
						robot.turnLeft();
						break;
					case NORTH:
						robot.turnRight();
						break;
					}
				} else if (minpriority == northPriority) {
					switch (this.robot.getOrientation()) {
					case NORTH:
						robot.moveFoward();
						setPriority(robot.getI() + 2, robot.getJ() + 1);
						setPriority(robot.getI() + 2, robot.getJ() - 1);
						setPriority(robot.getI() + 2, robot.getJ());
						break;
					case EAST:
						robot.turnLeft();
						break;
					case SOUTH:
						robot.turnLeft();
						break;
					case WEST:
						robot.turnRight();
						break;
					}
				}
			}

			this.map.updateRobotPosition();

			// sense current position
			this.robot.getSensorData(this.map);

			if (this.robot.getI() == 2 && this.robot.getJ() == 14) {
				reachedGoal = true;
				// System.out.println("reached goal point!");
			}

			// pause 1s after each move
			// according to the speed selected
			try {
				Thread.sleep(1000 / speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.map.updateRobotPosition();
			// test for percent coverage or timer
			if (percentFlag) {
				if (this.map.calPercent() >= percent) {
					interrupted = true;
				}
			}

			if (stopFlag) {
				if (this.map.calPercent() >= percent && reachedGoal == true || i > time) {
					interrupted = true;
					stopFlag = false;
				}
			}

			northPriority = calcPriority(0);
			southPriority = calcPriority(1);
			westPriority = calcPriority(2);
			eastPriority = calcPriority(3);

			minPriority_preweighted = Math.min(Math.min(Math.min(northPriority, southPriority), westPriority),
					eastPriority);

			if (minPriority_preweighted >= 9999) {
				interrupted = true;
			}

		} while ((!(this.robot.getI() == 19 && this.robot.getJ() == 2) || reachedGoal != true
				|| this.map.calPercent() < percent) && interrupted == false);// stop
																				// when
																				// robot
																				// traverse
																				// back
																				// to
																				// start
																				// after
																				// reaching
																				// goal

		// Exploration ended
		this.endExplore(false);
	}

	/**
	 * Receive sensor data then process and output command string
	 * 
	 * @param sensorData
	 *            string containing the output of the sensors
	 * @return String command of the Robot
	 */
	public String startExploration(String sensorData) {
		lastCommand = null;

		// +1 move made
		this.stepCountUp();

		// Update priority and dead ends
		this.checkDeadEnd(sensorData);

		// Check conditions to stop
		lastCommand = this.checkConditions();

		if (lastCommand == null) {
			// Calibrate if able or after n moves
			lastCommand = this.checkCalibrate();
		}

		if (lastCommand == null) {
			// Make decision as to where to move
			lastCommand = this.makeDecision();
		}

		return lastCommand;
	}

	/**
	 * Increases current move counter
	 */
	protected void stepCountUp() {
		this.countMove++;
		// System.out.println("Exploration Algorithm: Move #"+this.countMove);
	}

	public void timeup() {
		interrupted = true;
	}
}
