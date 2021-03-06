package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Client.AbstractClient;

public abstract class AbstractGUI {

	protected JFrame frame;
	protected JTextField textField;
	protected JTextArea messageArea;

	protected JButton help;
	private AbstractGUI self;
	private JButton likeButton;
	
	public AbstractGUI(AbstractClient client, String title) {

		frame = new JFrame(title);
		self = this;
		// Textfield where you enter your messages
		textField = new JTextField(40);
		textField.setEditable(false);		
		likeButton = new JButton("Like");
		JPanel sendPanel = new JPanel();
		sendPanel.add(textField);
		sendPanel.add(likeButton);
		
		likeButton.addActionListener(e-> {
			client.getOut().println("GIF " + "https://i.imgur.com/WljaRxX.gif");
			System.out.println("Thumbs Up Bro");
			likeButton.setEnabled(false);
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException ex) {
					}
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								likeButton.setEnabled(true);
							}
						});
					} catch (InvocationTargetException ex) {
					} catch (InterruptedException ex) {
					}
				}
			}).start();
		}); 

		// Textfield actionListener if there is any specific texts funny things will

		// happen, else just writes message and set the textfield to empty
		textField.addActionListener(e->{
					switch (textField.getText().trim().toLowerCase()) {
						case "/dance":	
						client.getOut().println("GIF " + "https://i.makeagif.com/media/3-27-2016/xHLL7Y.gif");break;
						case"/shutup": 
							client.getOut().println("GIF " + "https://i.imgur.com/HB7qjnW.gif");break;
						case "/gay" : 
							client.getOut().println("GIF " + "https://thumbs.gfycat.com/ImaginativeSecondhandHamadryas-size_restricted.gif");break;
						case "/740" : 
							client.getOut().println("GIF " + "https://thumbs.gfycat.com/GoodSimpleGermanspaniel-max-1mb.gif");break;
						default: break;	
					}

				if (!textField.getText().equalsIgnoreCase("")) {
					System.out.println("gui textfield");
					client.getOut().println(textField.getText());
					textField.setText("");
				}
		});

		// The messageArea a JTextArea where all the messages appears
		messageArea = new JTextArea(8, 40);
		messageArea.setEditable(false);
		help = new JButton("?");
		help.setSize(10, 20);
		help.addActionListener(e -> {
			JOptionPane.showMessageDialog(frame, "To send Private Message Write: \n!!name whitespace then message\n"
					+ "To send funny gifs wirte /nameOfGif");
		});

		// new font
		Font f = new Font("Comic Sans MS", Font.PLAIN, 15);

		// Setting the text in the textfield to black and adding font to both textfield
		// and messageArea
		textField.setForeground(Color.black);
		textField.setFont(f);
		messageArea.setFont(f);

		// Frame layout
		frame.getContentPane().add(help, "South");
		frame.getContentPane().add(sendPanel, "South");
		frame.getContentPane().add(new JScrollPane(messageArea), "Center");

		// frame settings, pack, visible, and close.
		frame.setSize(700, 350);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// Getters and setters for Textfield, MessageArea, JFrame
	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextArea getMessageArea() {
		return messageArea;
	}

	public void setMessageArea(JTextArea messageArea) {
		this.messageArea = messageArea;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
