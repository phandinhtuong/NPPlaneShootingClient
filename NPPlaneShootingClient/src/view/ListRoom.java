package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.RoomList;
import view.abstractView.View;

public class ListRoom extends View{

	public ListRoom(String playerID, final JFrame frame,RoomList roomList) {
		super(playerID, frame);
		// TODO Auto-generated constructor stub
		
		JLabel lblPlayerID = new JLabel("PlayerID: "+ playerID);
		lblPlayerID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblPlayerID.setBounds(0, 26, 374, 59);
		frame.getContentPane().add(lblPlayerID);
		
		
		JLabel lblRoomList = new JLabel("Room List: "+roomList.get(0).getRoomID());
		lblRoomList.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblRoomList.setBounds(141, 147, 395, 74);
		frame.getContentPane().add(lblRoomList);
		
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
