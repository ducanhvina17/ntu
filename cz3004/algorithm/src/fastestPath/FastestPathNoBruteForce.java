package fastestPath;

import java.util.ArrayList;
import java.util.PriorityQueue;

import io.FileRW;

public class FastestPathNoBruteForce {
	private static final int CELL_COST = 10;
	private static final String EXPLORED_MAP = "explored_map.txt";
	private int si, sj, ei, ej;
	private int[] vDetails, hDetails;
	private boolean[][] vClosed, hClosed;
	private int[][] map;
	private Cell[][] grid, vGrid, hGrid;
	private PriorityQueue<Cell> open;

	private ArrayList<Cell> path;

	/**
	 * Check and update the current cost of the current cell and one of the
	 * neighbour. If the temporary final cost is less than the initial final
	 * cost, use temporary final cost.
	 *
	 * @param current
	 *            the cell to be checked and updated.
	 * @param t
	 *            the neighbour of the current cell.
	 * @param cost
	 *            cost of current cell.
	 * @param c
	 *            closed[][] of the grid the cell is in.
	 */
	private void CheckAndUpdateCost(Cell current, Cell t, int cost, boolean[][] c) {
		if (t == null || c[t.i][t.j])
			return;

		int t_final_cost = 0;

		if (t.i == 0 || t.i == 19 || t.j == 0 || t.j == 14 || grid[t.i - 1][t.j - 1].details == 0
				|| grid[t.i - 1][t.j - 1].details == 2 || grid[t.i - 1][t.j].details == 0
				|| grid[t.i - 1][t.j].details == 2 || grid[t.i - 1][t.j + 1].details == 0
				|| grid[t.i - 1][t.j + 1].details == 2 || grid[t.i + 1][t.j - 1].details == 0
				|| grid[t.i + 1][t.j - 1].details == 2 || grid[t.i + 1][t.j].details == 0
				|| grid[t.i + 1][t.j].details == 2 || grid[t.i + 1][t.j + 1].details == 0
				|| grid[t.i + 1][t.j + 1].details == 2 || grid[t.i - 1][t.j + 1].details == 0
				|| grid[t.i - 1][t.j + 1].details == 2 || grid[t.i][t.j + 1].details == 0
				|| grid[t.i][t.j + 1].details == 2 || grid[t.i + 1][t.j + 1].details == 0
				|| grid[t.i + 1][t.j + 1].details == 2 || grid[t.i - 1][t.j - 1].details == 0
				|| grid[t.i - 1][t.j - 1].details == 2 || grid[t.i][t.j - 1].details == 0
				|| grid[t.i][t.j - 1].details == 2 || grid[t.i + 1][t.j - 1].details == 0
				|| grid[t.i + 1][t.j - 1].details == 2)
			t_final_cost = 9999;
		else
			t_final_cost += t.heuristicCost + cost;

		boolean inOpen = open.contains(t);

		if (!inOpen || t_final_cost < t.finalCost) {
			t.finalCost = t_final_cost;
			t.parent = current;
			if (!inOpen)
				open.add(t);
		}
	}

