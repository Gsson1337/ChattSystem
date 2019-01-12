package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import GUI.GUI;

public class ChatClient {
	//Clientens Reader Skrivare JFrame Textfield o TextArea 
    private BufferedReader in;
    private PrintWriter out;
    private GUI gui;

    public ChatClient() {
    	gui = new GUI(this);
    	
    	try {
			run();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
   
    }
    
    //N�r man startar programmet kmr en JOptionPane ruta d�r man skriver in IP address aka lokal aka 127.0.0.1
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
        	gui.getFrame(),
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
            JOptionPane.QUESTION_MESSAGE);
    }

    //Samma ruta som innan fast man ska skriva in vad man nickkar in-game 
    private String getName() {
        return JOptionPane.showInputDialog(
            gui.getFrame(),
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }
    //Connectar till servern efter man  skrivit in IP och namn 
    private void run() throws IOException {
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
            System.out.println(line);
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                gui.getTextField().setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                gui.getMessageArea().append(line.substring(8) + "\n");
            } else if (line.startsWith("NEWLOGIN")) {
            	//N�r en ny klient ansluter l�ggs den till i friendlist
            	gui.getFriendList().addUserToList(line.substring(9));
            } else if (line.startsWith("LOGOUT")) {
            	//N�r en annan klient disconnectar tas den bort fr�n listan
            	gui.getFriendList().removeUserFromList(line.substring(7));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
    }
	public PrintWriter getOut() {
		return out;
	}
	public void setOut(PrintWriter out) {
		this.out = out;
	}
}