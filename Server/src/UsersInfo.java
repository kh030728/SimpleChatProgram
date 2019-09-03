package chattingProgram;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersInfo {
	private static UsersInfo UserInstance = null;
	private ArrayList<User> userInfo = new ArrayList<User>();
	
	private UsersInfo() {
		
	}
	
	public static UsersInfo getInstance() {
		if(UserInstance == null) UserInstance = new UsersInfo();
		return UserInstance;
	}
	
	public void addUser(User user) {
		userInfo.add(user);
	}
	
	public void removeUser(String user) {
		for(int i = 0; i < userInfo.size(); i++) {
			if(userInfo.get(i).nickName == user) {
				userInfo.remove(i);
			}
			break;
		}
	}
	
	public Integer getSizeInfo() {
		return userInfo.size();
	}
	
	public User getUserInfo(int i) {
		return userInfo.get(i);
	}
	
	public Integer getUserInt(String userNick) {
		int listNum = 0;
		for(int i = 0; i < userInfo.size(); i++) {
			if(userInfo.get(i).nickName == userNick) {
				listNum = i;
				break;
			}
		}
		return listNum;
	}
}
