package directPlaying.refactorDataStructure;

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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import objectByteTransform.Deserialize;
import testOneClient.EnemyModel;
import testOneClient.MissileModel;
import testOneClient.PlaneModel;

public class Client {

	private static JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				@SuppressWarnings("unused")
				Client window = new Client();
				Client.getFrame().setVisible(true);
			}
		});
	}

	public Client() {
		initialize();
	}

	static int missileIndex = 0;
	static int numberOfMissiles = 400;
	static int numberOfEnemies = 100;
	static int myPlayerID = -1;
	static int numberOfPlayers = 3;

	static int missileWidthOrHeight = 52;
	

	static JLabel lblYouDie = new JLabel("You die!"); // display when player dies
	static JTextArea gameLog = new JTextArea(""); // display game log

	// model list from server

	// ArrayList<PlaneModel> modelPlaneList = new ArrayList<PlaneModel>();
	static List<PlaneModel> modelPlaneList = null;
	// PlaneModel[] modelPlaneList = new PlaneModel[numberOfPlayers];
	
//	MissileModel[][] modelMissileList = new MissileModel[numberOfPlayers][numberOfMissile];
	static List<MissileModel> modelMissileList = new ArrayList<MissileModel>();
	
	
//	static EnemyModel[][] modelEnemyList = new EnemyModel[numberOfPlayers][numberOfEnemyPlane];
	static List<EnemyModel> modelEnemyList = null;
	
	// label to display the model list
	static JLabel[] lblPlaneList = new JLabel[numberOfPlayers];
	
//	JLabel[][] lblMissileList = new JLabel[numberOfPlayers][numberOfMissile];
//	static JLabel lblMissile = new JLabel();
	//TODO
	
	
