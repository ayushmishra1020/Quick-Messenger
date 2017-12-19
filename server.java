package messenger;
import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class server extends JFrame {
	private JTextField usertext;
	private JTextArea chatwindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//constructor
	public server() {
		super("Quick Messenger");
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
		add(new JScrollPane(chatwindow));
		setSize(800,400);
		setVisible(true);
	}
	
	//set up and run the server
	public void startrunning() {
		try {
			server= new ServerSocket(6789,100);
			while(true) {
				try {
					//connect and have conversation
					waitforConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException) {
					showMessage("\nServer ended the connection!");
				}finally {
					closeCrap();
				}
			}
		}catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	

	


	//wait for connection and then display connection information
	private void waitforConnection() throws IOException {
		showMessage("waiting for someone to connect...\n");
		connection=server.accept();
		showMessage("Now connected to "+ connection.getInetAddress().getHostAddress());
	}
	
	//get stream to send and receive data
	private void setupStreams() throws IOException {
		output= new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input= new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams are now setup! \n");
	}
	
	//during the chat conversation
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
				showMessage("no message sent!");
			}
		}while(!message.equals("CLIENT - END"));
		
	}
	
	//close streams and sockets when you are done chatting
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
	
	//send message to client
	public void sendMessage(String message) {
		try {
			output.writeObject("Server - "+ message);
			output.flush();
			showMessage("\nServer - "+ message);
			
		}catch(IOException ioException) {
			chatwindow.append("ERROR: MESSAGE CAN`T BE SEND");
		}
	}
	
	//updates chatwindow
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatwindow.append(text);
				}
			}	
		);
	}
	
	//let the user type stuff into their box
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
