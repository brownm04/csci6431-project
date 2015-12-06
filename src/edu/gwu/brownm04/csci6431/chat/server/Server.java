package edu.gwu.brownm04.csci6431.chat.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

	public static void main(String[] args) {
		int portNumber = 4567;
		
		try {
			System.out.println("Attempting to start server listening on port " + portNumber);
			
			ServerSocket serverSocket = new ServerSocket(portNumber);
			
			System.out.println("Server listening on port " + portNumber);
			
			try {
				/* continue listening and accepting clients */
				while (true) {
					new ClientThread(serverSocket.accept()).start();
				}
			} catch (IOException e) {
				System.err.println("Problem accepting a client connection.");
				serverSocket.close();
			}
		} catch (IOException e) {
			System.err.println("Could not start the server listening on port " + portNumber);
			e.printStackTrace();
		}
	}
}
