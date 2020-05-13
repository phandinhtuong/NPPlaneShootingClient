package view.abstractView;

import javax.swing.JFrame;

public class View {
	protected View(String playerID, final JFrame frame){
		frame.setBounds(100, 100, 904, 767);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		frame.setVisible(true);
	}
}
