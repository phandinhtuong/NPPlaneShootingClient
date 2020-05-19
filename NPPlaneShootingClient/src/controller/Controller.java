package controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFrame;

import objectByteTransform.Deserialize;
import model.RoomList;
import view.Home;
import view.NewRoomCreated;

public class Controller {
	private static JFrame frame;
	static String ip = "127.0.0.1";
	static int port = 6789;
	static String fromServer;
	public static void main(String[] args) {
		
		Socket clientSocket;
		try {
			clientSocket = new Socket(ip, port);
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.writeBytes("c\n");
			
			
			fromServer = inFromServer.readLine();
			//System.out.println("FROM SERVER: " + fromServer);
			frame = new JFrame();
			Home home = new Home(inFromServer,outToServer,fromServer, frame);
			//home.displayHome(fromServer, frame);
			//clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
	}
	public void createRoom(BufferedReader inFromServer,DataOutputStream outToServer, JFrame frame,String playerID){
		
		try{
			//clientSocket = new Socket(ip,port);
			
			outToServer.writeBytes("r\n");
			while((fromServer = inFromServer.readLine())!=null){
				System.out.println(fromServer);
				NewRoomCreated newRoom = new NewRoomCreated(playerID, frame, fromServer);
				break;
			}
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public void listRoom(BufferedReader inFromServer,DataOutputStream outToServer, JFrame frame,String playerID){
		try{
			outToServer.writeBytes("l\n");
			RoomList roomList;
//			while((roomList=Deserialize.deserialize(inFromServer.read))!=null){
//				
//			}
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
