package directPlaying.refactorDataStructure;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DisplayAllPlayers {
//	//image of the plane
//	final static Image planeImage = new ImageIcon(Client.getFrame().getClass()
//			.getResource("/plane1.png")).getImage();
//	static int planeWidth = 86;
//	static int planeHeight = 84;
//	//array list of label to display planes
//	static ArrayList<JLabel> lblPlaneList = new ArrayList<JLabel>();
//
//	public static void displayAllPlayers() {
//		//in case the modelPlaneList not null - client could not receive server's package
//		if (Client.modelPlaneList != null) {
//			//dead count is used to know if all players are dead | dead count = plane list size => all dead
//			int deadCount = Client.modelPlaneList.size();
//			//traverse through plane list
//			for (PlaneModel planeModelInList : Client.modelPlaneList) {
//				//if this plane is mine
//				if (planeModelInList.getID() == Client.myPlaneID){
//					//set status of my plane
//					Client.modelPlaneLocal.setStatus(planeModelInList.getStatus());
//					//display score
//					Client.displayScore(planeModelInList.getScore());
//				}
//				//if this plane is dead
//				if (planeModelInList.getStatus().equals("dead")) {
//					//if this is my plane
//					if (planeModelInList.getID() == Client.myPlaneID) {
//						//display center message : you die
//						Client.displayCenterMessage("You die!");
//					}
//					// if this plane's label is visible
//					if (lblPlaneList.get(planeModelInList.getID()).isVisible()) {
//						//display this plane is dead
//						Client.displayGameLog("Player "
//								+ planeModelInList.getID() + " is dead.");
//						//set this plane's label to invisible
//						lblPlaneList.get(planeModelInList.getID()).setVisible(
//								false);
//					}
//					//if this plane is disconnected and label is visible
//				} else if (Client.modelPlaneList
//						.get(Client.indexOfPlaneWithID(planeModelInList.getID()))
//						.getStatus().equals("disconnected")
//						&& lblPlaneList.get(planeModelInList.getID())
//								.isVisible()) {
//					// String oldStatus = modelPlaneLocal.getStatus();
//					//display this player disconnected
//					Client.displayGameLog("Player " + planeModelInList.getID()
//							+ " disconnected.");
//					//set this plane's label to invisible
//					lblPlaneList.get(planeModelInList.getID())
//							.setVisible(false);
//					//if this plane's status is playing
//				} else if (Client.modelPlaneList
//						.get(Client.indexOfPlaneWithID(planeModelInList.getID()))
//						.getStatus().equals("playing")) {
//					//dead count - 1
//					deadCount--;
//					//display one player
//					displayOnePlayer(planeModelInList.getID());
//				}
//			}
//			//if all player are dead, display game over
//			if (deadCount == Client.modelPlaneList.size()) {
//				Client.displayCenterMessage("Game Over!");
//			}
//		}
//	}
//
//	@SuppressWarnings("deprecation")
//	//display one player
//	public static void displayOnePlayer(int i) {
//		// if plane label list size < plane list size, create new plane label
//		if (lblPlaneList.size() < Client.modelPlaneList.size()) {
//			JLabel lblPlane = new JLabel("");
//			lblPlane.setIcon(new ImageIcon(planeImage));
//			lblPlane.setBounds(Client.getFrame().getWidth() / 2 - planeWidth
//					/ 2 * i, Client.getFrame().getHeight() - planeHeight * 2,
//					planeWidth, planeHeight);
//			lblPlane.setVisible(false);
//			//add new plane label to plane label list
//			lblPlaneList.add(lblPlane);
//			lblPlaneList.get(i).setVisible(true);
//			//add new plane label to client frame
//			Client.getFrame().getContentPane()
//					.add(lblPlaneList.get(lblPlaneList.indexOf(lblPlane)));
//		} else {
//			// else, move the plane label
//			lblPlaneList.get(i).move(
//					Client.modelPlaneList.get(Client.indexOfPlaneWithID(i))
//							.getX(),
//					Client.modelPlaneList.get(Client.indexOfPlaneWithID(i))
//							.getY());
//			lblPlaneList.get(i).setVisible(true);
//		}
//	}
}
