package gamePlay;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Main;
import model.Missile;

public class DisplayAllMissiles {
	static JLabel lblMissile = null;
	final static Image missileImage = new ImageIcon(Main.getFrame().getClass()
			.getResource("/missile.png")).getImage();
	static ArrayList<JLabel> lblMissileList = new ArrayList<JLabel>();
	// model missile list - get from server to display all missiles
	static List<Missile> modelMissileList = null;

	public static void displayAllMissiles() {
		if (modelMissileList != null) {
			if (Main.lblCenterMessage.isVisible()
					&& Main.lblCenterMessage.getText().equals("Game Over!")) {
				for (int i = 0; i < modelMissileList.size(); i++) {
					lblMissileList.get(i).setVisible(false);
				}
			} else {
				for (int i = 0; i < modelMissileList.size(); i++) {
					if (modelMissileList.get(i).getStatus().equals("launched")) {
						launchMissileList(i);
					} else if (modelMissileList.get(i).getStatus()
							.equals("moving")) {
						moveMissileList(i);
					} else if (modelMissileList.get(i).getStatus()
							.equals("dead")) {
						deadMissileList(i);
					}
				}
				for (int j = modelMissileList.size(); j < lblMissileList.size(); j++) {
					removedeadMissileList(j);
				}
			}

		}
	}

	public static void launchMissileList(int i) {
		if (lblMissileList.size() < modelMissileList.size()) {
			lblMissile = new JLabel("");
			lblMissile.setIcon(new ImageIcon(missileImage));
			lblMissile.setSize(missileImage.getWidth(null),
					missileImage.getHeight(null));
			lblMissile.setVisible(false);
			lblMissileList.add(lblMissile);
			Main.getFrame()
					.getContentPane()
					.add(lblMissileList.get(lblMissileList.indexOf(lblMissile)));
		}

	}

	@SuppressWarnings("deprecation")
	public static void moveMissileList(int i) {
		if (lblMissileList.size() > i) {
			lblMissileList.get(i).move(modelMissileList.get(i).getX(),
					modelMissileList.get(i).getY());
			lblMissileList.get(i).setVisible(true);
		}

	}

	public static void deadMissileList(int i) {
		if (lblMissileList.size() > i
				&& lblMissileList.size() >= modelMissileList.size()) {
			lblMissileList.get(i).setVisible(false);
		}

	}

	public static void removedeadMissileList(int i) {
		if (lblMissileList.size() > i
				&& lblMissileList.size() >= modelMissileList.size()) {
			lblMissileList.get(i).setVisible(false);
			Main.getFrame().getContentPane().remove(lblMissileList.get(i));
			lblMissileList.remove(i);
		}

	}
}
