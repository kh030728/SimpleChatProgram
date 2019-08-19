package chattingProgram;

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
	
	public User getUserInfo(int i) {
		return userInfo.get(i);
	}
}
