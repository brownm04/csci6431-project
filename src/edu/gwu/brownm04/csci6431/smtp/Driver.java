package edu.gwu.brownm04.csci6431.smtp;

import java.io.IOException;

public class Driver {
	public static void main(String[] args) {
		String message = "This is a test message.";
		
		for (int i = 0; i < args.length - 1; ++i) {
			if (args[i] != null && args[i].startsWith("-")) {
				switch (args[i]) {
					case "-host":
						System.setProperty(Code1Props.SMTP_HOST_KEY, args[i+1]);
						++i;
						break;
					case "-port":
						System.setProperty(Code1Props.SMTP_PORT_KEY, args[i+1]);
						++i;
						break;
					case "-user":
						System.setProperty(Code1Props.SMTP_USER_KEY, args[i+1]);
						++i;
						break;
					case "-password":
						System.setProperty(Code1Props.SMTP_PASS_KEY, args[i+1]);
						++i;
						break;
					case "-message":
						message = args[i+1];
						++i;
						break;
					default:
						
				}
			}
		}

		try {
			SmtpHandler.sendMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
