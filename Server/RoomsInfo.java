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
}
