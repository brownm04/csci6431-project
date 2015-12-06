package edu.gwu.brownm04.csci6431.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 4567);
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			/* listen on stdIn for input from this client */
			new Thread() {
				public void run() {
					try {
						String fromStdIn;
						while ((fromStdIn = stdIn.readLine()) != null) {
							out.println(fromStdIn);
						}
					} catch (IOException e) {
						try {
							socket.close();
						} catch (IOException e1) {
							System.err.println("Error closing socket after error from standard in.");
							e1.printStackTrace();
						}
					}
				};
			}.start();
			
			/* listen on socket input stream for new messages to write out */
			new Thread() {
				public void run() {
					try {
						String fromServer;
						while ((fromServer = in.readLine()) != null) {
							System.out.println(fromServer);
						}
					} catch (IOException e) {
						try {
							socket.close();
						} catch (IOException e1) {
							System.err.println("Error closing socket after connection reset from server.");
							e1.printStackTrace();
						}
					}
				}
			}.start();
		} catch (IOException e) {
			System.err.println("Error connecting to server on port 4567, server may not be listening.");
			e.printStackTrace();
		}
	}
}
