package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Player;
import controller.Controller;
import view.abstractView.View;

public class Home extends View{
	public Home(final DataInputStream inFromServer,final DataOutputStream outToServer,final Player player, final JFrame frame) {
		
		super(player, frame);
		final String playerID = player.getPlayerID();
		JLabel lblPlayerID = new JLabel("PlayerID: "+ playerID);
		lblPlayerID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblPlayerID.setBounds(165, 61, 532, 46);
		frame.getContentPane().add(lblPlayerID);
		
		JButton btnCreateRoom = new JButton("Create room");
		btnCreateRoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//frame.removeAll();
				//frame.setVisible(false);
				Controller controller = new Controller();
				try {
					controller.createRoom(inFromServer,outToServer,frame,player);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//frame.setVisible(true);
			}
		});
		btnCreateRoom.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnCreateRoom.setBounds(253, 137, 235, 74);
		frame.getContentPane().add(btnCreateRoom);
		
		JButton btnJoinGame = new JButton("Join room");
		btnJoinGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Controller controller = new Controller();
				try {
					controller.listRoom(inFromServer, outToServer, frame, player);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnJoinGame.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnJoinGame.setBounds(253, 245, 235, 74);
		frame.getContentPane().add(btnJoinGame);
		
		JButton btnExitGame = new JButton("Exit game");
		btnExitGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});
		btnExitGame.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnExitGame.setBounds(253, 352, 235, 76);
		frame.getContentPane().add(btnExitGame);
		
		frame.setVisible(true);
	}

}
