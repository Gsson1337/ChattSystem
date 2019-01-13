package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {
	private String name;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			while (true) {
				out.println("SUBMITNAME");
				name = in.readLine();
				if (name == null) {
					return;
				}
				synchronized (ChatServer.names) {
					if (!ChatServer.names.contains(name)) {
						ChatServer.ListNames.add(name);
						ChatServer.names.add(name);
						System.out.println("namn ok");
						break;
					}
				}
			}
			out.println("NAMEACCEPTED");
			// Prints the new clients name to all the already connected users
			for (String oldName : ChatServer.ListNames) {
				if (!oldName.equals(this.name)) {
					out.println("NEWLOGIN " + oldName);
				}
			}
			// Prints the new clients name to the already existing ones
			for (PrintWriter writer : ChatServer.writers) {
				writer.println("NEWLOGIN " + name);
			}

			ChatServer.ListWriters.add(out);
			ChatServer.writers.add(out);

			// MESSAGE LOOP
			while (true) {
				String input = in.readLine();
				System.out.println(input);
				System.out.println(input);
				if (input == null) {
					return;
				} else if (input.startsWith("!!") && input.matches(".*\\s+.*")) {
					System.out.println(name);
					//ITS ALL FUCKED UPP
					String namn = input.substring(input.indexOf("!!") + 2, input.indexOf(" "));
					System.out.println("namn: " + namn);
					
					input = input.substring(input.indexOf(" "));
					int i = 0;
					if(ChatServer.names.contains(namn)) {
						for (String str : ChatServer.ListNames) {
							if (str.trim().contains(namn)) {
									PrintWriter writer = ChatServer.ListWriters.get(i);
									writer.println("PRIVATEMESSAGE " + "Private Message From " + name + ": " + input);
								} else if (str.trim().contains(name)) {
									PrintWriter writer = ChatServer.ListWriters.get(i);
									writer.println("PRIVATEMESSAGE " + "Private Message To " + namn + ": " + input);
								}
							i++;
						}
					}else {
						for (String str : ChatServer.ListNames) {
							if (str.trim().contains(name)) {
								PrintWriter writer = ChatServer.ListWriters.get(i);
								writer.println("PRIVATEMESSAGE " + "User: " + namn + ", doesn�t exist, you have no friends");
								}
							i++;
						}
					}
				} else if (input.startsWith("GIF")) {
					for (PrintWriter writer : ChatServer.writers) {
						writer.println("GIF " + input.substring(3));
					}
				} else {
					for (PrintWriter writer : ChatServer.writers) {
						// Global message writes name : then input
						
						writer.println("GLOBALMESSAGE " + name + ": " + input + " ");
						
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			// N�r klienten st�nger ner
			// When the client exits the chat
			if (name != null) {
				// Sending to all the connectiong users that a client has logged out
				for (PrintWriter writer : ChatServer.writers) {
					writer.println("LOGOUT " + name);
				}
				ChatServer.names.remove(name);
				ChatServer.ListNames.remove(name);
			}
			if (out != null) {
				ChatServer.writers.remove(out);
				ChatServer.ListWriters.remove(out);
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}
