/**
 * Data for Map
 */
package model;

import java.awt.Color;

/**
 * @author Leroy Lim
 *
 */
public interface MapParam {
	// Setting color for grid type
	public static final Color UNEXPLORED = new Color(255, 255, 255); // White
	public static final Color FREE = new Color(100, 100, 255); // Light Blue
	public static final Color OBSTACLE = new Color(0, 0, 0); // Black
	public static final Color GOAL = new Color(0, 180, 0); // Green

	public static final Color[] TERRAIN = { UNEXPLORED, FREE, OBSTACLE, GOAL };

	// Placeholder for Robot pieces
	/*
	 * 1 2 3 4 5 6 7 8 9
	 * 
	 * Facing top
	 */
	public static final Color WHOLE = new Color(100, 100, 100); // Grey
	public static final Color ROBOTFRONT = new Color(255, 255, 0); // Yellow

	public static final Color[] ROBOTPIECE = { WHOLE, ROBOTFRONT, WHOLE, WHOLE, WHOLE, WHOLE, WHOLE, WHOLE, WHOLE, };

	public static final Color TRAVELED = new Color(255, 100, 100); // Red

	public static final int NUM_ROWS = 22; // 20 + 1 + 1
	public static final int NUM_COLS = 17; // 15 + 1 + 1

	public static final int PREFERRED_GRID_SIZE_PIXELS = 30;
}
