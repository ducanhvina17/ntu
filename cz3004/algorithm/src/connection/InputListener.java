package connection;

public interface InputListener {

	/**
	 * Handler that determines what action to execute
	 * 
	 * @param s
	 *            String that is received from buffer
	 */
	void onReceive(String s);
}
