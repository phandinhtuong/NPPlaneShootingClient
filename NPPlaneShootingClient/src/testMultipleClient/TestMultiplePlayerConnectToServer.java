package testMultipleClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import testOneClient.EnemyModel;
import testOneClient.MissileModel;
import testOneClient.PlaneModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TestMultiplePlayerConnectToServer {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				TestMultiplePlayerConnectToServer window = new TestMultiplePlayerConnectToServer();
				window.frame.setVisible(true);
			}
		});
	}

	public TestMultiplePlayerConnectToServer() {
		initialize();
	}

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
	static JTextArea gameLog = new JTextArea(""); // display game log

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

	// String ip = "127.0.0.1";
	String ip = "";
	// /String ip = "192.168.31.153";

	int ipOk = 0;

	int port = 6789;
	Socket clientSocket;
	DataInputStream inFromServer;
	DataOutputStream outToServer;
	private JTextField txtIpHere;
	private JButton btnLocalhost;

	@SuppressWarnings("deprecation")
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);

		frame.setBounds(0, 0, 930, 992);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		gameLog.setOpaque(false);
		gameLog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		// frame.setCursor(frame.getToolkit().createCustomCursor(
		// new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
		// new Point(0, 0), "null"));
		// game log
		gameLog.setEditable(false);
		gameLog.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		JScrollPane scrollPaneGameLog = new JScrollPane();
		scrollPaneGameLog.setViewportView(gameLog);
		scrollPaneGameLog.setBounds(0, 0, 900, 100);
		scrollPaneGameLog.getViewport().setOpaque(false);
		scrollPaneGameLog.setOpaque(false);
		frame.getContentPane().add(scrollPaneGameLog);

		txtIpHere = new JTextField();
		txtIpHere.setText("");
		txtIpHere.setBounds(60, 142, 179, 26);
		frame.getContentPane().add(txtIpHere);
		txtIpHere.setColumns(10);
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayGameLog(Integer.toString((e.getID())));
				ip = txtIpHere.getText();
				displayGameLog(ip);
				ipOk = 1;
				txtIpHere.setVisible(false);
				btnConnect.setVisible(false);
				btnLocalhost.setVisible(false);
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
					while ((myPlayerIndex = inFromServer.readInt()) != -1) {
						displayGameLog("My Player Index is " + myPlayerIndex);
						// TODO
						modelPlaneList[myPlayerIndex].setX(modelPlaneLocal.getX());
						modelPlaneList[myPlayerIndex].setY(modelPlaneLocal.getY());
						modelPlaneList[myPlayerIndex].setStatus("playing");
						modelPlaneLocal.setID(myPlayerIndex);
						break;
					}
					frame.setTitle("Plane shooting gaem | player " + myPlayerIndex);
					// label to display when dead
					lblYouDie.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblYouDie.setForeground(Color.RED);
					lblYouDie.setHorizontalTextPosition(SwingConstants.CENTER);
					lblYouDie.setHorizontalAlignment(SwingConstants.CENTER);
					lblYouDie.setFont(new Font("Times New Roman", Font.BOLD, 99));
					lblYouDie.setBounds(frame.getWidth() / 2 - 200,
							frame.getHeight() / 2 - 100, 400, 200);
					frame.getContentPane().add(lblYouDie);

					lblYouDie.setVisible(false);

					// mouse pressed = launch missile
					frame.getContentPane().addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(final MouseEvent e) {
							if (modelPlaneLocal.getStatus().equals("playing")) {
								if (missileIndex < numberOfMissile) {
									displayGameLog("missileIndex lauched: "
											+ missileIndex);
									modelMissileLocal.setPlayerID(myPlayerIndex);
									modelMissileLocal.setID(missileIndex);
									modelMissileLocal.setX(e.getX());
									modelMissileLocal.setY(e.getY()
											- lblPlaneList[myPlayerIndex].getHeight()
											+ 50);
									modelMissileLocal.setStatus("launched");
									updateLocalMissileToServer();
									missileIndex = missileIndex + 1;
								} else {
									displayGameLog("Run out of missile!");
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
										modelPlaneLocal.setID(myPlayerIndex);
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
										modelPlaneLocal.setID(myPlayerIndex);
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
		});
		
		btnLocalhost = new JButton("Connect to localhost");
		btnLocalhost.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtIpHere.setText("127.0.0.1");
				btnConnect.doClick(100);
			//	btnConnect.mou
			}
		});
		btnLocalhost.setBounds(484, 141, 209, 29);
		frame.getContentPane().add(btnLocalhost);
		
		btnConnect.setBounds(265, 141, 115, 29);
		frame.getContentPane().add(btnConnect);
		
		

