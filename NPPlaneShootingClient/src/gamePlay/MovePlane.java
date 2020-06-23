package gamePlay;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import main.Main;
import objectByteTransform.Serialize;

public class MovePlane {
	public static void movePlane(final int roomID) {

		// mouse moved and dragged = move plane
		Main.getFrame().getContentPane()
				.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
						if (Main.modelPlaneLocal.getStatus().equals("playing")
								&& DisplayAllPlayers.lblPlaneList.size() > 0) {
							Main.modelPlaneLocal.setID(Main.myPlaneID);
							Main.modelPlaneLocal.setX(e.getX()
									- DisplayAllPlayers.planeWidth / 2);
							Main.modelPlaneLocal.setY(e.getY()
									- DisplayAllPlayers.planeHeight / 2);
							updateLocalPlaneToServer(roomID);
						}
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						if (Main.modelPlaneLocal.getStatus().equals("playing")
								&& DisplayAllPlayers.lblPlaneList.size() > 0) {
							Main.modelPlaneLocal.setID(Main.myPlaneID);
							Main.modelPlaneLocal.setX(e.getX()
									- DisplayAllPlayers.planeWidth / 2);
							Main.modelPlaneLocal.setY(e.getY()
									- DisplayAllPlayers.planeHeight / 2);
							updateLocalPlaneToServer(roomID);
						}
					}
				});
	}

	public static void updateLocalPlaneToServer(int roomID) {
		byte[] planeModelInByte;
		try {
			planeModelInByte = Serialize.serialize(Main.modelPlaneLocal);
			Main.outToServer.writeInt(1);
			Main.outToServer.writeInt(roomID);
			Main.outToServer.writeInt(planeModelInByte.length);
			Main.outToServer.write(planeModelInByte);
		} catch (IOException e1) {
			Main.displayGameLog(e1.getMessage());
			return;
		}

	}
}
