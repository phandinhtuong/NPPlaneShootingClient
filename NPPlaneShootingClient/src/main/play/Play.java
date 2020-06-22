package main.play;

import main.Main;

public class Play {
	public void play(int roomID){
		Main.getFrame().setVisible(false);
		Main.displayGameLog("roomID = "+roomID);
		//TODO load data from server
		LoadDataFromServer.loadDataFromServer(roomID);
		//TODO move plane
		MovePlane.movePlane(roomID);
		//TODO launch missile
		LaunchMissile.launchMissile(roomID);
		Main.getFrame().setVisible(true);
	}
}
