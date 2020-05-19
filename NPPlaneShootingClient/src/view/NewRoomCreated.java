package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import view.abstractView.View;

public class NewRoomCreated extends View{

	public NewRoomCreated(String playerID, final JFrame frame, String roomID) {
		super(playerID, frame);
		//frame.setVisible(false);
		JLabel lblPlayerID = new JLabel("PlayerID: "+ playerID);
		lblPlayerID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblPlayerID.setBounds(0, 26, 374, 59);
		frame.getContentPane().add(lblPlayerID);
		
		JLabel lblRoomid = new JLabel("RoomID: "+ roomID);
		lblRoomid.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblRoomid.setBounds(389, 28, 330, 57);
		frame.getContentPane().add(lblRoomid);
		
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
