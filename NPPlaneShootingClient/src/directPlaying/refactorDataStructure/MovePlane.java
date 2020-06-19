package directPlaying.refactorDataStructure;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import objectByteTransform.Serialize;

public class MovePlane {
	public static void movePlane(){
		// mouse moved and dragged = move plane
		Client.getFrame().getContentPane().addMouseMotionListener(
				new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						if (Client.modelPlaneLocal.getStatus()
								.equals("playing") && DisplayAllPlayers.lblPlaneList.size()>0) {
							Client.modelPlaneLocal.setID(Client.myPlayerID);
							Client.modelPlaneLocal.setX(e.getX()
									- DisplayAllPlayers.lblPlaneList.get(Client.myPlayerID).getWidth()
//									- Client.lblPlaneList[Client.myPlayerID].getWidth()
									/ 2);
							Client.modelPlaneLocal.setY(e.getY()
									- DisplayAllPlayers.lblPlaneList.get(Client.myPlayerID).getHeight()
//									- Client.lblPlaneList[Client.myPlayerID].getHeight()
									/ 2);
							updateLocalPlaneToServer();
						}
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						if (Client.modelPlaneLocal.getStatus().equals("playing") && DisplayAllPlayers.lblPlaneList.size()>0) {
							Client.modelPlaneLocal.setID(Client.myPlayerID);
							Client.modelPlaneLocal.setX(e.getX()
//									- Client.lblPlaneList[Client.myPlayerID].getWidth()
									- DisplayAllPlayers.lblPlaneList.get(Client.myPlayerID).getWidth()
									/ 2);
							Client.modelPlaneLocal.setY(e.getY()
//									- Client.lblPlaneList[Client.myPlayerID].getHeight()
									- DisplayAllPlayers.lblPlaneList.get(Client.myPlayerID).getHeight()
									/ 2);
							updateLocalPlaneToServer();
						}
					}
				});
	}
	public static void updateLocalPlaneToServer() {
		byte[] planeModelInByte = Serialize.serialize(Client.modelPlaneLocal);
		try {
			Client.outToServer.writeInt(1);
			Client.outToServer.writeInt(planeModelInByte.length);
			Client.outToServer.write(planeModelInByte);
		} catch (IOException e) {
			Client.displayGameLog(e.getMessage());
			return;
		}

	}
}
