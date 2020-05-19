package controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.swing.JFrame;

import objectByteTransform.Deserialize;
import model.Room;
import model.RoomList;
import view.Home;
import view.ListRoom;
import view.NewRoomCreated;

public class Controller {
	private static JFrame frame;
	static String ip = "127.0.0.1";
	static int port = 6789;
	static String fromServer;
	public static void main(String[] args) throws ClassNotFoundException {
		
		Socket clientSocket;
		try {
			clientSocket = new Socket(ip, port);
			DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.writeBytes("c\n");
			
//			byte[] byteFromServer = null;
//			inFromServer.readFully(byteFromServer);
//			fromServer = Deserialize.deserializeString(byteFromServer);
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
	public void createRoom(DataInputStream inFromServer,DataOutputStream outToServer, JFrame frame,String playerID){
		
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
	public void listRoom(DataInputStream inFromServer,DataOutputStream outToServer, JFrame frame,String playerID) throws ClassNotFoundException{
		
			try {
				outToServer.writeBytes("l\n");
			
			RoomList roomList;
			Room room;
//			while((roomList=Deserialize.deserialize(inFromServer.readf))!=null){
//				
//			}
			byte[] byteInFromServer = null;
			int i = 0;
			while((i=inFromServer.readInt())!=0){
				System.out.println(i);
				byteInFromServer = new byte[i];
				//System.out.println(fromServer);
				//byteInFromServer = fromServer.getBytes();
				//fromServer=inFromServer.readLine()
						
			inFromServer.read(byteInFromServer);
				break;
			}
			System.out.println(Arrays.toString(byteInFromServer));
			roomList = Deserialize.deserializeRoomList(byteInFromServer);
			System.out.println(roomList.get(0).getRoomID());
			ListRoom listRoom = new ListRoom(playerID, frame, roomList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
