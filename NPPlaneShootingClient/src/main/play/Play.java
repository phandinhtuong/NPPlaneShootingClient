package main.play;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;

import main.Main;
import main.room.JoinRoom;
import main.room.OutsideRoom;

public class Play {
	public void play(final int roomID){
		Main.getFrame().setVisible(false);
		Main.displayNumberOfMissilesLeft(Main.numberOfMissiles);
		Main.displayGameLog("roomID = "+roomID);
		//TODO load data from server
		LoadDataFromServer.loadDataFromServer(roomID);
		//TODO move plane
		MovePlane.movePlane(roomID);
		//TODO launch missile
		LaunchMissile.launchMissile(roomID);
		
		
		final JButton btnQuitGame = new JButton("Quit game");
		btnQuitGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(Main.lblCenterMessage.isVisible()){
						//remove all planes, enemies, missiles
//						if (DisplayAllMissiles.lblMissile!=null){
//							Main.getFrame().getContentPane().remove(DisplayAllMissiles.lblMissile);
//						}
//						if (DisplayAllEnemies.lblEnemy!=null){
//							Main.getFrame().getContentPane().remove(DisplayAllEnemies.lblEnemy);
//						}
						JoinRoom.removePlayerFromRoom(roomID);
						Main.hideCenterMessage();
						Main.lblNumberOfMissilesLeft.setVisible(false);
						Main.lblScore.setVisible(false);
						Main.getFrame().getContentPane().remove(btnQuitGame);
						if (OutsideRoom.uploadPlayerJoinRoomToServer(roomID)==1){
//							Main.getFrame().getContentPane().remove(btnCreateRoom);
//							Main.getFrame().getContentPane().remove(btnRefresh);
//							Main.getFrame().getContentPane().remove(scrollPaneRoomList);
							JoinRoom joinRoom = new JoinRoom();
							joinRoom.joinRoom(roomID);
						}else{
							Main.displayGameLog("The game started! Cannot join room!");
//							loadAllRoomsFromServer();
						}
//						JoinRoom joinRoom = new JoinRoom();
//						joinRoom.joinRoom(roomID);
					}else{
						//quit game
						Main.outToServer.writeInt(14);
						Main.outToServer.writeInt(roomID);
					}
				} catch (IOException e1) {
					Main.displayGameLog(e1.getMessage());
					e1.printStackTrace();
					return;
				}
			}
		});
		btnQuitGame.setBounds(790, 135, 115, 29);
		Main.getFrame().getContentPane().add(btnQuitGame);
		btnQuitGame.setVisible(true);
		Main.getFrame().setVisible(true);
	}
}
