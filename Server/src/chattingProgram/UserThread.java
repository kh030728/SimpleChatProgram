package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class UserThread extends Thread {
	Socket socket;
	String ip;

	public UserThread(Socket socket, String ip) {
		this.socket = socket;
		this.ip = ip;
	}

	public void run() {

		UsersInfo userInstance = UsersInfo.getInstance();
		RoomsInfo roomInstance = RoomsInfo.getInstance();
		Room room = new Room("1�����ε���", "������");
		roomInstance.addRoom(room);
		room = new Room("2����������", "kkh");
		roomInstance.addRoom(room);

		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // ��� ��Ʈ��

			String nickStr = Breader.readLine();
			System.out.println("�г��� ���� " + nickStr);

			// ���� �г��� ����
			if (nickStr.contains("NICKNAME_")) {
				System.out.println("�����г������� if ����");
				nickStr = nickStr.substring(9, nickStr.length());
				System.out.println(nickStr);
				User userInfo = new User(nickStr, socket);
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

			while (true) {
				System.out.println("while ����");
				String str = Breader.readLine(); // �Էµ� ����Ÿ�� �� �� ������ ����
				System.out.println(str); // �Է� Ȯ�ο�

				// Ŭ���̾�Ʈ ����
				// if(str.equals("bye")) break;

				// ������ ����(���� : "RNo���ȣRNa���̸�RPN�ο���")
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("������ if ����");
					String receiveRoomInfo = null;
					for (int i = 0; i < roomInstance.roomAll(); i++) {
						System.out.println("������ if ������ for ���� ");
						receiveRoomInfo = "RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n";
						Pwriter.println(receiveRoomInfo.getBytes("utf-8"));
						System.out.println("RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN"
								+ roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						Pwriter.flush();
						Thread.sleep(20);
						System.out.println("������ if ������ for ��");
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
					System.out.println("������ if ��");
					str = null;
				}

				// �� ����
				else if (str.contains("REQUEST_CREATE_ROOM_")) {
					System.out.println("����� if ����");
					str = str.substring(20, str.length()); // �Է� ������ �� ����
					// roomInfo.addRoom(str);
					System.out.println(str);
					// int roomNu = roomInfo.searchRoom(str);
					// System.out.println(roomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					// ������ ��� ������
					/*
					 * ���� ������ �ʿ� ���� ��� for(int j = 0;j < userInfo.userList.size();j++) {
					 * if(userInfo.userJoinRoomNu.get(j) == roomNu) {
					 * Pwriter.println(userInfo.userList.get(j)); Pwriter.flush();
					 * System.out.println(userInfo.userList.get(j)); Thread.sleep(20); } }
					 */
					System.out.println("����� if ��");
					str = null;
				}

				// ä�� ����
				else if (str.contains("%CHAT%&;_%$")) {
					String[] chatStr = str.split("&;_%$");
					// int userNu = userInfo.searchUser(chatStr[1]);
					// Pwriter.println(
					// "RNu" + userInfo.userJoinRoomNu.get(userNu) + "UNa" + chatStr[1] + "Chat" +
					// chatStr[2]);
				}
				System.out.println("while ��");
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
