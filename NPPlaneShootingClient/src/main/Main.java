package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.room.OutsideRoom;
import model.Player;
import objectByteTransform.Deserialize;

public class Main {
	// main frame
	private static JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					Main window = new Main();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	// IP address of server to connect
	String ip = "";
	// display game log
	static JTextArea gameLog = new JTextArea("");

	// font for display normal text
	Font normalFont = new Font("Times New Roman", Font.PLAIN, 30);
	Font gamelogFont = new Font("Times New Roman", Font.PLAIN, 20);
	// text field to input IP address of server
	private JTextField txtIpHere;
	// button connect to local host
	private JButton btnLocalhost;
	// socket to connect to server
	Socket clientSocket;
	// data input stream to get data from server
	public static DataInputStream inFromServer;
	// data output stream to send data to server
	public static DataOutputStream outToServer;
	// port of the server is 6789
	int port = 6789;
	// number of missiles of each plane
	public static int numberOfMissiles = 500;
	// this plane's index
	public static int myPlaneID = -1;
	// local model to send to server - use this model to move, launch missile
	public static Player modelPlaneLocal = null;
	// big center message : game over, you die, ...
	public static JLabel lblCenterMessage = new JLabel("");
	// big level notification
	public static JLabel lblBigLevel = new JLabel("");
	// // display number of enemies left
	// static JLabel lblNumberOfEnemiesLeft = new JLabel("");
	// display number of missiles left
	public static JLabel lblNumberOfMissilesLeft = new JLabel("Bullet: "
			+ numberOfMissiles);
	// display score of this plane
	public static JLabel lblScore = new JLabel("Score: 0");
	// display level of the game
	public static JLabel lblLevel = new JLabel("Level 1");
	// display result
	public static JLabel lblResult = new JLabel("");
	// player ID
	public static JLabel lblPlayerid = new JLabel("");

	private void initialize() {
		JFrame frame_1 = new JFrame();
		setFrame(frame_1);
		frame_1.getContentPane().setLayout(null);

		lblPlayerid.setBounds(110, 119, 308, 109);
		lblPlayerid.setFont(new Font("Times New Roman", Font.BOLD, 50));
		frame_1.getContentPane().add(lblPlayerid);
		lblPlayerid.setVisible(false);

		getFrame().setResizable(false);
		getFrame().getContentPane().setBackground(Color.WHITE);
		getFrame().setBounds(0, 0, 930, 992);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);

		gameLog.setOpaque(false);
		gameLog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		gameLog.setEditable(false);
		gameLog.setFont(gamelogFont);

		// scroll pane to display game log
		JScrollPane scrollPaneGameLog = new JScrollPane();
		scrollPaneGameLog.setBorder(BorderFactory.createEmptyBorder());
		scrollPaneGameLog.setViewportView(gameLog);
		scrollPaneGameLog.setBounds(0, 0, 1200, 100);
		scrollPaneGameLog.getViewport().setOpaque(false);
		scrollPaneGameLog.setOpaque(false);
		getFrame().getContentPane().add(scrollPaneGameLog);

		txtIpHere = new JTextField();
		txtIpHere.setText("");
		txtIpHere.setBounds(144, 500, 300, 50);
		txtIpHere.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		getFrame().getContentPane().add(txtIpHere);
		txtIpHere.setColumns(10);

		lblCenterMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCenterMessage.setForeground(Color.RED);
		lblCenterMessage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCenterMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblCenterMessage.setFont(new Font("Times New Roman", Font.BOLD, 99));
		lblCenterMessage.setBounds(getFrame().getWidth() / 2 - 300, getFrame()
				.getHeight() / 2 - 400, 600, 200);
		getFrame().getContentPane().add(lblCenterMessage);

		lblCenterMessage.setVisible(false);

		lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblResult.setHorizontalTextPosition(SwingConstants.CENTER);
		lblResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblResult.setFont(new Font("Times New Roman", Font.BOLD, 50));
		lblResult.setBounds(10, 100, 800, 900);
		getFrame().getContentPane().add(lblResult);

		lblResult.setVisible(false);

		lblBigLevel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBigLevel.setForeground(Color.RED);
		lblCenterMessage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCenterMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblBigLevel.setFont(new Font("Times New Roman", Font.BOLD, 80));
		lblBigLevel.setBounds(360, 360, 600, 200);
		getFrame().getContentPane().add(lblBigLevel);
		lblCenterMessage.setVisible(false);

		// connect to local host or input IP address of server
		final JButton btnConnect = new JButton("Connect");
		final JLabel lblOrInputIp = new JLabel(
				"Or input IP address of Server and click Connect:");

