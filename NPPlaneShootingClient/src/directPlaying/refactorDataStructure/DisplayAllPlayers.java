package directPlaying.refactorDataStructure;

import testOneClient.PlaneModel;

public class DisplayAllPlayers {
	public static void displayAllPlayers() {
		if (Client.modelPlaneList != null) {
			Client.modelPlaneLocal.setStatus(Client.modelPlaneList.get(Client.myPlayerID)
					.getStatus());
			// modelPlaneLocal = modelPlaneList[myPlayerID];
			// modelPlaneLocal = modelPlaneList.get(myPlayerID);
			for (PlaneModel planeModelInList : Client.modelPlaneList) {
				if (planeModelInList.getStatus().equals("dead")) {
					if (planeModelInList.getID() == Client.myPlayerID) {
						Client.lblYouDie.setVisible(true);
					}
					if (Client.lblPlaneList[planeModelInList.getID()].isVisible()) {
						Client.displayGameLog("Player " + planeModelInList.getID()
								+ " is dead.");
						Client.lblPlaneList[planeModelInList.getID()]
								.setVisible(false);
					}

				} else if (Client.modelPlaneList
						.get(Client.indexOfPlaneWithID(planeModelInList.getID()))
						.getStatus().equals("disconnected")
						&& Client.lblPlaneList[planeModelInList.getID()].isVisible()) {
					// String oldStatus = modelPlaneLocal.getStatus();
					Client.displayGameLog("Player " + planeModelInList.getID()
							+ " disconnected.");
					// modelPlaneLocal.setID(i);
					// modelPlaneLocal.setStatus("dead");
					// updateLocalPlaneToServer();
					// modelPlaneLocal.setID(myPlayerID);
					// modelPlaneLocal.setStatus(oldStatus);
					Client.lblPlaneList[planeModelInList.getID()].setVisible(false);
				} else if (Client.modelPlaneList
						.get(Client.indexOfPlaneWithID(planeModelInList.getID()))
						.getStatus().equals("playing")) {
					displayOnePlayer(planeModelInList.getID());
				}
			}
			// for (int i = 0; i < modelPlaneList.length; i++) {
			// if (modelPlaneList[i].getStatus().equals("dead")) {
			// if (i == myPlayerID) {
			// lblYouDie.setVisible(true);
			// }
			// if (lblPlaneList[i].isVisible()) {
			// displayGameLog("Player " + i + " is dead.");
			// lblPlaneList[i].setVisible(false);
			// }
			//
			// } else if
			// (modelPlaneList.get(indexOfPlaneWithID(i)).getStatus().equals("disconnected")&&lblPlaneList[i].isVisible())
			// {
			// //String oldStatus = modelPlaneLocal.getStatus();
			// displayGameLog("Player " + i + " disconnected.");
			// // modelPlaneLocal.setID(i);
			// // modelPlaneLocal.setStatus("dead");
			// // updateLocalPlaneToServer();
			// // modelPlaneLocal.setID(myPlayerID);
			// // modelPlaneLocal.setStatus(oldStatus);
			// lblPlaneList[i].setVisible(false);
			// } else if
			// (modelPlaneList.get(indexOfPlaneWithID(i)).getStatus().equals("playing"))
			// {
			// displayOnePlayer(i);
			// }
			// }
		}
	}
	@SuppressWarnings("deprecation")
	public static void displayOnePlayer(int i) {

		Client.lblPlaneList[i].setVisible(true);
		Client.lblPlaneList[i].move(Client.modelPlaneList.get(Client.indexOfPlaneWithID(i)).getX(),
				Client.modelPlaneList.get(Client.indexOfPlaneWithID(i)).getY());
	}
}
