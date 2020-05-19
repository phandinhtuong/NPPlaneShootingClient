package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ListRoomDesign {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListRoomDesign window = new ListRoomDesign();
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
	public ListRoomDesign() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 904, 767);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JLabel label = new JLabel("PlayerID: <dynamic>");
		label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		label.setBounds(0, 26, 374, 59);
		frame.getContentPane().add(label);
		
		JButton button = new JButton("Exit game");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		button.setBounds(718, 24, 164, 62);
		frame.getContentPane().add(button);
		
		JLabel lblRoomList = new JLabel("Room List: ");
		lblRoomList.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblRoomList.setBounds(141, 147, 395, 74);
		frame.getContentPane().add(lblRoomList);
		
		
	}

}
