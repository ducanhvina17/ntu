/**
 * A map that allows mouse controls
 */
package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * Note: i is y-coordinate j is x-coordinate
 * 
 * @author Leroy Lim
 *
 */
public class Map extends JPanel implements MapParam {

	private static final long serialVersionUID = 1065255176647841066L;
	private JPanel grid;
	private MapCell[][] mapCell;
	private Robot robot;
	private int start_i, start_j, goal_i, goal_j;
	private boolean isRealRun;

	// Constructor
	public Map() {
		this.robot = null;
		this.mapCell = new MapCell[NUM_ROWS][NUM_COLS];

		this.isRealRun = true; // By default, this is true

		// Set up Map Coord vars
		this.start_i = NUM_ROWS - 3;
		this.start_j = 2;
		this.goal_i = 2;
		this.goal_j = NUM_COLS - 3;

		// Set up grid layout
		this.grid = new JPanel();
		this.grid.setBackground(Color.GREEN);
		int preferredWidth = NUM_COLS * PREFERRED_GRID_SIZE_PIXELS;
		int preferredHeight = NUM_ROWS * PREFERRED_GRID_SIZE_PIXELS;
		grid.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		grid.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, 0, 0));
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				this.mapCell[i][j] = new MapCell(i, j); // Set all to Unexplored

				// Setting Border wall
				if (i == 0 || i == NUM_ROWS - 1 || j == 0 || j == NUM_COLS - 1) {
					this.mapCell[i][j].setState(2);
					this.mapCell[i][j].setWall();
					this.mapCell[i][j].setPriority(9999);
				}
				// Setting Goal Zone
				if (i >= 1 && i <= 3 && j >= 13 && j <= 15) {
					this.mapCell[i][j].setState(3);
					this.mapCell[i][j].setPriority(-1);
				}
				// Setting Start Zone
				if (i >= NUM_ROWS - 4 && i <= NUM_ROWS - 2 && j >= 1 && j <= 3) {
					this.mapCell[i][j].setState(3);
					this.mapCell[i][j].setPriority(10);
				}

				grid.add(this.mapCell[i][j]);
			}
		}
		this.add(grid);
	}

	public void addRobot(Robot r) {
		this.robot = r;
	}

	/**
	 * Calculates current % of map explored
	 * 
	 * @return current % explored
	 */
	public double calPercent() {
		int counter = 0;

		for (int i = 1; i < NUM_ROWS - 1; i++) {
			for (int j = 1; j < NUM_COLS - 1; j++) {
				// Check if cell is discovered
				if (mapCell[i][j].isDiscovered()) {
					counter++;
				}
			}
		}
		return ((counter * 100 / ((NUM_ROWS - 2) * (NUM_COLS - 2))));
	}

	/**
	 * Converts a binary into hexadecimal and format as String
	 * 
	 * @param s
	 *            Binary string to be converted
	 * @return string containing the hexadecimal
	 */
	private String convertToHexadecimal(String s) {
		String working = s;
		String result = new String("");
		int length = working.length();
		// Stuff to full byte
		if (length % 8 != 0) {
			int difference = 8 - (length % 8);
			for (int i = 0; i < difference; i++) {
				working = working.concat("0");
			}
			length = working.length();
		}
		for (int i = 0; i < length; i += 4) {
			result = result.concat(Integer.toHexString(Integer.parseInt(working.substring(i, i + 4), 2)));
		}
		return result;
	}

	/**
	 * Set Robot initial position as discovered
	 */
	public void discoverInitial() {
		int x = this.robot.getI();
		int y = this.robot.getJ();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				this.mapCell[i][j].discover();
			}
		}
	}

	/**
	 * Converts the map's x-coordinate to the Android's
	 * 
	 * @return x-coordinates with reference to Android's map
	 */
	public int getAndroidX(int j) {
		return j - 1;
	}

	/**
	 * Converts the map's y-coordinate to the Android's
	 * 
	 * @return y-coordinates with reference to Android's map
	 */
	public int getAndroidY(int i) {
		return NUM_ROWS - 1 - i - 1;
	}

	public int getGoal_I() {
		return this.goal_i;
	}

	public int getGoal_J() {
		return this.goal_j;
	}

	/**
	 * Getter method for mapCell
	 * 
	 * @return MapCell[][]
	 */
	public MapCell[][] getMapCell() {
		return mapCell;
	}

	public Robot getRobot() {
		return this.robot;
	}

	public int getStart_I() {
		return this.start_i;
	}

	public int getStart_J() {
		return this.start_j;
	}

	/**
	 * Encodes the map into a Hexadecimal string for storage
	 * 
	 * @return hexadecimal string
	 */
	public String getStringA() {
		// First 2 bit "11"
		// 0 - Unexplored
		// 1 - Explored
		// Last 2 bit "11"
		String mapFormat = new String("11");
		String mapHexaFormat;
		// Print Map for debug
		// this.printMap();
		for (int i = NUM_ROWS - 2; i > 0; i--) {
			for (int j = 0; j < NUM_COLS; j++) {
				// If wall, ignore
				if (mapCell[i][j].isWall())
					continue;
				if (mapCell[i][j].isDiscovered()) {
					mapFormat = mapFormat.concat("1");
				} else {
					mapFormat = mapFormat.concat("0");
				}
			}
			// System.out.println(" ");
		}
		mapFormat = mapFormat.concat("11");

		// Convert to hexadecimal
		mapHexaFormat = this.convertToHexadecimal(mapFormat);
		return mapHexaFormat;
	}

	/**
	 * Encodes the map into a Hexadecimal string for storage
	 * 
	 * @return hexadecimal string
	 */
	public String getStringB() {
		// Only explored cells will be represented
		// 0 - Free
		// 1 - Obstacle
		// Stuff the remaining with "0" to form a full byte
		String mapFormat = new String("");
		String mapHexaFormat;
		for (int i = NUM_ROWS - 2; i >= 1; i--) {
			for (int j = 1; j < NUM_COLS - 1; j++) {
				// Only take explored cells
				if (mapCell[i][j].isDiscovered()) {
					// If free cell, start and goal
					if (mapCell[i][j].getState() == 1 || mapCell[i][j].getState() == 3)
						mapFormat = mapFormat.concat("0");
					// If obstacle
					else if (mapCell[i][j].getState() == 2)
						mapFormat = mapFormat.concat("1");
				}
			}
		}
		// Convert to hexadecimal
		mapHexaFormat = this.convertToHexadecimal(mapFormat);
		return mapHexaFormat;
	}

	/**
	 * Prints out the current map.
	 * 
	 * This is mainly for debugging
	 */
	public void printMap() {
		System.out.println("Map: Map Output:");
		for (int i = 1; i < NUM_ROWS - 1; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				// Check if robot is not on this [i][j]
				if (!(i == this.robot.getI() && j == this.robot.getJ()))
					System.out.print(mapCell[i][j].getState());
				else
					// Print robot position
					System.out.print("X");
			}
			System.out.println(" ");
		}
	}

	/**
	 * Re-renders the entire map
	 */
	private void refresh() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				this.mapCell[i][j].repaintColor();
			}
		}
	}

	/**
	 * Removes the travel path from the map
	 */
	public void removeTravelPath() {
		for (int i = 1; i < NUM_ROWS - 1; i++) {
			for (int j = 1; j < NUM_COLS - 1; j++) {
				setNotTravelled(i, j);
			}
		}
		this.refresh();
	}

	/**
	 * Resets the map and set the robot to its starting position
	 */
	public void ResetMap() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				this.mapCell[i][j].ResetMapCell();

				// Setting Border wall
				if (i == 0 || i == NUM_ROWS - 1 || j == 0 || j == NUM_COLS - 1) {
					this.mapCell[i][j].setState(2);
					this.mapCell[i][j].setWall();
					this.mapCell[i][j].setPriority(9999);
				}
				// Setting Goal Zone
				if (i >= 1 && i <= 3 && j >= 13 && j <= 15) {
					this.mapCell[i][j].setState(3);
					this.mapCell[i][j].setPriority(-1);
				}
				// Setting Start Zone
				if (i >= NUM_ROWS - 4 && i <= NUM_ROWS - 2 && j >= 1 && j <= 3) {
					this.mapCell[i][j].setState(3);
					this.mapCell[i][j].setPriority(10);
				}
			}
		}

		refresh();

		if (robot != null) {
			robot.setOrientation(RobotData.INITIAL_DIRECTION);
			robot.setI(start_i);
			robot.setJ(start_j);
			getRobot().resetOldIJ();
			updateRobotPosition();
		}

		removeTravelPath();
		this.isRealRun = true;
	}

	public boolean robotAssigned() {
		if (this.robot != null)
			return true;
		else
			return false;
	}

	public void setDiscovered(int i, int j) {
		this.mapCell[i][j].discover();
	}

	public void setFree(int i, int j) {
		this.mapCell[i][j].setState(1);
	}

	private void setNotTravelled(int i, int j) {
		this.mapCell[i][j].notTraveled();
	}

	public void setObstacle(int i, int j) {
		this.mapCell[i][j].setObstacle();
	}

	/**
	 * Sets the flag to denote if the run is live run or simulated
	 * 
	 * @param value
	 */
	public void setRealRun(boolean value) {
		this.isRealRun = value;
	}

	private void setTravelled(int i, int j) {
		this.mapCell[i][j].traveled();
	}

	/**
	 * Shows the travel path of the Robot for Fastest Path
	 * 
	 * @param path
	 *            2D array containing the fastest path
	 */
	public void showTravelPath(int[][] path) {
		for (int i = 1; i < NUM_ROWS - 1; i++) {
			for (int j = 1; j < NUM_COLS - 1; j++) {
				if (path[i - 1][j - 1] == 8) {
					for (int a = i - 1; a <= i + 1; a++) {
						for (int b = j - 1; b <= j + 1; b++) {
							setTravelled(a, b);
						}
					}
				}
			}
		}
		this.refresh();
	}

	/**
	 * Updates priority and discover cells
	 * 
	 * @param x
	 *            y-coordinate of the map
	 * @param y
	 *            x-coordinate of the map
	 * @return MapCell object
	 */
	public MapCell uncoverACell(int x, int y) {

		// * Nhat's code
		if (!mapCell[x][y].isObstacle()) {
			if (((mapCell[x][y - 1].isObstacle() || mapCell[x][y - 2].isObstacle())
					&& (mapCell[x][y + 1].isObstacle() || mapCell[x][y + 2].isObstacle())
					&& (mapCell[x][y + 1].isObstacle() || mapCell[x][y - 1].isObstacle())) ||

					((mapCell[x - 1][y].isObstacle() || mapCell[x - 2][y].isObstacle())
							&& (mapCell[x + 1][y].isObstacle() || mapCell[x + 2][y].isObstacle())
							&& (mapCell[x + 1][y].isObstacle() || mapCell[x - 1][y].isObstacle()))
					||

					(mapCell[x - 1][y].isObstacle()
							&& (mapCell[x + 1][y - 1].isObstacle() || mapCell[x + 2][y - 1].isObstacle())
							&& (mapCell[x + 1][y + 1].isObstacle() || mapCell[x + 2][y + 1].isObstacle()))
					|| (mapCell[x + 1][y].isObstacle()
							&& (mapCell[x - 1][y - 1].isObstacle() || mapCell[x - 2][y - 1].isObstacle())
							&& (mapCell[x - 1][y + 1].isObstacle() || mapCell[x - 2][y + 1].isObstacle()))
					|| (mapCell[x][y + 1].isObstacle()
							&& (mapCell[x + 1][y - 1].isObstacle() || mapCell[x + 1][y - 2].isObstacle())
							&& (mapCell[x - 1][y - 1].isObstacle() || mapCell[x - 1][y - 2].isObstacle()))
					|| (mapCell[x][y - 1].isObstacle()
							&& (mapCell[x + 1][y + 1].isObstacle() || mapCell[x + 1][y + 2].isObstacle())
							&& (mapCell[x - 1][y + 1].isObstacle() || mapCell[x - 1][y + 2].isObstacle()))) {
				mapCell[x][y].setPriority(100);
			}
		}

		// Checks if real run
		if (this.isRealRun) {
			// Refresh state, only for non wall and goal start
			if (!(mapCell[x][y].getState() == 3 || mapCell[x][y].isWall()))
				mapCell[x][y].setState(1);
		}
		mapCell[x][y].discover();
		return mapCell[x][y];
	}

	/**
	 * Updates Robot position in UI
	 */
	public void updateRobotPosition() {
		int x, y, k, l;
		// Set New Robot Coordinates
		x = this.robot.getOldI();
		y = this.robot.getOldJ();
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				this.mapCell[i][j].vacate();
			}
		}

		// Set New Robot Coordinates
		x = this.robot.getI();
		y = this.robot.getJ();
		switch (this.robot.getOrientation()) {
		case 0:
			// Facing Left
			l = 3;
			for (int i = x - 1; i <= x + 1; i++) {
				k = 0;
				for (int j = y - 1; j <= y + 1; j++) {
					this.mapCell[i][j].occupy();
					this.mapCell[i][j].setRobotPiece(l + k);
					k += 3;
				}
				l--;
			}
			break;
		case 1:
			// Facing Up
			k = 1;
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					this.mapCell[i][j].occupy();
					this.mapCell[i][j].setRobotPiece(k);
					k++;
				}
			}
			break;
		case 2:
			// Facing Right
			l = 1;
			for (int i = x - 1; i <= x + 1; i++) {
				k = 6;
				for (int j = y - 1; j <= y + 1; j++) {
					this.mapCell[i][j].occupy();
					this.mapCell[i][j].setRobotPiece(l + k);
					k -= 3;
				}
				l++;
			}
			break;
		case 3:
			// Facing Down
			k = 9;
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					this.mapCell[i][j].occupy();
					this.mapCell[i][j].setRobotPiece(k);
					k--;
				}
			}
			break;
		default:
			System.out.println("Robot Error: Failed to get Orientation.");
			break;
		}

		// Update GUI
		this.refresh();
	}
}