		btnLocalhost = new JButton("Connect to localhost");
		btnLocalhost.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtIpHere.setText("127.0.0.1");
				btnConnect.doClick(100);
				// btnConnect.mou
			}
		});
		btnLocalhost.setBounds(290, 302, 300, 50);
		btnLocalhost.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		getFrame().getContentPane().add(btnLocalhost);

		btnConnect.setBounds(450, 500, 300, 50);
		btnConnect.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		getFrame().getContentPane().add(btnConnect);

		lblOrInputIp.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblOrInputIp.setBounds(175, 400, 700, 50);
		getFrame().getContentPane().add(lblOrInputIp);

		btnConnect.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				// get IP from input
				ip = txtIpHere.getText();
				displayGameLog("Connected to server IP: " + ip);

				// hide all the input IP and connect buttons
				txtIpHere.setVisible(false);
				btnConnect.setVisible(false);
				btnLocalhost.setVisible(false);
				lblOrInputIp.setVisible(false);
				try {
					// connect to server socket through IP and port
					clientSocket = new Socket(ip, port);
					// data input stream to send to server
					inFromServer = new DataInputStream(clientSocket
							.getInputStream());
					// data output stream from server
					outToServer = new DataOutputStream(clientSocket
							.getOutputStream());
					int readPlaneID = -1;
					while ((readPlaneID = inFromServer.readInt()) != -1) {
						// byte array to get plane model in byte from server
						byte[] planeModelFromServerInByte = new byte[readPlaneID];
						inFromServer.read(planeModelFromServerInByte);
						// deserialize local plane
						modelPlaneLocal = Deserialize
								.deserializePlayer(planeModelFromServerInByte);
						// get player id
						myPlaneID = modelPlaneLocal.getID();
						break;
					}
					displayPlayerID(myPlaneID);

					// set title of the frame
					getFrame().setTitle(
							"Plane shooting gaem brrrr brrr | player "
									+ myPlaneID);

					// lblNumberOfEnemiesLeft.setFont(normalFont);
					// lblNumberOfEnemiesLeft.setBounds(0,100,150,30);
					// getFrame().getContentPane().add(lblNumberOfEnemiesLeft);
					// lblNumberOfEnemiesLeft.setVisible(false);

					lblNumberOfMissilesLeft.setFont(normalFont);
					lblNumberOfMissilesLeft.setBounds(790, 0, 150, 50);
					getFrame().getContentPane().add(lblNumberOfMissilesLeft);
					lblNumberOfMissilesLeft.setVisible(false);

					lblScore.setFont(normalFont);
					lblScore.setBounds(790, 40, 150, 50);
					getFrame().getContentPane().add(lblScore);
					lblScore.setVisible(false);

					lblLevel.setFont(normalFont);
					lblLevel.setBounds(790, 80, 150, 50);
					getFrame().getContentPane().add(lblLevel);
					lblLevel.setVisible(false);

					// outside room to create room or join room
					OutsideRoom outsideRoom = new OutsideRoom();
					outsideRoom.outsideRoom();
					getFrame().setVisible(true);
				} catch (IOException | ClassNotFoundException e2) {
					getFrame().setCursor(Cursor.DEFAULT_CURSOR);
					displayGameLog("Disable to connect to server: "
							+ e2.getMessage());
					displayGameLog("Please connect again!");
					txtIpHere.setVisible(true);
					btnConnect.setVisible(true);
					btnLocalhost.setVisible(true);
					lblOrInputIp.setVisible(true);

				}
			}
		});

	}

	public static JFrame getFrame() {
		return frame;
	}

	public static void setFrame(JFrame frame) {
		Main.frame = frame;
	}

	// display game log
	public static void displayGameLog(String s) {
		gameLog.setText(gameLog.getText() + s + "\n");
	}

	// public static void displayNumberOfEnemiesLeft(int i){
	// lblNumberOfEnemiesLeft.setText(i+" enemies left");
	// lblNumberOfEnemiesLeft.setVisible(true);
	// }
	public static void displayPlayerID(int i) {
		lblPlayerid.setText("Player ID : " + i);
		lblPlayerid.setVisible(true);

	}

	public static void hidePlayerID() {
		lblPlayerid.setVisible(false);
	}

	public static void displayNumberOfMissilesLeft(int i) {
		lblNumberOfMissilesLeft.setText("Bullet: " + i);
		lblNumberOfMissilesLeft.setVisible(true);
	}

	public static void displayScore(int i) {
		lblScore.setText("Score: " + i);
		lblScore.setVisible(true);
	}

	public static void displayLevel(int i) {
		lblLevel.setText("Level " + i);
		lblLevel.setVisible(true);
	}

	public static void displayCenterMessage(String s) {
		lblCenterMessage.setText(s);
		if (!lblCenterMessage.isVisible()) {
			lblCenterMessage.setVisible(true);
		}
	}

	public static void displayResult(String s) {
		lblResult.setText(s);
		lblResult.setVisible(true);
	}

	public static void hideCenterMessage() {
		lblCenterMessage.setVisible(false);
	}

	public static void displayBigLevel(int i) {
		lblBigLevel.setText("Level " + i);
		lblBigLevel.setVisible(true);
	}

	public static void hideBigLevel() {
		lblBigLevel.setVisible(false);
	}
}
