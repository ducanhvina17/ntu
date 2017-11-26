/**
 * This is the Class describing the Robot
 */
package model;

import java.util.Arrays;

import connection.CommunicatorRpi;

/**
 * Note: i is y-coordinate j is x-coordinate
 * 
 * @author Leroy Lim
 * @author Nhat
 *
 */
public class Robot implements RobotData, MapParam {

	private int i;
	private int j;

	/*
	 * orientation 0 - Left 1 - Up 2 - Right 3 - Down
	 */
	private int orientation;

	private int oldI;
	private int oldJ;

	private int oldOrientation;

	// sensors of robot
	private RobotSensor sensor;

	// the memory of robot
	private boolean previousRightBorder = false;
	private boolean previousLeftWall = false;

	/**
	 * Constructor
	 * 
	 * @param i
	 *            y-coordinate of the starting position of the Robot's center
	 * @param j
	 *            x-coordinate of the starting position of the Robot's center
	 * @param initialOrientation
	 *            Initial Orientation of the Robot (0 - Left, 1 - Up, 2 - Right,
	 *            3 - Down)
	 */
	public Robot(int i, int j, int initialOrientation, RobotSensor sensor) {
		this.i = i;
		this.j = j;
		this.oldI = i;
		this.oldJ = j;
		this.orientation = initialOrientation;
		this.oldOrientation = initialOrientation;
		this.sensor = sensor;

		// Enable sensors
		this.addLeftFrontSensor();
		this.addMiddleFrontSensor();
		this.addRightFrontSensor();
		this.addTopLeftSensor();
		this.addTopRightSensor();

	}

	public void addBottomLeftSensor() {
		sensor.setBottomLeftSensor(true);
	}

	public void addBottomRightSensor() {
		sensor.setBottomRightSensor(true);
	}

	public void addLeftFrontSensor() {
		sensor.setLeftFrontSensor(true);
	}

	public void addMiddleFrontSensor() {
		sensor.setMiddleFrontSensor(true);
	}

	public void addMiddleLeftSensor() {
		sensor.setMiddleLeftSensor(true);
	}

	public void addMiddleRightSensor() {
		sensor.setMiddleRightSensor(true);
	}

	public void addRightFrontSensor() {
		sensor.setRightFrontSensor(true);
	}

	public void addTopLeftSensor() {
		sensor.setTopLeftSensor(true);
	}

	public void addTopRightSensor() {
		sensor.setTopRightSensor(true);
	}

	/**
	 * Formats the status of the robot into the string format to be sent to
	 * Android
	 * 
	 * @param status
	 *            Current status of the robot
	 * @return formatted string to be sent to Android
	 */
	public String androidStringEncode(String status, Map map) {
		return map.getStringA() + " " + map.getStringB() + " " + (map.getAndroidX(this.getJ()) - 1) + " "
				+ (map.getAndroidY(this.getI()) - 1) + " " + this.getOrientation() + " " + status;
	}

