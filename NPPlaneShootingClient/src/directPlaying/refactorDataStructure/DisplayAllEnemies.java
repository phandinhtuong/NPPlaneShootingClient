package directPlaying.refactorDataStructure;

public class DisplayAllEnemies {
	public void displayAllEnemies() {
		if (Client.modelEnemyList != null) {
			for (int j = 0; j < Client.modelEnemyList.length; j++) {
				for (int i = 0; i < Client.modelEnemyList[j].length; i++) {
					if (Client.modelEnemyList[j][i].getStatus().equals("dead"))
						Client.lblEnemyList[j][i].setVisible(false);
					else if (Client.modelEnemyList[j][i].getStatus().equals("created")) {
						displayOneEnemy(j, i);
					}

				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	public void displayOneEnemy(final int j, final int i) {
		Client.lblEnemyList[j][i].setVisible(true);
		Client.lblEnemyList[j][i].move(Client.modelEnemyList[j][i].getX(),
				Client.modelEnemyList[j][i].getY());
	}
}