//		txtIpHere.setVisible(true);
//		btnConnect.setVisible(true);
//		frame.setVisible(true);
		// while(ipOk != 1){
	

		// }

		

	}

	public void loadDataFromServer() {
		int delay = 10;
		ActionListener taskPerformer = new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					outToServer.writeInt(4);
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
					displayAllEnemies();
				} catch (IOException e) {
					displayGameLog(e.getMessage());
					((Timer) evt.getSource()).stop();
					return;
				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}

	public void displayAllPlayers() {
		modelPlaneLocal = modelPlaneList[myPlayerIndex];
		for (int i = 0; i < modelPlaneList.length; i++) {
			if (modelPlaneList[i].getStatus().equals("dead")) {
				if (i == myPlayerIndex) {
					lblYouDie.setVisible(true);
				}
				if (lblPlaneList[i].isVisible()) {
					displayGameLog("Player " + i + " is dead.");
					lblPlaneList[i].setVisible(false);
				}

			} else if (modelPlaneList[i].getStatus().equals("disconnected")) {
				String oldStatus = modelPlaneLocal.getStatus();
				displayGameLog("Player " + i + " disconnected.");
				modelPlaneLocal.setID(i);
				modelPlaneLocal.setStatus("dead");
				updateLocalPlaneToServer();
				modelPlaneLocal.setID(myPlayerIndex);
				modelPlaneLocal.setStatus(oldStatus);
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
					displayOneMissile(j, i);// TODO
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
					displayOneEnemy(j, i);
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

	public void displayOneMissile(final int j, final int i) {
		displayGameLog("playerID:" + j + " missileID:" + i + " x:"
				+ modelMissileList[j][i].getX() + " y:"
				+ modelMissileList[j][i].getY() + " launched");
		int delay = 50; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int missileX = modelMissileList[j][i].getX()
					- lblMissileList[j][i].getWidth() / 2; // x does not change
			int missileY = 0;
			int enemyPlaneListIndexDie = -1; // dead enemy index
			int k = 0;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				if (modelMissileList[j][i].getStatus().equals("dead")) {
					displayGameLog("playerID:" + j + " missileID:" + i + " x:"
							+ modelMissileList[j][i].getX() + " y:"
							+ modelMissileList[j][i].getY() + " dead");
					lblMissileList[j][i].setVisible(false);
					((Timer) evt.getSource()).stop();
					return;
				} else {
					// y change with speed
					missileY = modelMissileList[j][i].getY() - 15 * count;
					for (k = 0; k < numberOfPlayers; k++) {
						enemyPlaneListIndexDie = checkCollisionListMissileEnemies(
								missileX, missileY,
								lblMissileList[j][i].getWidth(),
								lblMissileList[j][i].getHeight(), k);
						if (enemyPlaneListIndexDie != -1)
							break;
					}

					if (count == 1080 || missileY < 15
							|| (enemyPlaneListIndexDie != -1)) {
						// missile kills enemy
						if (enemyPlaneListIndexDie != -1) {
							lblEnemyList[k][enemyPlaneListIndexDie]
									.setVisible(false);
							modelEnemyLocal.setPlayerID(k);
							modelEnemyLocal.setID(enemyPlaneListIndexDie);
							modelEnemyLocal.setStatus("dead");
							modelEnemyList[k][enemyPlaneListIndexDie]
									.setStatus("dead");
							updateLocalEnemyToServer();
							displayGameLog("missileIndex = " + missileIndex
									+ " destroyed enemyPlaneListIndex = "
									+ enemyPlaneListIndexDie);
							enemyPlaneListIndexDie = -1;
						}
						displayGameLog("enemyPlaneListIndexDie = "
								+ enemyPlaneListIndexDie);
						modelMissileLocal.setPlayerID(j);
						modelMissileLocal.setID(i);
						modelMissileLocal.setStatus("dead");
						modelMissileList[j][i].setStatus("dead");
						updateLocalMissileToServer();
						((Timer) evt.getSource()).stop();
						return;
					} else {
						lblMissileList[j][i].setVisible(true);
						lblMissileList[j][i].move(missileX, missileY);
					}
					count++;
				}
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public void displayOneEnemy(final int j, final int i) {
		int delay = 10;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int enemyPlaneY = 0;// initial y of enemy
			int k = -1;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				if (modelEnemyList[j][i].getStatus().equals("dead")) {
					lblEnemyList[j][i].setVisible(false);
					((Timer) evt.getSource()).stop();
					return;
				} else {
					enemyPlaneY = count - lblEnemyList[j][i].getHeight();// speed
					if ((k = checkCollisionListEnemyPlanes(
							modelEnemyList[j][i].getX(), enemyPlaneY,
							lblEnemyList[j][i].getWidth(),
							lblEnemyList[j][i].getHeight())) != -1) {
						// update dead player
						String oldStatus = modelPlaneLocal.getStatus();
						modelPlaneLocal.setID(k);
						modelPlaneLocal.setStatus("dead");
						updateLocalPlaneToServer();
						modelPlaneLocal.setID(myPlayerIndex);
						modelPlaneLocal.setStatus(oldStatus);
						modelPlaneList[k].setStatus("dead");

						lblEnemyList[j][i].setVisible(false);
						modelEnemyLocal.setID(i);
						modelEnemyLocal.setPlayerID(j);
						modelEnemyLocal.setStatus("dead");
						updateLocalEnemyToServer();
						((Timer) evt.getSource()).stop();
						return;

					}
					// if
					// (checkCollisionListEnemyPlanes(modelEnemyLocal.getX(),
					// enemyPlaneY, lblEnemyList[j][i].getWidth(),
					// lblEnemyList[j][i].getHeight())!=-1) {
					// modelPlaneLocal.setStatus("Died");
					// updateLocalPlaneToServer();
					// // lblPlaneList[myPlayerIndex]
					// // allPlayers[myPlayerIndex].setVisible(false);
					// // TODO
					// lblYouDie.setVisible(true);
					// frame.setCursor(Cursor.DEFAULT_CURSOR);
					// displayGameLog("you ded by enemy plane index " +
					// i
					// + " of player " + j);
					// }

					if (enemyPlaneY >= frame.getHeight()
							- lblEnemyList[j][i].getHeight()) {
						modelEnemyLocal.setPlayerID(j);
						modelEnemyLocal.setID(i);
						modelEnemyLocal.setStatus("dead");
						modelEnemyList[j][i].setStatus("dead");
						updateLocalEnemyToServer();
						// allEnemies[j][i].setVisible(false);
						((Timer) evt.getSource()).stop();
						return;
					} else {
						lblEnemyList[j][i].setVisible(true);
						lblEnemyList[j][i].move(modelEnemyList[j][i].getX(),
								enemyPlaneY);
					}
					count++;
				}
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public void updateLocalPlaneToServer() {
		byte[] planeModelInByte = serialize(modelPlaneLocal);
		try {
			outToServer.writeInt(1);
			outToServer.writeInt(planeModelInByte.length);
			outToServer.write(planeModelInByte);
		} catch (IOException e) {
			displayGameLog(e.getMessage());
			return;
		}

	}

	public void updateLocalMissileToServer() {
		byte[] missileModelInByte = serialize(modelMissileLocal);
		try {
			outToServer.writeInt(2);
			outToServer.writeInt(missileModelInByte.length);
			outToServer.write(missileModelInByte);
		} catch (IOException e) {
			displayGameLog(e.getMessage());
			return;
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
			return;
		}
	}

	public void createEnemy() {
		int delay = 1000;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;

			public void actionPerformed(ActionEvent evt) {
				if (count == numberOfEnemyPlane
						&& modelPlaneLocal.getStatus().equals("dead")) {
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

	public int checkCollisionListMissileEnemies(int x, int y, int width,
			int height, int enemyPlayerIndex) {
		for (int i = 0; i < lblEnemyList[enemyPlayerIndex].length; i++) {
			if (modelEnemyList[enemyPlayerIndex][i].getStatus().equals(
					"created")) {
				if (checkOneCollisionMissileEnemy(x, y, width, height,
						enemyPlayerIndex, i)) {
					return i;
				}
			}

		}
		return -1;
	}

	public boolean checkOneCollisionMissileEnemy(int x, int y, int width,
			int height, int enemyPlayerIndex, int enemyListIndex) {
		Rectangle a = new Rectangle(x, y, width, height);
		Rectangle b = new Rectangle(
				lblEnemyList[enemyPlayerIndex][enemyListIndex].getX(),
				lblEnemyList[enemyPlayerIndex][enemyListIndex].getY(),
				lblEnemyList[enemyPlayerIndex][enemyListIndex].getWidth(),
				lblEnemyList[enemyPlayerIndex][enemyListIndex].getHeight());
		if (a.intersects(b))
			return true;
		else
			return false;
	}

	public int checkCollisionListEnemyPlanes(int x, int y, int width, int height) {
		for (int i = 0; i < numberOfPlayers; i++) {
			if (modelPlaneList[i].getStatus().equals("playing")) {
				if (checkOneCollisionEnemyPlane(x, y, width, height, i))
					return i;
			}
		}
		return -1;
	}

	public boolean checkOneCollisionEnemyPlane(int x, int y, int width,
			int height, int playerIndex) {
		Rectangle a = new Rectangle(x, y, width, height);
		Rectangle b = new Rectangle(lblPlaneList[playerIndex].getX(),
				lblPlaneList[playerIndex].getY(),
				lblPlaneList[playerIndex].getWidth(),
				lblPlaneList[playerIndex].getHeight());
		if (a.intersects(b))
			return true;
		else
			return false;
	}

	public static void displayGameLog(String s) {
		gameLog.setText(gameLog.getText() + s + "\n");
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
}
