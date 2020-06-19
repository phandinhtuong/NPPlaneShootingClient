package directPlaying.refactorDataStructure;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Timer;

import objectByteTransform.Deserialize;

public class LoadDataFromServer {
	public static void loadDataFromServer() {
		int delay = 10;
		ActionListener taskPerformer = new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					Client.outToServer.writeInt(3);
					while((i = Client.inFromServer.readInt()) != 0){
//						Client.displayGameLog(i+"");
						if (i==1){
							Client.lblYouDie.setText("Victory!");
							Client.lblYouDie.setVisible(true);
						}
						break;
					}
					Client.outToServer.writeInt(4);
					while ((i = Client.inFromServer.readInt()) != 0) {
						byte[] planeModelListInByte = new byte[i];
						Client.inFromServer.read(planeModelListInByte);
						Client.modelPlaneList = Deserialize
								.deserializePlaneModelArrayList(planeModelListInByte);
						break;
					}
					Client.outToServer.writeInt(5);
					while ((i = Client.inFromServer.readInt()) != 0) {
						byte[] missileModelListInByte = new byte[i];
						Client.inFromServer.read(missileModelListInByte);
						Client.modelMissileList = Deserialize
								.deserializeMissileModelArrayList(missileModelListInByte);
						break;
					}
					 Client.outToServer.writeInt(6);
					 while ((i = Client.inFromServer.readInt()) != 0) {
					 byte[] enemyModelListInByte = new byte[i];
					 Client.inFromServer.read(enemyModelListInByte);
					 Client.modelEnemyList =
					 Deserialize.deserializeEnemyModelArrayList(enemyModelListInByte);
					 break;
					 }
//					Client.displayGameLog(Client.modelMissileList.g);
					DisplayAllPlayers.displayAllPlayers();
					DisplayAllMissiles.displayAllMissiles();
					DisplayAllEnemies.displayAllEnemies();
				} catch (IOException e) {
					Client.displayGameLog("st");
					Client.displayGameLog(e.getMessage());
					((Timer) evt.getSource()).stop();
					return;
				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}

}