//	static JLabel[][] lblEnemyList = new JLabel[numberOfPlayers][numberOfEnemyPlane];
	
	
	// local model to send to server
	// PlaneModel modelPlaneLocal = new PlaneModel(-1, 500, 500, "playing");

	static PlaneModel modelPlaneLocal = null;
	static MissileModel modelMissileLocal = new MissileModel(0, 0, 0, 0, "ready");

	// String ip = "127.0.0.1";
	String ip = "";
	// /String ip = "192.168.31.153";

	int ipOk = 0;

	int port = 6789;
	Socket clientSocket;
	static DataInputStream inFromServer;
	static DataOutputStream outToServer;
	private JTextField txtIpHere;
	private JButton btnLocalhost;

	@SuppressWarnings("deprecation")
	private void initialize() {
		setFrame(new JFrame());
		getFrame().getContentPane().setBackground(Color.WHITE);

		getFrame().setBounds(0, 0, 930, 992);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		// Load images
		
		final Image planeImage = new ImageIcon(Client.getFrame().getClass().getResource("/plane1.png"))
				.getImage();
		
		
		
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
		getFrame().getContentPane().add(scrollPaneGameLog);

		txtIpHere = new JTextField();
		txtIpHere.setText("");
		txtIpHere.setBounds(242, 189, 179, 26);
		getFrame().getContentPane().add(txtIpHere);
		txtIpHere.setColumns(10);

		// connect to localhost or input IP address of server
		final JButton btnConnect = new JButton("Connect");
		final JLabel lblOrInputIp = new JLabel("Or input IP address of Server:");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// displayGameLog(Integer.toString((e.getID())));
				ip = txtIpHere.getText();
				displayGameLog("Connected to server IP: " + ip);
				ipOk = 1;
				txtIpHere.setVisible(false);
				btnConnect.setVisible(false);
				btnLocalhost.setVisible(false);
				lblOrInputIp.setVisible(false);
				try {
					clientSocket = new Socket(ip, port);
					inFromServer = new DataInputStream(clientSocket
							.getInputStream());
					outToServer = new DataOutputStream(clientSocket
							.getOutputStream());

					// Initial all models and label
					for (int j = 0; j < numberOfPlayers; j++) {

						// Initial modelPlaneList
						// modelPlaneList[j] = new PlaneModel(j, 0, 0,
						// "waiting");

						lblPlaneList[j] = new JLabel("");
						lblPlaneList[j].setIcon(new ImageIcon(planeImage));
						lblPlaneList[j].setBounds(getFrame().getWidth() / 2
								- planeImage.getWidth(null) / 2 * j,
								getFrame().getHeight() - planeImage.getHeight(null)
										* 2, planeImage.getWidth(null),
								planeImage.getHeight(null));
						lblPlaneList[j].setVisible(false);
						getFrame().getContentPane().add(lblPlaneList[j]);
//						for (int i = 0; i < numberOfMissile; i++) {
////							modelMissileList[j][i] = new MissileModel(0, 0, 0,
////									0, "ready");
//							lblMissileList[j][i] = new JLabel("");
//							lblMissileList[j][i].setIcon(new ImageIcon(
//									missileImage));
//							lblMissileList[j][i].setSize(
//									missileImage.getWidth(null),
//									missileImage.getHeight(null));
//							lblMissileList[j][i].setVisible(false);
//							frame.getContentPane().add(lblMissileList[j][i]);
//						}
						//TODO
//						lblMissile = new JLabel("");
//						lblMissile.setIcon(new ImageIcon(
//								missileImage));
//						lblMissile.setSize(
//								missileImage.getWidth(null),
//								missileImage.getHeight(null));
//						lblMissile.setVisible(false);
//						frame.getContentPane().add(lblMissile);
						
						
//						for (int i = 0; i < numberOfEnemyPlane; i++) {
//							modelEnemyList[j][i] = new EnemyModel(0, 0, 0, 0,
//									"ready");
//							lblEnemyList[j][i] = new JLabel("");
//							lblEnemyList[j][i]
//									.setIcon(new ImageIcon(enemyImage));
//							lblEnemyList[j][i].setSize(
//									enemyImage.getWidth(null),
//									enemyImage.getHeight(null));
//							lblEnemyList[j][i].setVisible(false);
//							getFrame().getContentPane().add(lblEnemyList[j][i]);
//						}
					}
//					 displayGameLog("fasf"+Integer.toString(lblMissile.getWidth())+Integer.toString(
//					 lblMissile.getHeight()));
					// displayGameLog("fasf"+Integer.toString(lblEnemyList[0][0].getWidth())+Integer.toString(
					// lblEnemyList[0][0].getHeight()));
					// displayGameLog("player"+Integer.toString(lblPlaneList[0].getWidth())+Integer.toString(
					// lblPlaneList[0].getHeight()));

					// updateLocalPlaneToServer(); //
					int i;
					while ((i = inFromServer.readInt()) != -1) {
						byte[] planeModelFromServerInByte = new byte[i];
						inFromServer.read(planeModelFromServerInByte);
						modelPlaneLocal = Deserialize
								.deserializePlaneModel(planeModelFromServerInByte);
						myPlayerID = modelPlaneLocal.getID();
						break;
					}

					// get player index
					// while ((myPlayerID = inFromServer.readInt()) != -1) {
					// displayGameLog("My Player Index is " + myPlayerID);
					// // TODO
					// //
					// modelPlaneList[myPlayerID].setX(modelPlaneLocal.getX());
					// //
					// modelPlaneList[myPlayerID].setY(modelPlaneLocal.getY());
					// // modelPlaneList[myPlayerID].setStatus("playing");
					// modelPlaneLocal.setID(myPlayerID);
					// break;
					// }
					getFrame().setTitle("Plane shooting gaem | player " + myPlayerID);
					// label to display when dead
					lblYouDie.setAlignmentX(Component.CENTER_ALIGNMENT);
					lblYouDie.setForeground(Color.RED);
					lblYouDie.setHorizontalTextPosition(SwingConstants.CENTER);
					lblYouDie.setHorizontalAlignment(SwingConstants.CENTER);
					lblYouDie
							.setFont(new Font("Times New Roman", Font.BOLD, 99));
					lblYouDie.setBounds(getFrame().getWidth() / 2 - 200,
							getFrame().getHeight() / 2 - 100, 500, 200);
					getFrame().getContentPane().add(lblYouDie);

					lblYouDie.setVisible(false);
					LoadDataFromServer.loadDataFromServer();
					MovePlane.movePlane();
					LaunchMissile.launchMissile();

					getFrame().setVisible(true);
				} catch (IOException e2) {
					getFrame().setCursor(Cursor.DEFAULT_CURSOR);
					displayGameLog("Disable to connect to server: "
							+ e2.getMessage());
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
				// btnConnect.mou
			}
		});
		btnLocalhost.setBounds(314, 102, 209, 29);
		getFrame().getContentPane().add(btnLocalhost);

		btnConnect.setBounds(436, 188, 115, 29);
		getFrame().getContentPane().add(btnConnect);

		lblOrInputIp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOrInputIp.setBounds(305, 147, 228, 26);
		getFrame().getContentPane().add(lblOrInputIp);

		// txtIpHere.setVisible(true);
		// btnConnect.setVisible(true);
		// frame.setVisible(true);
		// while(ipOk != 1){

		// }

	}

	static public int indexOfPlaneWithID(int ID) {
		for (PlaneModel planeModelInList : modelPlaneList) {
			if (planeModelInList.getID() == ID) {
				return ID;
			}
		}
		return -1;
	}

	public static void displayGameLog(String s) {
		gameLog.setText(gameLog.getText() + s + "\n");
	}

	public static JFrame getFrame() {
		return frame;
	}

	public static void setFrame(JFrame frame) {
		Client.frame = frame;
	}
}
