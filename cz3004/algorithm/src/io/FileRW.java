/**
 * 
 */
package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import fastestPath.Cell;
import model.Map;
import model.MapCell;
import model.MapParam;

/**
 * A singleton class that handles file I/O
 * 
 * @author Leroy Lim
 *
 */
public class FileRW {

	private static FileRW fileRW = new FileRW();

	public static FileRW getInstance() {
		return fileRW;
	}

	/**
	 * Takes in a txt file and updates the Map given
	 * 
	 * Legend for the txt
	 * <ul>
	 * <li>0 - Unexplored</li>
	 * <li>1 - Explored</li>
	 * <li>2 - Obstacle</li>
	 * <li>8 - Fastest Path</li>
	 * </ul>
	 * 
	 * @param map
	 *            Map object to be updated
	 * @param fileName
	 *            file name of the text file containing the map data
	 */
	public static void loadMap(Map map, String fileName) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s;
		int r = 0;

		try {
			while ((s = br.readLine()) != null) {
				for (int i = 0; i < 15; i++) {
					if ((s.charAt(i) - '0') == 2) {
						map.getMapCell()[r + 1][i + 1].setState(2);
						map.getMapCell()[r + 1][i + 1].setPriority(9999);
					}
				}

				r++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Takes in a txt file and load the map data into a 2D int array
	 * 
	 * Legend for the txt
	 * <ul>
	 * <li>0 - Unexplored</li>
	 * <li>1 - Explored</li>
	 * <li>2 - Obstacle</li>
	 * <li>8 - Fastest Path</li>
	 * </ul>
	 * 
	 * @param fileName
	 *            file name of the text file containing the map data
	 * @return a 2D int array containing the map data
	 */
	public static int[][] loadMap(String fileName) {
		int[][] map = new int[MapParam.NUM_ROWS - 2][MapParam.NUM_COLS - 2];
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s;
		int r = 0;

		try {
			while ((s = br.readLine()) != null) {
				for (int i = 0; i < 15; i++)
					map[r][i] = s.charAt(i) - '0';

				r++;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Reads array and convert to Fast Path Input Format
	 * 
	 * <ul>
	 * <li>0 - Unexplored</li>
	 * <li>1 - Free</li>
	 * <li>2 - Obstacle</li>
	 * </ul>
	 * 
	 * @param map
	 *            MapCell array
	 * @param fileName
	 *            name of file to be stored
	 */
	public static void storeExploredMap(MapCell[][] map, String fileName) {
		String line;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 1; i < MapParam.NUM_ROWS - 1; i++) {
			line = new String();
			for (int j = 1; j < MapParam.NUM_COLS - 1; j++) {
				if (map[i][j].getState() == 1 || map[i][j].getState() == 3) {
					line = line.concat("1");
				} else if (map[i][j].getState() == 2) {
					line = line.concat("2");
				}

				else {
					// map[i][j].getState() == 0
					line = line.concat("0");
				}
			}
			// Write the row into file
			try {
				bw.write(line);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads array and convert to Fast Path Output Format
	 * 
	 * <ul>
	 * <li>0 - Unexplored</li>
	 * <li>1 - Free</li>
	 * <li>2 - Obstacle</li>
	 * <li>8 - Fastest Path</li>
	 * </ul>
	 * 
	 * @param map
	 *            MapCell array
	 * @param fileName
	 *            name of file to be stored
	 */
	public static void storeFastestPathMap(Cell[][] map, String fileName) {
		String line;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < MapParam.NUM_ROWS - 2; i++) {
			line = new String();
			for (int j = 0; j < MapParam.NUM_COLS - 2; j++) {
				if (map[i][j].getDetails() == 2) // Obstacle
					line = line.concat("2");
				else if (map[i][j].getDetails() == 0) // Unexplored
					line = line.concat("0");
				else if (map[i][j].getDetails() == 8) // Fastest path
					line = line.concat("8");
				else // Path
					line = line.concat("1");
			}
			// Write the row into file
			try {
				bw.write(line);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stores the given string into a text file for storage purposes
	 * 
	 * @param fileName
	 *            name of the output text file
	 */
	public static void storeStringText(String fileName, String text) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);

		try {
			bw.write(text);
			bw.close();
			fw.close();
			System.out.println("File created: " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileRW() {

	}
}
