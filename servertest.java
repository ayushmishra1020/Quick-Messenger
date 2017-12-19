package messenger;
import javax.swing.JFrame;

public class servertest {
	public static void main(String args[]){
		server onserver= new server();
		onserver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		onserver.startrunning();
	}
	
}
