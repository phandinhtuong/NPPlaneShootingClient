package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListRoomDesign {

	private JFrame frame;
	private JTable table;

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
		label.setBounds(0, 26, 374, 59);
		label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		frame.getContentPane().add(label);
		
		JButton button = new JButton("Exit game");
		button.setBounds(718, 24, 164, 62);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		frame.getContentPane().add(button);
		
		String[] columnNames = {"#","roomID"};
		
		String[][] data = new String[2][2];
		for(int i=0;i<2;i++){
			data[i][0] = Integer.toString(i);
			data[i][1] = Integer.toString(i);
		}
		table = new JTable(data,columnNames);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		table.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(70, 150, 668, 482);
		frame.getContentPane().add(scrollPane);
		
		
		
		
		
	}
}
