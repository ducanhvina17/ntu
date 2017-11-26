package connection;

/**
 * Class handling communications between PC and RPi
 * 
 * @author Leroy
 * @author Yaqian
 *
 */
public class CommunicatorRpi {
	private static CommunicatorRpi instance = new CommunicatorRpi();
	public static CommunicatorRpi getInstance() {
		return CommunicatorRpi.instance;
	}

	private pcConnection conn;

	private CommunicatorRpi() {
		conn = null;
		InputListener il = new InputReceive();

		conn = new pcConnection(il);

	}

	public void closeConnection() {
		conn.close();
	}

	public pcConnection getConn() {
		return this.conn;
	}

	public boolean isConnected() {
		return this.conn.isConnected();
	}
}
