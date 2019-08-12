package chattingProgram;

import java.util.ArrayList;

public class UserInfo {
	
	ArrayList<String> userList = new ArrayList();
	ArrayList<Integer> userJoinRoomNu = new ArrayList();
	
	public void addUser(String strU) {
		userList.add(strU);
		userJoinRoomNu.add(0);
	}
	
	public int searchUser(String str) {
		return userList.indexOf(str);
	}
}