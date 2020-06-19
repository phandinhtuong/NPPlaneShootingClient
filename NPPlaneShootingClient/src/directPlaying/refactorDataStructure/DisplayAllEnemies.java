package directPlaying.refactorDataStructure;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DisplayAllEnemies {
	static JLabel lblEnemy = null;
	final static Image enemyImage = new ImageIcon(Client.getFrame().getClass().getResource(
			"/enemyPlaneGraySmaller.png")).getImage();
	static ArrayList<JLabel> lblEnemyList = new ArrayList<JLabel>();
	public static void displayAllEnemies() {
		if (Client.modelEnemyList != null) {
			for (int i = 0; i < Client.modelEnemyList.size(); i++) {
					if (Client.modelEnemyList.get(i).getStatus().equals("created"))
						createEnemyList(i);
					else if (Client.modelEnemyList.get(i).getStatus().equals("moving")) {
						moveEnemyList(i);
					} else if (Client.modelEnemyList.get(i).getStatus().equals("dead")){
						deadEnemyList(i);
					}

				
			}
			for (int j = Client.modelEnemyList.size(); j < lblEnemyList.size();j++){
				removedeadEnemyList(j);
			}
		}
	}
	public static void createEnemyList(int i){
		if (lblEnemyList.size()<Client.modelEnemyList.size()){
			lblEnemy = new JLabel("");
			lblEnemy.setIcon(new ImageIcon(enemyImage));
			lblEnemy.setSize(enemyImage.getWidth(null), enemyImage.getHeight(null));
			lblEnemy.setVisible(false);
			lblEnemyList.add(lblEnemy);
//			Client.displayGameLog("Player "+Client.modelEnemyList.get(i).getPlayerID()+" has "+(Client.numberOfEnemies -Client.modelEnemyList.get(i).getID()-1)+" enemies left");
			Client.getFrame().getContentPane().add(lblEnemyList.get(lblEnemyList.indexOf(lblEnemy)));
			
		}
	}
	@SuppressWarnings("deprecation")
	public static void moveEnemyList(int i){
		if (lblEnemyList.size() > i){
			lblEnemyList.get(i).move(Client.modelEnemyList.get(i).getX(), Client.modelEnemyList.get(i).getY());
			lblEnemyList.get(i).setVisible(true);
		}
		
	}
	public static void deadEnemyList(int i){
		if (lblEnemyList.size() > i && lblEnemyList.size() >= Client.modelEnemyList.size()){
			lblEnemyList.get(i).setVisible(false);
//			Client.getFrame().getContentPane().remove(lblEnemyList.get(i));
//			Client.displayGameLog("lblEnemyList.size() before: "+lblEnemyList.size());
//			lblEnemyList.remove(i);
//			Client.displayGameLog("lblEnemyList.size() after: "+lblEnemyList.size());
		}
	}
	public static void removedeadEnemyList(int i){
		if (lblEnemyList.size() > i && lblEnemyList.size() >= Client.modelEnemyList.size()){
			lblEnemyList.get(i).setVisible(false);
			Client.getFrame().getContentPane().remove(lblEnemyList.get(i));
//			Client.displayGameLog("lblEnemyList.size() before: "+lblEnemyList.size());
			lblEnemyList.remove(i);
//			Client.displayGameLog("lblEnemyList.size() after: "+lblEnemyList.size());
		}
	}
}
