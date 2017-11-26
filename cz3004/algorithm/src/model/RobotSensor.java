package model;

/**
 * Configuration for Robot sensors.
 * 
 * Edit the range at RobotData.java
 * 
 * @author Nhat
 *
 */
public class RobotSensor {
	// Ranges for Front, Left and Right sensors
	private int sensorFShortest;
	private int sensorFLongest;
	private int sensorLShortest;
	private int sensorLLongest;
	private int sensorRShortest;
	private int sensorRLongest;

	// Front Sensors
	private boolean leftFrontSensor;
	private boolean middleFrontSensor;
	private boolean rightFrontSensor;

	// Left Sensors
	private boolean topLeftSensor;
	private boolean middleLeftSensor;
	private boolean bottomLeftSensor;

	// Right Sensors
	private boolean topRightSensor;
	private boolean middleRightSensor;
	private boolean bottomRightSensor;

	/**
	 * Determines the range of the sensors
	 * 
	 * @param _sensorFShortest
	 * @param _sensorFLongest
	 * @param _sensorLShortest
	 * @param _sensorLLongest
	 * @param _sensorRShortest
	 * @param _sensorRLongest
	 */
	public RobotSensor(int _sensorFShortest, int _sensorFLongest, int _sensorLShortest, int _sensorLLongest,
			int _sensorRShortest, int _sensorRLongest) {
		setSensorFShortest(_sensorFShortest);
		setSensorFLongest(_sensorFLongest);
		setSensorLShortest(_sensorLShortest);
		setSensorLLongest(_sensorLLongest);
		setSensorRShortest(_sensorRShortest);
		setSensorRLongest(_sensorRLongest);
	}

	/**
	 * @return the sensorFLongest
	 */
	public int getSensorFLongest() {
		return sensorFLongest;
	}

	/**
	 * @return the sensorFShortest
	 */
	public int getSensorFShortest() {
		return sensorFShortest;
	}

	/**
	 * @return the sensorLLongest
	 */
	public int getSensorLLongest() {
		return sensorLLongest;
	}

	/**
	 * @return the sensorLShortest
	 */
	public int getSensorLShortest() {
		return sensorLShortest;
	}

	/**
	 * @return the sensorRLongest
	 */
	public int getSensorRLongest() {
		return sensorRLongest;
	}

	/**
	 * @return the sensorRShortest
	 */
	public int getSensorRShortest() {
		return sensorRShortest;
	}

	/**
	 * @return the bottomLeftSensor
	 */
	public boolean hasBottomLeftSensor() {
		return bottomLeftSensor;
	}

	/**
	 * @return the bottomRightSensor
	 */
	public boolean hasBottomRightSensor() {
		return bottomRightSensor;
	}

	/**
	 * @return the leftFrontSensor
	 */
	public boolean hasLeftFrontSensor() {
		return leftFrontSensor;
	}

	/**
	 * @return the middleFrontSensor
	 */
	public boolean hasMiddleFrontSensor() {
		return middleFrontSensor;
	}

	/**
	 * @return the middleLeftSensor
	 */
	public boolean hasMiddleLeftSensor() {
		return middleLeftSensor;
	}

	/**
	 * @return the middleRightSensor
	 */
	public boolean hasMiddleRightSensor() {
		return middleRightSensor;
	}

	/**
	 * @return the rightFrontSensor
	 */
	public boolean hasRightFrontSensor() {
		return rightFrontSensor;
	}

	/**
	 * @return the topLeftSensor
	 */
	public boolean hasTopLeftSensor() {
		return topLeftSensor;
	}

	/**
	 * @return the topRightSensor
	 */
	public boolean hasTopRightSensor() {
		return topRightSensor;
	}

	/**
	 * @param bottomLeftSensor
	 *            the bottomLeftSensor to set
	 */
	public void setBottomLeftSensor(boolean bottomLeftSensor) {
		this.bottomLeftSensor = bottomLeftSensor;
	}

	/**
	 * @param bottomRightSensor
	 *            the bottomRightSensor to set
	 */
	public void setBottomRightSensor(boolean bottomRightSensor) {
		this.bottomRightSensor = bottomRightSensor;
	}

	/**
	 * @param leftFrontSensor
	 *            the leftFrontSensor to set
	 */
	public void setLeftFrontSensor(boolean leftFrontSensor) {
		this.leftFrontSensor = leftFrontSensor;
	}

	/**
	 * @param middleFrontSensor
	 *            the middleFrontSensor to set
	 */
	public void setMiddleFrontSensor(boolean middleFrontSensor) {
		this.middleFrontSensor = middleFrontSensor;
	}

	/**
	 * @param middleLeftSensor
	 *            the middleLeftSensor to set
	 */
	public void setMiddleLeftSensor(boolean middleLeftSensor) {
		this.middleLeftSensor = middleLeftSensor;
	}

	/**
	 * @param middleRightSensor
	 *            the middleRightSensor to set
	 */
	public void setMiddleRightSensor(boolean middleRightSensor) {
		this.middleRightSensor = middleRightSensor;
	}

	/**
	 * @param rightFrontSensor
	 *            the rightFrontSensor to set
	 */
	public void setRightFrontSensor(boolean rightFrontSensor) {
		this.rightFrontSensor = rightFrontSensor;
	}

	/**
	 * @param sensorFLongest
	 *            the sensorFLongest to set
	 */
	public void setSensorFLongest(int sensorFLongest) {
		this.sensorFLongest = sensorFLongest;
	}

	/**
	 * @param sensorFShortest
	 *            the sensorFShortest to set
	 */
	public void setSensorFShortest(int sensorFShortest) {
		this.sensorFShortest = sensorFShortest;
	}

	/**
	 * @param sensorLLongest
	 *            the sensorLLongest to set
	 */
	public void setSensorLLongest(int sensorLLongest) {
		this.sensorLLongest = sensorLLongest;
	}

	/**
	 * @param sensorLShortest
	 *            the sensorLShortest to set
	 */
	public void setSensorLShortest(int sensorLShortest) {
		this.sensorLShortest = sensorLShortest;
	}

	/**
	 * @param sensorRLongest
	 *            the sensorRLongest to set
	 */
	public void setSensorRLongest(int sensorRLongest) {
		this.sensorRLongest = sensorRLongest;
	}

	/**
	 * @param sensorRShortest
	 *            the sensorRShortest to set
	 */
	public void setSensorRShortest(int sensorRShortest) {
		this.sensorRShortest = sensorRShortest;
	}

	/**
	 * @param topLeftSensor
	 *            the topLeftSensor to set
	 */
	public void setTopLeftSensor(boolean topLeftSensor) {
		this.topLeftSensor = topLeftSensor;
	}

	/**
	 * @param topRightSensor
	 *            the topRightSensor to set
	 */
	public void setTopRightSensor(boolean topRightSensor) {
		this.topRightSensor = topRightSensor;
	}
}
