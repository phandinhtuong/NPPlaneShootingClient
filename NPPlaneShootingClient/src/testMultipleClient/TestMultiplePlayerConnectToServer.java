package testMultipleClient;

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

import testOneClient.EnemyModel;
import testOneClient.MissileModel;
import testOneClient.PlaneModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TestMultiplePlayerConnectToServer {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestMultiplePlayerConnectToServer window = new TestMultiplePlayerConnectToServer();
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
	 * @throws ClassNotFoundException
	 */
	public TestMultiplePlayerConnectToServer() throws UnknownHostException,
			IOException, ClassNotFoundException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	int missileIndex = 0;
	int numberOfMissile = 100;
	int numberOfEnemyPlane = 100;
	int myPlayerIndex = -1;
	static int numberOfPlayers = 3;

	// Load images
	Image missileImage = new ImageIcon(this.getClass().getResource(
			"/missile.png")).getImage();
	Image planeImage = new ImageIcon(this.getClass().getResource("/plane1.png"))
			.getImage();
	Image enemyImage = new ImageIcon(this.getClass().getResource(
			"/enemyPlaneGraySmaller.png")).getImage();

	JLabel lblYouDie = new JLabel("You die!");

	// model list from server
	PlaneModel[] planeModelList = new PlaneModel[numberOfPlayers];
	MissileModel[][] missileModelList = new MissileModel[numberOfPlayers][numberOfMissile];
	EnemyModel[][] enemyModelList = new EnemyModel[numberOfPlayers][numberOfEnemyPlane];

	// label to display
	JLabel[] allPlayers = new JLabel[numberOfPlayers];
	JLabel[][] allMissiles = new JLabel[numberOfPlayers][numberOfMissile];
	JLabel[][] allEnemies = new JLabel[numberOfPlayers][numberOfEnemyPlane];

	// model to send to server
	PlaneModel planeModel = new PlaneModel(0, 0, "playing");
	MissileModel missileModel = new MissileModel(0, 0, 0, 0, "ready");
	EnemyModel enemyModel = new EnemyModel(0, 0, 0, "ready");

	String ip = "127.0.0.1";
	int port = 6789;
	final Socket clientSocket = new Socket(ip, port); // request connection
	final DataInputStream inFromServer = new DataInputStream(
			clientSocket.getInputStream());
	final DataOutputStream outToServer = new DataOutputStream(
			clientSocket.getOutputStream());

	private void initialize() throws UnknownHostException, IOException,
			ClassNotFoundException {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					clientSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Plane shooting gaem");
		frame.setBounds(0, 0, 1080, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setCursor(frame.getToolkit().createCustomCursor(
				new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
				new Point(0, 0), "null"));

		// Initial all models and label
		for (int j = 0; j < numberOfPlayers; j++) {
			planeModelList[j] = new PlaneModel(0, 0, "waiting");
			allPlayers[j] = new JLabel("");
			allPlayers[j].setIcon(new ImageIcon(planeImage));
			allPlayers[j].setBounds(
					frame.getWidth() / 2 - planeImage.getWidth(null) / 2 * j,
					frame.getHeight() - planeImage.getHeight(null) * 2,
					planeImage.getWidth(null), planeImage.getHeight(null));
			allPlayers[j].setVisible(false);
			frame.getContentPane().add(allPlayers[j]);
			for (int i = 0; i < numberOfMissile; i++) {
				missileModelList[j][i] = new MissileModel(0, 0, 0, 0, "ready");
				allMissiles[j][i] = new JLabel("");
				allMissiles[j][i].setIcon(new ImageIcon(missileImage));
				allMissiles[j][i].setSize(missileImage.getWidth(null),
						missileImage.getHeight(null));
				allMissiles[j][i].setVisible(false);
				frame.getContentPane().add(allMissiles[j][i]);
			}
			for (int i = 0; i < numberOfEnemyPlane; i++) {
				enemyModelList[j][i] = new EnemyModel(0, 0, 0, "ready");
				allEnemies[j][i] = new JLabel("");
				allEnemies[j][i].setIcon(new ImageIcon(enemyImage));
				allEnemies[j][i].setSize(enemyImage.getWidth(null),
						enemyImage.getHeight(null));
				allEnemies[j][i].setVisible(false);
				frame.getContentPane().add(allEnemies[j][i]);
			}
		}

		// get player index
		while ((myPlayerIndex = inFromServer.readInt()) != -1) {
			System.out.println("My Player Index is " + myPlayerIndex);
			// TODO
			// allPlayers[myPlayerIndex].setVisible(true);
			break;
		}

		// label to display when dead
		lblYouDie.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblYouDie.setForeground(Color.RED);
		lblYouDie.setHorizontalTextPosition(SwingConstants.CENTER);
		lblYouDie.setHorizontalAlignment(SwingConstants.CENTER);
		lblYouDie.setFont(new Font("Times New Roman", Font.BOLD, 99));
		lblYouDie.setBounds(frame.getWidth() / 2 - 200,
				frame.getHeight() / 2 - 100, 400, 200);
		frame.getContentPane().add(lblYouDie);
		lblYouDie.setVisible(false);

		// mouse pressed = launch missile
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if (planeModel.getStatus().equals("playing")) {
					if (missileIndex < numberOfMissile) {
						System.out.println("missileIndex lauched: "
								+ missileIndex);

						missileModel.setID(missileIndex);
						missileModel.setX(e.getX());
						missileModel.setY(e.getY()
								- allPlayers[myPlayerIndex].getHeight() + 50);
						missileModel.setStatus("launched");
						try {
							missileMoveThroughServer();
						} catch (ClassNotFoundException | IOException e1) {

							e1.printStackTrace();
						}
						missileIndex = missileIndex + 1;
					} else {
						System.out.println("Run out of missile!");
					}

				}

			}
		});

		// mouse moved and dragged = move plane
		frame.getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (planeModel.getStatus().equals("playing"))
					try {
						planeModel.setX(e.getX()
								- allPlayers[myPlayerIndex].getWidth() / 2);
						planeModel.setY(e.getY()
								- allPlayers[myPlayerIndex].getHeight() / 2);
						planeMoveThroughServer();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (planeModel.getStatus().equals("playing"))
					try {
						planeModel.setX(e.getX()
								- allPlayers[myPlayerIndex].getWidth() / 2);
						planeModel.setY(e.getY()
								- allPlayers[myPlayerIndex].getHeight() / 2);
						planeMoveThroughServer();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
			}
		});

		loadDataFromServer();
		 createEnemy();
		frame.setVisible(true);
	}

	public void loadDataFromServer() throws IOException, ClassNotFoundException {
		int delay = 100;
		ActionListener taskPerformer = new ActionListener() {
			int i = 0;

			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					outToServer.writeInt(4);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					while ((i = inFromServer.readInt()) != 0) {
						byte[] planeModelListInByte = new byte[i];
						inFromServer.read(planeModelListInByte);
						planeModelList = deserializePlaneModelList(planeModelListInByte);
						break;
					}
					while ((i = inFromServer.readInt()) != 0) {
						byte[] missileModelListInByte = new byte[i];
						inFromServer.read(missileModelListInByte);
						missileModelList = deserializeMissileModelList(missileModelListInByte);
						break;
					}
					while ((i = inFromServer.readInt()) != 0) {
						byte[] enemyModelListInByte = new byte[i];
						inFromServer.read(enemyModelListInByte);
						enemyModelList = deserializeEnemyModelList(enemyModelListInByte);
						break;
					}
					displayAllPlayers();
					displayAllMissiles();
					// TODO
					displayAllEnemies();
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("Connection to server interrupted.");
				}
			}
		};
		new Timer(delay, taskPerformer).start();
	}

	public void displayAllPlayers() {
		for (int i = 0; i < planeModelList.length; i++) {
			if (planeModelList[i].getStatus().equals("dead")) {
				allPlayers[i].setVisible(false);
			} else if (planeModelList[i].getStatus().equals("playing")) {
				displayOnePlayer(i);
			}
		}
	}

	public void displayAllMissiles() {
		for (int j = 0; j < missileModelList.length; j++) {
			for (int i = 0; i < missileModelList[j].length; i++) {
				if (missileModelList[j][i].getStatus().equals("dead"))
					allMissiles[j][i].setVisible(false);
				else if (missileModelList[j][i].getStatus().equals("launched")
						&& !allMissiles[j][i].isVisible()) {
					missileMove(j, i);// TODO
				}

			}
		}
	}

	public void displayAllEnemies() {
		for (int j = 0; j < enemyModelList.length; j++) {
			for (int i = 0; i < enemyModelList[j].length; i++) {
				if (enemyModelList[j][i].getStatus().equals("dead"))
					allEnemies[j][i].setVisible(false);
				else if (enemyModelList[j][i].getStatus().equals("created")
						&& !allEnemies[j][i].isVisible()) {
					enemyMove(j, i);
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	public void displayOnePlayer(int i) {
		allPlayers[i].setVisible(true);
		allPlayers[i].move(planeModelList[i].getX(), planeModelList[i].getY());
	}

	public static PlaneModel[] deserializePlaneModelList(byte[] data)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (PlaneModel[]) is.readObject();
	}

	public static MissileModel[][] deserializeMissileModelList(byte[] data)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (MissileModel[][]) is.readObject();
	}

	public static EnemyModel[][] deserializeEnemyModelList(byte[] data)
			throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return (EnemyModel[][]) is.readObject();
	}

	public void planeMoveThroughServer() throws IOException,
			ClassNotFoundException {
		byte[] planeModelInByte = serialize(planeModel);
		outToServer.writeInt(1);
		outToServer.writeInt(planeModelInByte.length);
		outToServer.write(planeModelInByte);
	}

	public void missileMoveThroughServer() throws IOException,
			ClassNotFoundException {
		byte[] missileModelInByte = serialize(missileModel);
		outToServer.writeInt(2);
		outToServer.writeInt(missileModelInByte.length);
		outToServer.write(missileModelInByte);
	}

	public void enemyMoveThroughServer() throws IOException,
			ClassNotFoundException {
		byte[] enemyModelInByte = serialize(enemyModel);
		outToServer.writeInt(3);
		outToServer.writeInt(enemyModelInByte.length);
		outToServer.write(enemyModelInByte);
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

	public void missileMove(final int j, final int i) {
		System.out.println("playerID:" + j + " missileID:" + i + " x:"
				+ missileModelList[j][i].getX() + " y:"
				+ missileModelList[j][i].getY());
		int delay = 1; // milliseconds
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int missileX = missileModelList[j][i].getX()
					- allMissiles[j][i].getWidth() / 2;
			int missileY = 0;
			int enemyPlaneListIndexDie = -1;
			int k = 0;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				// missileX = x - missileList[missileIndex].getX()+
				// missileList[missileIndex].getWidth()/2;
				missileY = missileModelList[j][i].getY() - count;// speed
				// if (count == 1080 || y - 65 - 5 * count < -38 ||
				// checkCollision(missileX, missileY,enemyPlane)) {
				for (k = 0; k < numberOfPlayers; k++) {
					enemyPlaneListIndexDie = checkCollisionList(missileX,
							missileY, allMissiles[j][i].getWidth(),
							allMissiles[j][i].getHeight(), allEnemies[k]);
					if (enemyPlaneListIndexDie != -1)
						break;
				}

				if (count == 1080 || allMissiles[j][i].getY() < 0
						|| (enemyPlaneListIndexDie != -1)) {
					if (enemyPlaneListIndexDie != -1) {
						// System.out.println("enemyPlaneListIndexDie "
						// + enemyPlaneListIndexDie);
						// allEnemies[k][enemyPlaneListIndexDie].setVisible(false);
						// TODO
						// frame.getContentPane().remove(
						// allEnemies[k][enemyPlaneListIndexDie]); // remove
						// enemy
						// allEnemies[enemyPlaneListIndexDie] = null;
						enemyModel.setPlayerID(k);
						enemyModel.setID(enemyPlaneListIndexDie);
						enemyModel.setStatus("dead");
						try {
							enemyMoveThroughServer();
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
						System.out.println("missileIndex = " + missileIndex
								+ " destroyed enemyPlaneListIndex = "
								+ enemyPlaneListIndexDie);

						enemyPlaneListIndexDie = -1;
					}
					// System.out.println("enemyPlaneListIndexDie = "+enemyPlaneListIndexDie);
					missileModel.setPlayerID(j);
					missileModel.setID(i);
					missileModel.setStatus("dead");
					((Timer) evt.getSource()).stop();
					try {
						missileMoveThroughServer();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					// allMissiles[j][i].setVisible(false);
					// frame.getContentPane().remove(allMissiles[j][i]);
					
				} else {
					allMissiles[j][i].setVisible(true);
					allMissiles[j][i].move(missileX, missileY);
				}
				count++;
			}
		};
		// new Timer(delay, taskPerformer[missileIndex]).start();
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public int checkCollisionList(int x, int y, int width, int height,
			JLabel[] ObjectList) {
		for (int i = 0; i < ObjectList.length; i++) {
			if (ObjectList[i] != null) {
				if (checkCollision(x, y, width, height, ObjectList[i]))
					return i;
			}
		}
		return -1;
	}

	public boolean checkCollision(int x, int y, int width, int height,
			JLabel objectB) {
		Rectangle a = new Rectangle(x, y, width, height);
		Rectangle b = new Rectangle(objectB.getX(), objectB.getY(),
				objectB.getWidth(), objectB.getHeight());
		if (a.intersects(b))
			return true;
		else
			return false;
	}

	public void createEnemy() {
		int delay = 1000;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;

			public void actionPerformed(ActionEvent evt) {
				if (count == numberOfEnemyPlane) {
					((Timer) evt.getSource()).stop();
				} else {
					enemyModel = new EnemyModel(myPlayerIndex, count,
							(int) (Math.random() * (frame.getWidth()
									- enemyImage.getWidth(null) - 1)) + 1,
							"created");
					try {
						enemyMoveThroughServer();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}
				count++;
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}

	public void enemyMove(final int j, final int i) {
		int delay = 15;
		ActionListener taskPerformer = new ActionListener() {
			int count = 0;
			int enemyPlaneY = 0;

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent evt) {
				enemyPlaneY = count - allEnemies[j][i].getHeight();// speed
				if (planeModel.getStatus().equals("playing")&&allEnemies[j][i].isVisible()) {
					if (checkCollision(enemyModel.getX(), enemyPlaneY,
							allEnemies[j][i].getWidth(),
							allEnemies[j][i].getHeight(),
							allPlayers[myPlayerIndex])) {
						planeModel.setStatus("Died");
						try {
							planeMoveThroughServer();
						} catch (ClassNotFoundException | IOException e) {
							e.printStackTrace();
						}
						// allPlayers[myPlayerIndex].setVisible(false);
						// TODO
						lblYouDie.setVisible(true);
						frame.setCursor(Cursor.DEFAULT_CURSOR);
						System.out.println("you ded by enemy plane index " + i
								+ " of player " + j);
					}
				}
				if (enemyPlaneY >= frame.getHeight()
						- allEnemies[j][i].getHeight()) {
					enemyModel.setPlayerID(j);
					enemyModel.setID(i);
					enemyModel.setStatus("dead");
					allEnemies[j][i].setVisible(false);
					try {
						
						enemyMoveThroughServer();
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
					// allEnemies[j][i].setVisible(false);
					((Timer) evt.getSource()).stop();
				} else {
					allEnemies[j][i].setVisible(true);
					allEnemies[j][i].move(enemyModelList[j][i].getX(),
							enemyPlaneY);
				}
				count++;
			}
		};
		Timer t = new Timer(delay, taskPerformer);
		t.start();
	}
}
