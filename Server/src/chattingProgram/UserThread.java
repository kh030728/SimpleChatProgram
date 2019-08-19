package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

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
		Room r1 = new Room("1�����ε���", "������");
		roomInstance.addRoom(r1);
		Room r2 = new Room("2����������", "kkh");
		roomInstance.addRoom(r2);
		
		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // ��� ��Ʈ��
			String nickRecieve = Breader.readLine();
			String nickStr = new String(nickRecieve);
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
					for (int i = 0; i < roomInstance.getRoomAll(); i++) {
						System.out.println("������ if ������ for ���� ");
						Pwriter.println("RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN"	+ roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						System.out.println("RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN"	+ roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
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
					System.out.println(str);
					Room room = new Room(str, nickStr);
					roomInstance.addRoom(room);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					System.out.println("����� if ��");
					str = null;
				}
				
				// �� ����
				else if(str.contains("REQUEST_JOIN_ROOM_")) {
					System.out.println("�� ���� if ����");
					str = str.substring(19, str.length());
					//������ ���� (��ȣ or ����)�� ���� �г��� �޾ƿ��� ���ø� �̿��ؼ� ���� �����ϱ�
					int roomNu = Integer.parseInt("");
					String userNick = "";
					roomInstance.getRoomInfo(roomNu).setAddEntry(userNick); // �� ������ ���� ���� �ֱ�
					for(int i = 0; i < userInstance.getSizeInfo(); i++) { // ���� ������ ������ �� �ֱ�
						if(userInstance.getUserInfo(i).nickName == userNick) {
							userInstance.getUserInfo(i).joinRoomNo = roomNu;
						}
					}
					// ������ ��� ������
					for(int i = 0; i < roomInstance.getRoomInfo(roomNu).entryList.size(); i++) {
						Pwriter.println(roomInstance.getRoomInfo(roomNu).entryList.get(i));
						Pwriter.flush();
						System.out.println("������ 1 : " + roomInstance.getRoomInfo(roomNu).entryList.get(i));
						Thread.sleep(20);
					}
					str = null;
				}
				
				// �� ������
				else if(str.contains("REQUEST_OUT_ROOM_")) {
					System.out.println("�� ������ if ����");
					str = str.substring(18, str.length());
					System.out.println(str);
					roomInstance.removeRoom(Integer.parseInt(str));
					str = null;
				}

				// ä�� ����
				else if (str.contains("%CHAT%&;_%$")) {
					String[] chatStr = str.split("&;_%$");
					int roomNu = 0;
					for(int i = 0; i < userInstance.getSizeInfo(); i++) {
						if(userInstance.getUserInfo(i).nickName == chatStr[1]) { // �� ��ȣ ���ϱ�
							roomNu = userInstance.getUserInfo(i).joinRoomNo;
						}
						break;
					}
					for(int i = 0; i < userInstance.getSizeInfo(); i++) { // ä�� ������
						if(userInstance.getUserInfo(i).joinRoomNo == roomNu) {
							PrintWriter sendChat = new PrintWriter(userInstance.getUserInfo(i).getSocket().getOutputStream());
							sendChat.println(chatStr[2]);
							sendChat.flush();
						}
					}
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
