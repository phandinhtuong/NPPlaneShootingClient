package directPlaying.refactorDataStructure;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import directPlaying.testOneClient.PlaneModel;

public class DisplayAllPlayers {
	final static Image planeImage = new ImageIcon(Client.getFrame().getClass()
			.getResource("/plane1.png")).getImage();
	static int planeWidth = 86;
	static int planeHeight = 84;

	static ArrayList<JLabel> lblPlaneList = new ArrayList<JLabel>();

	public static void displayAllPlayers() {
		if (Client.modelPlaneList != null) {
			Client.modelPlaneLocal.setStatus(Client.modelPlaneList.get(
					Client.myPlayerID).getStatus());
			int count = Client.modelPlaneList.size();
			for (PlaneModel planeModelInList : Client.modelPlaneList) {
				if (planeModelInList.getStatus().equals("dead")) {
//					count++;
					if (planeModelInList.getID() == Client.myPlayerID) {
						Client.displayCenterMessage("You die!");
					}
					if (lblPlaneList.get(planeModelInList.getID()).isVisible()) {
						Client.displayGameLog("Player "
								+ planeModelInList.getID() + " is dead.");
						lblPlaneList.get(planeModelInList.getID()).setVisible(
								false);
					}
				} else if (Client.modelPlaneList
						.get(Client.indexOfPlaneWithID(planeModelInList.getID()))
						.getStatus().equals("disconnected")
						&& lblPlaneList.get(planeModelInList.getID())
								.isVisible()) {
					// String oldStatus = modelPlaneLocal.getStatus();
					Client.displayGameLog("Player " + planeModelInList.getID()
							+ " disconnected.");
					lblPlaneList.get(planeModelInList.getID())
							.setVisible(false);
				} else if (Client.modelPlaneList
						.get(Client.indexOfPlaneWithID(planeModelInList.getID()))
						.getStatus().equals("playing")) {
					count--;
					displayOnePlayer(planeModelInList.getID());
				}
			}
			if (count == Client.modelPlaneList.size()) {
				Client.displayCenterMessage("Game Over!");
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void displayOnePlayer(int i) {
		if (lblPlaneList.size() < Client.modelPlaneList.size()) {
			JLabel lblPlane = new JLabel("");
			lblPlane.setIcon(new ImageIcon(planeImage));
			lblPlane.setBounds(Client.getFrame().getWidth() / 2 - planeWidth
					/ 2 * i, Client.getFrame().getHeight() - planeHeight * 2,
					planeWidth, planeHeight);
			lblPlane.setVisible(false);
			lblPlaneList.add(lblPlane);
			lblPlaneList.get(i).setVisible(true);
			Client.getFrame().getContentPane()
					.add(lblPlaneList.get(lblPlaneList.indexOf(lblPlane)));
		} else {
			lblPlaneList.get(i).move(
					Client.modelPlaneList.get(Client.indexOfPlaneWithID(i))
							.getX(),
					Client.modelPlaneList.get(Client.indexOfPlaneWithID(i))
							.getY());
			lblPlaneList.get(i).setVisible(true);
		}
	}
}
