package edu.gwu.brownm04.csci6431.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

/**
 * Client thread that handles listening for messages from a specific client
 * and writing messages to a given client.
 * 
 * @author snes
 *
 */
public class ClientThread extends Thread {
	
	private final Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	/**
	 * Name the thread and assign the socket.
	 * 
	 * @param socket - socket the client is connecting to
	 */
	public ClientThread(final Socket socket) {
		super("ClientThread-" + UUID.randomUUID());
		this.socket = socket;
		
		ClientThreadMessageHandler.addClient(this);
	}
	
	/**
	 * Writes a message to the socket using the socket's output stream.
	 * 
	 * @param message - message to write to client
	 */
	public void writeMessage(final String message) {
		if (out != null) {
			out.println(message);
		}
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out.println("Welcome to the chat server. Your ID is " + getName());
			
			/* handle the incoming stream from the client */
			new Thread() {
				public void run() {
					try {
						String fromClient;
						while ((fromClient = in.readLine()) != null) {
							ClientThreadMessageHandler.broadcastMessage(ClientThread.this, fromClient);
						}
					} catch (IOException e) {
						System.out.println("AAAAH");
						/* remote closed connection, remove from broadcast pool and clean up */
						ClientThreadMessageHandler.removeClient(ClientThread.this);
						
						out = null;
						in = null;
						
						try {
							ClientThread.this.socket.close();
						} catch (IOException e1) {
							System.err.println("Problem closing the socket for client " + ClientThread.this.getName());
							e1.printStackTrace();
						}
					}
				};
			}.start();
		} catch (IOException e) {
			out = null;
			in = null;
			
			System.err.println("Problem opening outputStream to client socket.");
			e.printStackTrace();
		}
	}
}
