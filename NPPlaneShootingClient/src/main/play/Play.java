package main.play;

import main.Main;

public class Play {
	public static void play(final int roomID) {
		Main.getFrame().setVisible(false);
		Main.displayNumberOfMissilesLeft(Main.numberOfMissiles);
		Main.displayLevel(1);
		Main.displayBigLevel(1);
		Main.hidePlayerID();
		Main.displayGameLog("");
		Main.displayGameLog("");
		Main.displayGameLog("");
		Main.displayGameLog("");
		// load all data from server
		LoadDataFromServer.loadDataFromServer(roomID);
		// move plane
		MovePlane.movePlane(roomID);
		// launch missile
		LaunchMissile.missileIndex = 0;
		LaunchMissile launchMissile = new LaunchMissile();
		launchMissile.launchMissile(roomID);
		Main.getFrame().setVisible(true);
	}
}
