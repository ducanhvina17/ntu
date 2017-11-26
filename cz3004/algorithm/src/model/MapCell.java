/**
 * This class controls a cell of a map
 */
package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.Main;

/**
 * 
 * @author Leroy Lim
 *
 */
public class MapCell extends JPanel implements MapParam {

	private static final long serialVersionUID = 8180206171750515931L;
	private int state; // 0 - Unexplored, 1 - Free, 2 - Obstacle, 3 - Start/Goal
	private int row, col; // Coordinates of the cell
	private int priority; // Used in algorithms
	private boolean wall;
	private boolean occupied;
	private boolean discovered;
	private boolean traveled;
	private int robotPiece; // 0: No robot, 1-9: (From top left going down, to
							// the last piece)
	private JLabel label;

	// Constructor
	public MapCell(int row, int col) {
		this.row = row;
		this.col = col;
		this.priority = 0; // Default priority is 0
		this.state = 0; // Default state is Unexplored
		this.wall = false; // Default cell is not a wall
		this.occupied = false; // Default cell is not occupied
		this.discovered = false; // Default cell is not discovered
		this.traveled = false; // Default cell is not traveled
		this.label = new JLabel(col + ", " + row);
		this.label.setForeground(Color.BLACK);
		this.label.setFont(new Font("Serif", Font.PLAIN, 10));
		this.add(label);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		repaintColor();

		// Set Mouse Event Handler
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Checks if cell can be edited
				if (Main.getMode() != 0 || wall == true)
					return;
				// Cell is unexplored
				if (state == 0) {
					setObstacle();
					System.out.println("Changed to Black");
					System.out.println(row + " " + col);
				}
				// Cell is Obstacle
				else if (state == 2) {
					setState(0);
					setPriority(0);
					System.out.println("Changed to Red");
					System.out.println(row + " " + col);
				}
				// Any other cell
				else {
					JFrame errorMessageDialog = null;
					JOptionPane.showMessageDialog(errorMessageDialog,
							"Cannot put walls in start, goal or explored area ! ");
				}
			}
		});
	}

	// Sets the cell as discovered
	public void discover() {
		this.discovered = true;
		if (state == 0)
			state = 1;
	}

	public int getCol() {
		return this.col;
	}

	public int getPriority() {
		return priority;
	}

	public int getRobotPiece() {
		return this.robotPiece;
	}

	public int getRow() {
		return this.row;
	}

	public int getState() {
		return this.state;
	}

	// Checks if the cell has been discovered
	public boolean isDiscovered() {
		return discovered;
	}

	/**
	 * Check if the Cell is an obstacle
	 * 
	 * @return true if the Cell is an obstacle
	 */
	public boolean isObstacle() {
		// If the cell has not been discovered, we pretend it is not an obstacle
		// I mean, how could we had known? #YOLO
		if (!isDiscovered())
			return false;
		if (this.state == 2)
			return true;
		else
			return false;
	}

	public boolean isOccupied() {
		return this.occupied;
	}

	public boolean isTraveled() {
		return traveled;
	}

	/**
	 * Check if the Cell is a Wall fixture
	 * 
	 * @return true if the Cell is a Wall
	 */
	public boolean isWall() {
		return this.wall;
	}

	public void notTraveled() {
		this.traveled = false;
	}

	// Sets the cell as occupied by robot
	public void occupy() {
		this.occupied = true;
	}

	/**
	 * Sets the color of the cell based on the current state of the cell
	 */
	void repaintColor() {
		// Checks if cell is occupied by robot
		if (!this.occupied) {
			// If traveled by robot
			if (this.traveled)
				setBackground(TRAVELED);
			// Checks if cell is discovered or in Editing mode
			else if (this.discovered || this.wall || this.state == 3 || Main.getMode() == 0)
				setBackground(TERRAIN[state]); // Set Color
			else
				setBackground(TERRAIN[0]);
		} else
			setBackground(ROBOTPIECE[this.robotPiece - 1]);
		revalidate();
		repaint();
	}

	public void ResetMapCell() {
		this.priority = 0; // Default priority is 0
		this.state = 0; // Default state is Unexplored
		this.wall = false; // Default cell is not a wall
		this.occupied = false; // Default cell is not occupied
		this.discovered = false; // Default cell is not discovered
		this.traveled = false; // Default cell is not traveled
		this.label = new JLabel(col + ", " + row);
	}

	public void setLabel(String text) {
		this.label.setText(text);
	}

	/**
	 * Sets the given Cell as an obstacle
	 */
	public void setObstacle() {
		if (state == 3)
			return;
		this.setState(2);
		this.setPriority(9999);
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setRobotPiece(int newRobotPiece) {
		this.robotPiece = newRobotPiece;
	}

	public void setState(int newState) {
		this.state = newState;
		repaintColor();
	}

	public void setWall() {
		this.wall = true;
		this.discovered = true;
		this.setState(2);
		this.setPriority(9999);
	}

	public void traveled() {
		this.traveled = true;
	}

	// Sets the cell as no longer occupied by robot
	public void vacate() {
		this.occupied = false;
		this.robotPiece = 0;
	}
}
