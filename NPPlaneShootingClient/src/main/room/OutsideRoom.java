package main.room;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import objectByteTransform.Deserialize;
import main.Main;
import model.Room;

public class OutsideRoom {
	// create room button
	public static JButton btnCreateRoom = null;
	static List<Room> roomListFromServer = null;
	// scroll pane room list
	static JScrollPane scrollPaneRoomList = null;
	// table room list
	public static JTable table;
	// table header of room list
	public static String[] tableHeader = { "Room ID", "Room Name", "Host ID",
			"#Players", "Status" };
	// refresh button to refresh room list from server
	static JButton btnRefresh = null;

	public void outsideRoom() {
		Main.getFrame().setVisible(false);

		btnRefresh = new JButton("Refresh");
		// refresh room list from server
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadAllRoomsFromServer();
			}
		});
		btnRefresh.setBounds(274, 177, 145, 38);
		Main.getFrame().getContentPane().add(btnRefresh);
		btnRefresh.setVisible(true);

		// add scroll pane to main frame
		scrollPaneRoomList = new JScrollPane();
		scrollPaneRoomList.setBounds(5, 286, 900, 383);
		Main.getFrame().getContentPane().add(scrollPaneRoomList);
		scrollPaneRoomList.setVisible(false);
		loadAllRoomsFromServer();

		btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.setBounds(101, 177, 145, 38);
		Main.getFrame().getContentPane().add(btnCreateRoom);
		// display create room button
		btnCreateRoom.setVisible(true);
		btnCreateRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.getFrame().getContentPane().remove(btnCreateRoom);
				Main.getFrame().getContentPane().remove(btnRefresh);
				Main.getFrame().getContentPane().remove(scrollPaneRoomList);
				CreateRoom createRoom = new CreateRoom();
				createRoom.createRoom();
				// loadAllRoomsFromServer();
			}
		});
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
				break;
			}
		} catch (IOException e2) {
			Main.displayGameLog(e2.getMessage());
		}
		// there are no rooms from server
		if (roomListFromServer.size() == 0) {

			Main.displayGameLog("There are no rooms!");
		} else {
			Object[][] tableData = new Object[roomListFromServer.size()][5];
			// display all rooms from server
			for (int i = 0; i < roomListFromServer.size(); i++) {
				tableData[i][0] = roomListFromServer.get(i).getRoomID();
				tableData[i][1] = roomListFromServer.get(i).getRoomName();
				tableData[i][2] = roomListFromServer.get(i).getHostPlayerID();
				tableData[i][3] = roomListFromServer.get(i)
						.getPlayerListInRoom().size();
				tableData[i][4] = roomListFromServer.get(i).getStatus();
			}

			// load data to table and display
			table = new JTable(tableData, tableHeader);
			table.getTableHeader().setFont(
					new Font("Times New Roman", Font.PLAIN, 30));
			table.getTableHeader().setPreferredSize(new Dimension(10, 30));
			table.setRowHeight(30);
			table.setFont(new Font("Times New Roman", Font.PLAIN, 30));
			scrollPaneRoomList.setViewportView(table);
			scrollPaneRoomList.setVisible(true);

			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					int roomID = (int) table.getModel().getValueAt(
							table.getSelectedRow(), 0);
					Main.getFrame().getContentPane().remove(btnCreateRoom);
					Main.getFrame().getContentPane().remove(btnRefresh);
					Main.getFrame().getContentPane().remove(scrollPaneRoomList);
					JoinRoom joinRoom = new JoinRoom();
					joinRoom.joinRoom(roomID);

				}
			});

		}
	}
}
