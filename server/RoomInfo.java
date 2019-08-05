package chattingProgram;

import java.util.ArrayList;

public class RoomInfo {
	ArrayList<String> roomName = new ArrayList();
	ArrayList<String> roomPeople = new ArrayList();	
	//ArrayList<String> InfoChild = new ArrayList();
		
	public void addRoom(String strNa, String strPe) {
		//InfoChild.add(str);
		//InfoChild.add("0");
		roomName.add(strNa);
		roomPeople.add(strPe);
		}
	
	public int sizeRoom() {
		int size = roomName.size();
		return size;
	}
	
	public String nameRoom(int i) {
		String name = roomName.get(i);
		return name;
	}
	
	public String peopleNumber(int i) {
		String PNumber = roomPeople.get(i);
		return PNumber;
	}

}
