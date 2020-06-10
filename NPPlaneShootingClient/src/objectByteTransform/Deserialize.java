package objectByteTransform;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import directPlaying.refactorDataStructure.Client;
import testOneClient.EnemyModel;
import testOneClient.MissileModel;
import testOneClient.PlaneModel;
import model.Player;
import model.Room;
import model.RoomList;
public class Deserialize {
	public static RoomList deserializeRoomList(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (RoomList) is.readObject();
	}
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
	public static PlaneModel deserializePlaneModel(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (PlaneModel) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}
	}

	public static MissileModel deserializeMissileModel(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (MissileModel) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}
	}

	public static EnemyModel deserializeEnemyModel(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (EnemyModel) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}
	}

	public static PlaneModel[] deserializePlaneModelList(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (PlaneModel[]) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}
	}

	public static MissileModel[][] deserializeMissileModelList(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (MissileModel[][]) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}

	}

	public static EnemyModel[][] deserializeEnemyModelList(byte[] data) {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (EnemyModel[][]) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}
	}
	public static ArrayList<PlaneModel> deserializePlaneModelArrayList(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			return (ArrayList<PlaneModel>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			Client.displayGameLog(e.getMessage());
			return null;
		}
	}
}
