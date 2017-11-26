package connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class pcConnection {
	private PrintWriter pw;
	private Scanner sc;
	private Socket socket;
	private InputListener il = null;

	/**
	 * Connects to the rpi upon initialization of the class.
	 */
	public pcConnection(InputListener il) {
		initCommunication();
		this.il = il;
	}

	/**
	 * Close the connection.c
	 * 
	 * @throws IOException
	 */
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InputListener getListener() {
		return this.il;
	}

	/**
	 * Initiates the communication between PC and RPi
	 */
	private void initCommunication() {
		try {
			socket = new Socket("192.168.12.12", 6666);
			pw = new PrintWriter(socket.getOutputStream());
			sc = new Scanner(socket.getInputStream());
		} catch (ConnectException e) {
			System.out.println("Communicator Module: Connection timed out. Attempting reconnection...");
			initCommunication();
		} catch (SocketException e) {
			System.out.println("Communicator Module: Connection timed out. Attempting reconnection...");
			initCommunication();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if PC is connected
	 * 
	 * @return status of connection of PC
	 */
	public boolean isConnected() {
		return this.socket.isConnected();
	}

	/**
	 * Receives message from rpi.
	 * 
	 * Reads the message line by line, separated by \n character.
	 * 
	 * Attempts to reconnect upon losing connection.
	 * 
	 * @return string received.
	 * @throws IOException
	 */
	public void receive() {
		/*
		 * int numBytes; b = new byte[1024];
		 */
		if (socket.isConnected())
			System.out.println("Communicator Module: Socket connected");
		else
			System.out.println("Communicator Module: Socket not connected");

		while (true) {
			// System.out.println("In read while loop");
			try {
				String s = sc.nextLine();
				il.onReceive(s);
			} catch (Exception e) {
				System.out.println("Communicator Module: Receive failed...Reconnecting...");
				initCommunication();
				receive();
			}
		}

	}

	/**
	 * Sends a message to rpi.
	 * 
	 * Will not send any String messages if command to send is an empty string
	 * or null object
	 * 
	 * @param receiver_id
	 *            0 - Android ; 1 - Arduino
	 * @param msg
	 *            string to be sent.
	 * @throws IOException
	 */
	public void send(int receiver_id, String msg) {
		// If empty string or null object, do not send out
		if (msg == "" || msg == null) {
			System.out.println("Communicator Module: No message to send.");
		}
		// Else, send out message
		else {
			msg = "2" + receiver_id + msg;
			try {
				System.out.println("Communicator Module: Sending message: " + msg);
				pw.println(msg);
				pw.flush();
				// Thread.sleep(20); // Delay of 20ms to prevent concatenate of
				// messages
			} catch (Exception e) {
				System.out.println("Communicator Module: Connection lost. Re-connecting...");
				System.out.println("Communicator Module: Error message: " + e.getMessage());
				close();
				initCommunication();
			}
		}
	}
}
