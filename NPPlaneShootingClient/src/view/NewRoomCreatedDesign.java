package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewRoomCreatedDesign {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewRoomCreatedDesign window = new NewRoomCreatedDesign();
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
	public NewRoomCreatedDesign() {
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
		
		JLabel lblRoomid = new JLabel("RoomID: ");
		lblRoomid.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblRoomid.setBounds(389, 28, 330, 57);
		frame.getContentPane().add(lblRoomid);
	}
}
