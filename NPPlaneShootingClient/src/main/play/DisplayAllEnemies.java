package main.play;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Main;
import model.Enemy;

public class DisplayAllEnemies {
	static JLabel lblEnemy = null;
	final static Image enemyImage = new ImageIcon(Main.getFrame().getClass().getResource(
			"/enemyPlaneGraySmaller.png")).getImage();
	static ArrayList<JLabel> lblEnemyList = new ArrayList<JLabel>();
	// model enemy list - get from server to display all enemies
		static List<Enemy> modelEnemyList = null;
	public static void displayAllEnemies() {
		if (modelEnemyList != null) {
			if (Main.lblCenterMessage.isVisible()){
				for (int i = 0; i < modelEnemyList.size(); i++) {
					lblEnemyList.get(i).setVisible(false);
				}
			}else {
				for (int i = 0; i < modelEnemyList.size(); i++) {
					if (modelEnemyList.get(i).getStatus().equals("created"))
						createEnemyList(i);
					else if (modelEnemyList.get(i).getStatus().equals("moving")) {
						moveEnemyList(i);
					} else if (modelEnemyList.get(i).getStatus().equals("dead")){
						deadEnemyList(i);
					}

				
			}
			for (int j = modelEnemyList.size(); j < lblEnemyList.size();j++){
				removedeadEnemyList(j);
			}
			}
			
		}
	}
	public static void createEnemyList(int i){
		if (lblEnemyList.size()<modelEnemyList.size()){
			lblEnemy = new JLabel("");
			lblEnemy.setIcon(new ImageIcon(enemyImage));
			lblEnemy.setSize(enemyImage.getWidth(null), enemyImage.getHeight(null));
			lblEnemy.setVisible(false);
			lblEnemyList.add(lblEnemy);
//			Client.displayGameLog("Player "+Client.modelEnemyList.get(i).getPlayerID()+" has "+(Client.numberOfEnemies -Client.modelEnemyList.get(i).getID()-1)+" enemies left");
			Main.getFrame().getContentPane().add(lblEnemyList.get(lblEnemyList.indexOf(lblEnemy)));
			
		}
	}
	@SuppressWarnings("deprecation")
	public static void moveEnemyList(int i){
		if (lblEnemyList.size() > i){
			lblEnemyList.get(i).move(modelEnemyList.get(i).getX(), modelEnemyList.get(i).getY());
			lblEnemyList.get(i).setVisible(true);
		}
		
	}
	public static void deadEnemyList(int i){
		if (lblEnemyList.size() > i && lblEnemyList.size() >= modelEnemyList.size()){
			lblEnemyList.get(i).setVisible(false);
//			Client.getFrame().getContentPane().remove(lblEnemyList.get(i));
//			Client.displayGameLog("lblEnemyList.size() before: "+lblEnemyList.size());
//			lblEnemyList.remove(i);
//			Client.displayGameLog("lblEnemyList.size() after: "+lblEnemyList.size());
		}
	}
	public static void removedeadEnemyList(int i){
		if (lblEnemyList.size() > i && lblEnemyList.size() >= modelEnemyList.size()){
			lblEnemyList.get(i).setVisible(false);
			Main.getFrame().getContentPane().remove(lblEnemyList.get(i));
//			Client.displayGameLog("lblEnemyList.size() before: "+lblEnemyList.size());
			lblEnemyList.remove(i);
//			Client.displayGameLog("lblEnemyList.size() after: "+lblEnemyList.size());
		}
	}
}
