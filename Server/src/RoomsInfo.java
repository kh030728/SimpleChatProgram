package chattingProgram;

import java.util.ArrayList;

public class RoomsInfo {
	private static RoomsInfo roomInstance = null;
	private ArrayList<Room> roomInfo = new ArrayList<Room>();
	
	private RoomsInfo() {
		
	}
	
	public static RoomsInfo getInstance() {
		if(roomInstance == null) roomInstance = new RoomsInfo();
		return roomInstance;
	}
	
	public void addRoom(Room room) {
		roomInfo.add(room);
	}
	
	public void removeRoom(int i) {
		roomInfo.remove(i);
	}
	
	public Room getRoomInfo(int i) {
		return roomInfo.get(i);
	}
	
	public Integer getRoomAll() {
		return roomInfo.size();
	}
	
	public Integer getRoomNum(String roomName) {
		int roomNum = 0;
		for(int i = 0; i < roomInfo.size(); i++) {
			if(roomInfo.get(i).roomNa == roomName) {
				roomNum = i;
				roomInfo.get(i).roomNu = i+1;
				break;
			}
		}
		return roomNum;
	}
	
	public void sort() {
		Room temp;
		for(int i = 0; i < roomInfo.size()-1; i++) {
			if(roomInfo.get(i).roomNu > roomInfo.get(i+1).roomNu) {
				temp = roomInfo.get(i);
				roomInfo.add(i, roomInfo.get(i+1));
				roomInfo.add(i+1, temp);
				temp = null;
			}
		}
	}
}
