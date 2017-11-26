package explore;

import java.util.ArrayList;

import connection.CommunicatorRpi;
import fastestPath.Cell;
import fastestPath.FastestPathNoBruteForce;
import io.FileRW;
import model.Map;
import model.MapCell;
import model.MapParam;

public class LeftWallHug extends Explore {
	private boolean reachedStart;
	private boolean prevLeftWall;
	private int moveForward, movedLeft;
	private int calibrated;

	// Constructors
	public LeftWallHug(Map map) {
		super(map);
	}

	public LeftWallHug(Map map, int speed) {
		super(map, speed);
	}

	@Override
	public String checkCalibrate() {
		String lastCommand = null;
		String calibrateMsg = null;
		moveSinceCalibrate++;

		// Check if in corner
		if (calibrated <= 0) {
			// System.out.println("Corner Calibration: Checking for corners to
			// calibrate.");
			calibrateMsg = robot.cornerCalibrate(map);
			lastCommand = calibrateMsg;
		}

		if (lastCommand != null)
			calibrated = MOVESBEFORECALIBRATE;

		/*
		 * if (calibrateMsg == null) { // Check if there is any wall to
		 * calibrate calibrateMsg = robot.calibrate(map, moveSinceCalibrate,
		 * MOVESBEFORECALIBRATE);
		 * System.out.println("Calibration Check: Direction to Move:" +
		 * calibrateMsg); if (calibrateMsg != null) { // yes if (calibrateMsg !=
		 * "") CommunicatorRpi.getInstance().getConn().send(1, calibrateMsg);
		 * moveSinceCalibrate = 0;
		 * System.out.println("Exploration Algorithm: Robot calibrating.");
		 * lastCommand = this.robot.returnFromCalibration(calibrateMsg); return
		 * lastCommand; } } else calibrated = MOVESBEFORECALIBRATE;
		 */
		return lastCommand;
	}

	private boolean impossibleGrid(int i, int j) {
		return (i == 1 && j == 1 && this.map.getMapCell()[3][3].isObstacle())
				|| (i == 1 && j == 15 && this.map.getMapCell()[13][3].isObstacle())
				|| (i == 20 && j == 1 && this.map.getMapCell()[18][3].isObstacle())
				|| (i == 20 && j == 15 && this.map.getMapCell()[18][13].isObstacle());
	}

	@Override
	public String makeDecision() {
		String lastCommand = null;
		// System.out.println("Robot Position: (x: " + this.robot.getJ() + ",
		// y:" + this.robot.getI() + ") i:" + this.robot.getI() + " j:" +
		// this.robot.getJ() + " Direction: " + robot.getOrientation());
		// System.out.println("Left Wall Hug: prevLeftWall: " + prevLeftWall + "
		// moveForward: " + moveForward);
		// Check if left wall is present
		if (this.leftWall()) {
			moveForward = 0;
			movedLeft = 0;
			prevLeftWall = true;
			// If robot facing front wall too,
			// Turn right
			if (this.frontWall())
				lastCommand = this.robot.turnRight();
			else
				lastCommand = this.robot.moveFoward();
		}
		// If left wall not present
		else {
			if (prevLeftWall) {
				// Turn left, if going into empty space
				lastCommand = this.robot.turnLeft();
				movedLeft++;
			} else if (movedLeft > 3) {
				// End infinite loop until find back wall
				if (this.frontWall())
					lastCommand = this.robot.turnRight();
				else
					lastCommand = this.robot.moveFoward();
			} else if (moveForward >= 2) {
				lastCommand = this.robot.turnLeft();
				movedLeft++;
				moveForward = 0;
			} else if (!this.frontWall()) {
				lastCommand = this.robot.moveFoward();
				moveForward++;
			} else
				lastCommand = this.robot.turnRight();
			prevLeftWall = false;
		}
		return lastCommand;
	}

