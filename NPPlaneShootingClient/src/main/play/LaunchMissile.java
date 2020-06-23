package main.play;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import main.Main;
import model.Missile;
import objectByteTransform.Serialize;

public class LaunchMissile {
	static Missile modelMissileLocal = null;
	// this plane's missile index
	static int missileIndex = 0;
	// number of missiles of each plane
	static int numberOfMissiles = 500;
	// missile's width or height
	static int missileWidthOrHeight = 52;
	private static int roomID;
	public static MouseAdapter launch = new MouseAdapter() {
		@Override
		public void mousePressed(final MouseEvent e) {
			if (Main.modelPlaneLocal.getStatus().equals("playing")
					&& DisplayAllPlayers.lblPlaneList.size() > 0) {
				if (missileIndex < numberOfMissiles) {
					// displayGameLog("missileIndex lauched: "
					// + missileIndex);
					modelMissileLocal = new Missile(0, 0, 0, 0, "ready");
					modelMissileLocal.setPlayerID(Main.myPlaneID);
					modelMissileLocal.setMissileID(missileIndex);
					modelMissileLocal.setX(e.getX() - missileWidthOrHeight / 2);
					modelMissileLocal.setY(e.getY()
							- DisplayAllPlayers.planeHeight + 50);
					modelMissileLocal.setStatus("launched");

					// Main.displayGameLog("id "+modelMissileLocal.getMissileID());
					// DisplayAllMissiles.lblMissileList.ad
					updateLocalMissileToServer(modelMissileLocal, getRoomID());

					missileIndex = missileIndex + 1;
					// Client.displayGameLog("You have "+(Client.numberOfMissiles-Client.missileIndex)
					// +" missiles left!");
					Main.displayNumberOfMissilesLeft(numberOfMissiles
							- missileIndex);
					// Main.displayGameLog("mouse pressed");
				} else {
					Main.displayGameLog("Run out of missile!");
				}

			}

		}
	};

	public void launchMissile(final int roomID) {
		LaunchMissile.setRoomID(roomID);

		// while (modelPlaneList != null) {
		// mouse pressed = launch missile / Create missile

		Main.getFrame().getContentPane().addMouseListener(launch);

	}

	public static void updateLocalMissileToServer(Missile modelMissileLocal,
			int roomID) {
		byte[] missileModelInByte;
		// Main.displayGameLog(modelMissileLocal.getStatus());
		try {
			missileModelInByte = Serialize.serialize(modelMissileLocal);
			Main.outToServer.writeInt(2);
			Main.outToServer.writeInt(roomID);
			Main.outToServer.writeInt(missileModelInByte.length);
			Main.outToServer.write(missileModelInByte);
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			// e.printStackTrace();
			return;
		}
	}

	public static int getRoomID() {
		return roomID;
	}

	public static void setRoomID(int roomID) {
		LaunchMissile.roomID = roomID;
	}

}
