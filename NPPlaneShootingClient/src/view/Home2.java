package view;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Home2 {
	private JFrame frame;
	public void displayHome(String playerID) {
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
		frame.setVisible(true);
		
	}
}
