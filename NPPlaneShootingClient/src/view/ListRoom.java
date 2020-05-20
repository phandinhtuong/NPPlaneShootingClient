package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import controller.Controller;
import model.Player;
import model.RoomList;
import view.abstractView.View;

public class ListRoom extends View{

	public ListRoom(final DataInputStream inFromServer,final DataOutputStream outToServer,final Player player, final JFrame frame,final RoomList roomList) {
		super(player, frame);
		// TODO Auto-generated constructor stub
		String playerID = player.getPlayerID();
		JLabel lblPlayerID = new JLabel("PlayerID: "+ playerID);
		lblPlayerID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblPlayerID.setBounds(0, 26, 374, 59);
		frame.getContentPane().add(lblPlayerID);
		
		
//		JLabel lblRoomList = new JLabel("Room List: "+roomList.get(0).getRoomID());
//		lblRoomList.setFont(new Font("Times New Roman", Font.PLAIN, 30));
//		lblRoomList.setBounds(141, 147, 395, 74);
//		frame.getContentPane().add(lblRoomList);
		
		String[] columnNames = {"#","roomID"};
		
		final String[][] data = new String[roomList.size()][2];
		for(int i=0;i<roomList.size();i++){
			data[i][0] = Integer.toString(i);
			data[i][1] = roomList.get(i).getRoomID();
		}
		
		final JTable roomListTable = new JTable(data,columnNames);
		roomListTable.getTableHeader().setFont(new Font("Times New Roman", Font.PLAIN, 30));
		roomListTable.setRowHeight(30);
		roomListTable.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		roomListTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				///System.out.println(roomListTable.getSelectedRow());
				int rowSelected = roomListTable.getSelectedRow();
				System.out.println("Room "+data[rowSelected][1]+" selected.");
				Controller controller = new Controller();
				
				try {
					controller.joinRoom(inFromServer, outToServer, player, roomList.get(rowSelected).getRoomID(),frame);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JScrollPane roomListScrollPane = new JScrollPane(roomListTable);
		roomListScrollPane.setBounds(70, 150, 668, 482);
		frame.getContentPane().add(roomListScrollPane);
		
		JButton btnExitGame = new JButton("Exit game");
		btnExitGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});
		btnExitGame.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnExitGame.setBounds(718, 24, 164, 62);
		frame.getContentPane().add(btnExitGame);
		
		frame.setVisible(true);
	}

}