	/**
	 * The main function of the fastest path class. Everything will be called
	 * here. Steps : 1. Load map and initialize grids. 2. Do some magic with the
	 * cell scores. 3. Update the grid with path. 4. Select the faster path. 5.
	 * Save the map with fastest path.
	 *
	 * @param si
	 *            starting point row.
	 * @param sj
	 *            starting point column.
	 * @param ei
	 *            end point row.
	 * @param ej
	 *            end point column.
	 */
	public ArrayList<Cell> FindFastestPath(int si, int sj, int ei, int ej) {
		this.si = si;
		this.sj = sj;
		this.ei = ei;
		this.ej = ej;
		vDetails = new int[2];
		vDetails[0] = 0;
		vDetails[1] = 0;
		hDetails = new int[2];
		hDetails[0] = 0;
		hDetails[1] = 0;
		vClosed = new boolean[20][15];
		hClosed = new boolean[20][15];
		map = new int[20][15];
		grid = new Cell[20][15];
		vGrid = new Cell[20][15];
		hGrid = new Cell[20][15];

		path = new ArrayList<>();

		// Add open cell to PriorityQueue
		this.open = new PriorityQueue<>((Object o1, Object o2) -> {
			Cell c1 = (Cell) o1;
			Cell c2 = (Cell) o2;
			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});

		LoadMap();
		InitializeCost();
		InitializeMap();

		if ((map[ei][ej] == 0 || map[ei][ej] == 2) || (map[ei - 1][ej + 1] == 0 || map[ei - 1][ej + 1] == 2)
				|| (map[ei - 1][ej] == 0 || map[ei - 1][ej] == 2)
				|| (map[ei - 1][ej - 1] == 0 || map[ei - 1][ej - 1] == 2)
				|| (map[ei + 1][ej + 1] == 0 || map[ei + 1][ej + 1] == 2)
				|| (map[ei + 1][ej] == 0 || map[ei + 1][ej] == 2)
				|| (map[ei + 1][ej - 1] == 0 || map[ei + 1][ej - 1] == 2)
				|| (map[ei][ej + 1] == 0 || map[ei][ej + 1] == 2) || (map[ei][ej - 1] == 0 || map[ei][ej - 1] == 2))
			return null;

		// Calculate the cost for each cell
		Magic(true);
		Magic(false);

		// Update map with fastest path included
		RoadToLeaderboardNo1(true);
		RoadToLeaderboardNo1(false);

		if (!vClosed[ei][ej] && !hClosed[ei][ej])
			return null;

		// Use the grid which has less number of steps and less turns
		if (vDetails[0] == 0)
			grid = hGrid;
		else if (hDetails[0] == 0)
			grid = vGrid;
		else if (vDetails[0] < hDetails[0])
			grid = vGrid;
		else if (vDetails[0] > hDetails[0])
			grid = hGrid;
		else
			grid = vDetails[1] <= hDetails[1] ? vGrid : hGrid;

		return path;
	}

	/**
	 * Initialize the cost of each cells for the grids. The cost of the cell is
	 * initialize based on its distance from the end position.
	 */
	private void InitializeCost() {
		for (int i = 19; i >= 0; --i) {
			for (int j = 0; j < 15; ++j) {
				grid[i][j] = new Cell(i, j);
				vGrid[i][j] = new Cell(i, j);
				hGrid[i][j] = new Cell(i, j);
				grid[i][j].heuristicCost = Math.abs(i - ei) + Math.abs(j - ej);
				vGrid[i][j].heuristicCost = Math.abs(i - ei) + Math.abs(j - ej);
				hGrid[i][j].heuristicCost = Math.abs(i - ei) + Math.abs(j - ej);
			}
		}

		grid[ei][ej].finalCost = 0;
		vGrid[ei][ej].finalCost = 0;
		hGrid[ei][ej].finalCost = 0;
	}

