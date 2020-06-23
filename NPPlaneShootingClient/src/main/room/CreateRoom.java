package main.room;

import java.io.IOException;

import main.Main;

public class CreateRoom {
	public void createRoom() {
		try {
			// create room
			Main.outToServer.writeInt(8);
			int i = -1;
			while ((i = Main.inFromServer.readInt()) != -1) {
				// request join room to server
				OutsideRoom.uploadPlayerJoinRoomToServer(i);
				// room interface
				JoinRoom joinRoom = new JoinRoom();
				joinRoom.joinRoom(i);
				break;
			}
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
		}
	}
}
