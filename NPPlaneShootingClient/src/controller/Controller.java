package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFrame;

import model.Player;
import model.Room;
import model.RoomList;
import objectByteTransform.Deserialize;
import objectByteTransform.Serialize;
import view.Home;
import view.JoinRoom;
import view.ListRoom;
import view.CreateNewRoom;

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
			Player player = new Player(fromServer);
			Home home = new Home(inFromServer,outToServer,player, frame);
			//home.displayHome(fromServer, frame);
			//clientSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		
	}
	public void createRoom(DataInputStream inFromServer,DataOutputStream outToServer, JFrame frame,Player player) throws ClassNotFoundException{
		
		try{
			//clientSocket = new Socket(ip,port);
			
			outToServer.writeBytes("r\n");
			while((fromServer = inFromServer.readLine())!=null){
				System.out.println(fromServer);
				Room room = new Room(fromServer,player);
				CreateNewRoom newRoom = new CreateNewRoom(inFromServer,outToServer,player, frame, room);
				break;
			}
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public void listRoom(DataInputStream inFromServer,DataOutputStream outToServer, JFrame frame,Player player) throws ClassNotFoundException{
		
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
			//System.out.println(Arrays.toString(byteInFromServer));
			roomList = Deserialize.deserializeRoomList(byteInFromServer);
//			for(i=0;i<roomList.size();i++){
//					System.out.println(Integer.toString(i));
//				System.out.println(roomList.get(i).getRoomID());
//			}
			//System.out.println(roomList.get(0).getRoomID());
			ListRoom listRoom = new ListRoom(inFromServer,outToServer,player, frame, roomList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public void joinRoom(DataInputStream inFromServer,DataOutputStream outToServer,Player player,String roomID,JFrame frame) throws ClassNotFoundException, IOException{
		Room room = updatePlayerInRoom(inFromServer, outToServer, player,roomID);
		JoinRoom joinRoom = new JoinRoom(inFromServer, outToServer, player, frame, room);
	}
	public Room updatePlayerInRoom(DataInputStream inFromServer,DataOutputStream outToServer,Player player,String roomID) throws IOException, ClassNotFoundException{
		
		
		outToServer.writeBytes(roomID+'\n');
		
		
		byte[] playerInByte = Serialize.serialize(player);
		outToServer.writeInt(playerInByte.length);
		outToServer.write(playerInByte);
		int i =0;
		byte[] byteInFromServer = null;
		while((i=inFromServer.readInt())!=0){
			byteInFromServer = new byte[i];
			inFromServer.read(byteInFromServer);
			break;
		}
		Room room = Deserialize.deserializeRoom(byteInFromServer);
		return room;
	}
}
