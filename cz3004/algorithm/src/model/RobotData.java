/**
 * Maybe will convert to enum type later
 */
package model;

/**
 * @author Leroy Lim
 *
 */
public interface RobotData {
	// the 4 directions that the robot could face
	static final int NORTH = 1;
	static final int SOUTH = 3;
	static final int EAST = 2;
	static final int WEST = 0;

	// Initial direction that robot will face
	static final int INITIAL_DIRECTION = NORTH;

	// String commands for the robot
	static final String FORWARD = "w";
	static final String TURNLEFT = "a";
	static final String BACKWARD = "s";
	static final String TURNRIGHT = "d";
	static final String STOP = "t";
	static final String BEGIN = "b";
	static final String L_CALIBRATE = "q";
	static final String R_CALIBRATE = "r";
	static final String CALIBRATE = "c";
	static final String CORNER_CALIBRATE = "l";
	static final String PREPCHALLENGE = "p"; // Calibrate before going for
												// challenge

	// Sensor Constants
	static final int F_SHORTEST = 0;
	static final int F_LONGEST = 2;
	static final int L_SHORTEST = 0;
	static final int L_LONGEST = 2;
	static final int R_SHORTEST = 0;
	static final int R_LONGEST = 2;
}
