package directPlaying.refactorDataStructure;

import java.util.ArrayList;
import java.util.Arrays;
import testOneClient.PlaneModel;

public class TestFindObjectInList {
	static ArrayList<PlaneModel> modelPlaneList = new ArrayList<PlaneModel>();
	public static void main(String[] args){
		PlaneModel planeModel = new PlaneModel(1, 500, 500, "playing");
//		PlaneModel planeModel2 = new PlaneModel(1, 500, 500, "playing");
		modelPlaneList.add(planeModel);
//		modelPlaneList.add(planeModel2);
		System.out.println("modelPlaneList.indexOf(planeModel) = "+modelPlaneList.indexOf(planeModel));
//		PlaneModel planeModel2 = new PlaneModel(1, 500, 500, "playing");
//		System.out.println("modelPlaneList.indexOf(planeModel2) = "+modelPlaneList.indexOf(planeModel2));
		planeModel.setX(200);
		planeModel.setID(2);
		planeModel.setY(250);
		planeModel.setStatus("dead");
		System.out.println("modelPlaneList.indexOf(planeModel) = "+modelPlaneList.indexOf(planeModel));
//		modelPlaneList.forEach(System.out.println());
		
		for (PlaneModel planeModelInList : modelPlaneList){
			
			System.out.println(planeModelInList.getID());
			System.out.println(planeModelInList.getX());
			System.out.println(planeModelInList.getY());
			System.out.println(planeModelInList.getStatus());
			
		}
//		byte[] pl
//		System.out.println(modelPlaneList.get(modelPlaneList.indexOf(planeModel)).getStatus());
	}
}
