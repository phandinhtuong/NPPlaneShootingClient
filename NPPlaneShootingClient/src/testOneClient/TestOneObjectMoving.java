package testOneClient;

import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import java.awt.event.MouseAdapter;

public class TestOneObjectMoving {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestOneObjectMoving window = new TestOneObjectMoving();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public TestOneObjectMoving() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	int missileIndex = 0;
	ActionListener taskPerformer[] = new ActionListener[100];
	private void initialize() throws IOException {
		frame = new JFrame();

		frame.setBounds(0, 0, 1049, 836);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// frame.pack();

		final JLabel plane = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/plane1.png"))
				.getImage();
		plane.setIcon(new ImageIcon(img));
		plane.setBounds(130, 521, 86, 84);
		frame.getContentPane().add(plane);

		final JLabel missile = new JLabel("");
		Image img2 = new ImageIcon(this.getClass().getResource("/missile.png"))
				.getImage();
		missile.setIcon(new ImageIcon(img2));
		missile.setBounds(147, 466, 46, 39);
		missile.setVisible(false);

		frame.getContentPane().add(missile);

		 final JLabel[] missileList = new JLabel[100];
		 for (int i = 0;i<100;i++){
		 missileList[i] = new JLabel("");
		 missileList[i].setIcon(new ImageIcon(img2));
		 missileList[i].setBounds(147, 466, 46, 39);
		 missileList[i].setVisible(false);
		 frame.getContentPane().add(missileList[i]);
		 
		 }
		 
		 

		frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				plane.move(e.getX() - 42, e.getY() - 37);
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				plane.move(e.getX() - 42, e.getY() - 37);
			}
		});
		
		
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				System.out.println("missileIndex: "+missileIndex);
				int delay = 10; // milliseconds
				taskPerformer[missileIndex] = new ActionListener() {
					int count = 0;

					public void actionPerformed(ActionEvent evt) {
						if (count == 1000 || e.getY() - 65 - 5 * count < 0) {
							((Timer) evt.getSource()).stop();
						}
						missileList[missileIndex].setVisible(true);
						missileList[missileIndex].move(e.getX() - 25, e.getY() - 65 - 5 * count);
						// System.out.println(SwingUtilities.isEventDispatchThread());
						count++;
					}
				};
				new Timer(delay, taskPerformer[missileIndex]).start();
				missileIndex=missileIndex+1;
			}

		});
		frame.setVisible(true);
	}

}
