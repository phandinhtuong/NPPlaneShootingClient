package testMultipleClientGraphicOnServer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import objectByteTransform.Deserialize;
import objectByteTransform.Serialize;
import testOneClient.EnemyModel;
import testOneClient.MissileModel;
import testOneClient.PlaneModel;

public class Client {

	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Client window = new Client();
				window.frame.setVisible(true);
			}
		});
	}

	public Client() {
		initialize();
	}

	int missileIndex = 0;
	int numberOfMissile = 100;
	int numberOfEnemyPlane = 100;
	int myPlayerID = -1;
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
		txtIpHere.setBounds(242, 189, 179, 26);
		frame.getContentPane().add(txtIpHere);
		txtIpHere.setColumns(10);
		
		
		//connect to localhost or input IP address of server
		final JButton btnConnect = new JButton("Connect");
		final JLabel lblOrInputIp = new JLabel("Or input IP address of Server:");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				displayGameLog(Integer.toString((e.getID())));
				ip = txtIpHere.getText();
				displayGameLog("Connected to server IP: "+ip);
				ipOk = 1;
				txtIpHere.setVisible(false);
				btnConnect.setVisible(false);
				btnLocalhost.setVisible(false);
				lblOrInputIp.setVisible(false);
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
							modelEnemyList[j][i] = new EnemyModel(0, 0, 0,0, "ready");
							lblEnemyList[j][i] = new JLabel("");
							lblEnemyList[j][i].setIcon(new ImageIcon(enemyImage));
							lblEnemyList[j][i].setSize(enemyImage.getWidth(null),
									enemyImage.getHeight(null));
							lblEnemyList[j][i].setVisible(false);
							frame.getContentPane().add(lblEnemyList[j][i]);
						}
					}
