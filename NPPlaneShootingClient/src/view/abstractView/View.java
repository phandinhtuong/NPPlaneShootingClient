package view.abstractView;

import javax.swing.JFrame;
import model.*;
public class View {
	protected View(Player player, final JFrame frame){
		frame.setBounds(100, 100, 904, 767);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		frame.getContentPane().removeAll();
		frame.setVisible(false);
	}
}
