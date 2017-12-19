
import javax.swing.JFrame;
import java.net.*;
import java.io.*;
import java.util.*;


public class clienttest {
	public static void main(String args[]) throws Exception{
		InetAddress localhost = InetAddress.getLocalHost(); 
		client onclient= new client((localhost.getHostAddress()).trim());
		onclient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		onclient.startrunning();
	}
	
}
