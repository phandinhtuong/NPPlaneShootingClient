package objectByteTransform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import main.Main;
import model.Missile;
import model.Player;
import model.Room;
import directPlaying.refactorDataStructure.Client;

public class Serialize {
	public static byte[] serialize(Room room) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(room);
	    return out.toByteArray();
	}
	public static byte[] serialize(Player player) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(player);
	    return out.toByteArray();
	}
//	public static byte[] serialize(PlaneModel planeModel) {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try {
//			ObjectOutputStream os = new ObjectOutputStream(out);
//			os.writeObject(planeModel);
//			return out.toByteArray();
//		} catch (IOException e) {
//			Client.displayGameLog(e.getMessage());
//			return null;
//		}
//	}

	public static byte[] serialize(Missile missileModel) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(missileModel);
			return out.toByteArray();
		} catch (IOException e) {
			Main.displayGameLog(e.getMessage());
			return null;
		}
	}
//
//	public static byte[] serialize(EnemyModel enemyModel) {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try {
//			ObjectOutputStream os = new ObjectOutputStream(out);
//			os.writeObject(enemyModel);
//			return out.toByteArray();
//		} catch (IOException e) {
//			Client.displayGameLog(e.getMessage());
//			return null;
//		}
//	}
//	public static byte[] serialize(ArrayList<PlaneModel> modelPlaneList){
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		try {
//			ObjectOutputStream os = new ObjectOutputStream(out);
//			os.writeObject(modelPlaneList);
//			return out.toByteArray();
//		} catch (IOException e) {
//			Main.displayGameLog(e.getMessage());
//			return null;
//		}
//	}
}
