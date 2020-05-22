package testMultipleClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import testOneClient.EnemyModel;
import testOneClient.MissileModel;
import testOneClient.PlaneModel;

import javax.swing.JScrollPane;

public class TestMultiplePlayerConnectToServer {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TestMultiplePlayerConnectToServer window = new TestMultiplePlayerConnectToServer();
				window.frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestMultiplePlayerConnectToServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	int missileIndex = 0;
	int numberOfMissile = 100;
	int numberOfEnemyPlane = 100;
	int myPlayerIndex = -1;
	static int numberOfPlayers = 3;

	// Load images
	Image missileImage = new ImageIcon(this.getClass().getResource(
			"/missile.png")).getImage();
	Image planeImage = new ImageIcon(this.getClass().getResource("/plane1.png"))
			.getImage();
	Image enemyImage = new ImageIcon(this.getClass().getResource(
			"/enemyPlaneGraySmaller.png")).getImage();

	JLabel lblYouDie = new JLabel("You die!"); // display when player dies
	static JTextArea gameLog = new JTextArea("A JTextArea object represents a multiline area for displaying text.\n "
        + "You can change the number of lines that can be displayed at a time, \n"
        + "as well as the number of columns. You can wrap lines and words too.\n "
        + "You can change the number of lines that can be displayed at a time, \n"
        + "as well as the number of columns. You can wrap lines and words too.\n "
        + "You can change the number of lines that can be displayed at a time, \n"
        + "as well as the number of columns. You can wrap lines and words too.\n "
        + "You can also put your JTextArea in a JScrollPane to make it scrollable.\n"); // display game log

	// model list from server
	PlaneModel[] modelPlaneList = new PlaneModel[numberOfPlayers];
	MissileModel[][] modelMissileList = new MissileModel[numberOfPlayers][numberOfMissile];
	EnemyModel[][] modelEnemyList = new EnemyModel[numberOfPlayers][numberOfEnemyPlane];

	// label to display the model list
	JLabel[] lblPlaneList = new JLabel[numberOfPlayers];
	JLabel[][] lblMissileList = new JLabel[numberOfPlayers][numberOfMissile];
	JLabel[][] lblEnemyList = new JLabel[numberOfPlayers][numberOfEnemyPlane];

	// local model to send to server
	PlaneModel modelPlaneLocal = new PlaneModel(-1, 500, 500, "playing");
	MissileModel modelMissileLocal = new MissileModel(0, 0, 0, 0, "ready");
	EnemyModel modelEnemyLocal = new EnemyModel(0, 0, 0, "ready");

	String ip = "127.0.0.1";
	int port = 6789;
	Socket clientSocket;
	DataInputStream inFromServer;
	DataOutputStream outToServer;
	

	@SuppressWarnings("deprecation")
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Plane shooting gaem");
		frame.setBounds(0, 0, 1080, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setCursor(frame.getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));
		// game log
		gameLog.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		
		//gameLog.setBounds(0, 0, frame.getWidth(), 100);
		//gameLog.setVisible(false);
		JScrollPane scrollPaneGameLog = new JScrollPane();
		scrollPaneGameLog.setBounds(0, 0, 913, 123);
		//scrollPaneGameLog.setVisible(true);
		frame.getContentPane().add(scrollPaneGameLog);
		scrollPaneGameLog.setViewportView(gameLog);
		