	private String movement(ArrayList<Cell> path, int j) {
		String lastCommand = "";

		// Check if goal
		if ((map.getMapCell()[path.get(j).getI()][path.get(j).getJ() + 2].getState() == 0
				|| map.getMapCell()[path.get(j).getI()][path.get(j).getJ() + 2].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI()][path.get(j).getJ() + 1].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1 - 1][path.get(j).getJ() + 1].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI()][path.get(j).getJ()].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1 - 1][path.get(j).getJ() + 1 - 1].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI() + 2][path.get(j).getJ() + 2].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1 + 1][path.get(j).getJ() + 1 + 1].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI() + 2][path.get(j).getJ() + 1].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1 + 1][path.get(j).getJ() + 1].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI() + 2][path.get(j).getJ()].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1 + 1][path.get(j).getJ() + 1 - 1].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI() + 1][path.get(j).getJ() + 2].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1][path.get(j).getJ() + 1 + 1].getState() == 2)
				|| (map.getMapCell()[path.get(j).getI() + 1][path.get(j).getJ()].getState() == 0
						|| map.getMapCell()[path.get(j).getI() + 1][path.get(j).getJ() + 1 - 1].getState() == 2))
			return "b";
		// Move to west
		else if (this.robot.getI() - 1 == path.get(j).getI() && this.robot.getJ() - 1 > path.get(j).getJ()) {
			if (this.robot.getOrientation() == 0) // Facing west
				lastCommand = this.robot.moveFoward();
			else if (this.robot.getOrientation() == 1) // Facing north
				lastCommand = this.robot.turnLeft();
			else if (this.robot.getOrientation() == 2) // Facing east
				lastCommand = this.robot.turnRight();
			else if (this.robot.getOrientation() == 3) // Facing south
				lastCommand = this.robot.turnRight();
		}
		// Move to east
		else if (this.robot.getI() - 1 == path.get(j).getI() && this.robot.getJ() - 1 < path.get(j).getJ()) {
			if (this.robot.getOrientation() == 0) // Facing west
				lastCommand = this.robot.turnRight();
			else if (this.robot.getOrientation() == 1) // Facing north
				lastCommand = this.robot.turnRight();
			else if (this.robot.getOrientation() == 2) // Facing east
				lastCommand = this.robot.moveFoward();
			else if (this.robot.getOrientation() == 3) // Facing south
				lastCommand = this.robot.turnLeft();
		}
		// Move forward
		else if (this.robot.getI() - 1 > path.get(j).getI() && this.robot.getJ() - 1 == path.get(j).getJ()) {
			if (this.robot.getOrientation() == 0) // Facing west
				lastCommand = this.robot.turnRight();
			else if (this.robot.getOrientation() == 1) // Facing north
				lastCommand = this.robot.moveFoward();
			else if (this.robot.getOrientation() == 2) // Facing east
				lastCommand = this.robot.turnLeft();
			else if (this.robot.getOrientation() == 3) // Facing south
				lastCommand = this.robot.turnRight();
		}
		// Move to south
		else if (this.robot.getI() - 1 < path.get(j).getI() && this.robot.getJ() - 1 == path.get(j).getJ()) {
			if (this.robot.getOrientation() == 0) // Facing west
				lastCommand = this.robot.turnLeft();
			else if (this.robot.getOrientation() == 1) // Facing north
				lastCommand = this.robot.turnRight();
			else if (this.robot.getOrientation() == 2) // Facing east
				lastCommand = this.robot.turnRight();
			else if (this.robot.getOrientation() == 3) // Facing south
				lastCommand = this.robot.moveFoward();
		}

		return lastCommand;
	}

	private ArrayList<Cell> NeighborPath(MapCell end) {
		FastestPathNoBruteForce fp = new FastestPathNoBruteForce();
		ArrayList<Cell> path = null;

		// Left
		if (end.getCol() > 3) {
			this.robot.resetOldIJ();
			path = fp.FindFastestPath(this.robot.getI() - 1, this.robot.getJ() - 1, end.getRow() - 1, end.getCol() - 3);

			if (path == null || path.size() == 1) {
				// Bottom
				if (this.robot.getI() > end.getRow()) {
					if (end.getRow() < 19) {
						this.robot.resetOldIJ();
						path = fp.FindFastestPath(this.robot.getI() - 1, this.robot.getJ() - 1, end.getRow() + 1,
								end.getCol() - 1);

						if (path == null || path.size() == 1) {
							// Top
							if (end.getRow() > 3) {
								this.robot.resetOldIJ();
								path = fp.FindFastestPath(this.robot.getI() - 1, this.robot.getJ() - 1,
										end.getRow() - 3, end.getCol() - 1);

								if (path == null || path.size() == 1) {
									// Right
									if (end.getCol() < 14) {
										this.robot.resetOldIJ();
										path = fp.FindFastestPath(this.robot.getI() - 1, this.robot.getJ() - 1,
												end.getRow() - 1, end.getCol() + 1);
									}
								}
							}
						}
					}
				}
			}
		}

		return path;
	}

	private String pathToCell() {
		String lastCommand;

		ArrayList<MapCell> missedGrid = new ArrayList<>();

		for (int i = MapParam.NUM_ROWS - 1; i > 0; i--) {
			for (int j = 0; j < model.MapParam.NUM_COLS; j++) {
				MapCell cur = map.getMapCell()[i][j];
				if (!cur.isDiscovered()) {
					if (impossibleGrid(i, j))
						continue;

					missedGrid.add(cur);
					// System.out.println("Added: (" + cur.getCol() + ", " +
					// cur.getRow() + ")");
				}
			}
		}

		if (missedGrid.size() == 0)
			return "";

		ArrayList<Integer> chance = new ArrayList<>();

		for (int i = 0; i < missedGrid.size(); i++)
			chance.add(0);

		ArrayList<Cell> path;
		FastestPathNoBruteForce fp = new FastestPathNoBruteForce();

		do {
			for (int i = 0; i < missedGrid.size(); i++) // missedGrid.size()
			{
				MapCell end = missedGrid.get(i);

				if (end.isDiscovered()) {
					missedGrid.remove(i);
					missedGrid.trimToSize();
					chance.remove(i);
					chance.trimToSize();
					i--;
					continue;
				}

				int oi = end.getRow(), oj = end.getCol();

				if (oi == 1)
					oi += 1;
				else if (oi == 20)
					oi -= 1;

				if (oj == 1)
					oj += 1;
				else if (oj == 15)
					oj -= 1;

				end = this.map.getMapCell()[oi][oj];

				System.out.println("GoTo: " + end.getRow() + ", " + end.getCol() + " -> Chance: " + chance.get(i));

				path = fp.FindFastestPath(robot.getI() - 1, robot.getJ() - 1, end.getRow() - 1, end.getCol() - 1);

				if (path == null || path.size() == 1) {
					path = NeighborPath(end);
				}

				System.out.println("Path: " + path);

				if (path == null || path.size() == 1) {
					chance.set(i, chance.get(i) + 1);
					if (chance.get(i) >= 50) {
						missedGrid.remove(i);
						missedGrid.trimToSize();
						chance.remove(i);
						chance.trimToSize();
						i--;
					} else {
						missedGrid.add(missedGrid.remove(i));
						missedGrid.trimToSize();
						chance.add(chance.remove(i));
						chance.trimToSize();
						i--;
					}

					System.out.println("No path available!");
					continue;
				}

				lastCommand = "";

				for (int j = 1; j < path.size();) {
					super.stepCountUp(); // +1 move made
					this.robot.getSensorData(map); // Simulate sensors

					if (end.isDiscovered()) {
						System.out.println("Discovered");
						missedGrid.remove(i);
						missedGrid.trimToSize();
						chance.remove(i);
						chance.trimToSize();
						i--;
						break;
					}

					lastCommand = movement(path, j);
					System.out.println("Move: " + lastCommand);

					// Stop if obstacle
					if (lastCommand == "b") {
						chance.set(i, chance.get(i) + 1);
						if (chance.get(i) >= 50) {
							missedGrid.remove(i);
							missedGrid.trimToSize();
							chance.remove(i);
							chance.trimToSize();
							i--;
						} else {
							missedGrid.add(missedGrid.remove(i));
							chance.add(chance.remove(i));
							i--;
						}
						break;
					} else if (lastCommand == "w")
						j++;

					if (super.speed != 0) {
						try {
							Thread.sleep(1000 / super.speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					map.updateRobotPosition();
				}
				System.out.println(lastCommand);
				FileRW.storeExploredMap(map.getMapCell(), "explored_map.txt"); // Save
																				// current
																				// map
																				// state
			}
		} while (missedGrid.size() > 0);

		this.keepExploring = false;
		return "";
	}

	private String pathToCellReal() {
		String lastCommand;

		ArrayList<MapCell> missedGrid = new ArrayList<>();

		for (int i = MapParam.NUM_ROWS - 1; i > 0; i--) {
			for (int j = 0; j < model.MapParam.NUM_COLS; j++) {
				MapCell cur = map.getMapCell()[i][j];
				if (!cur.isDiscovered()) {
					if (impossibleGrid(i, j))
						continue;

					missedGrid.add(cur);
				}
			}
		}

		if (missedGrid.size() == 0)
			return "";

		ArrayList<Integer> chance = new ArrayList<>();

		for (int i = 0; i < missedGrid.size(); i++)
			chance.add(0);

		ArrayList<Cell> path;
		FastestPathNoBruteForce fp = new FastestPathNoBruteForce();

		do {
			for (int i = 0; i < missedGrid.size(); i++) {
				MapCell end = missedGrid.get(i);

				if (end.isDiscovered()) {
					missedGrid.remove(i);
					missedGrid.trimToSize();
					chance.remove(i);
					chance.trimToSize();
					i--;
					continue;
				}

				int oi = end.getRow(), oj = end.getCol();

				if (oi == 1)
					oi += 1;
				else if (oi == 20)
					oi -= 1;

				if (oj == 1)
					oj += 1;
				else if (oj == 15)
					oj -= 1;

				end = this.map.getMapCell()[oi][oj];

				path = fp.FindFastestPath(robot.getI() - 1, robot.getJ() - 1, end.getRow() - 1, end.getCol() - 1);

				if (path == null || path.size() == 1) {
					path = NeighborPath(end);
				}

				if (path == null || path.size() == 1) {
					chance.set(i, chance.get(i) + 1);
					if (chance.get(i) >= 50) {
						missedGrid.remove(i);
						missedGrid.trimToSize();
						chance.remove(i);
						chance.trimToSize();
						i--;
					} else {
						missedGrid.add(missedGrid.remove(i));
						missedGrid.trimToSize();
						chance.add(chance.remove(i));
						chance.trimToSize();
						i = 0;
					}

					continue;
				}

				for (int j = 1; j < path.size();) {
					if (end.isDiscovered()) {
						missedGrid.remove(i);
						missedGrid.trimToSize();
						chance.remove(i);
						chance.trimToSize();
						i--;
						break;
					}

					lastCommand = movement(path, j);

					// Stop if obstacle
					if (lastCommand == "b") {
						chance.set(i, chance.get(i) + 1);
						if (chance.get(i) >= 50) {
							missedGrid.remove(i);
							missedGrid.trimToSize();
							chance.remove(i);
							chance.trimToSize();
							i--;
						} else {
							missedGrid.add(missedGrid.remove(i));
							missedGrid.trimToSize();
							chance.add(chance.remove(i));
							chance.trimToSize();
							i--;
						}
					} else if (lastCommand == "w")
						j++;

					if (super.speed != 0) {
						try {
							Thread.sleep(1000 / super.speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					map.updateRobotPosition();
					return lastCommand;
				}

				FileRW.storeExploredMap(map.getMapCell(), "explored_map.txt"); // Save
																				// current
																				// map
																				// state
			}
		} while (missedGrid.size() > 0);

		this.keepExploring = false;
		return super.checkConditions();
	}

	@Override
	public void startExploration() {
		do {
			super.stepCountUp(); // +1 move made
			this.robot.getSensorData(map); // Simulate sensors

			// Detect if the robot has reached goal
			if (this.robot.getI() == 2 && this.robot.getJ() == 14) {
				reachedGoal = true;
				// System.out.println("Reached goal point!");
			}

			if (keepExploring)
				keepExploring = !(this.map.calPercent() >= percent && reachedGoal || interrupted);

			// Do nothing if paused
			if (pauseFlag)
				continue;

			// when the robot get back to start point and stop exploring
			// The STOP command will be sent
			if (!keepExploring) {
				// System.out.println("Exploration Algorithm: Ended due to
				// flag");
				this.endExplore(false);
				break;
			}

			// Detect if the robot has reached start
			if (this.robot.getI() == this.map.getStart_I() && this.robot.getJ() == this.map.getStart_J()) {
				if (super.countMove > 5) {
					reachedStart = true;
					// System.out.println("Reached start point!");
				}
			}

			// Make decision as to where to move
			if (!reachedStart)
				this.makeDecision();
			else
				super.makeDecision();

			// Pause 1s after each move according to the speed selected
			try {
				Thread.sleep(1000 / super.speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			map.updateRobotPosition();
		} while (this.keepExploring);

		// FileRW.storeExploredMap(map.getMapCell(), "explored_map.txt"); //
		// Save current map state
		//
		// System.out.println("All movement: \n" + pathToCell());

		// System.out.println("Simulation: Exited loop");
	}

	@Override
	public String startExploration(String sensorData) {
		String lastCommand;

		super.stepCountUp(); // +1 move made

		// Update priority and dead ends
		super.checkDeadEnd(sensorData);

		// Check conditions to stop
		lastCommand = super.checkConditions();

		// Detect if the robot has reached start
		if (this.robot.getI() == this.map.getStart_I() && this.robot.getJ() == this.map.getStart_J()) {
			if (super.countMove > 5) {
				reachedStart = true;
				CommunicatorRpi.getInstance().getConn().send(1, "b");
				// System.out.println("Reached start point!");
			}
		}

		// Calibrate if able or after n moves
		if (lastCommand == null)
			lastCommand = this.checkCalibrate();

		// Make decision as to where to move
		if (lastCommand == null) {
			calibrated--;
			if (!reachedStart)
				lastCommand = this.makeDecision();
			else {
				// Shortest path
				// Note: Right now, this is re-called after every move in case
				// of ghost block
				// FileRW.storeExploredMap(map.getMapCell(),
				// "explored_map.txt"); // Save current map state
				// lastCommand = pathToCellReal();
				/*
				 * super.keepExploring = false; System.out.
				 * println("Exploration Algorithm: Exploration ended prematurely. Reached Start"
				 * ); lastCommand = RobotData.STOP; this.endExplore(true);
				 */

				// * Nhat's code activated here
				lastCommand = super.makeDecision();

			}
		}

		return lastCommand;
	}
}
