package directPlaying.refactorDataStructure;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DisplayAllMissiles {
	static JLabel lblMissile = null;
	final static Image missileImage = new ImageIcon(Client.getFrame()
			.getClass().getResource("/missile.png")).getImage();
	static ArrayList<JLabel> lblMissileList = new ArrayList<JLabel>();

	public static void displayAllMissiles() {
		if (Client.modelMissileList != null) {
			for (int i = 0; i < Client.modelMissileList.size(); i++) {
				if (Client.modelMissileList.get(i).getStatus()
						.equals("launched")) {
					launchMissileList(i);
				} else if (Client.modelMissileList.get(i).getStatus()
						.equals("moving")) {
					moveMissileList(i);
				} else if (Client.modelMissileList.get(i).getStatus()
						.equals("dead")) {
					deadMissileList(i);
				}
			}
		}
	}

	public static void launchMissileList(int i) {
		if (lblMissileList.size() +1 == Client.modelMissileList.size()) {
			lblMissile = new JLabel("");
			lblMissile.setIcon(new ImageIcon(missileImage));
			lblMissile.setSize(missileImage.getWidth(null),
					missileImage.getHeight(null));
			lblMissile.setVisible(false);
			lblMissileList.add(lblMissile);
			Client.getFrame()
					.getContentPane()
					.add(lblMissileList.get(lblMissileList.indexOf(lblMissile)));
		}

	}

	@SuppressWarnings("deprecation")
	public static void moveMissileList(int i) {
		if (lblMissileList.size() > i) {
			lblMissileList.get(i).move(Client.modelMissileList.get(i).getX(),
					Client.modelMissileList.get(i).getY());
			lblMissileList.get(i).setVisible(true);
		}

	}

	public static void deadMissileList(int i) {
		if (lblMissileList.size() > i
				&& lblMissileList.size() == Client.modelMissileList.size()) {
			lblMissileList.get(i).setVisible(false);
			Client.getFrame().getContentPane().remove(lblMissileList.get(i));
			Client.displayGameLog("lblMissileList before: "
					+ lblMissileList.size());
			lblMissileList.remove(i);
			Client.displayGameLog("lblMissileList after: "
					+ lblMissileList.size());
		}

	}
}