//displayGameLog("fasf"+Integer.toString(lblMissileList[0][0].getWidth())+Integer.toString(
//								lblMissileList[0][0].getHeight()));
//displayGameLog("fasf"+Integer.toString(lblEnemyList[0][0].getWidth())+Integer.toString(
//		lblEnemyList[0][0].getHeight()));
//					displayGameLog("player"+Integer.toString(lblPlaneList[0].getWidth())+Integer.toString(
//							lblPlaneList[0].getHeight()));	
					
					
					// get player index
					while ((myPlayerID = inFromServer.readInt()) != -1) {
						displayGameLog("My Player Index is " + myPlayerID);
						// TODO
						modelPlaneList[myPlayerID].setX(modelPlaneLocal.getX());
						modelPlaneList[myPlayerID].setY(modelPlaneLocal.getY());
						modelPlaneList[myPlayerID].setStatus("playing");
						modelPlaneLocal.setID(myPlayerID);
						break;
					}
					frame.setTitle("Plane shooting gaem | player " + myPlayerID);
					// label to display when dead
					lblYouDie.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblYouDie.setForeground(Color.RED);
					lblYouDie.setHorizontalTextPosition(SwingConstants.CENTER);
					lblYouDie.setHorizontalAlignment(SwingConstants.CENTER);
					lblYouDie.setFont(new Font("Times New Roman", Font.BOLD, 99));
					lblYouDie.setBounds(frame.getWidth() / 2 - 200,
							frame.getHeight() / 2 - 100, 500, 200);
					frame.getContentPane().add(lblYouDie);

					lblYouDie.setVisible(false);

					// mouse pressed = launch missile / Create missile
					frame.getContentPane().addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(final MouseEvent e) {
							if (modelPlaneLocal.getStatus().equals("playing")) {
								if (missileIndex < numberOfMissile) {
//									displayGameLog("missileIndex lauched: "
//											+ missileIndex);
									modelMissileLocal.setPlayerID(myPlayerID);
									modelMissileLocal.setID(missileIndex);
									modelMissileLocal.setX(e.getX()-lblMissileList[0][0].getWidth()/2);
									modelMissileLocal.setY(e.getY()
											- lblPlaneList[myPlayerID].getHeight()
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
									if (modelPlaneList[myPlayerID].getStatus().equals("playing")) {
										modelPlaneLocal.setID(myPlayerID);
										modelPlaneLocal.setX(e.getX()
												- lblPlaneList[myPlayerID]
														.getWidth() / 2);
										modelPlaneLocal.setY(e.getY()
												- lblPlaneList[myPlayerID]
														.getHeight() / 2);
										updateLocalPlaneToServer();
									}
								}

								@Override
								public void mouseDragged(MouseEvent e) {
									if (modelPlaneLocal.getStatus().equals("playing")) {
										modelPlaneLocal.setID(myPlayerID);
										modelPlaneLocal.setX(e.getX()
												- lblPlaneList[myPlayerID]
														.getWidth() / 2);
										modelPlaneLocal.setY(e.getY()
												- lblPlaneList[myPlayerID]
														.getHeight() / 2);
										updateLocalPlaneToServer();
									}
								}
							});

					loadDataFromServer();
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
		btnLocalhost.setBounds(314, 102, 209, 29);
		frame.getContentPane().add(btnLocalhost);
		
		btnConnect.setBounds(436, 188, 115, 29);
		frame.getContentPane().add(btnConnect);
		
		
		lblOrInputIp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOrInputIp.setBounds(305, 147, 228, 26);
		frame.getContentPane().add(lblOrInputIp);
		
		

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
						modelPlaneList = Deserialize.deserializePlaneModelList(planeModelListInByte);
						break;
					}
					outToServer.writeInt(5);
					while ((i = inFromServer.readInt()) != 0) {
						byte[] missileModelListInByte = new byte[i];
						inFromServer.read(missileModelListInByte);
						modelMissileList = Deserialize.deserializeMissileModelList(missileModelListInByte);
						break;
					}
					outToServer.writeInt(6);
					while ((i = inFromServer.readInt()) != 0) {
						byte[] enemyModelListInByte = new byte[i];
						inFromServer.read(enemyModelListInByte);
						modelEnemyList = Deserialize.deserializeEnemyModelList(enemyModelListInByte);
						break;
					}
					displayAllPlayers();
					displayAllMissiles();
					displayAllEnemies();
				} catch (IOException e) {
					displayGameLog("st");
					displayGameLog(e.getMessage());
					((Timer) evt.getSource()).stop();
					return;
				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}

	public void displayAllPlayers() {
		if (modelPlaneList!=null){
			modelPlaneLocal = modelPlaneList[myPlayerID];
			for (int i = 0; i < modelPlaneList.length; i++) {
				if (modelPlaneList[i].getStatus().equals("dead")) {
					if (i == myPlayerID) {
						lblYouDie.setVisible(true);
					}
					if (lblPlaneList[i].isVisible()) {
						displayGameLog("Player " + i + " is dead.");
						lblPlaneList[i].setVisible(false);
					}

				} else if (modelPlaneList[i].getStatus().equals("disconnected")&&lblPlaneList[i].isVisible()) {
					//String oldStatus = modelPlaneLocal.getStatus();
					displayGameLog("Player " + i + " disconnected.");
//					modelPlaneLocal.setID(i);
//					modelPlaneLocal.setStatus("dead");
//					updateLocalPlaneToServer();
//					modelPlaneLocal.setID(myPlayerID);
//					modelPlaneLocal.setStatus(oldStatus);
					lblPlaneList[i].setVisible(false);
				} else if (modelPlaneList[i].getStatus().equals("playing")) {
					displayOnePlayer(i);
				}
			}
		}
	}

	public void displayAllMissiles() {
		if (modelMissileList!=null){
			for (int j = 0; j < modelMissileList.length; j++) {
				for (int i = 0; i < modelMissileList[j].length; i++) {
					if (modelMissileList[j][i].getStatus().equals("dead"))
						lblMissileList[j][i].setVisible(false);
					else if (modelMissileList[j][i].getStatus().equals("launched")){
						displayOneMissile(j, i);
					}

				}
			}
		}
	}

	public void displayAllEnemies() {
		if (modelEnemyList!=null){
			for (int j = 0; j < modelEnemyList.length; j++) {
				for (int i = 0; i < modelEnemyList[j].length; i++) {
					if (modelEnemyList[j][i].getStatus().equals("dead"))
						lblEnemyList[j][i].setVisible(false);
					else if (modelEnemyList[j][i].getStatus().equals("created")
							) {
						displayOneEnemy(j, i);
					}

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

	@SuppressWarnings("deprecation")
	public void displayOneMissile(final int j, final int i) {
		lblMissileList[j][i].setVisible(true);
		lblMissileList[j][i].move(modelMissileList[j][i].getX(), modelMissileList[j][i].getY());
	}

	@SuppressWarnings("deprecation")
	public void displayOneEnemy(final int j, final int i) {
						lblEnemyList[j][i].setVisible(true);
						lblEnemyList[j][i].move(modelEnemyList[j][i].getX(),
								modelEnemyList[j][i].getY());
	}

	public void updateLocalPlaneToServer() {
		byte[] planeModelInByte = Serialize.serialize(modelPlaneLocal);
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
		byte[] missileModelInByte = Serialize.serialize(modelMissileLocal);
		try {
			outToServer.writeInt(2);
			outToServer.writeInt(missileModelInByte.length);
			outToServer.write(missileModelInByte);
		} catch (IOException e) {
			displayGameLog(e.getMessage());
			return;
		}
	}

	

	

	public static void displayGameLog(String s) {
		gameLog.setText(gameLog.getText() + s + "\n");
	}
}
