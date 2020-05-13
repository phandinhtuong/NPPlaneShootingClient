package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import view.Home;
import view.Home2;

public class Controller {

	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		Socket clientSocket = new Socket(ip, 6789);
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		String fromServer = inFromServer.readLine();
		//System.out.println("FROM SERVER: " + fromServer);
		Home2 home = new Home2();
		home.displayHome(fromServer);
		clientSocket.close();
	}
}
