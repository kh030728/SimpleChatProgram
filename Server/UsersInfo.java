package chattingProgram;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
	
	public void removeUser(int i) {
		userInfo.remove(i);
	}
	
	public Integer getSizeInfo() {
		return userInfo.size();
	}
	
	public User getUserInfo(int i) {
		return userInfo.get(i);
	}
	
	public Socket getUserSocket(String userNick) {
		Socket userSocket = null;
		for(int i = 0; i < userInfo.size(); i++) {
			if(userInfo.get(i).nickName == userNick) {
				userSocket = userInfo.get(i).socket;
			}
		}
		return userSocket;
	}
}
