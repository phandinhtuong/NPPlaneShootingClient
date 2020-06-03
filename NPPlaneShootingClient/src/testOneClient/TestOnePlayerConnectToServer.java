package testOneClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TestOnePlayerConnectToServer {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestOnePlayerConnectToServer window = new TestOnePlayerConnectToServer();
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
	 * @throws UnknownHostException
	 */
	public TestOnePlayerConnectToServer() throws UnknownHostException,
			IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	int missileIndex = 0;
	int numberOfMissile = 100;
	int numberOfEnemyPlane = 100;

	Image missileImage = new ImageIcon(this.getClass().getResource(
			"/missile.png")).getImage();
	Image planeImage = new ImageIcon(this.getClass().getResource("/plane1.png"))
			.getImage();
	Image enemyImage = new ImageIcon(this.getClass().getResource(
			"/enemyPlaneGraySmaller.png")).getImage();
	JLabel lblYouDie = new JLabel("You die!");

	JLabel plane = new JLabel("");

	JLabel enemyPlaneList[] = new JLabel[numberOfEnemyPlane];

	JLabel[] missileList = new JLabel[numberOfMissile];

	PlaneModel planeModel = new PlaneModel(0,0, 0,"playing");
	MissileModel missileModel = new MissileModel(0,0, 0, 0,"ready");
	EnemyModel enemyModel = new EnemyModel(0,0,0,0,"ready");
	@SuppressWarnings("resource")
	private void initialize() throws UnknownHostException, IOException {
		String ip = "127.0.0.1";
//		String ip = "192.168.31.153";
		int port = 6789;

		Socket clientSocket;

		clientSocket = new Socket(ip, port);
		final DataInputStream inFromServer = new DataInputStream(
				clientSocket.getInputStream());
		final DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());

		frame = new JFrame();
		
		
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Plane shooting gaem");
		frame.setBounds(0, 0, 1080, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setCursor(frame.getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));
		

		plane.setIcon(new ImageIcon(planeImage));
		plane.setBounds(frame.getWidth() / 2 - planeImage.getWidth(null) / 2,
				frame.getHeight() - planeImage.getHeight(null) * 2,
				planeImage.getWidth(null), planeImage.getHeight(null));
		frame.getContentPane().add(plane);

		createEnemyPlanes(enemyImage, plane,inFromServer,outToServer);

		lblYouDie.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblYouDie.setForeground(Color.RED);
		lblYouDie.setHorizontalTextPosition(SwingConstants.CENTER);
		lblYouDie.setHorizontalAlignment(SwingConstants.CENTER);
		lblYouDie.setFont(new Font("Times New Roman", Font.BOLD, 99));
		lblYouDie.setBounds(frame.getWidth() / 2 - 200,
				frame.getHeight() / 2 - 100, 400, 200);
		frame.getContentPane().add(lblYouDie);
		lblYouDie.setVisible(false);
		// final JLabel missile = new JLabel("");

		// missile.setIcon(new ImageIcon(img2));
		// missile.setBounds(147, 466, 46, 39);
		// missile.setVisible(false);
		//
		// frame.getContentPane().add(missile);

		for (int i = 0; i < numberOfMissile; i++) {
			missileList[i] = new JLabel("");
			missileList[i].setIcon(new ImageIcon(missileImage));
			missileList[i].setSize(missileImage.getWidth(null),
					missileImage.getHeight(null));
			// missileList[i].setBounds(147, 466, missileImage.getWidth(null),
			// missileImage.getHeight(null));
			missileList[i].setVisible(false);
			frame.getContentPane().add(missileList[i]);
		}

		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if (plane.isVisible()) {

					if (missileIndex < numberOfMissile) {
						System.out.println("missileIndex lauched: "
								+ missileIndex);

						missileModel.setID(missileIndex);
						missileModel.setX(e.getX());
						missileModel.setY(e.getY() - plane.getHeight() + 50);
						try {
							missileMoveThroughServer(missileModel,
									inFromServer, outToServer);
						} catch (ClassNotFoundException | IOException e1) {

							e1.printStackTrace();
						}

						// missileMove(missileList, missileIndex,
						// e.getX(),e.getY() - plane.getHeight() + 50);

						missileIndex = missileIndex + 1;
					} else {
						System.out.println("Run out of missile!");
					}

				}

			}
		});

		frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (plane.isVisible())
					try {
						planeModel.setX(e.getX() - plane.getWidth() / 2);
						planeModel.setY(e.getY() - plane.getHeight() / 2);
						planeMoveThroughServer(planeModel, inFromServer,
								outToServer);
					} catch (IOException e1) {

						e1.printStackTrace();
					}
					// plane.move(e.getX() - plane.getWidth() / 2, e.getY()
					// - plane.getHeight() / 2);
					catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (plane.isVisible())
					try {
						planeModel.setX(e.getX() - plane.getWidth() / 2);
						planeModel.setY(e.getY() - plane.getHeight() / 2);
						planeMoveThroughServer(planeModel, inFromServer,
								outToServer);
					} catch (IOException e1) {

						e1.printStackTrace();
					}
					// plane.move(e.getX() - plane.getWidth() / 2, e.getY()
					// - plane.getHeight() / 2);
					catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
			}
		});
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					if (plane.isVisible())
						try {
							planeModel.setX(plane.getX()+5);
							planeModel.setY(plane.getY());
							planeMoveThroughServer(planeModel, inFromServer,
									outToServer);
						} catch (IOException e1) {

							e1.printStackTrace();
						}
						// plane.move(e.getX() - plane.getWidth() / 2, e.getY()
						// - plane.getHeight() / 2);
						catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					
				}else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					if (plane.isVisible())
						try {
							planeModel.setX(plane.getX()-5);
							planeModel.setY(plane.getY());
							planeMoveThroughServer(planeModel, inFromServer,
									outToServer);
						} catch (IOException e1) {

							e1.printStackTrace();
						}
						// plane.move(e.getX() - plane.getWidth() / 2, e.getY()
						// - plane.getHeight() / 2);
						catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
				}else if (e.getKeyCode() == KeyEvent.VK_UP){
					if (plane.isVisible())
						try {
							planeModel.setX(plane.getX());
							planeModel.setY(plane.getY()-5);
							planeMoveThroughServer(planeModel, inFromServer,
									outToServer);
						} catch (IOException e1) {

							e1.printStackTrace();
						}
						// plane.move(e.getX() - plane.getWidth() / 2, e.getY()
						// - plane.getHeight() / 2);
						catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					
				}else if (e.getKeyCode() == KeyEvent.VK_DOWN){
					if (plane.isVisible())
						try {
							planeModel.setX(plane.getX());
							planeModel.setY(plane.getY()+5);
							planeMoveThroughServer(planeModel, inFromServer,
									outToServer);
						} catch (IOException e1) {

							e1.printStackTrace();
						}
						// plane.move(e.getX() - plane.getWidth() / 2, e.getY()
						// - plane.getHeight() / 2);
						catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					
				}else if (e.getKeyCode() == KeyEvent.VK_SPACE){
					if (plane.isVisible()) {

						if (missileIndex < numberOfMissile) {
							System.out.println("missileIndex lauched: "
									+ missileIndex);

							missileModel.setID(missileIndex);
							missileModel.setX(plane.getX());
							missileModel.setY(plane.getY() - plane.getHeight() + 50);
							try {
								missileMoveThroughServer(missileModel,
										inFromServer, outToServer);
							} catch (ClassNotFoundException | IOException e1) {

								e1.printStackTrace();
							}

							// missileMove(missileList, missileIndex,
							// e.getX(),e.getY() - plane.getHeight() + 50);

							missileIndex = missileIndex + 1;
						} else {
							System.out.println("Run out of missile!");
						}

					}
				}
			}
		});
		frame.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	public void planeMoveThroughServer(PlaneModel planeModel,
			DataInputStream inFromServer, DataOutputStream outToServer)
			throws IOException, ClassNotFoundException {
		byte[] planeModelInByte = serialize(planeModel);
		outToServer.writeInt(1);
		outToServer.writeInt(planeModelInByte.length);
		outToServer.write(planeModelInByte);
		int i = 0;
		while ((i = inFromServer.readInt()) != 0) {
			byte[] planeModelFromServerInByte = new byte[i];
			inFromServer.read(planeModelFromServerInByte);
			PlaneModel planeModelFromServer = deserializePlaneModel(planeModelFromServerInByte);
			plane.move(planeModelFromServer.getX(), planeModelFromServer.getY());
			break;
		}

	}

	public void missileMoveThroughServer(MissileModel missileModel,
			DataInputStream inFromServer, DataOutputStream outToServer)
			throws IOException, ClassNotFoundException {

		byte[] missileModelInByte = serialize(missileModel);
		outToServer.writeInt(2);
		outToServer.writeInt(missileModelInByte.length);
		outToServer.write(missileModelInByte);
		int i = 0;
		while ((i = inFromServer.readInt()) != 0) {
			byte[] missileModelFromServerInByte = new byte[i];
			inFromServer.read(missileModelFromServerInByte);
			MissileModel missileModelFromServer = deserializeMissileModel(missileModelFromServerInByte);
			missileMove(missileList, missileModelFromServer.getID(),
					missileModelFromServer.getX(),
					missileModelFromServer.getY());
			break;
		}
		// missileMove(missileList, missileIndex, e.getX(),e.getY() -
		// plane.getHeight() + 50);
	}

	public void enemyMoveThroughServer(EnemyModel enemyModel,DataInputStream inFromServer, DataOutputStream outToServer) throws IOException, ClassNotFoundException{
		byte[] enemyModelInByte = serialize(enemyModel);
		outToServer.writeInt(3);
		outToServer.writeInt(enemyModelInByte.length);
		outToServer.write(enemyModelInByte);
		int i = 0;
		while ((i = inFromServer.readInt())!=0){
			byte[] enemyModelFromServerInbyte = new byte[i];
			inFromServer.read(enemyModelFromServerInbyte);
			EnemyModel enemyModelFromServer = deserializeEnemyModel(enemyModelFromServerInbyte);
			enemyPlaneMove(enemyModelFromServer.getX(), plane, enemyModelFromServer.getID());
			//TODO
			break;
		}
		
		
	}

	public static byte[] serialize(PlaneModel planeModel) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(planeModel);
		return out.toByteArray();
	}

	public static byte[] serialize(MissileModel missileModel)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(missileModel);
		return out.toByteArray();
	}

	public static byte[] serialize(EnemyModel enemyModel) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(enemyModel);
		return out.toByteArray();
	}

	public static PlaneModel deserializePlaneModel(byte[] data)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (PlaneModel) is.readObject();
	}

	public static MissileModel deserializeMissileModel(byte[] data)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (MissileModel) is.readObject();
	}

	public static EnemyModel deserializeEnemyModel(byte[] data)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (EnemyModel) is.readObject();
	}

	public void missileMove(final JLabel[] missileList, final int missileIndex,
			final int x, final int y) {
		int delay = 15; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int missileX = x - missileList[missileIndex].getWidth() / 2;
			int missileY = 0;
			int enemyPlaneListIndexDie = -1;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				// missileX = x - missileList[missileIndex].getX()+
				// missileList[missileIndex].getWidth()/2;
				missileY = y - 05 * count;// can change speed here by y
				// if (count == 1080 || y - 65 - 5 * count < -38 ||
				// checkCollision(missileX, missileY,enemyPlane)) {
				enemyPlaneListIndexDie = checkCollisionList(missileX, missileY,
						missileList[missileIndex].getWidth(),
						missileList[missileIndex].getHeight(), enemyPlaneList);
				if (count == 1080 || y - 65 - 5 * count < -38
						|| (enemyPlaneListIndexDie != -1)) {
					if (enemyPlaneListIndexDie != -1) {
						System.out.println("enemyPlaneListIndexDie "
								+ enemyPlaneListIndexDie);
						enemyPlaneList[enemyPlaneListIndexDie]
								.setVisible(false);

						frame.getContentPane().remove(
								enemyPlaneList[enemyPlaneListIndexDie]); // remove
																			// enemy
						enemyPlaneList[enemyPlaneListIndexDie] = null;
						System.out.println("missileIndex = " + missileIndex
								+ " destroyed enemyPlaneListIndex = "
								+ enemyPlaneListIndexDie);
						enemyPlaneListIndexDie = -1;
					}
					// System.out.println("enemyPlaneListIndexDie = "+enemyPlaneListIndexDie);

					missileList[missileIndex].setVisible(false);
					frame.getContentPane().remove(missileList[missileIndex]);
					missileList[missileIndex] = null;

					((Timer) evt.getSource()).stop();
				} else {
					missileList[missileIndex].setVisible(true);
					missileList[missileIndex].move(missileX, missileY);
				}
				count++;
			}
		};
		// new Timer(delay, taskPerformer[missileIndex]).start();
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public int checkCollisionList(int x, int y, int width, int height,
			JLabel[] enemyPlaneList) {
		for (int i = 0; i < enemyPlaneList.length; i++) {
			if (enemyPlaneList[i] != null) {
				if (checkCollision(x, y, width, height, enemyPlaneList[i]))
					return i;
			}
		}
		return -1;
	}

	public boolean checkCollision(int x, int y, int width, int height,
			JLabel plane) {
		Rectangle m = new Rectangle(x, y, width, height);
		Rectangle e = new Rectangle(plane.getX(), plane.getY(),
				plane.getWidth(), plane.getHeight());
		if (m.intersects(e))
			return true;
		else
			return false;
	}

	public void createEnemyPlanes(final Image enemyImage, final JLabel plane,final DataInputStream inFromServer, final DataOutputStream outToServer) {
		int delay = 1000;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;

			public void actionPerformed(ActionEvent evt) {
				if (count == numberOfEnemyPlane) {
					// enemyPlane.setVisible(false);
					((Timer) evt.getSource()).stop();
				} else {
					enemyPlaneList[count] = new JLabel("");
					enemyPlaneList[count].setIcon(new ImageIcon(enemyImage));
					enemyPlaneList[count].setSize(enemyImage.getWidth(null),
							enemyImage.getHeight(null));
					// enemyPlaneList[count].setBounds(0,0,enemyImage.getWidth(null),enemyImage.getHeight(null));

					// TODO
					enemyModel.setX((int) (Math.random() * (frame.getWidth()
							- enemyImage.getWidth(null) - 1)) + 1);
					enemyModel.setID(count);
					try {
						enemyMoveThroughServer(enemyModel, inFromServer, outToServer);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					enemyPlaneMove((int) (Math.random() * (frame.getWidth()
//							- enemyImage.getWidth(null) - 1)) + 1, plane, count);

					frame.getContentPane().add(enemyPlaneList[count]);
				}
				count++;
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public void enemyPlaneMove(final int x, final JLabel plane, final int index) {
		int delay = 20;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int enemyPlaneY = 0;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				if (enemyPlaneList[index] != null) {
					enemyPlaneY = 2 * count - enemyPlaneList[index].getHeight();// can
																				// change
					// speed by
					// y and
					// delay
					if (plane.isVisible()) {
						if (checkCollision(x, enemyPlaneY,
								enemyPlaneList[index].getWidth(),
								enemyPlaneList[index].getHeight(), plane)) {

							plane.setVisible(false);
							frame.getContentPane().remove(plane);
							lblYouDie.setVisible(true);
							frame.setCursor(Cursor.DEFAULT_CURSOR);
							System.out.println("you ded by enemy plane index "
									+ index);

						}
					}

					if (enemyPlaneY >= frame.getHeight()
							- enemyPlaneList[index].getHeight()) {
						enemyPlaneList[index].setVisible(false);

						((Timer) evt.getSource()).stop();
					} else {
						enemyPlaneList[index].setVisible(true);
						enemyPlaneList[index].move(x, enemyPlaneY);
					}
					count++;
				}

			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

}
