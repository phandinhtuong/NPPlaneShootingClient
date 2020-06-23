package objectByteTransform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import main.Main;
import model.Enemy;
import model.Missile;
import model.Player;
import model.Room;
public class Deserialize {
	public static String deserializeString(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (String) is.readObject();
	}
	public static Player deserializePlayer(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (Player) is.readObject();
	}
	public static Room deserializeRoom(byte[] data) throws ClassNotFoundException, IOException{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (Room) is.readObject();
	}
	@SuppressWarnings("unchecked")
	public static List<Player> deserializePlaneModelArrayList(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (List<Player>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Main.displayGameLog(e.getMessage());
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static List<Missile> deserializeMissileModelArrayList(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (List<Missile>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Main.displayGameLog(e.getMessage());
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static List<Enemy> deserializeEnemyModelArrayList(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (List<Enemy>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Main.displayGameLog(e.getMessage());
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static List<Room> deserializeRoomModelArrayList(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (List<Room>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Main.displayGameLog(e.getMessage());
			return null;
		}
	}
}
