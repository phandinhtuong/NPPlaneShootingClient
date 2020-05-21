package testOneClient;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javafx.scene.layout.Border;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.Color;

public class TestOneObjectMoving {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestOneObjectMoving window = new TestOneObjectMoving();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public TestOneObjectMoving() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	int missileIndex = 0;
	int numberOfMissile = 100;
	int numberOfEnemyPlane = 100;
	JLabel enemyPlaneList[] = new JLabel[numberOfEnemyPlane];
	Image missileImage = new ImageIcon(this.getClass().getResource("/missile.png"))
	.getImage();
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);

		frame.setBounds(0, 0, 1080, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JLabel plane = new JLabel("");
		Image planeImage = new ImageIcon(this.getClass().getResource("/plane1.png"))
				.getImage();
		plane.setIcon(new ImageIcon(planeImage));
		plane.setBounds(frame.getWidth()/2-planeImage.getWidth(null)/2, frame.getHeight()-planeImage.getHeight(null)*2, planeImage.getWidth(null), planeImage.getHeight(null));
		frame.getContentPane().add(plane);
		
		
		final Image enemyImage = new ImageIcon(this.getClass().getResource("/enemyPlaneGraySmaller.png"))
		.getImage();
		
//		for (int i =0;i<5;i++){
//			
//			enemyPlaneList[i] = new JLabel("");
//			enemyPlaneList[i].setIcon(new ImageIcon(img3));
//			enemyPlaneList[i].setBounds(144,53,58,57);
//			enemyPlaneMove(enemyPlaneList[i],(int) (Math.random() * (1000 - 1)) + 1);
//			frame.getContentPane().add(enemyPlaneList[i]);
//		}
		
		createEnemyPlanes(enemyPlaneList,enemyImage);
		
		
//		final JLabel missile = new JLabel("");
		
//		missile.setIcon(new ImageIcon(img2));
//		missile.setBounds(147, 466, 46, 39);
//		missile.setVisible(false);
//
//		frame.getContentPane().add(missile);

		 final JLabel[] missileList = new JLabel[numberOfMissile];
		 for (int i = 0;i<numberOfMissile;i++){
		 missileList[i] = new JLabel("");
		 missileList[i].setIcon(new ImageIcon(missileImage));
		 missileList[i].setSize(missileImage.getWidth(null), missileImage.getHeight(null));
		// missileList[i].setBounds(147, 466, missileImage.getWidth(null), missileImage.getHeight(null));
		 missileList[i].setVisible(false);
		 frame.getContentPane().add(missileList[i]);
		 }
		frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseMoved(MouseEvent e) {
				plane.move(e.getX() - plane.getWidth()/2, e.getY() - plane.getHeight()/2);
			}
			@SuppressWarnings("deprecation")
			@Override
			public void mouseDragged(MouseEvent e) {
				plane.move(e.getX() - plane.getWidth()/2, e.getY() - plane.getHeight()/2);
			}
		});
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if (missileIndex<numberOfMissile){
					System.out.println("missileIndex lauched: "+missileIndex);
					missileMove(missileList,missileIndex,e.getX(),e.getY()-plane.getHeight()+50,enemyPlaneList);
					missileIndex=missileIndex+1;
				}else{
					System.out.println("Run out of missile!");
				}
			}
		});
		frame.setVisible(true);
	}
	public void missileMove(final JLabel[] missileList, final int missileIndex, final int x, final int y,final JLabel[] enemyPlaneList){
		int delay = 15; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int missileX = x - missileList[missileIndex].getWidth()/2;
			int missileY = 0;
			int enemyPlaneListIndexDie = -1;
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				//missileX = x - missileList[missileIndex].getX()+ missileList[missileIndex].getWidth()/2;
				missileY = y - 05 * count;// can change speed here by y
				//if (count == 1080 || y - 65 - 5 * count < -38 || checkCollision(missileX, missileY,enemyPlane)) {
				enemyPlaneListIndexDie = checkCollisionList(missileX, missileY,enemyPlaneList);
				if (count == 1080 || y - 65 - 5 * count < -38 || (enemyPlaneListIndexDie!=-1)) {
					if (enemyPlaneListIndexDie!=-1){
						enemyPlaneList[enemyPlaneListIndexDie].setVisible(false);
						
						frame.getContentPane().remove(enemyPlaneList[enemyPlaneListIndexDie]); //remove enemy
						enemyPlaneList[enemyPlaneListIndexDie] = null;
						System.out.println("missileIndex = "+missileIndex+" destroyed enemyPlaneListIndex = "+enemyPlaneListIndexDie);
						enemyPlaneListIndexDie = -1;
					}
					//System.out.println("enemyPlaneListIndexDie = "+enemyPlaneListIndexDie);
					
					
					missileList[missileIndex].setVisible(false);
					frame.getContentPane().remove(missileList[missileIndex]);
					missileList[missileIndex] = null;
					
					
					((Timer) evt.getSource()).stop();
				}else{
					missileList[missileIndex].setVisible(true);
					missileList[missileIndex].move(missileX,missileY);
				}
				count++;
			}
		};
//		new Timer(delay, taskPerformer[missileIndex]).start();
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}
	public int checkCollisionList(int x, int y, JLabel[] enemyPlaneList){
		for (int i = 0; i < enemyPlaneList.length;i++){
			if (enemyPlaneList[i] != null){
				if (checkCollision(x, y, enemyPlaneList[i])) return i;
			}
		}
		return -1;
	}
	public boolean checkCollision(int x, int y, JLabel enemyPlane){
		Rectangle m = new Rectangle(x,y,missileImage.getWidth(null),missileImage.getHeight(null));
		Rectangle e = new Rectangle(enemyPlane.getX(),enemyPlane.getY(),enemyPlane.getWidth(),enemyPlane.getHeight());
		if (m.intersects(e)) return true;
		else return false;
	}
	public void createEnemyPlanes(final JLabel[] enemyPlaneList, final Image enemyImage){
		int delay = 1000;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			public void actionPerformed(ActionEvent evt) {
				if (count == numberOfEnemyPlane){
					//enemyPlane.setVisible(false);
					((Timer) evt.getSource()).stop();
				}else{
					enemyPlaneList[count] = new JLabel("");
					enemyPlaneList[count].setIcon(new ImageIcon(enemyImage));
					enemyPlaneList[count].setSize(enemyImage.getWidth(null),enemyImage.getHeight(null));
					//enemyPlaneList[count].setBounds(0,0,enemyImage.getWidth(null),enemyImage.getHeight(null));
					enemyPlaneMove(enemyPlaneList[count],(int) (Math.random() * (frame.getWidth()-enemyImage.getWidth(null) - 1)) + 1);
					frame.getContentPane().add(enemyPlaneList[count]);
				}
				count++;
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}
	public void enemyPlaneMove(final JLabel enemyPlane, final int x){
		int delay = 20;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int enemyPlaneY = 0;
			public void actionPerformed(ActionEvent evt) {
				enemyPlaneY = 2*count-enemyPlane.getHeight();// can change speed by y and delay
				if ( enemyPlaneY >= frame.getHeight()-enemyPlane.getHeight()){
					enemyPlane.setVisible(false);
					((Timer) evt.getSource()).stop();
				}else{
					enemyPlane.setVisible(true);
					enemyPlane.move(x, enemyPlaneY);
				}
				count++;
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}
	
}
