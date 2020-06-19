package directPlaying.refactorDataStructure;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import objectByteTransform.Serialize;

public class LaunchMissile {
	public static void launchMissile() {
		
		
//		while (modelPlaneList != null) {
			// mouse pressed = launch missile / Create missile
			Client.getFrame().getContentPane().addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(final MouseEvent e) {
					if (Client.modelPlaneLocal.getStatus().equals("playing") && DisplayAllPlayers.lblPlaneList.size()>0) {
						if (Client.missileIndex < Client.numberOfMissiles) {
							// displayGameLog("missileIndex lauched: "
							// + missileIndex);
							Client.modelMissileLocal.setPlayerID(Client.myPlayerID);
							Client.modelMissileLocal.setID(Client.missileIndex);
							Client.modelMissileLocal.setX(e.getX()
									- Client.missileWidthOrHeight / 2);
							Client.modelMissileLocal.setY(e.getY()
									- DisplayAllPlayers.planeHeight + 50);
							Client.modelMissileLocal.setStatus("launched");
							
							
//							DisplayAllMissiles.lblMissileList.ad
							updateLocalMissileToServer();
							
							Client.missileIndex = Client.missileIndex + 1;
//							Client.displayGameLog("You have "+(Client.numberOfMissiles-Client.missileIndex) +" missiles left!");
							Client.displayNumberOfMissilesLeft(Client.numberOfMissiles-Client.missileIndex);
						} else {
							Client.displayGameLog("Run out of missile!");
						}

					}

				}
			});

			
//		}

	}
	
	

	public static void updateLocalMissileToServer() {
		byte[] missileModelInByte = Serialize.serialize(Client.modelMissileLocal);
		try {
			Client.outToServer.writeInt(2);
			Client.outToServer.writeInt(missileModelInByte.length);
			Client.outToServer.write(missileModelInByte);
		} catch (IOException e) {
			Client.displayGameLog(e.getMessage());
			return;
		}
	}

}
