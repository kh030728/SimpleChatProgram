package chattingProgram;

import java.util.ArrayList;

public class RoomInfo {
	ArrayList<String> roomName = new ArrayList();
	ArrayList<Integer> roomPeople = new ArrayList();

	public int searchRoom(String str) {
		return roomName.indexOf(str);
	}

	public void addRoom(String str) {
		roomName.add(str);
		roomPeople.add(1);
	}

	public int sizeRoom() {
		int size = roomName.size();
		return size;
	}

	public String nameRoom(int i) {
		String name = roomName.get(i);
		return name;
	}

	public int peopleNumber(int i) {
		int PNumber = roomPeople.get(i);
		return PNumber;
	}

}
