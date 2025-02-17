package gamePlay;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Main;
import model.Player;

public class DisplayAllPlayers {
	// image of the plane
	final static Image planeImage = new ImageIcon(Main.getFrame().getClass()
			.getResource("/plane1.png")).getImage();
	static int planeWidth = 86;
	static int planeHeight = 84;
	static JLabel lblPlane = null;
	// array list of label to display planes
	static ArrayList<JLabel> lblPlaneList = new ArrayList<JLabel>();
	// model plane list - get from server to display all planes
	static List<Player> modelPlaneList = null;

	// private static int lblPlane;
	public static void displayAllPlayers() {
		// in case the modelPlaneList not null - client could not receive
		// server's package
		if (modelPlaneList != null) {
			// mininum player ID to subtract to display plane label in list

			// dead count is used to know if all players are dead | dead count =
			// plane list size => all dead
			int deadCount = modelPlaneList.size();
			// traverse through plane list
			for (Player planeModelInList : modelPlaneList) {

				// if this plane is mine
				if (planeModelInList.getID() == Main.myPlaneID) {
					// set status of my plane
					Main.modelPlaneLocal
							.setStatus(planeModelInList.getStatus());
					// display score
					Main.displayScore(planeModelInList.getScore());
				}
				// if this plane is dead
				if (planeModelInList.getStatus().equals("dead")) {
					// if this is my plane
					if (planeModelInList.getID() == Main.myPlaneID) {
						// display center message : you die
						Main.displayCenterMessage("You die!");
					}
					// if this plane's label is visible
					if (lblPlaneList.get(planeModelInList.getID()).isVisible()) {
						// display this plane is dead
						Main.displayGameLog("Player "
								+ planeModelInList.getID() + " is dead.");
						// set this plane's label to invisible
						lblPlaneList.get(planeModelInList.getID()).setVisible(
								false);
					}
					// if this plane is disconnected and label is visible
				} else if (modelPlaneList
						.get(indexOfPlaneWithID(planeModelInList.getID()))
						.getStatus().equals("disconnected")
						&& lblPlaneList.get(planeModelInList.getID())
								.isVisible()) {
					// String oldStatus = modelPlaneLocal.getStatus();
					// display this player disconnected
					Main.displayGameLog("Player " + planeModelInList.getID()
							+ " disconnected.");
					// set this plane's label to invisible
					lblPlaneList.get(planeModelInList.getID())
							.setVisible(false);
					// if this plane's status is playing
				} else if (modelPlaneList
						.get(indexOfPlaneWithID(planeModelInList.getID()))
						.getStatus().equals("playing")) {
					// dead count - 1
					deadCount--;
					// display one player
					displayOnePlayer(planeModelInList.getID());
				}

			}
			// if all player are dead, display game over
			if (deadCount == modelPlaneList.size()) {
				Main.displayCenterMessage("Game Over!");
				String s = "<html>";
				for (int i = 0; i < modelPlaneList.size(); i++) {
					// modelPlaneList.get(i).getID();
					// modelPlaneList.get(i).getScore();
					s = s + "Player " + modelPlaneList.get(i).getID()
							+ "'s score = " + modelPlaneList.get(i).getScore()
							+ " <br/>";
				}
				s = s + "</html>";
				Main.displayResult(s);
			}
		}
	}

	@SuppressWarnings("deprecation")
	// display one player
	public static void displayOnePlayer(int planeID) {
		// if plane label list size < plane list size, create new plane label
		if (planeID >= lblPlaneList.size()) {
			lblPlane = new JLabel("");
			lblPlane.setIcon(new ImageIcon(planeImage));
			lblPlane.setBounds(Main.getFrame().getWidth() / 2 - planeWidth / 2
					* planeID, Main.getFrame().getHeight() - planeHeight * 2,
					planeWidth, planeHeight);
			lblPlane.setVisible(false);
			// add new plane label to plane label list
			lblPlaneList.add(lblPlane);
			lblPlaneList.get(lblPlaneList.indexOf(lblPlane)).setVisible(false);
			// add new plane label to client frame
			Main.getFrame().getContentPane()
					.add(lblPlaneList.get(lblPlaneList.indexOf(lblPlane)));

		} else {
			// else, move the plane label
			// Main.displayGameLog("lblPlaneList.size() "+lblPlaneList.size());
			lblPlaneList.get(planeID).move(
					modelPlaneList.get(indexOfPlaneWithID(planeID)).getX(),
					modelPlaneList.get(indexOfPlaneWithID(planeID)).getY());
			lblPlaneList.get(planeID).setVisible(true);
		}
	}

	static public int indexOfPlaneWithID(int ID) {
		for (int i = 0; i < modelPlaneList.size(); i++) {
			if (modelPlaneList.get(i).getID() == ID)
				return i;
		}
		return -1;
	}
}
