package edu.gwu.brownm04.csci6431.chat.server;

import java.util.Collection;
import java.util.HashSet;

public class ClientThreadMessageHandler {

	private static final Collection<ClientThread> CLIENT_THREADS = new HashSet<ClientThread>();
	
	/**
	 * Adds a client to the list of clients to send messages to.
	 * 
	 * @param clientThread - client thread to add to pool
	 */
	public static void addClient(final ClientThread clientThread) {
		broadcastMessage(clientThread, "Client " + clientThread.getName() + " has connected.", true);
		
		CLIENT_THREADS.add(clientThread);
	}
	
	/**
	 * Removes a client from the list of clients to send messages to.
	 * 
	 * @param clientThread client thread to remove from pool
	 */
	public static void removeClient(final ClientThread clientThread) {
		if (CLIENT_THREADS.contains(clientThread)) {
			CLIENT_THREADS.remove(clientThread);
			
			broadcastMessage(clientThread, "Client " + clientThread.getName() + " has disconnected.", true);
		}
	}
	
	/**
	 * Broadcasts a message to all clients on this server.
	 * 
	 * @param clientThread - client thread broadcasting the message
	 * @param message - message to broadcast to all clients
	 */
	public static void broadcastMessage(final ClientThread clientThread, final String message) {
		broadcastMessage(clientThread, message, false);
	}
	
	/**
	 * Broadcasts a message to all clients on this server with or without a username prepend on the message.
	 * 
	 * @param clientThread - client thread broadcasting the message
	 * @param message - message being broadcast
	 * @param isServerMessage - if true, avoids prepending username to message, else prepends the user name
	 */
	public static void broadcastMessage(final ClientThread clientThread, final String message, boolean isServerMessage) {
		System.out.println((isServerMessage ? "" : clientThread.getName() + ": ") + message);
		CLIENT_THREADS.forEach(
				client -> { if (!client.equals(clientThread)) client.writeMessage((isServerMessage ? "" : clientThread.getName() + ": ") + message); });
	}
}
