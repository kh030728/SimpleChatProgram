package chattingProgram;

import java.util.ArrayList;

public class RoomInfo {
	ArrayList<ArrayList<String>> roomInfo = new ArrayList();
	ArrayList<String> InfoChild = new ArrayList();
		
	public void addRoom(String str) {
		str = "This is Room Room Room"; 
		InfoChild.add(str);
		InfoChild.add("0");
		roomInfo.add(InfoChild);
		}
	
	public int sizeRoom() {
		int size = roomInfo.size();
		return size;
	}
	
	public String nameRoom(int i) {
		String name = roomInfo.get(i).get(0);
		return name;
	}
	
	public String peopleNumber(int i) {
		String PNumber = roomInfo.get(1).get(i);
		return PNumber;
	}

}
