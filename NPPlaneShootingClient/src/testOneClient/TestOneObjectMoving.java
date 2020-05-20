package testOneClient;

import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

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
	 * @throws IOException 
	 */
	public TestOneObjectMoving() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				//System.out.println("Mouse: x = "+e.getX()+" y = "+e.getY());
			}
		});
		frame.setBounds(100, 100, 672, 661);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
//		JLabel lblNewLabel = new JLabel(new ImageIcon("..\\image\\plane.png"));
//		lblNewLabel.setBounds(129, 210, 69, 20);
//		frame.getContentPane().add(lblNewLabel);
//		
//		frame.add(new JLabel(new ImageIcon("..\\image/plane.png")));
//		String path = "plane.png";
//        File file = new File(path);
//        BufferedImage image = ImageIO.read(file);
//        JLabel label = new JLabel(new ImageIcon(image));
//        frame.getContentPane().add(label);
//        
		//System.out.println(System.getProperty("C:/Users/ASUS/git/NPPlaneShootingClient/NPPlaneShootingClient/src/testOneClient/plane.png"));
        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(240, 496, 115, 29);
        frame.getContentPane().add(btnNewButton);
        //frame.pack();
        
        JLabel lable = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("/plane.png")).getImage();
        lable.setIcon(new ImageIcon(img));
        lable.setBounds(10, 10, 585, 431);
        frame.getContentPane().add(lable);
        frame.setVisible(true);
	}
}