	/**
	 * Load the map details into the grids.
	 */
	private void InitializeMap() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				grid[i][j].details = map[i][j];
				vGrid[i][j].details = map[i][j];
				hGrid[i][j].details = map[i][j];
			}
		}
	}

	/**
	 * Calls FileWriter and loads the map into the algorithm.
	 */
	private void LoadMap() {
		this.map = FileRW.loadMap(EXPLORED_MAP);
	}

	/**
	 * Calculate all the cost of the cells in the grid.
	 *
	 * @param voh
	 *            vertical over horizontal?
	 */
	private void Magic(boolean voh) {
		if (voh) {
			open.add(vGrid[si][sj]); // Add the start location to open list
			vGrid[si][sj - 1].heuristicCost += 20;
			vGrid[si][sj + 1].heuristicCost += 20;
		} else {
			open.add(hGrid[si][sj]); // Add the start location to open list
			hGrid[si - 1][sj].heuristicCost += 20;
			hGrid[si + 1][sj].heuristicCost += 20;
		}

		boolean ver = voh;
		Cell current, t;

		while (true) {
			current = open.poll(); // Get cell from the open list

			if (current == null) // If the open list is empty, stop searching
				break;

			if (current.details == 0 || current.details == 2) // Avoid
																// Unexplored
																// and obstacle
				continue;

			if (voh) {
				vClosed[current.i][current.j] = true; // Set cell as covered
				if (current.equals(vGrid[ei][ej]))
					return;
			} else {
				hClosed[current.i][current.j] = true; // Set cell as covered
				if (current.equals(hGrid[ei][ej]))
					return;
			}

			if (current.parent != null) {
				if (current.i == current.parent.i && current.j != current.parent.j && ver)
					ver = false;
				else if (current.i != current.parent.i && current.j == current.parent.j && !ver)
					ver = true;
			}

			if (voh) // If vertical is preferred, calculate the front or bottom
						// first
			{
				if (current.i - 1 >= 0) {
					t = vGrid[current.i - 1][current.j];
					if (!ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, vClosed);
				}

				if (current.i + 1 < vGrid.length) {
					t = vGrid[current.i + 1][current.j];
					if (!ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, vClosed);
				}

				if (current.j - 1 >= 0) {
					t = vGrid[current.i][current.j - 1];
					if (ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, vClosed);
				}

				if (current.j + 1 < vGrid[0].length) {
					t = vGrid[current.i][current.j + 1];
					if (ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, vClosed);
				}
			} else // If horizontal is preferred, calculate the left or right
					// first.
			{
				if (current.j + 1 < hGrid[0].length) {
					t = hGrid[current.i][current.j + 1];
					if (ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, hClosed);
				}

				if (current.j - 1 >= 0) {
					t = hGrid[current.i][current.j - 1];
					if (ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, hClosed);
				}

				if (current.i - 1 >= 0) {
					t = hGrid[current.i - 1][current.j];
					if (!ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, hClosed);
				}

				if (current.i + 1 < hGrid.length) {
					t = hGrid[current.i + 1][current.j];
					if (!ver)
						t.heuristicCost += 200;
					CheckAndUpdateCost(current, t, current.finalCost + CELL_COST, hClosed);
				}
			}

			for (int i = 0; i < vGrid.length; i++) {
				vGrid[i][0].finalCost = 9999;
				vGrid[i][14].finalCost = 9999;
				hGrid[i][0].finalCost = 9999;
				hGrid[i][14].finalCost = 9999;
			}

			for (int i = 0; i < vGrid[0].length; i++) {
				vGrid[0][i].finalCost = 9999;
				vGrid[19][i].finalCost = 9999;
				hGrid[0][i].finalCost = 9999;
				hGrid[19][i].finalCost = 9999;
			}
		}
	}

	/**
	 * Update the grid with fastest path. The fastest path is found by tracing
	 * back from the end point. When parent is not null, trace back. Increment
	 * the steps count every loop. If the robot turns, increment the turns
	 * count.
	 *
	 * @param voh
	 *            vertical over horizontal?
	 * @return the steps count and turns count.
	 */
	private void RoadToLeaderboardNo1(boolean voh) {
		// If there's a grid that is not covered, do not use the grid
		if (voh)
			if (!vClosed[ei][ej])
				return;
			else if (!hClosed[ei][ej])
				return;

		boolean ver = voh;

		Cell current = voh ? vGrid[ei][ej] : hGrid[ei][ej];

		if (voh)
			vGrid[current.i][current.j].details = 8;
		else
			hGrid[current.i][current.j].details = 8;

		while (current.parent != null) {
			if (current.i == current.parent.i && current.j != current.parent.j) {
				if (current.i == ei && current.j == ej)
					ver = false;
				else if (ver) {
					if (voh)
						vDetails[1]++;
					else
						hDetails[1]++;

					ver = false;
				}
			} else if (current.i != current.parent.i && current.j == current.parent.j) {
				if (current.i == ei && current.j == ej)
					ver = true;
				else if (!ver) {
					if (voh)
						vDetails[1]++;
					else
						hDetails[1]++;

					ver = true;
				}
			}

			if (voh) {
				vDetails[0]++;
				current = current.parent;
				vGrid[current.i][current.j].details = 8;
				path.add(0, vGrid[current.i][current.j]);
			} else {
				hDetails[0]++;
				current = current.parent;
				hGrid[current.i][current.j].details = 8;
				path.add(0, hGrid[current.i][current.j]);
			}
		}
	}
}
