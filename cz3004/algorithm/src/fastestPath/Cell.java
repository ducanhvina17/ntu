package fastestPath;

public class Cell {
	int i, j;
	int heuristicCost = 0; // Heuristic cost
	int finalCost = 0; // G + H
	Cell parent;
	int details;

	Cell(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public int getDetails() {
		return this.details;
	}

	public int getI() {
		return this.i;
	}

	public int getJ() {
		return this.j;
	}

	@Override
	public String toString() {
		return "[" + this.i + ", " + this.j + "]";
	}
}
