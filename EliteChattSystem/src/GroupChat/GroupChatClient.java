package GroupChat;

import java.io.IOException;

import Client.AbstractClient;
import Client.ChatClient;
import Client.ChatCommands;
import GUI.GroupChatGUI;

/**
 * 
 * @author alejyb1
 *
 */
public class GroupChatClient extends AbstractClient implements Runnable {
	private Thread thread;

	/**
	 * @param serverAdress
	 *            (groupchatserver IpAdress for socket connection)
	 * @param port
	 *            (groupchatserver port)
	 */
	public GroupChatClient(String serverAdress, int port) {
		super(serverAdress, port);
		System.out.println("groupchatclient cons" + Thread.currentThread());
		gui = new GroupChatGUI(this, "GROUPCHAT");
		thread = new Thread(this);
		thread.start();
	}

	public void run() {

		// Process all messages from server, according to the protocol.
		out.println(ChatClient.getUsername());
		try {
			while (true) {
				System.out.println("groupchat run while " + Thread.currentThread());
				String line = in.readLine();
				System.out.println("line : " + line);
				ChatCommands.inputCommandsGroup(out, line, this);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GroupChatGUI getGUI() {
		return (GroupChatGUI) gui;
	}
}
