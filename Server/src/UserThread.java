package chattingProgram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class UserThread extends Thread {
	Socket socket;
	String ip;
	User userInfo;

	public UserThread(Socket socket, String ip) {
		this.socket = socket;
		this.ip = ip;
	}

	public void run() {
		UsersInfo userInstance = UsersInfo.getInstance();
		RoomsInfo roomInstance = RoomsInfo.getInstance();

		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // ��� ��Ʈ��
			String nickStr = Breader.readLine();
			System.out.println("�г��� ���� " + nickStr);

			// ���� �г��� ����
			if (nickStr.contains("NICKNAME%$%")) {
				System.out.println("�����г������� if ����");
				nickStr = nickStr.substring(11, nickStr.length());
				System.out.println(nickStr);
				for(int i = 0; i < userInstance.getSizeInfo(); i++) {
					if(userInstance.getUserInfo(i).nickName == nickStr) {
						Pwriter.println("NICKNAME_NO");
						Pwriter.flush();
						return;
					}
				}
				Pwriter.println("NICKNAME_OK");
				Pwriter.flush();
				userInfo = new User(nickStr, socket);
				userInstance.addUser(userInfo);
				System.out.println("�����г������� if ��");
			} else {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
					}
				}
			}
			System.out.println("============================");
			System.out.println("============================");
			while (true) {
				String str = Breader.readLine(); // �Էµ� ����Ÿ�� �� �� ������ ����
				System.out.println("���� �޼��� : " + str); // �Է� Ȯ�ο�

				// Ŭ���̾�Ʈ ����
				// if(str.equals("bye")) break;

				// ������ ����
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("������ ��û �޼��� ���� �Ϸ� / ���� �޼��� : " + str);
					for (int i = 0; i < roomInstance.getRoomAll(); i++) {
						System.out.println("������ �������� " + i + "�� �غ���");
						Pwriter.println("NOTIFY_ADD_ROOM%$%" + (i+1) + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entry.size() + "\r\n");
						System.out.println("NORIFY_ADD_ROOM%$%" + i + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entry.size() + "\r\n");
						Pwriter.flush();
						Thread.sleep(50);
						System.out.println("������ �������� " + i + "�� �Ϸ�");
					}
					System.out.println("������ ���� ��");
					str = null;
				}

				// �� ����
				else if (str.contains("REQUEST_CREATE_ROOM%$%")) {
					System.out.println("����� ��û �޼��� ���� �Ϸ� / ���� �޼��� : " + str);
					String[] createRoomStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : �� ����
					System.out.println("�޼��� �и� Ȯ�ο� - 0 : " + createRoomStr[0] + " 1 : " + createRoomStr[1]);
					Room room = new Room(createRoomStr[1], userInfo);
					roomInstance.addRoom(room);
					int addRoomNu = roomInstance.getRoomNum(createRoomStr[1]); // ������ ���� �� ��ȣ ã��
					roomInstance.sort(); // �� ��ȣ�� �� ��� ����
					userInfo.roomNu = addRoomNu;
					System.out.println("������ �� ��ȣ : " + addRoomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM%$%" + addRoomNu);
					Pwriter.flush();
					str = null;
					str = Breader.readLine();
					System.out.println("�߰� �޼��� Ȯ�� : " + str);
					if(str.equals("READY_FOR_JOIN")) {
						Pwriter.println("USERS%$%" + userInfo.nickName);
						Pwriter.flush();
					}
					for (int i = 0; i < userInstance.getSizeInfo(); i++) { // �� ���� �� ���ŵ� �� ���� �� ����
						System.out.println("���� �� ���ŵ� �� ���� ���� �غ���");
						PrintWriter NotifyAddRoom = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
						NotifyAddRoom.println("NOTIFY_ADD_ROOM%$%" + (addRoomNu+1) + "%$%" + createRoomStr[1] + "%$%" + roomInstance.getRoomInfo(addRoomNu).entry.size() +"\r\n");
						NotifyAddRoom.flush();
						System.out.println("���ŵ� �� ���� ���� �Ϸ�");
					}
					System.out.println("�� ������ ���� �� ��ó�� �Ϸ�");
					createRoomStr = null;
					str = null;
				}

				// �� ����
				else if (str.contains("REQUEST_JOIN_ROOM%$%")) {
					System.out.println("�� ���� �޼��� ���� �Ϸ� / ���� �޼��� : " + str);
					String[] joinRoomStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : ������ �� ��ȣ
					System.out.println("�޼��� �и� Ȯ�ο� - 0 : " + joinRoomStr[0] + " 1 : " + joinRoomStr[1]);
					int roomNu = Integer.parseInt(joinRoomStr[1]);  // �� ��ȣ					
					roomInstance.getRoomInfo(roomNu).AddEntry(userInfo); // �� ������ ���� ���� �ֱ�
					userInfo.roomNu = roomNu; // ���� ������ ���� �� ��ȣ ����
					Pwriter.println("FINISH_JOIN");
					Pwriter.flush();
					
					str = null;
					str = Breader.readLine();
					System.out.println("���� Ȯ�ο�" + str);
					if(str.equals("READY_FOR_JOIN")) { // �� ó�� �� �޼��� �ޱ�
						// ������ ��� ������
						String users = "USERS";
						for (int i = 0; i < roomInstance.getRoomInfo(roomNu).entry.size(); i++) {
							System.out.println("������ ��� �������� �� / ���� ���൵ : " + users);
							users += "%$%";
							users += roomInstance.getRoomInfo(roomNu).entry.get(i).nickName;
							System.out.println("������ ��� �������� �� / ���� �Ϸ�� ���� : " + users);
						}
						users += "\r\n";
						Pwriter.println(users);
						Pwriter.flush();
						
						// ������ �濡 ������ ���� ����
						ArrayList<User> joinUsers = roomInstance.getRoomInfo(roomNu).entry; // ������ ���� ���� ����Ʈ
						System.out.println("������ ���� ���ŵ� ���� ��� : " + joinUsers);
						for (int i = 0; i < joinUsers.size(); i++) { // ä�� ����
							System.out.println("������ ���� �����鿡�� ������ ���� ������ / ���� " + i + "�� �������� ���� ��� ��");
							PrintWriter sendChat = new PrintWriter(joinUsers.get(i).socket.getOutputStream());
							sendChat.println("JOIN_USER%$%" + userInfo.nickName);
							sendChat.flush();
							System.out.println("������ ���� �����鿡�� ������ ���� ������ / ���� " + i + "�� �������� ���� �Ϸ� ����");
						}
						System.out.println("������ ���� �������� ���ο� ������ ���� �Ϸ�");
						
						System.out.println("��ü �������� �ο����� ����� �� �˸�");
						for (int i = 0; i < userInstance.getSizeInfo(); i++) {
							System.out.println(i + "�� �������� ���� �غ� ��");
							PrintWriter sendChangePeople = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
							sendChangePeople.println("NOTIFY_CHAGE_ROOM%$%" + (roomNu+1) + "%$%" + roomInstance.getRoomInfo(roomNu).entry.size());
							sendChangePeople.flush();
							System.out.println(i + "�� �������� ���� �Ϸ�");
						}
					}
					System.out.println("�� ������ ��ó�� �Ϸ�");
					joinRoomStr = null;
					str = null;
				}

				// �� ������
				else if (str.contains("REQUEST_OUT_ROOM")) {
					System.out.println("�� ������ �޼��� ��û ���� �Ϸ� / ���ŵ� �޼��� : " + str);
					int roomNu = userInfo.roomNu;
					userInfo.roomNu = 0;			
					roomInstance.getRoomInfo(roomNu).RemoveEntry(userInfo); // �ش� ���� ���� ��Ͽ��� ���� ���� ����
					Pwriter.println("OUT_ROOM_OK");
					Pwriter.flush();
					// ���� ���� ���� ����
					ArrayList<User> joinUsers = roomInstance.getRoomInfo(roomNu).entry; // ������ ���� ���� ����Ʈ
					if (joinUsers.size() != 0) {
						System.out.println("�ش� �濡 �����ִ� ������ �ִٸ� �� �����鿡�� ���� �޼��� ����");
						for (int i = 0; i < joinUsers.size(); i++) { // ä�� ����
							System.out.println(i + "�� �������� ���� �غ���");
							PrintWriter sendChat = new PrintWriter(joinUsers.get(i).socket.getOutputStream());
							sendChat.println("OUT_USER%$%" + userInfo.nickName);
							sendChat.flush();
							System.out.println(i + "�� �������� ���� �Ϸ�");
						}
					}
					else {
						roomInstance.removeRoom(roomNu);
						for (int i = 0; i < userInstance.getSizeInfo(); i++) {
							System.out.println(i + "�� �������� ���� �غ� ��");
							PrintWriter sendChangePeople = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
							sendChangePeople.println("NOTIFY_REMOVE_ROOM%$%" + (roomNu+1));
							sendChangePeople.flush();
							System.out.println(i + "�� �������� ���� �Ϸ�");
						}
					}
					str = null;
				}

				// ä�� ����
				else if (str.contains("SEND_CHAT%$%")) {
					System.out.println("ä�� ���� ��û �޼��� / ���� �޼��� : " + str);
					String[] chatStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : ä�� ����
					System.out.println("���� �޼��� �и� Ȯ�� - [0] : " + chatStr[0] + " / [1] : " + chatStr[1]);
					ArrayList<User> userList = roomInstance.getRoomInfo(Integer.parseInt(chatStr[1])).entry;
					for (int i = 0; i < userList.size(); i++) { // ä�� ����
						System.out.println("���� " + i + "�� �������� ä�� ���� �غ� ��");
						PrintWriter sendChat = new PrintWriter(userList.get(i).socket.getOutputStream());
						sendChat.println("SEND_CHAT%$%" + userInfo.nickName + "%$%" + chatStr[1] + "\r\n");
						sendChat.flush();
						System.out.println("���� " + i + "�� �������� ä�� ���� �Ϸ�");
					}
				}
				
				// ���α׷� ����
				else if(str.contains("REQUEST_OUT_USER")) {
					System.out.println("ä�� ���� ��û �޼��� / ���� �޼��� : " + str);
					userInstance.removeUser(userInfo.nickName);
					for(int i = 0; i < userInstance.getSizeInfo(); i++) {
						if(userInstance.getUserInfo(i).nickName == userInfo.nickName) {
							System.out.println("������ ��������");
							return;
						}
					}
					System.out.println("������ ����µ� ����");
				}
				
				// ���� ����
				else if(socket == null) {
					if(userInfo.roomNu != 0) {
						if(roomInstance.getRoomInfo(userInfo.roomNu).entry != null) {
							ArrayList<User> roomUser = roomInstance.getRoomInfo(userInfo.roomNu).entry;
							for(int i = 0; i < roomUser.size(); i++) {
								PrintWriter notifyOutRoom = new PrintWriter(roomUser.get(i).socket.getOutputStream());
								notifyOutRoom.println("OUT_USER%$%" + userInfo.nickName);
								notifyOutRoom.flush();
							}
						}
						else {
							int roomNu = userInfo.roomNu; 
							roomInstance.removeRoom(userInfo.roomNu);
							for (int i = 0; i < userInstance.getSizeInfo(); i++) {
								System.out.println(i + "�� �������� ���� �غ� ��");
								PrintWriter sendChangePeople = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
								sendChangePeople.println("NOTIFY_REMOVE_ROOM%$%" + (roomNu+1));
								sendChangePeople.flush();
								System.out.println(i + "�� �������� ���� �Ϸ�");
							}
						}
					}
					userInstance.removeUser(userInfo.nickName);
					
				}
				System.out.println("====================================");
			}
		} catch (Exception e) {
			System.out.println("userthread : " + e.getMessage());
		} finally { // ���� ����� ������ ����
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
