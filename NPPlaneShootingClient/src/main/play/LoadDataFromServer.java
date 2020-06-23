package main.play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import main.Main;
import objectByteTransform.Deserialize;

public class LoadDataFromServer {
	public static void loadDataFromServer(final int roomID) {
		int delay = 10;
		ActionListener taskPerformer = new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					if (Main.lblCenterMessage.isVisible()){
						
						((Timer) evt.getSource()).stop();
						return;
					}
					// check if victory or not
					// Main.outToServer.writeInt(3);
					// Main.outToServer.writeInt(roomID);
					// while((i = Main.inFromServer.readInt()) != 0){
					// // Client.displayGameLog(i+"");
					// if (i==-1){
					// // Client.displayGameLog("0 enemies left.");
					// Main.displayNumberOfEnemiesLeft(0);
					// Main.displayCenterMessage("Victory!");
					// }else {
					// // Client.displayGameLog(i+" enemies left.");
					// Main.displayNumberOfEnemiesLeft(i);
					// }
					// break;
					// }
					// load all players
					Main.outToServer.writeInt(4);
					Main.outToServer.writeInt(roomID);
					while ((i = Main.inFromServer.readInt()) != 0) {
						byte[] planeModelListInByte = new byte[i];
						Main.inFromServer.read(planeModelListInByte);
						DisplayAllPlayers.modelPlaneList = Deserialize
								.deserializePlaneModelArrayList(planeModelListInByte);
						break;
					}
					// load all missiles
					Main.outToServer.writeInt(5);
					Main.outToServer.writeInt(roomID);
					while ((i = Main.inFromServer.readInt()) != 0) {
						byte[] missileModelListInByte = new byte[i];
						Main.inFromServer.read(missileModelListInByte);
						DisplayAllMissiles.modelMissileList = Deserialize
								.deserializeMissileModelArrayList(missileModelListInByte);
						break;
					}
					// load all enemies
					Main.outToServer.writeInt(6);
					Main.outToServer.writeInt(roomID);
					while ((i = Main.inFromServer.readInt()) != 0) {
						byte[] enemyModelListInByte = new byte[i];
						Main.inFromServer.read(enemyModelListInByte);
						DisplayAllEnemies.modelEnemyList = Deserialize
								.deserializeEnemyModelArrayList(enemyModelListInByte);
						break;
					}
					// Client.displayGameLog(Client.modelMissileList.g);
					DisplayAllPlayers.displayAllPlayers();
					DisplayAllMissiles.displayAllMissiles();
					DisplayAllEnemies.displayAllEnemies();
				} catch (IOException e) {
					Main.displayGameLog("something worng");
					Main.displayGameLog(e.getMessage());
					((Timer) evt.getSource()).stop();
					return;
				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}
}
