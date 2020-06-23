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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import objectByteTransform.Deserialize;
import main.room.OutsideRoom;
import model.Player;

import javax.swing.JTable;

public class Main {
	// main frame
	private static JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
	Font normalFont = new Font("Times New Roman", Font.PLAIN, 20);

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
	static int numberOfMissiles = 400;
	// this plane's index
	public static int myPlaneID = -1;
	// local model to send to server - use this model to move, launch missile
	public static Player modelPlaneLocal = null;
	// big center message : game over, you die, ...
	static JLabel lblCenterMessage = new JLabel("");
	// display number of enemies left
	static JLabel lblNumberOfEnemiesLeft = new JLabel("");
	// display number of missiles left
	static JLabel lblNumberOfMissilesLeft = new JLabel(numberOfMissiles
			+ " missiles left");
	// display score of this plane
	static JLabel lblScore = new JLabel("Score: 0");
	
	
	private void initialize() {
		JFrame frame_1 = new JFrame();
		setFrame(frame_1);
		frame_1.getContentPane().setLayout(null);
		
		
		
		
		
		
		
		
		getFrame().getContentPane().setBackground(Color.WHITE);
		getFrame().setBounds(0, 0, 930, 992);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);

		gameLog.setOpaque(false);
		gameLog.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		gameLog.setEditable(false);
		gameLog.setFont(normalFont);

		// scroll pane to display game log
		JScrollPane scrollPaneGameLog = new JScrollPane();
		scrollPaneGameLog.setViewportView(gameLog);
		scrollPaneGameLog.setBounds(0, 0, 900, 100);
		scrollPaneGameLog.getViewport().setOpaque(false);
		scrollPaneGameLog.setOpaque(false);
		getFrame().getContentPane().add(scrollPaneGameLog);
		// TODO scroll pane have no more bound

		txtIpHere = new JTextField();
		txtIpHere.setText("");
		txtIpHere.setBounds(242, 189, 200, 26);
		getFrame().getContentPane().add(txtIpHere);
		txtIpHere.setColumns(10);

		lblCenterMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCenterMessage.setForeground(Color.RED);
		lblCenterMessage.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCenterMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblCenterMessage
				.setFont(new Font("Times New Roman", Font.BOLD, 99));
		lblCenterMessage.setBounds(getFrame().getWidth() / 2 - 300,
				getFrame().getHeight() / 2 - 100, 600, 200);
		getFrame().getContentPane().add(lblCenterMessage);

		lblCenterMessage.setVisible(false);
		
		
		// connect to local host or input IP address of server
		final JButton btnConnect = new JButton("Connect");
		final JLabel lblOrInputIp = new JLabel("Or input IP address of Server:");

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

		btnConnect.setBounds(450, 188, 115, 29);
		getFrame().getContentPane().add(btnConnect);
		
		lblOrInputIp.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOrInputIp.setBounds(305, 147, 228, 26);
		getFrame().getContentPane().add(lblOrInputIp);
		
		btnConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// get IP from input
				ip = txtIpHere.getText();
				displayGameLog("Connected to server IP: " + ip);

				//hide all the input IP and connect buttons
				txtIpHere.setVisible(false);
				btnConnect.setVisible(false);
				btnLocalhost.setVisible(false);
				lblOrInputIp.setVisible(false);
				try{
					//connect to server socket through IP and port
					clientSocket = new Socket(ip, port);
					//data input stream to send to server
					inFromServer = new DataInputStream(clientSocket
							.getInputStream());
					//data output stream from server
					outToServer = new DataOutputStream(clientSocket
							.getOutputStream());
					int readPlaneID = -1;
					while((readPlaneID = inFromServer.readInt())!= -1){
						//byte array to get plane model in byte from server
						byte[] planeModelFromServerInByte = new byte[readPlaneID];
						inFromServer.read(planeModelFromServerInByte);
						//deserialize local plane
						modelPlaneLocal = Deserialize.deserializePlayer(planeModelFromServerInByte);
						//get player id
						myPlaneID = modelPlaneLocal.getID();
						break;
					}
					//set title of the frame
					getFrame().setTitle("Plane shooting gaem brrrr brrr | player " + myPlaneID);
					
					lblNumberOfEnemiesLeft.setFont(normalFont);
					lblNumberOfEnemiesLeft.setBounds(0,100,150,30);
					getFrame().getContentPane().add(lblNumberOfEnemiesLeft);
					lblNumberOfEnemiesLeft.setVisible(false);
					
					lblNumberOfMissilesLeft.setFont(normalFont);
					lblNumberOfMissilesLeft.setBounds(0,130,150,30);
					getFrame().getContentPane().add(lblNumberOfMissilesLeft);
					
					lblScore.setFont(normalFont);
					lblScore.setBounds(0,160,150,30);
					getFrame().getContentPane().add(lblScore);
					
					//outside room to create room or join room
					OutsideRoom outsideRoom = new OutsideRoom();
					outsideRoom.outsideRoom();
					getFrame().setVisible(true);
				}catch(IOException | ClassNotFoundException e2){
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
	public static void displayNumberOfEnemiesLeft(int i){
		lblNumberOfEnemiesLeft.setText(i+" enemies left");
		lblNumberOfEnemiesLeft.setVisible(true);
	}
	public static void displayNumberOfMissilesLeft(int i){
		lblNumberOfMissilesLeft.setText(i+" missiles left");
		lblNumberOfMissilesLeft.setVisible(true);
	}
	public static void displayScore(int i){
		lblScore.setText("Score: "+i);
		lblScore.setVisible(true);
	}
	public static void displayCenterMessage(String s){
		lblCenterMessage.setText(s);
		lblCenterMessage.setVisible(true);
	}
}
