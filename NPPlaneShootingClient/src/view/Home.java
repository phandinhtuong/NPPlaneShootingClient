package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(final String playerID) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home window = new Home(playerID);
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
	public Home(String playerID) {
		initialize(playerID);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String playerID) {
		frame = new JFrame();
		frame.setBounds(100, 100, 811, 467);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("PlayerID: "+ playerID);
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblNewLabel.setBounds(211, 48, 532, 46);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Create room");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnNewButton.setBounds(253, 137, 235, 74);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Join game");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		btnNewButton_1.setBounds(253, 245, 235, 74);
		frame.getContentPane().add(btnNewButton_1);
	}
}
