package chattingProgram;

import java.util.ArrayList;

public class Room {
	String roomNa = null;
	ArrayList<User> entry = new ArrayList();
	
	public Room(String roomNa, User Leader) {
		this.roomNa = roomNa;
		entry.add(Leader);
	}
	
	public String getRoomNa() {
		return roomNa;
	}
	
	public ArrayList getEntry() {
		return entry;
	}
	
	public void AddEntry(User entryUser) {
		entry.add(entryUser);
	}
	
	public void RemoveEntry(User user) {
		for(int i = 0; i < entry.size(); i++) {
			if(entry.get(i) == user) {
				entry.remove(i);
			}
			break;
		}
	}
	
}
