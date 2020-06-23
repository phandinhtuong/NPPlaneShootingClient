package roomManagement;

import gamePlay.Play;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;

import main.Main;
import model.Player;
import objectByteTransform.Deserialize;

public class JoinRoom {

	static List<Player> playerListFromServer = null;
	// back button
	public static JButton btnBack = null;
	// ready button
	public static JButton btnReady = null;
	// scroll pane player list
	static JScrollPane scrollPanePlayerList = new JScrollPane();
	// table room list
	public static JTable table;
	// table header of room list
	public static String[] tableHeader = { "Player ID", "Status" };
	//room ID label
	static JLabel lblRoomID = null;

	public void joinRoom(final int roomID) {
		Main.getFrame().setVisible(false);
		lblRoomID = new JLabel("Room ID: " + roomID);
		lblRoomID.setBounds(130, 220, 418, 72);
		lblRoomID.setFont(new Font("Times New Roman", Font.PLAIN, 40));
		Main.getFrame().getContentPane().add(lblRoomID);
		lblRoomID.setVisible(true);

		btnBack = new JButton("Back");
		btnBack.setBounds(101, 777, 200, 50);
		btnBack.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		Main.getFrame().getContentPane().add(btnBack);
		// display create room button
		btnBack.setVisible(true);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// remove player from room
				Main.getFrame().getContentPane().remove(scrollPanePlayerList);
				Main.getFrame().getContentPane().remove(btnBack);
				Main.getFrame().getContentPane().remove(btnReady);
				Main.getFrame().getContentPane().remove(lblRoomID);
				OutsideRoom outsideRoom = new OutsideRoom();
				outsideRoom.outsideRoom();
				removePlayerFromRoom(roomID);
			}
		});

		// add scroll pane to main frame
		scrollPanePlayerList.setBounds(5, 286, 900, 383);
		Main.getFrame().getContentPane().add(scrollPanePlayerList);
		scrollPanePlayerList.setVisible(false);
		loadAllPlayersFromServer(roomID);

		btnReady = new JButton("Ready");
		btnReady.setBounds(350, 777, 200, 50);
		btnReady.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		Main.getFrame().getContentPane().add(btnReady);
		btnReady.setVisible(true);
		btnReady.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnReady.getText().equals("Ready")) {
					btnReady.setText("Cancel");
					uploadReadyState(roomID);
				} else {
					btnReady.setText("Ready");
					uploadUnreadyState(roomID);
				}

			}
		});
		Main.getFrame().setVisible(true);
	}

	private static void loadAllPlayersFromServer(final int roomID) {
		int delay = 500;
		// load data every 500 milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int i = 0;
			int thisPlaneStillInThisRoom = 0;
			int numberOfPlayersReady = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Main.outToServer.writeInt(11);
					Main.outToServer.writeInt(roomID);
					while ((i = Main.inFromServer.readInt()) != 0) {
						byte[] playerListFromServerInByte = new byte[i];
						Main.inFromServer.read(playerListFromServerInByte);
						playerListFromServer = Deserialize
								.deserializePlaneModelArrayList(playerListFromServerInByte);
						break;
					}
				} catch (IOException e2) {
					Main.displayGameLog(e2.getMessage());
					((Timer) e.getSource()).stop();
					return;
				}
				// there are no rooms from server
				if (playerListFromServer.size() == 0) {
					((Timer) e.getSource()).stop();
					return;
				} else {
					Object[][] tableData = new Object[playerListFromServer
							.size()][5];
					// display all rooms from server
					for (int i = 0; i < playerListFromServer.size(); i++) {

						tableData[i][0] = playerListFromServer.get(i).getID();
						tableData[i][1] = playerListFromServer.get(i)
								.getStatus();
						if (playerListFromServer.get(i).getID() == Main.myPlaneID) {
							thisPlaneStillInThisRoom = 1;
						}
						if (playerListFromServer.get(i).getStatus()
								.equals("ready")
								|| playerListFromServer.get(i).getStatus()
										.equals("playing")) {
							numberOfPlayersReady++;
						}

					}
					if (thisPlaneStillInThisRoom != 1) {
						((Timer) e.getSource()).stop();
						return;
					}
					thisPlaneStillInThisRoom = 0;

					// if all player ready, the ready button turn into start
					// game
					if (numberOfPlayersReady == playerListFromServer.size()) {
						Main.getFrame().getContentPane().remove(btnBack);
						Main.getFrame().getContentPane().remove(btnReady);
						Main.getFrame().getContentPane()
								.remove(scrollPanePlayerList);
						Main.getFrame().getContentPane().remove(lblRoomID);
						startGame(roomID);
						((Timer) e.getSource()).stop();
						return;
					} else {
						numberOfPlayersReady = 0;
					}

					// load data to table and display
					table = new JTable(tableData, tableHeader);
					table.getTableHeader().setFont(
							new Font("Times New Roman", Font.PLAIN, 30));
					table.getTableHeader().setPreferredSize(
							new Dimension(10, 40));
					table.setRowHeight(30);
					table.setFont(new Font("Times New Roman", Font.PLAIN, 30));
					scrollPanePlayerList.setViewportView(table);
					scrollPanePlayerList.setVisible(true);

				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}

	private static void startGame(int roomID) {
		try {
			Main.outToServer.writeInt(13);
			Main.outToServer.writeInt(roomID);
			Play.play(roomID);
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void removePlayerFromRoom(int roomID) {
		try {
			Main.outToServer.writeInt(10);
			Main.outToServer.writeInt(roomID);
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void uploadReadyState(int roomID) {
		try {
			Main.outToServer.writeInt(12);
			Main.outToServer.writeInt(roomID);
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void uploadUnreadyState(int roomID) {
		try {
			Main.outToServer.writeInt(15);
			Main.outToServer.writeInt(roomID);
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			e.printStackTrace();
		}
	}

}
