package roomManagement;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import objectByteTransform.Deserialize;
import main.Main;
import model.Room;

public class OutsideRoom {
	// create room button
	public static JButton btnCreateRoom = null;
	static JLabel lblRoomlist = new JLabel("RoomList");
	static List<Room> roomListFromServer = null;
	// scroll pane room list
	static JScrollPane scrollPaneRoomList = null;
	// table room list
	public static JTable table;
	// table header of room list
	public static String[] tableHeader = { "Room ID", "#Players", "Status" };
	// refresh button to refresh room list from server
	static JButton btnRefresh = null;

	public void outsideRoom() {
		Main.getFrame().setVisible(false);
		Main.hideCenterMessage();
		btnRefresh = new JButton("Refresh");
		// refresh room list from server
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadAllRoomsFromServer();
			}
		});
		btnRefresh.setBounds(350, 777, 200, 50);
		btnRefresh.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		Main.getFrame().getContentPane().add(btnRefresh);
		btnRefresh.setVisible(true);

		// add scroll pane to main frame
		scrollPaneRoomList = new JScrollPane();
		scrollPaneRoomList.setBounds(5, 286, 900, 383);
		Main.getFrame().getContentPane().add(scrollPaneRoomList);
		scrollPaneRoomList.setVisible(false);

		btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.setBounds(101, 777, 200, 50);
		btnCreateRoom.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		Main.getFrame().getContentPane().add(btnCreateRoom);
		// display create room button
		btnCreateRoom.setVisible(true);
		btnCreateRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getFrame().getContentPane().remove(btnCreateRoom);
				Main.getFrame().getContentPane().remove(btnRefresh);
				Main.getFrame().getContentPane().remove(scrollPaneRoomList);
				Main.getFrame().getContentPane().remove(lblRoomlist);
				CreateRoom createRoom = new CreateRoom();
				createRoom.createRoom();
				// loadAllRoomsFromServer();
			}
		});
		loadAllRoomsFromServer();
		Main.getFrame().setVisible(true);
	}

	private static void loadAllRoomsFromServer() {
		try {
			int i = 0;
			Main.outToServer.writeInt(7);
			while ((i = Main.inFromServer.readInt()) != 0) {
				byte[] roomListFromServerInByte = new byte[i];
				Main.inFromServer.read(roomListFromServerInByte);
				roomListFromServer = Deserialize
						.deserializeRoomModelArrayList(roomListFromServerInByte);
				i = 0;
				break;

			}
			// if (roomListFromServer.size()!=0){
			Object[][] tableData = new Object[roomListFromServer.size()][5];
			// display all rooms from server
			for (int j = 0; j < roomListFromServer.size(); j++) {
				tableData[j][0] = roomListFromServer.get(j).getRoomID();
				tableData[j][1] = roomListFromServer.get(j)
						.getPlayerListInRoom().size();
				tableData[j][2] = roomListFromServer.get(j).getStatus();
			}

			lblRoomlist.setBounds(130, 220, 418, 72);
			lblRoomlist.setFont(new Font("Times New Roman", Font.PLAIN, 40));
			Main.getFrame().getContentPane().add(lblRoomlist);
			lblRoomlist.setVisible(true);

			// load data to table and display
			table = new JTable(tableData, tableHeader);
			table.getTableHeader().setFont(
					new Font("Times New Roman", Font.PLAIN, 30));
			table.getTableHeader().setPreferredSize(new Dimension(10, 40));
			table.setRowHeight(30);
			table.setFont(new Font("Times New Roman", Font.PLAIN, 30));
			scrollPaneRoomList.setViewportView(table);
			scrollPaneRoomList.setVisible(true);

			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int roomID = (int) table.getModel().getValueAt(
							table.getSelectedRow(), 0);
					int res = uploadPlayerJoinRoomToServer(roomID);
					if (res == 1) {
						Main.getFrame().getContentPane().remove(btnCreateRoom);
						Main.getFrame().getContentPane().remove(btnRefresh);
						Main.getFrame().getContentPane()
								.remove(scrollPaneRoomList);
						// lblRoomlist.setVisible(aFlag);
						Main.getFrame().getContentPane().remove(lblRoomlist);
						JoinRoom joinRoom = new JoinRoom();
						joinRoom.joinRoom(roomID);
					} else if (res == 0) {
						Main.displayGameLog("The game started! Cannot join room!");
						loadAllRoomsFromServer();
					} else if (res == 2) {
						Main.displayGameLog("The room does not exist anymore!");
						loadAllRoomsFromServer();
					}

				}
			});
		} catch (IOException e2) {
			Main.displayGameLog(e2.getMessage());
			e2.printStackTrace();
		}

	}

	public static int uploadPlayerJoinRoomToServer(int roomID) {
		try {
			Main.outToServer.writeInt(9);
			Main.outToServer.writeInt(roomID);
			int i = Main.inFromServer.readInt();
			return i;
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}
}