		try {
			clientSocket = new Socket(ip, port);
			inFromServer = new DataInputStream(clientSocket.getInputStream());
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			// Initial all models and label
			for (int j = 0; j < numberOfPlayers; j++) {
				modelPlaneList[j] = new PlaneModel(j, 0, 0, "waiting");
				lblPlaneList[j] = new JLabel("");
				lblPlaneList[j].setIcon(new ImageIcon(planeImage));
				lblPlaneList[j].setBounds(
						frame.getWidth() / 2 - planeImage.getWidth(null) / 2
								* j,
						frame.getHeight() - planeImage.getHeight(null) * 2,
						planeImage.getWidth(null), planeImage.getHeight(null));
				lblPlaneList[j].setVisible(false);
				frame.getContentPane().add(lblPlaneList[j]);
				for (int i = 0; i < numberOfMissile; i++) {
					modelMissileList[j][i] = new MissileModel(0, 0, 0, 0,
							"ready");
					lblMissileList[j][i] = new JLabel("");
					lblMissileList[j][i].setIcon(new ImageIcon(missileImage));
					lblMissileList[j][i].setSize(missileImage.getWidth(null),
							missileImage.getHeight(null));
					lblMissileList[j][i].setVisible(false);
					frame.getContentPane().add(lblMissileList[j][i]);
				}
				for (int i = 0; i < numberOfEnemyPlane; i++) {
					modelEnemyList[j][i] = new EnemyModel(0, 0, 0, "ready");
					lblEnemyList[j][i] = new JLabel("");
					lblEnemyList[j][i].setIcon(new ImageIcon(enemyImage));
					lblEnemyList[j][i].setSize(enemyImage.getWidth(null),
							enemyImage.getHeight(null));
					lblEnemyList[j][i].setVisible(false);
					frame.getContentPane().add(lblEnemyList[j][i]);
				}
			}

			// get player index
			try {
				while ((myPlayerIndex = inFromServer.readInt()) != -1) {
					displayGameLog("My Player Index is " + myPlayerIndex);
					// System.out.println("My Player Index is " +
					// myPlayerIndex);
					// TODO
					modelPlaneList[myPlayerIndex].setX(modelPlaneLocal.getX());
					modelPlaneList[myPlayerIndex].setY(modelPlaneLocal.getY());
					modelPlaneList[myPlayerIndex].setStatus("playing");
					modelPlaneLocal.setID(myPlayerIndex);
					// allPlayers[myPlayerIndex].setVisible(true);
					break;
				}
			} catch (IOException e2) {
				displayGameLog(e2.getMessage());
			}

			// label to display when dead
			lblYouDie.setAlignmentX(Component.CENTER_ALIGNMENT);
			lblYouDie.setForeground(Color.RED);
			lblYouDie.setHorizontalTextPosition(SwingConstants.CENTER);
			lblYouDie.setHorizontalAlignment(SwingConstants.CENTER);
			lblYouDie.setFont(new Font("Times New Roman", Font.BOLD, 99));
			lblYouDie.setBounds(frame.getWidth() / 2 - 200,
					frame.getHeight() / 2 - 100, 400, 200);
			frame.getContentPane().add(lblYouDie);
			scrollPaneGameLog.setBounds(97, 121, 867, 123);
			
			frame.getContentPane().add(scrollPaneGameLog);
			lblYouDie.setVisible(false);

			// mouse pressed = launch missile
			frame.getContentPane().addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(final MouseEvent e) {
					if (modelPlaneLocal.getStatus().equals("playing")) {
						if (missileIndex < numberOfMissile) {
							System.out.println("missileIndex lauched: "
									+ missileIndex);
							modelMissileLocal.setID(missileIndex);
							modelMissileLocal.setX(e.getX());
							modelMissileLocal.setY(e.getY()
									- lblPlaneList[myPlayerIndex].getHeight()
									+ 50);
							modelMissileLocal.setStatus("launched");
							missileMoveThroughServer();
							missileIndex = missileIndex + 1;
						} else {
							System.out.println("Run out of missile!");
						}

					}

				}
			});

			// mouse moved and dragged = move plane
			frame.getContentPane().addMouseMotionListener(
					new MouseMotionAdapter() {
						@Override
						public void mouseMoved(MouseEvent e) {
							if (modelPlaneLocal.getStatus().equals("playing")) {
								modelPlaneLocal.setX(e.getX()
										- lblPlaneList[myPlayerIndex]
												.getWidth() / 2);
								modelPlaneLocal.setY(e.getY()
										- lblPlaneList[myPlayerIndex]
												.getHeight() / 2);
								updateLocalPlaneToServer();
							}
						}

						@Override
						public void mouseDragged(MouseEvent e) {
							if (modelPlaneLocal.getStatus().equals("playing")) {
								modelPlaneLocal.setX(e.getX()
										- lblPlaneList[myPlayerIndex]
												.getWidth() / 2);
								modelPlaneLocal.setY(e.getY()
										- lblPlaneList[myPlayerIndex]
												.getHeight() / 2);
								updateLocalPlaneToServer();
							}
						}
					});

			loadDataFromServer();
			createEnemy();
			frame.setVisible(true);
		} catch (IOException e2) {
			frame.setCursor(Cursor.DEFAULT_CURSOR);
			displayGameLog("Disable to connect to server: " + e2.getMessage());
			return;
		}

	}

	public void loadDataFromServer() {
		int delay = 1;
		ActionListener taskPerformer = new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					outToServer.writeInt(4);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					while ((i = inFromServer.readInt()) != 0) {
						byte[] planeModelListInByte = new byte[i];
						inFromServer.read(planeModelListInByte);
						modelPlaneList = deserializePlaneModelList(planeModelListInByte);
						break;
					}
					while ((i = inFromServer.readInt()) != 0) {
						byte[] missileModelListInByte = new byte[i];
						inFromServer.read(missileModelListInByte);
						modelMissileList = deserializeMissileModelList(missileModelListInByte);
						break;
					}
					while ((i = inFromServer.readInt()) != 0) {
						byte[] enemyModelListInByte = new byte[i];
						inFromServer.read(enemyModelListInByte);
						modelEnemyList = deserializeEnemyModelList(enemyModelListInByte);
						break;
					}
					displayAllPlayers();
					displayAllMissiles();
					// TODO
					displayAllEnemies();
				} catch (IOException e) {
					System.out.println("Connection to server interrupted.");
				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}

	public void displayAllPlayers() {
		for (int i = 0; i < modelPlaneList.length; i++) {
			if (modelPlaneList[i].getStatus().equals("dead")) {
				lblPlaneList[i].setVisible(false);
			} else if (modelPlaneList[i].getStatus().equals("playing")) {
				displayOnePlayer(i);
			}
		}
	}

	public void displayAllMissiles() {
		for (int j = 0; j < modelMissileList.length; j++) {
			for (int i = 0; i < modelMissileList[j].length; i++) {
				if (modelMissileList[j][i].getStatus().equals("dead"))
					lblMissileList[j][i].setVisible(false);
				else if (modelMissileList[j][i].getStatus().equals("launched")
						&& !lblMissileList[j][i].isVisible()) {
					missileMove(j, i);// TODO
				}

			}
		}
	}

	public void displayAllEnemies() {
		for (int j = 0; j < modelEnemyList.length; j++) {
			for (int i = 0; i < modelEnemyList[j].length; i++) {
				if (modelEnemyList[j][i].getStatus().equals("dead"))
					lblEnemyList[j][i].setVisible(false);
				else if (modelEnemyList[j][i].getStatus().equals("created")
						&& !lblEnemyList[j][i].isVisible()) {
					enemyMove(j, i);
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	public void displayOnePlayer(int i) {
		lblPlaneList[i].setVisible(true);
		lblPlaneList[i]
				.move(modelPlaneList[i].getX(), modelPlaneList[i].getY());
	}

	public static PlaneModel[] deserializePlaneModelList(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (PlaneModel[]) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public static MissileModel[][] deserializeMissileModelList(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (MissileModel[][]) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			displayGameLog(e.getMessage());
			return null;
		}

	}

	public static EnemyModel[][] deserializeEnemyModelList(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (EnemyModel[][]) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public void updateLocalPlaneToServer() {
		byte[] planeModelInByte = serialize(modelPlaneLocal);
		try {
			outToServer.writeInt(1);
			outToServer.writeInt(planeModelInByte.length);
			outToServer.write(planeModelInByte);
		} catch (IOException e) {
			displayGameLog(e.getMessage());
		}

	}

	public void missileMoveThroughServer() {
		byte[] missileModelInByte = serialize(modelMissileLocal);
		try {
			outToServer.writeInt(2);
			outToServer.writeInt(missileModelInByte.length);
			outToServer.write(missileModelInByte);
		} catch (IOException e) {
			displayGameLog(e.getMessage());
		}
	}

	public void updateLocalEnemyToServer() {
		byte[] enemyModelInByte = serialize(modelEnemyLocal);
		try {
			outToServer.writeInt(3);
			outToServer.writeInt(enemyModelInByte.length);
			outToServer.write(enemyModelInByte);
		} catch (IOException e) {
			displayGameLog(e.getMessage());
		}
	}

	public static byte[] serialize(PlaneModel planeModel) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(planeModel);
			return out.toByteArray();
		} catch (IOException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public static byte[] serialize(MissileModel missileModel) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(missileModel);
			return out.toByteArray();
		} catch (IOException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public static byte[] serialize(EnemyModel enemyModel) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(enemyModel);
			return out.toByteArray();
		} catch (IOException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public static PlaneModel deserializePlaneModel(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (PlaneModel) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public static MissileModel deserializeMissileModel(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (MissileModel) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public static EnemyModel deserializeEnemyModel(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (EnemyModel) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			displayGameLog(e.getMessage());
			return null;
		}
	}

	public void missileMove(final int j, final int i) {
		System.out.println("playerID:" + j + " missileID:" + i + " x:"
				+ modelMissileList[j][i].getX() + " y:"
				+ modelMissileList[j][i].getY() + " launched");
		int delay = 1; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int missileX = modelMissileList[j][i].getX()
					- lblMissileList[j][i].getWidth() / 2;
			int missileY = 0;
			int enemyPlaneListIndexDie = -1;
			int k = 0;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				if (modelMissileList[j][i].getStatus().equals("dead")) {
					System.out.println("playerID:" + j + " missileID:" + i
							+ " x:" + modelMissileList[j][i].getX() + " y:"
							+ modelMissileList[j][i].getY() + " dead");
					((Timer) evt.getSource()).stop();
				} else {
					// missileX = x - missileList[missileIndex].getX()+
					// missileList[missileIndex].getWidth()/2;
					missileY = modelMissileList[j][i].getY() - 5 * count;// speed
					// if (count == 1080 || y - 65 - 5 * count < -38 ||
					// checkCollision(missileX, missileY,enemyPlane)) {
					// for (k = 0; k < numberOfPlayers; k++) {
					// enemyPlaneListIndexDie = checkCollisionList(missileX,
					// missileY, allMissiles[j][i].getWidth(),
					// allMissiles[j][i].getHeight(), allEnemies[k]);
					// if (enemyPlaneListIndexDie != -1)
					// break;
					// }

					if (count == 1080 || lblMissileList[j][i].getY() < 15
							|| (enemyPlaneListIndexDie != -1)) {
						// if (enemyPlaneListIndexDie != -1) {
						// // System.out.println("enemyPlaneListIndexDie "
						// // + enemyPlaneListIndexDie);
						// //
						// allEnemies[k][enemyPlaneListIndexDie].setVisible(false);
						// // TODO
						// // frame.getContentPane().remove(
						// // allEnemies[k][enemyPlaneListIndexDie]); // remove
						// // enemy
						// // allEnemies[enemyPlaneListIndexDie] = null;
						// enemyModel.setPlayerID(k);
						// enemyModel.setID(enemyPlaneListIndexDie);
						// enemyModel.setStatus("dead");
						// try {
						// enemyMoveThroughServer();
						// } catch (ClassNotFoundException | IOException e) {
						// e.printStackTrace();
						// }
						// System.out.println("missileIndex = " + missileIndex
						// + " destroyed enemyPlaneListIndex = "
						// + enemyPlaneListIndexDie);
						//
						// enemyPlaneListIndexDie = -1;
						// }
						// System.out.println("enemyPlaneListIndexDie = "+enemyPlaneListIndexDie);
						modelMissileLocal.setPlayerID(j);
						modelMissileLocal.setID(i);
						modelMissileLocal.setStatus("dead");
						modelMissileList[j][i].setStatus("dead");
						((Timer) evt.getSource()).stop();
						missileMoveThroughServer();

					} else {
						lblMissileList[j][i].setVisible(true);
						lblMissileList[j][i].move(missileX, missileY);
					}
					count++;
				}

			}
		};
		// new Timer(delay, taskPerformer[missileIndex]).start();
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public int checkCollisionList(int x, int y, int width, int height,
			JLabel[] ObjectList) {
		for (int i = 0; i < ObjectList.length; i++) {
			if (ObjectList[i] != null) {
				if (checkCollision(x, y, width, height, ObjectList[i]))
					return i;
			}
		}
		return -1;
	}

	public boolean checkCollision(int x, int y, int width, int height,
			JLabel objectB) {
		Rectangle a = new Rectangle(x, y, width, height);
		Rectangle b = new Rectangle(objectB.getX(), objectB.getY(),
				objectB.getWidth(), objectB.getHeight());
		if (a.intersects(b))
			return true;
		else
			return false;
	}

	public void createEnemy() {
		int delay = 10000;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;

			public void actionPerformed(ActionEvent evt) {
				if (count == numberOfEnemyPlane) {
					((Timer) evt.getSource()).stop();
				} else {
					modelEnemyLocal = new EnemyModel(myPlayerIndex, count,
							(int) (Math.random() * (frame.getWidth()
									- enemyImage.getWidth(null) - 1)) + 1,
							"created");
					updateLocalEnemyToServer();
				}
				count++;
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public void enemyMove(final int j, final int i) {
		int delay = 1;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int enemyPlaneY = 0;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {

				if (modelEnemyList[j][i].getStatus().equals("dead")) {
					lblEnemyList[j][i].setVisible(false);
					((Timer) evt.getSource()).stop();
					return;
				} else {

				}
				enemyPlaneY = count - lblEnemyList[j][i].getHeight();// speed
				if (modelPlaneLocal.getStatus().equals("playing")) {
					if (checkCollision(modelEnemyLocal.getX(), enemyPlaneY,
							lblEnemyList[j][i].getWidth(),
							lblEnemyList[j][i].getHeight(),
							lblPlaneList[myPlayerIndex])) {
						modelPlaneLocal.setStatus("Died");
						updateLocalPlaneToServer();
						// allPlayers[myPlayerIndex].setVisible(false);
						// TODO
						lblYouDie.setVisible(true);
						frame.setCursor(Cursor.DEFAULT_CURSOR);
						System.out.println("you ded by enemy plane index " + i
								+ " of player " + j);
					}
				}
				if (enemyPlaneY >= frame.getHeight()
						- lblEnemyList[j][i].getHeight()) {
					modelEnemyLocal.setPlayerID(j);
					modelEnemyLocal.setID(i);
					modelEnemyLocal.setStatus("dead");
					modelEnemyList[j][i].setStatus("dead");
					updateLocalEnemyToServer();
					// allEnemies[j][i].setVisible(false);
					((Timer) evt.getSource()).stop();
				} else {
					lblEnemyList[j][i].setVisible(true);
					lblEnemyList[j][i].move(modelEnemyList[j][i].getX(),
							enemyPlaneY);
				}
				count++;

			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public static void displayGameLog(String s) {
//		System.out.println("gameLog.getName() "+gameLog.getName());
//		System.out.println("gameLog.getText() "+gameLog.getText());
		
		//gameLog.setText("<html>"+gameLog.getText()+"<br/>"+s+"</html>");
//		gameLog.setText("<html>"+gameLog.getText()+"<br/>"+s);
//		gameLog.setText(gameLog.getText()+"<br/>"+s);
//		gameLog.setText(gameLog.getText()+"<br/>"+s);
//		gameLog.setText(gameLog.getText()+"<br/>"+s);
//		gameLog.setText(gameLog.getText()+"<br/>"+s+"</html>");
		//gameLog.setText(s);
		//gameLog.setVisible(true);
	}
}