	public String backToStartEncoder(String msg) {
		// String[] tmp = msg.split("(?!^)");
		msg = "+\n21" + msg;
		// for (String str : tmp) {
		// msg = msg + str + "\n" + "21";
		// }
		return msg;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public String calibrate(Map map, int currentMove, int limit) {
		String msg = null;
		MapCell[][] mapCell = map.getMapCell();
		int directionToMove;
		int[] dirList;

		// Check only if front has wall
		if (currentMove <= limit) {
			switch (this.orientation) {
			case NORTH:
				dirList = new int[] { NORTH };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			case SOUTH:
				dirList = new int[] { SOUTH };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			case EAST:
				dirList = new int[] { EAST };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			case WEST:
				dirList = new int[] { WEST };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			default:
				msg = null;
			}
		}

		else {
			switch (this.orientation) {
			case NORTH:
				dirList = new int[] { NORTH, EAST, WEST };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			case SOUTH:
				dirList = new int[] { SOUTH, EAST, WEST };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			case EAST:
				dirList = new int[] { EAST, NORTH, SOUTH };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			case WEST:
				dirList = new int[] { WEST, NORTH, SOUTH };
				for (int dir : dirList) {
					directionToMove = this.checkWall(dir, mapCell);
					if (directionToMove != -1) {
						msg = this.decideMovement_FP(directionToMove, true);
						break;
					}
				}
				break;
			default:
				msg = null;
			}

		}
		return msg;
	}

	/**
	 * Checks if the robot is facing a wall and return the direction if true
	 * 
	 * @param direction
	 * @param mapCell
	 * @return
	 */
	private int checkWall(int direction, MapCell[][] mapCell) {
		int y = this.i;
		int x = this.j;
		switch (direction) {
		case NORTH:
			for (int i = -1; i < 2; i++) {
				if (!(mapCell[y - 2][x + i].isObstacle()))
					return -1;
			}
			return NORTH;
		case SOUTH:
			for (int i = -1; i < 2; i++) {
				if (!(mapCell[y + 2][x + i].isObstacle()))
					return -1;
			}
			return SOUTH;
		case EAST:
			for (int i = -1; i < 2; i++) {
				if (!(mapCell[y + i][x + 2].isObstacle()))
					return -1;
			}
			return EAST;
		case WEST:
			for (int i = -1; i < 2; i++) {
				if (!(mapCell[y + i][x - 2].isObstacle()))
					return -1;
			}
			return WEST;
		default:
			return -1;
		}
	}

	/**
	 * Checks if there any corner to calibrate. As per requested by Gerrard
	 * 
	 * @param map
	 * @return
	 */
	public String cornerCalibrate(Map map) {
		String msg = null;
		MapCell[][] mapCell = map.getMapCell();
		int directionToMove;
		int wallCounter = 0;
		int[] dirList;
		int[] toMoveList = new int[3];

		// Get which walls are available to code
		switch (this.orientation) {
		case NORTH:
			dirList = new int[] { NORTH, EAST, WEST };
			for (int dir : dirList) {
				directionToMove = this.checkWall(dir, mapCell);
				if (directionToMove != -1) {
					toMoveList[wallCounter] = dir;
					wallCounter++;
				}
			}
			break;
		case SOUTH:
			dirList = new int[] { SOUTH, EAST, WEST };
			for (int dir : dirList) {
				directionToMove = this.checkWall(dir, mapCell);
				if (directionToMove != -1) {
					toMoveList[wallCounter] = dir;
					wallCounter++;
				}
			}
			break;
		case EAST:
			dirList = new int[] { EAST, NORTH, SOUTH };
			for (int dir : dirList) {
				directionToMove = this.checkWall(dir, mapCell);
				if (directionToMove != -1) {
					toMoveList[wallCounter] = dir;
					wallCounter++;
				}
			}
			break;
		case WEST:
			dirList = new int[] { WEST, NORTH, SOUTH };
			for (int dir : dirList) {
				directionToMove = this.checkWall(dir, mapCell);
				if (directionToMove != -1) {
					toMoveList[wallCounter] = dir;
					wallCounter++;
				}
			}
			break;
		default:
			msg = null;
		}

		// If corners available, calibrate from in front first
		if (wallCounter >= 2) {
			for (int dir : toMoveList) {
				msg = this.decideMovement_FP(dir, true);
				if (msg.equals("")) {
					CommunicatorRpi.getInstance().getConn().send(1, CORNER_CALIBRATE);
				} else {
					CommunicatorRpi.getInstance().getConn().send(1, msg);
					break;
				}
			}
			// turns the robot out to continue movement
			msg = this.returnFromCalibration(msg);
		}
		return msg;
	}

	/**
	 * Decides what movements to make based on current orientation
	 * 
	 * This is based on the formula current orientation - direction to move
	 * towards
	 * <ul>
	 * <li>Move forward if result is 0</li>
	 * <li>Turn left if result is 1 or -3</li>
	 * <li>Turn right if result is -1 or 3</li>
	 * <li>180 turn if result is -2 or 2</li>
	 * </ul>
	 * Of the two possibilities for deciding, the former is the more probable
	 * one.
	 * 
	 * 
	 * Note: Rotates the robot
	 * 
	 * @param directionToMove
	 *            the direction to move towards
	 * @return String command to Arduino
	 */
	private String decideMovement_FP(int directionToMove, boolean isCalibrate) {
		String msg = "";
		switch (this.orientation - directionToMove) {
		case 0:
			// Same direction, proceed forward
			if (!isCalibrate)
				msg = this.moveFoward();
			break;
		case 1:
		case -3:
			if (isCalibrate)
				msg = this.turnLeftCalibrate();
			else
				msg = this.turnLeft();
			break;
		case 2:
		case -2:
			msg = this.turnRight();
			msg += this.turnRight();
			break;
		case -1:
		case 3:
			if (isCalibrate)
				msg = this.turnRightCalibrate();
			else
				msg = this.turnRight();
			break;
		default:
			System.out.println("Robot Movement Error: No direction given to head towards!");
			break;
		}
		return msg;
	}

	// Decode data and return coordinates string
	public void decodeFrontSensorData(Map map, int offset, int sensorValue) {
		if (sensorValue >= sensor.getSensorFLongest()) {
			int i;
			boolean crashed = false;
			for (i = sensor.getSensorFShortest(); i < sensor.getSensorFLongest(); i++) {
				crashed = false;
				switch (this.orientation) {
				case NORTH:
					if (this.i - 2 - i >= 0) {
						map.uncoverACell(this.i - 2 - i, this.j + offset);
					} else
						crashed = true;
					break;
				case SOUTH:
					if (this.i + 2 + i <= NUM_ROWS - 1) {
						map.uncoverACell(this.i + 2 + i, this.j - offset);
					} else
						crashed = true;
					break;
				case EAST:
					if (this.j + 2 + i <= NUM_COLS - 1) {
						map.uncoverACell(this.i + offset, this.j + 2 + i);
					} else
						crashed = true;
					break;
				case WEST:
					if (this.j - 2 - i >= 0) {
						map.uncoverACell(this.i - offset, this.j - 2 - i);
					} else
						crashed = true;
					break;
				}
				if (crashed) {
					break;
				}
			}
		} else {
			switch (this.orientation) {
			case NORTH:
				if (this.i - 2 - sensorValue >= 0) {
					map.getMapCell()[this.i - 2 - sensorValue][this.j + offset].discover();
					map.getMapCell()[this.i - 2 - sensorValue][this.j + offset].setObstacle();
					for (int i = this.i - 2; i > this.i - 2 - sensorValue; i--) {
						map.uncoverACell(i, this.j + offset);
					}
				}
				break;
			case SOUTH:
				if (this.i + 2 + sensorValue <= NUM_ROWS - 1) {
					map.getMapCell()[this.i + 2 + sensorValue][this.j - offset].discover();
					map.getMapCell()[this.i + 2 + sensorValue][this.j - offset].setObstacle();
					for (int i = this.i + 2; i < this.i + 2 + sensorValue; i++) {
						map.uncoverACell(i, this.j - offset);
					}
				}
				break;
			case EAST:
				if (this.j + 2 + sensorValue <= NUM_COLS - 1) {
					map.getMapCell()[this.i + offset][this.j + 2 + sensorValue].discover();
					map.getMapCell()[this.i + offset][this.j + 2 + sensorValue].setObstacle();
					for (int j = this.j + 2; j < this.j + 2 + sensorValue; j++) {
						map.uncoverACell(this.i + offset, j);
					}
				}
				break;
			case WEST:
				if (this.j - 2 - sensorValue >= 0) {
					map.getMapCell()[this.i - offset][this.j - 2 - sensorValue].discover();
					map.getMapCell()[this.i - offset][this.j - 2 - sensorValue].setObstacle();
					for (int j = this.j - 2; j > this.j - 2 - sensorValue; j--) {
						map.uncoverACell(this.i - offset, j);
					}
				}
				break;
			}
		}
	}

	public void decodeLeftSensorData(Map map, int offset, int sensorValue) {
		if (sensorValue >= sensor.getSensorLLongest()) {
			int i;
			boolean crashed = false;
			for (i = sensor.getSensorLShortest(); i < sensor.getSensorLLongest(); i++) {
				crashed = false;
				switch (this.orientation) {
				case NORTH:
					if (this.j - 2 - i >= 0) {
						map.uncoverACell(this.i + offset, this.j - 2 - i);
					} else
						crashed = true;
					break;
				case SOUTH:
					if (this.j + 2 + i <= NUM_COLS - 1) {
						map.uncoverACell(this.i - offset, this.j + 2 + i);
					} else
						crashed = true;
					break;
				case EAST:
					if (this.i - 2 - i >= 0) {
						map.uncoverACell(this.i - 2 - i, this.j - offset);
					} else
						crashed = true;
					break;
				case WEST:
					if (this.i + 2 + i <= NUM_ROWS - 1) {
						map.uncoverACell(this.i + 2 + i, this.j + offset);
					} else
						crashed = true;
					break;
				}
				if (crashed) {
					break;
				}
			}
		} else {
			switch (this.orientation) {
			case NORTH:
				if (this.j - 2 - sensorValue >= 0) {
					map.getMapCell()[this.i + offset][this.j - 2 - sensorValue].discover();
					map.getMapCell()[this.i + offset][this.j - 2 - sensorValue].setObstacle();
					for (int j = this.j - 2; j > this.j - 2 - sensorValue; j--) {
						map.uncoverACell(this.i + offset, j);
					}
				}
				break;
			case SOUTH:
				if (this.j + 2 + sensorValue <= NUM_COLS - 1) {
					map.getMapCell()[this.i - offset][this.j + 2 + sensorValue].discover();
					map.getMapCell()[this.i - offset][this.j + 2 + sensorValue].setObstacle();
					for (int j = this.j + 2; j < this.j + 2 + sensorValue; j++) {
						map.uncoverACell(this.i - offset, j);
					}
				}
				break;
			case EAST:
				if (this.i - 2 - sensorValue >= 0) {
					map.getMapCell()[this.i - 2 - sensorValue][this.j - offset].discover();
					map.getMapCell()[this.i - 2 - sensorValue][this.j - offset].setObstacle();
					for (int i = this.i - 2; i > this.i - 2 - sensorValue; i--) {
						map.uncoverACell(i, this.j - offset);
					}
				}
				break;
			case WEST:
				if (this.i + 2 + sensorValue <= NUM_ROWS - 1) {
					map.getMapCell()[this.i + 2 + sensorValue][this.j + offset].discover();
					map.getMapCell()[this.i + 2 + sensorValue][this.j + offset].setObstacle();
					for (int i = this.i + 2; i < this.i + 2 + sensorValue; i++) {
						map.uncoverACell(i, this.j + offset);
					}
				}
				break;
			}
		}
	}

	public void decodeRightSensorData(Map map, int offset, int sensorValue) {
		if (sensorValue >= sensor.getSensorRLongest()) {
			int i;
			boolean crashed = false;
			for (i = sensor.getSensorRShortest(); i < sensor.getSensorRLongest(); i++) {
				crashed = false;
				switch (this.orientation) {
				case NORTH:
					if (this.j + 2 + i <= NUM_COLS - 1) {
						map.uncoverACell(this.i + offset, this.j + 2 + i);
					} else
						crashed = true;
					break;
				case SOUTH:
					if (this.j - 2 - i >= 0) {
						map.uncoverACell(this.i - offset, this.j - 2 - i);
					} else
						crashed = true;
					break;
				case EAST:
					if (this.i + 2 + i <= NUM_ROWS - 1) {
						map.uncoverACell(this.i + 2 + i, this.j - offset);
					} else
						crashed = true;
					break;
				case WEST:
					if (this.i - 2 - i >= 0) {
						map.uncoverACell(this.i - 2 - i, this.j + offset);
					} else
						crashed = true;
					break;
				}
				if (crashed) {
					break;
				}
			}
		} else {
			switch (this.orientation) {
			case NORTH:
				if (this.j + 2 + sensorValue <= NUM_COLS - 1) {
					map.getMapCell()[this.i + offset][this.j + 2 + sensorValue].discover();
					map.getMapCell()[this.i + offset][this.j + 2 + sensorValue].setObstacle();
					for (int j = this.j + 2; j < this.j + 2 + sensorValue; j++) {
						map.uncoverACell(this.i + offset, j);
					}
				}
				break;
			case SOUTH:
				if (this.j - 2 - sensorValue >= 0) {
					map.getMapCell()[this.i - offset][this.j - 2 - sensorValue].discover();
					map.getMapCell()[this.i - offset][this.j - 2 - sensorValue].setObstacle();
					for (int j = this.j - 2; j > this.j - 2 - sensorValue; j--) {
						map.uncoverACell(this.i - offset, j);
					}
				}
				break;
			case EAST:
				if (this.i + 2 + sensorValue <= NUM_ROWS - 1) {
					map.getMapCell()[this.i + 2 + sensorValue][this.j - offset].discover();
					map.getMapCell()[this.i + 2 + sensorValue][this.j - offset].setObstacle();
					for (int i = this.i + 2; i < this.i + 2 + sensorValue; i++) {
						map.uncoverACell(i, this.j - offset);
					}
				}
				break;
			case WEST:
				if (this.i - 2 - sensorValue >= 0) {
					map.getMapCell()[this.i - 2 - sensorValue][this.j + offset].discover();
					map.getMapCell()[this.i - 2 - sensorValue][this.j + offset].setObstacle();
					for (int i = this.i - 2; i > this.i - 2 - sensorValue; i--) {
						map.uncoverACell(i, this.j + offset);
					}
				}
				break;
			}
		}
	}

	/**
	 * Encodes the movement output from moveToPosition() for Arduino
	 * 
	 * @param msg
	 *            original sequence of movements for Arduino
	 * @return string command for Arduino
	 */
	public String fastestPathEncoder(String msg) {
		int counter = 0;
		// msg = msg.replace("21", "").replace("|", "");
		for (int i = 0; i < msg.length(); i++) {
			// Add counter if is w
			if (msg.substring(i, i + 1).equals("w")) {
				counter++;

				// If at end of string
				if (i == msg.length() - 1 && counter != 0) {
					msg = msg.substring(0, i - counter + 1) + counter;
					// Reset the counter
					// counter = 0;
					break;
				}
			}

			else if (counter == 0)
				continue;
			else {
				msg = msg.substring(0, i - counter) + counter + msg.substring(i, msg.length());
				// Reset the counter
				// counter = 0;
				msg = fastestPathEncoder(msg);
				break;
			}
		}
		// Check if null
		if (msg != null)
			msg += "+";
		else
			msg = "+";
		// System.out.println("Fastest Path Encoder: Working string:"+ msg);
		return msg;
	}

	// Utility function to get front sensor data
	// offset is based on north direction, modify for others
	public void getFrontSensorData(Map map, int offset) {
		MapCell uncovered = null;
		int i;
		boolean crashed = false;
		for (i = sensor.getSensorFShortest(); i < sensor.getSensorFLongest(); i++) {
			crashed = false;
			switch (this.orientation) {
			case NORTH:
				if (this.i - 2 - i >= 0) {
					uncovered = map.uncoverACell(this.i - 2 - i, this.j + offset);
				} else
					crashed = true;
				break;
			case SOUTH:
				if (this.i + 2 + i <= NUM_ROWS - 1) {
					uncovered = map.uncoverACell(this.i + 2 + i, this.j - offset);
				} else
					crashed = true;
				break;
			case EAST:
				if (this.j + 2 + i <= NUM_COLS - 1) {
					uncovered = map.uncoverACell(this.i + offset, this.j + 2 + i);
				} else
					crashed = true;
				break;
			case WEST:
				if (this.j - 2 - i >= 0) {
					uncovered = map.uncoverACell(this.i - offset, this.j - 2 - i);
				} else
					crashed = true;
				break;
			}
			if (crashed || uncovered.isObstacle()) {
				break;
			}
		}
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	// Utility function to get left sensor data
	// offset is based on north direction, modify for others
	public void getLeftSensorData(Map map, int offset) {
		MapCell uncovered = null;
		int i;
		boolean crashed = false;
		for (i = sensor.getSensorLShortest(); i < sensor.getSensorLLongest(); i++) {
			crashed = false;
			switch (this.orientation) {
			case NORTH:
				if (this.j - 2 - i >= 0)
					uncovered = map.uncoverACell(this.i + offset, this.j - 2 - i);
				else
					crashed = true;
				break;
			case SOUTH:
				if (this.j + 2 + i <= NUM_COLS - 1)
					uncovered = map.uncoverACell(this.i - offset, this.j + 2 + i);
				else
					crashed = true;
				break;
			case EAST:
				if (this.i - 2 - i >= 0)
					uncovered = map.uncoverACell(this.i - 2 - i, this.j - offset);
				else
					crashed = true;
				break;
			case WEST:
				if (this.i + 2 + i <= NUM_ROWS - 1)
					uncovered = map.uncoverACell(this.i + 2 + i, this.j + offset);
				else
					crashed = true;
				break;
			}
			if (crashed || uncovered.isObstacle())
				break;
		}
	}

	public int getOldI() {
		return this.oldI;
	}

	public int getOldJ() {
		return this.oldJ;
	}

	public int getOldOrientation() {
		return this.oldOrientation;
	}

	public int getOrientation() {
		return orientation;
	}

	public boolean getPreviousLeftWall() {
		return previousLeftWall;
	}

	public boolean getPreviousRightBorder() {
		return previousRightBorder;
	}

	// Utility function to get right sensor data
	// offset is based on north direction, modify for others
	public void getRightSensorData(Map map, int offset) {
		MapCell uncovered = null;
		int i;
		boolean crashed = false;
		for (i = sensor.getSensorRShortest(); i < sensor.getSensorRLongest(); i++) {
			crashed = false;
			switch (this.orientation) {
			case NORTH:
				if (this.j + 2 + i <= NUM_COLS - 1)
					uncovered = map.uncoverACell(this.i + offset, this.j + 2 + i);
				else
					crashed = true;
				break;
			case SOUTH:
				if (this.j - 2 - i >= 0)
					uncovered = map.uncoverACell(this.i - offset, this.j - 2 - i);
				else
					crashed = true;
				break;
			case EAST:
				if (this.i + 2 + i <= NUM_ROWS - 1)
					uncovered = map.uncoverACell(this.i + 2 + i, this.j - offset);
				else
					crashed = true;
				break;
			case WEST:
				if (this.i - 2 - i >= 0)
					uncovered = map.uncoverACell(this.i - 2 - i, this.j + offset);
				else
					crashed = true;
				break;
			}
			if (crashed || uncovered.isObstacle())
				break;
		}
	}

	// method for a full sensor (simulation)
	public void getSensorData(Map map) {
		if (sensor.hasLeftFrontSensor()) {
			getFrontSensorData(map, -1);
		}
		if (sensor.hasMiddleFrontSensor()) {
			getFrontSensorData(map, 0);
		}
		if (sensor.hasRightFrontSensor()) {
			getFrontSensorData(map, 1);
		}
		if (sensor.hasTopLeftSensor()) {
			getLeftSensorData(map, -1);
		}
		if (sensor.hasMiddleLeftSensor()) {
			getLeftSensorData(map, 0);
		}
		if (sensor.hasBottomLeftSensor()) {
			getLeftSensorData(map, 1);
		}
		if (sensor.hasTopRightSensor()) {
			getRightSensorData(map, -1);
		}
		if (sensor.hasMiddleRightSensor()) {
			getRightSensorData(map, 0);
		}
		if (sensor.hasBottomRightSensor()) {
			getRightSensorData(map, 1);
		}
		map.updateRobotPosition();
	}

	// method for a full sensor (real robot)
	// sensor data (distance) has format "L0|L1|L2|F0|F1|F2|R0|R1|R2"
	public void getSensorDataReal(String sensorData, Map map) {
		int[] sensorValue = new int[9];
		Arrays.fill(sensorValue, 1000);
		String[] sensorDataArr = sensorData.split("\\|", -1);
		for (int i = 0; i < sensorDataArr.length - 1; i++) {
			// System.out.println("Sensor Array: Substring: " +
			// sensorDataArr[i]);
			String position = sensorDataArr[i].substring(0, 2);
			String strDistance = sensorDataArr[i].substring(2);
			int distance;
			// If value too big
			if (strDistance.equals("Inf"))
				distance = 1000;
			else {
				try {
					distance = Integer.parseInt(strDistance);
				} catch (Exception e) {
					distance = 1000;
				}
			}
			int value;
			if (-4 <= distance && distance <= 5) {
				value = 0;
			} else if (6 <= distance && distance <= 15) {
				value = 1;
			} else {
				value = 1000;
			}
			switch (position) {
			case "TL":
				sensorValue[0] = value;
				break;
			case "ML":
				sensorValue[1] = value;
				break;
			case "BL":
				sensorValue[2] = value;
				break;
			case "LF":
				sensorValue[3] = value;
				break;
			case "MF":
				sensorValue[4] = value;
				break;
			case "RF":
				sensorValue[5] = value;
				break;
			case "TR":
				sensorValue[6] = value;
				break;
			case "MR":
				sensorValue[7] = value;
				break;
			case "BR":
				sensorValue[8] = value;
				break;
			default:
				// System.out.println("Wrong sensor name");
				return;
			}
		}

		if (sensor.hasLeftFrontSensor()) {
			decodeFrontSensorData(map, -1, sensorValue[3]);
		}
		if (sensor.hasMiddleFrontSensor()) {
			// System.out.println("MF sensor value: " + sensorValue[4]);
			decodeFrontSensorData(map, 0, sensorValue[4]);
		}
		if (sensor.hasRightFrontSensor()) {
			decodeFrontSensorData(map, 1, sensorValue[5]);
		}
		if (sensor.hasTopLeftSensor()) {
			decodeLeftSensorData(map, -1, sensorValue[0]);
		}
		if (sensor.hasMiddleLeftSensor()) {
			decodeLeftSensorData(map, 0, sensorValue[1]);
		}
		if (sensor.hasBottomLeftSensor()) {
			decodeLeftSensorData(map, 1, sensorValue[2]);
		}
		if (sensor.hasTopRightSensor()) {
			decodeRightSensorData(map, -1, sensorValue[6]);
		}
		if (sensor.hasMiddleRightSensor()) {
			decodeRightSensorData(map, 0, sensorValue[7]);
		}
		if (sensor.hasBottomRightSensor()) {
			decodeRightSensorData(map, 1, sensorValue[8]);
		}
		map.updateRobotPosition();

		String msg = this.androidStringEncode("Exploring", map);
		CommunicatorRpi.getInstance().getConn().send(0, msg);
	}

	public String moveFoward() {
		// Record current position as old
		this.oldI = i;
		this.oldJ = j;

		switch (this.orientation) {
		case NORTH:
			this.i--;
			break;
		case SOUTH:
			this.i++;
			break;
		case EAST:
			this.j++;
			break;
		case WEST:
			this.j--;
			break;
		}
		return FORWARD;
	}

	/**
	 * Moves to given coordinates in the map given the Fastest Path result
	 * 
	 * @param map
	 *            Map object containing the explored map
	 * @param path
	 *            2D array output from Fastest Path Algorithm. 8 denotes the
	 *            path to take
	 * @param end_i
	 *            y-coordinates to head towards to
	 * @param end_j
	 *            x-coordinates to head towards to
	 */
	public void moveToPosition(Map map, int[][] path, int end_i, int end_j, boolean backToStart) {
		moveToPosition(map, path, end_i, end_j, 0, "", backToStart);
	}

	/**
	 * Moves to given coordinates in the map given the Fastest Path result
	 * 
	 * @param map
	 *            Map object containing the explored map
	 * @param path
	 *            2D array output from Fastest Path Algorithm. 8 denotes the
	 *            path to take
	 * @param end_i
	 *            y-coordinates to head towards to
	 * @param end_j
	 *            x-coordinates to head towards to
	 * @param speed
	 *            Speed of the robot to move. 0 if it is the real robot
	 * @param backToStart
	 *            True if backToStart
	 */
	public void moveToPosition(Map map, int[][] path, int end_i, int end_j, int speed, String msg,
			boolean backToStart) {
		int directionToMove = 0;
		boolean found = false;
		String commandMsg = null;
		// Note: given array is [i-1][j-1] from the Robot's map

		// Check where to move left or right
		for (int j = this.j - 1; j <= this.j + 1; j += 2) {
			// End loop if direction has been found
			if (found)
				break;

			// Part of path and not from previous position
			if (path[this.i - 1][j - 1] == 8 && this.oldJ != j) {
				// Check whether to move robot
				commandMsg = decideMovement_FP(directionToMove, false);
				found = true;
			} else
				directionToMove += 2;
		}

		// Check where to move up or down
		directionToMove -= 3;
		for (int i = this.i - 1; i <= this.i + 1; i += 2) {
			// End loop if direction has been found
			if (found)
				break;

			// Part of path and not from previous position
			if (path[i - 1][this.j - 1] == 8 && this.oldI != i) {
				// Check whether to move robot
				commandMsg = decideMovement_FP(directionToMove, false);
				found = true;
			} else
				directionToMove += 2;
		}

		// Check if it is a simulation or real run
		// If speed == 0, it is a real run, send command
		if (speed == 0) {
			// System.out.println("Fastest Path: "+found+ " Message: "+
			// commandMsg);
			if (commandMsg != null)
				// msg = msg + commandMsg + "|21"; // For | separate message
				msg = msg + commandMsg;
			else
				// Do nothing
				// return;
				;
		}

		// Else, it is a simulation
		// pause 1s after each move
		// according to the speed selected
		else {
			try {
				Thread.sleep(1000 / speed);
			} catch (InterruptedException e) {
				System.out.println("Robot: moveToPosition() interrupted.");
			}
		}

		// Update UI
		map.updateRobotPosition();

		// Check if reached end point
		if (this.i == end_i && this.j == end_j) {
			// System.out.println("Robot reached destination successful!");
			if (speed == 0) {
				if (!backToStart) {
					msg = this.fastestPathEncoder(msg);
				} else {
					msg = this.backToStartEncoder(msg);
				}
				CommunicatorRpi.getInstance().getConn().send(1, msg);
			}
			return;
		}
		// Else, repeat
		else
			moveToPosition(map, path, end_i, end_j, speed, msg, backToStart);
	}

	/**
	 * Clears memory of old i,j of the robot
	 */
	public void resetOldIJ() {
		this.oldI = this.i;
		this.oldJ = this.j;
	}

	/**
	 * Returns the robot back to its original orientation after calibration
	 * 
	 * @param calibrateMsg
	 * @return
	 */
	public String returnFromCalibration(String calibrateMsg) {
		String lastCommand = null;
		switch (calibrateMsg) {
		case L_CALIBRATE:
			lastCommand = this.turnRight();
			break;
		case R_CALIBRATE:
			lastCommand = this.turnLeft();
			break;
		default:
			// Do nothing
			break;
		}
		return lastCommand;
	}

	public void setI(int i) {
		this.oldI = this.i;
		this.i = i;
	}

	public void setJ(int j) {
		this.oldJ = this.j;
		this.j = j;
	}

	public void setOrientation(int newOrientation) {
		this.oldOrientation = this.orientation;
		this.orientation = newOrientation;
	}

	public void setPreviousLeftWall(boolean b) {
		this.previousLeftWall = b;
	}

	public void setPreviousRightBorder(boolean b) {
		this.previousRightBorder = b;
	}

	/**
	 * Turns the robot 180 degrees.
	 * 
	 * Note that is achieved by turning the same direction twice
	 * 
	 * Change accordingly to which rotation is more accurate
	 * 
	 * @return String command to Arduino
	 */
	public String turnBack() {
		CommunicatorRpi.getInstance().getConn().send(1, this.turnRight());
		return this.turnRight();
	}

	/**
	 * Turns the Robot to the Left by 90 degrees, w.r.t. to the robot's
	 * direction
	 */
	public String turnLeft() {
		this.orientation = (this.orientation - 1) % 4;
		if (this.orientation < 0)
			this.orientation += 4;
		return TURNLEFT;
	}

	/**
	 * Turns the Robot to the Left by 90 degrees, w.r.t. to the robot's
	 * direction
	 */
	public String turnLeftCalibrate() {
		this.turnLeft();
		return L_CALIBRATE;
	}

	/**
	 * Turns the Robot to the Right by 90 degrees, w.r.t. to the robot's
	 * direction
	 */
	public String turnRight() {
		this.orientation = (this.orientation + 1) % 4;
		return TURNRIGHT;
	}

	public String turnRightCalibrate() {
		this.turnRight();
		return R_CALIBRATE;
	}
}
