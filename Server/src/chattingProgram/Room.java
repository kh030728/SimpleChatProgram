package chattingProgram;

import java.util.ArrayList;

public class Room {
	String roomNa = null;
	ArrayList<String> entryList = new ArrayList();
	
	public Room(String roomNa, String Leader) {
		this.roomNa = roomNa;
		entryList.add(Leader);
	}
	
	public String getRoomNa() {
		return roomNa;
	}
	
	
	public ArrayList getEntryList() {
		return entryList;
	}
	
	public void setAddEntry(String entryUser) {
		entryList.add(entryUser);
	}
	
	public void setRemoveEntry(String entryUser) {
		for(int i = 0; i < entryList.size(); i++) {
			if(entryList.get(i) == entryUser) {
				entryList.remove(i);
			}
		}
	}
	
}
