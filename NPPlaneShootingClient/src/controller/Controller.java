package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JFrame;

import view.Home;

public class Controller {
	private static JFrame frame;
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int port = 6789;
		Socket clientSocket;
		try {
			clientSocket = new Socket(ip, port);
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			String fromServer = inFromServer.readLine();
			//System.out.println("FROM SERVER: " + fromServer);
			frame = new JFrame();
			Home home = new Home(fromServer, frame);
			//home.displayHome(fromServer, frame);
			clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
	}
	public static void createRoom(){
		
	}
}
