package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomeDesign {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(final String playerID) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeDesign window = new HomeDesign(playerID);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HomeDesign(String playerID) {
		initialize(playerID);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String playerID) {
		frame = new JFrame();
		frame.setBounds(100, 100, 904, 767);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblPlayerID = new JLabel("PlayerID: "+ playerID);
		lblPlayerID.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblPlayerID.setBounds(165, 61, 532, 46);
		frame.getContentPane().add(lblPlayerID);
		
		JButton btnCreateRoom = new JButton("Create room");
		btnCreateRoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		btnCreateRoom.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnCreateRoom.setBounds(253, 137, 235, 74);
		frame.getContentPane().add(btnCreateRoom);
		
		JButton btnJoinGame = new JButton("Join game");
		btnJoinGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		btnJoinGame.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnJoinGame.setBounds(253, 245, 235, 74);
		frame.getContentPane().add(btnJoinGame);
		
		JButton btnExitGame = new JButton("Exit game");
		btnExitGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnExitGame.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnExitGame.setBounds(253, 352, 235, 76);
		frame.getContentPane().add(btnExitGame);
	}
}
