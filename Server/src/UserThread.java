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
		/*
		 * Room r1 = new Room("1�����ε���", "������"); roomInstance.addRoom(r1); Room r2 = new
		 * Room("2����������", "kkh"); roomInstance.addRoom(r2);
		 */

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

				// ������ ����(���� : "RNo���ȣRNa���̸�RPN�ο���")
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("������ ��û �޼��� ���� �Ϸ� / ���� �޼��� : " + str);
					for (int i = 0; i < roomInstance.getRoomAll(); i++) {
						System.out.println("������ �������� " + i + "�� �غ���");
						Pwriter.println("NOTIFY_ADD_ROOM%$%" + i + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entry.size() + "\r\n");
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
					String[] createRoomStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : �� ����, [2] : ��û ����
					System.out.println("�޼��� �и� Ȯ�ο� - 0 : " + createRoomStr[0] + " 1 : " + createRoomStr[1] + " 2 : " + createRoomStr[2]);
					Room room = new Room(createRoomStr[1], userInfo);
					roomInstance.addRoom(room);
					int addRoomNu = 0;
					for (int i = roomInstance.getRoomAll() - 1; i >= 0; i--) {
						System.out.println("������ �� ��ȣ ã�� �� / ���� Ž�� ������ �� ��ȣ : " + i);
						if (roomInstance.getRoomInfo(i).roomNa == createRoomStr[1]) {
							addRoomNu = i;
							System.out.println("Ž�� �Ϸ��� �� ��ȣ : " + i);
							break;
						}
					}
					System.out.println("������ �� ��ȣ : " + addRoomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM%$%" + addRoomNu);
					Pwriter.flush();
					str = null;
					str = Breader.readLine();
					System.out.println("�߰� �޼��� Ȯ�� : " + str);
					if(str.equals("READY_FOR_JOIN")) {
						Pwriter.println("USERS%$%" + createRoomStr[2]);
						Pwriter.flush();
					}
					for (int i = 0; i < userInstance.getSizeInfo(); i++) { // �� ���� �� ���ŵ� �� ���� �� ����
						System.out.println("���� �� ���ŵ� �� ���� ���� �غ���");
						PrintWriter NotifyAddRoom = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
						NotifyAddRoom.println("NOTIFY_ADD_ROOM%$%" + addRoomNu + "%$%" + createRoomStr[1] + "%$%" + roomInstance.getRoomInfo(addRoomNu).entry.size() +"\r\n");
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
					String[] joinRoomStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : ������ ����, [2] : ������ �� ��ȣ
					System.out.println("�޼��� �и� Ȯ�ο� - 0 : " + joinRoomStr[0] + " 1 : " + joinRoomStr[1] + " 2 : " + joinRoomStr[2]);
					roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).AddEntry(userInfo); // �� ������ ���� ���� �ֱ�
					Pwriter.println("FINISH_JOIN");
					Pwriter.flush();
					str = null;
					str = Breader.readLine();
					System.out.println("���� Ȯ�ο�" + str);
					if(str.equals("READY_FOR_JOIN")) { // �� ó�� �� �޼��� �ޱ�
						// ������ ��� ������
						String users = "USERS";
						for (int i = 0; i < roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entry.size(); i++) {
							System.out.println("������ ��� �������� �� / ���� ���൵ : " + users);
							users += "%$%";
							users += roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entry.get(i).nickName;
							System.out.println("������ ��� �������� �� / ���� �Ϸ�� ���� : " + users);
						}
						users += "\r\n";
						Pwriter.println(users);
						Pwriter.flush();
						
						// ������ �濡 ������ ���� ����
						ArrayList<User> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entry; // ������ ���� ���� ����Ʈ
						System.out.println("������ ���� ���ŵ� ���� ��� : " + joinUsers);
						for (int i = 0; i < joinUsers.size(); i++) { // ä�� ����
							System.out.println("������ ���� �����鿡�� ������ ���� ������ / ���� " + i + "�� �������� ���� ��� ��");
							PrintWriter sendChat = new PrintWriter(joinUsers.get(i).socket.getOutputStream());
							sendChat.println("JOIN_USER%$%" + joinRoomStr[1]);
							sendChat.flush();
							System.out.println("������ ���� �����鿡�� ������ ���� ������ / ���� " + i + "�� �������� ���� �Ϸ� ����");
						}
						System.out.println("������ ���� �������� ���ο� ������ ���� �Ϸ�");
						System.out.println("��ü �������� �ο����� ����� �� �˸�");
						for (int i = 0; i < userInstance.getSizeInfo(); i++) {
							System.out.println(i + "�� �������� ���� �غ� ��");
							PrintWriter sendChangePeople = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
							sendChangePeople.println("NOTIFY_CHAGE_ROOM%$%" + joinRoomStr[2] + "%$%" + roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entry.size());
							sendChangePeople.flush();
							System.out.println(i + "�� �������� ���� �Ϸ�");
						}
					}
					System.out.println("�� ������ ��ó�� �Ϸ�");
					joinRoomStr = null;
					str = null;
				}

				// �� ������
				else if (str.contains("REQUEST_OUT_ROOM%$%")) {
					System.out.println("�� ������ �޼��� ��û ���� �Ϸ� / ���ŵ� �޼��� : " + str);
					String[] outRoomStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : ���� ����, [2]: ���� �� ��ȣ
					System.out.println("�޼��� �и� Ȯ�ο� - 0 : " + outRoomStr[0] + " 1 : " + outRoomStr[1] + " 2 : " + outRoomStr[2]);
					roomInstance.getRoomInfo(Integer.parseInt(outRoomStr[2])).RemoveEntry(userInfo); // �ش� ���� ���� ��Ͽ��� ���� ���� ����
					// ���� ���� ���� ����
					ArrayList<User> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(outRoomStr[2])).entry; // ������ ���� ���� ����Ʈ
					if (joinUsers.size() != 0) {
						System.out.println("�ش� �濡 �����ִ� ������ �ִٸ� �� �����鿡�� ���� �޼��� ����");
						for (int i = 0; i < joinUsers.size(); i++) { // ä�� ����
							System.out.println(i + "�� �������� ���� �غ���");
							PrintWriter sendChat = new PrintWriter(joinUsers.get(i).socket.getOutputStream());
							sendChat.println(outRoomStr[1] + "���� �����ϼ̽��ϴ�.");
							sendChat.flush();
							System.out.println(i + "�� �������� ���� �Ϸ�");
						}
					}
					System.out.println("�� ������ ��û�� ��ó�� �Ϸ�");
					outRoomStr = null;
					str = null;
				}

				// ä�� ����
				else if (str.contains("SEND_CHAT%$%")) {
					System.out.println("ä�� ���� ��û �޼��� / ���� �޼��� : " + str);
					String[] chatStr = str.split("\\%\\$\\%"); // [0] : ��û �޼���, [1] : �� ��ȣ, [2] : ���� �г���, [3] : ä�� ����
					System.out.println("���� �޼��� �и� Ȯ�� - [0] : " + chatStr[0] + " / [1] : " + chatStr[1] + " / [2] : " + chatStr[2] + " / [3] : " + chatStr[3]);
					ArrayList<User> userList = roomInstance.getRoomInfo(Integer.parseInt(chatStr[1])).entry;
					for (int i = 0; i < userList.size(); i++) { // ä�� ����
						System.out.println("���� " + i + "�� �������� ä�� ���� �غ� ��");
						PrintWriter sendChat = new PrintWriter(userList.get(i).socket.getOutputStream());
						sendChat.println(str + "\r\n");
						sendChat.flush();
						System.out.println("���� " + i + "�� �������� ä�� ���� �Ϸ�");
					}
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
