package edu.gwu.brownm04.csci6431.smtp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SmtpHandler {
	
	public static void sendMessage(final String message) throws IOException {
		String smtpHost = System.getProperty(Code1Props.SMTP_HOST_KEY, "smtp.gmail.com");
		int smtpPort = Integer.parseInt(System.getProperty(Code1Props.SMTP_PORT_KEY, "465"));
		
		try {
			/* open a secure socket to the server */
			SSLSocket socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(InetAddress.getByName(smtpHost), smtpPort);
			
			/* encode the username and password in Base64 for SMTP */
			Encoder base64Encoder = Base64.getEncoder();
			String smtpUser = base64Encoder.encodeToString("USERNAMEHERE".getBytes());
			String smtpPass = base64Encoder.encodeToString("PASSWORDHERE".getBytes());
			
			/* set up the input and output streams to the socket */
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			BufferedReader is = new BufferedReader(new InputStreamReader(dis));
			
			try {
				/* send the email message */
				smtp("HELO " + smtpHost, os, is);					// identifying the SMTP server
				smtp("AUTH LOGIN", os, is);							// specifying I'm adding credentials to login
				smtp(smtpUser, os, is);								// Base64 username
				smtp(smtpPass, os, is);								// Base64 password
				smtp("MAIL FROM: <foo@gmail.com>", os, is);			// specifying who the email is from
				smtp("RCPT TO: <bar@gmail.com>", os, is);			// specifying who I want to send email to
				smtp("DATA", os, is);								// denoting I'm adding email data now
				smtp("Subject: Email Test", os, is);				// adding the email subject
				smtp("This is a test email\r\n.\r\nQUIT", os, is);	// adding the email contents
			} catch (IOException e) {
				System.err.println("Problem sending SMTP command to server");
				e.printStackTrace();
			}
			
			/* clean up the open resources */
			is.close();
			os.close();
			
			socket.close();
		} catch (IOException e) {
			System.err.println("Problem establishing a secure socket connection to server.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message over the given output stream and expects an SMTP response
	 * on the given input stream.
	 * 
	 * @param message - message to write to output stream
	 * @param os - output stream
	 * @param is - input stream
	 * @throws IOException if the response is null or erroneous.
	 */
	private static void smtp(String message, OutputStream os, BufferedReader is) throws IOException {
		/* send the message */
		System.out.println("Sending: " + message);
		os.write((message + "\r\n").getBytes());
		
		/* get the response from the server */
		String inLine = is.readLine();
		
		/* Make sure we got content back */
		if (inLine != null) {
			inLine.trim();
			System.out.println("Response: " + inLine);
			
			/* check if we got an error status back (4xx or 5xx) */
			if (inLine.startsWith("4") || inLine.startsWith("5")) {
				throw new IOException("Error occurred in message reception.");
			}
		} else {
			throw new IOException("Response was null.");
		}
	}
}
