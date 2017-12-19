
import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class client extends JFrame {
	private JTextField usertext;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message="";
	private String serverIP;
	private Socket connection;
	
	//constructor
	public client(String host) {
		super("CLient Side");
		serverIP=host;
		usertext=new JTextField();
		usertext.setEditable(false);
		usertext.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					sendMessage(event.getActionCommand());
					usertext.setText("");
					}
				}
			);
		add(usertext,BorderLayout.NORTH);
		chatwindow=new JTextArea();
		add(new JScrollPane(chatwindow),BorderLayout.CENTER);
		setSize(800,400);
		setVisible(true);
	}
	
	//connect to server
	public void startrunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException) {
			showMessage("Client terminated connection!");
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}finally {
			closeCrap();
		}
	}
	
	//connect to server
	private void connectToServer() throws IOException {
		showMessage("Attempting Connection\n");
		connection = new Socket(InetAddress.getByName(serverIP),6789 );
		showMessage("Connected to: "+ connection.getInetAddress().getHostAddress());
	}
	
	
	

	

	//setup streams to send and receive messages
	private void setupStreams() throws IOException {
		output= new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input= new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams are now setup! \n");
	}
	
	//while chatting with server
	private void whileChatting() throws IOException{
		String message="you are now connected!\n";
		showMessage(message);
		ableToType(true);
		do {
			//have a coversation
			try {
				message=(String)input.readObject();
				showMessage("\n"+ message);
			}catch(ClassNotFoundException classNotFoundException ) {
				showMessage("\n Don`t know object type!");
			}
		}while(!message.equals("SERVER - END"));
	}
	
	//close the streams and sockets
	public void closeCrap() {
		showMessage("\n Closing Connections \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	//send messages to server
	public void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - "+ message);
			output.flush();
			showMessage("\nCLIENT - "+ message);
			
		}catch(IOException ioException) {
			chatwindow.append("ERROR: MESSAGE CAN`T BE SEND");
		} 
	}
	
	//change/update chatwindow
	private void showMessage(final String m) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatwindow.append(m);
				}
			}	
		);
	}
	
	//gives user the permission to type messages into the text box
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						usertext.setEditable(tof);
				}
			}	
		);
	}
}
