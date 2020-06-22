package directPlaying.testOneClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;

import model.Player;

public class OneClientMain {
	public static void main(String[] args) throws ClassNotFoundException {
		JFrame frame;
		String ip = "127.0.0.1";
		int port = 6789;
		String fromServer;
		Socket clientSocket;
		try {
			clientSocket = new Socket(ip, port);
			DataInputStream inFromServer = new DataInputStream(
					clientSocket.getInputStream());
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			//outToServer.writeBytes("c\n");

			// byte[] byteFromServer = null;
			// inFromServer.readFully(byteFromServer);
			// fromServer = Deserialize.deserializeString(byteFromServer);
			while((fromServer = inFromServer.readLine())!=null){
				
			}
			fromServer = inFromServer.readLine();
			// System.out.println("FROM SERVER: " + fromServer);
			frame = new JFrame();
//			Player player = new Player(fromServer);
//			Home home = new Home(inFromServer, outToServer, player, frame);
			// home.displayHome(fromServer, frame);
			// clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}

	}
}
