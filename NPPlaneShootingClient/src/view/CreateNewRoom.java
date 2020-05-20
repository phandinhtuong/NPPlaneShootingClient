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
import model.Room;
import view.abstractView.View;

public class CreateNewRoom extends View{

	public CreateNewRoom(DataInputStream inFromServer,DataOutputStream outToServer,Player player, final JFrame frame, Room room) throws ClassNotFoundException, IOException {
		super(player, frame);
		//frame.setVisible(false);
		String roomID = room.getRoomID();
		String playerID = player.getPlayerID();
		JLabel lblPlayerID = new JLabel("PlayerID: "+ playerID);
		lblPlayerID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblPlayerID.setBounds(0, 26, 374, 59);
		frame.getContentPane().add(lblPlayerID);
		
		JLabel lblRoomid = new JLabel("RoomID: "+ roomID);
		lblRoomid.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblRoomid.setBounds(389, 28, 330, 57);
		frame.getContentPane().add(lblRoomid);
		
		
		Controller controller = new Controller();
		int i = 0;
		String[] columnNames = {"#","playerID","status"};
		String[][] data = null;
		JTable playerListTable = null;
		JScrollPane playerListScrollPane;
		
		//TODO update continuously???
		room = controller.updatePlayerInRoom(inFromServer, outToServer, player, roomID);
		data = new String[room.getPlayerList().size()][3];
		for(i=0;i<room.getPlayerList().size();i++){
			data[i][0] = Integer.toString(i);
			data[i][1] = room.getPlayerList().get(i).getPlayerID();
			data[i][2] = room.getPlayerList().get(i).getStatus();
		}	
		playerListTable = new JTable(data,columnNames);
		playerListTable.getTableHeader().setFont(new Font("Times New Roman", Font.PLAIN, 30));
		playerListTable.setRowHeight(30);
		playerListTable.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		
		playerListScrollPane = new JScrollPane(playerListTable);
		playerListScrollPane.setBounds(70, 150, 668, 482);
		frame.getContentPane().add(playerListScrollPane);
		
		
		
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
