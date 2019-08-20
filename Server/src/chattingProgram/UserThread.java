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
			if (nickStr.contains("NICKNAME%$%")) {
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
						Pwriter.println("ROOM%$%" + (i + 1) + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						System.out.println("ROOM%$%" + (i + 1) + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
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
				else if (str.contains("REQUEST_CREATE_ROOM%$%")) {
					System.out.println("����� if ����");
					System.out.println(str);
					String[] createRoomStr = str.split("%$%"); // [0] : ��û �޼���, [1] : �� ����, [2] : ��û ����
					System.out.println(createRoomStr[0] + " " + createRoomStr[1] + " " + createRoomStr[2]);
					Room room = new Room(createRoomStr[1], createRoomStr[2]);
					roomInstance.addRoom(room);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					System.out.println("����� if ��");
					createRoomStr = null;
					str = null;
					
					for (int i = 0; i < roomInstance.getRoomAll(); i++) { // �� ���� �� ���ŵ� �� ���� �� ����
						Pwriter.println("ROOM%$%" + (i + 1) + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						Pwriter.flush();
						Thread.sleep(20);
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
				}
				
				// �� ����
				else if(str.contains("REQUEST_JOIN_ROOM%$%")) {
					System.out.println("�� ���� if ����");
					String[] joinRoomStr = str.split("%$%"); // [0] : ��û �޼���, [1] : ������ ����, [2] : ������ �� ��ȣ
					roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).setAddEntry(joinRoomStr[1]); // �� ������ ���� ���� �ֱ�
					// ������ ��� ������
					for(int i = 0; i < roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList.size(); i++) {
						Pwriter.println(roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList.get(i));
						Pwriter.flush();
						System.out.println("������ 1 : " + roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList.get(i));
						Thread.sleep(20);
					}
					//������ �濡 ������ ���� ����
					ArrayList<String> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList; // ������ ���� ���� ����Ʈ
					for(int i = 0; i < joinUsers.size(); i++) {	// ä�� ����
						PrintWriter sendChat = new PrintWriter(userInstance.getUserSocket(joinUsers.get(i)).getOutputStream());
						sendChat.println(joinRoomStr[1] + "���� �����ϼ̽��ϴ�.");
						sendChat.flush();
					}
					joinRoomStr = null;
					str = null;
				}
				
				// �� ������
				else if(str.contains("REQUEST_OUT_ROOM%$%")) {
					System.out.println("�� ������ if ����");
					System.out.println(str);
					String[] outRoomStr = str.split("%$%"); // [0] : ��û �޼���, [1] : ���� ����, [2]: ���� �� ��ȣ
					roomInstance.getRoomInfo(Integer.parseInt(outRoomStr[2])).setRemoveEntry(outRoomStr[1]); // �ش� ���� ���� ��Ͽ��� ���� ���� ����
					//���� ���� ���� ����
					ArrayList<String> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(outRoomStr[2])).entryList; // ������ ���� ���� ����Ʈ
					for(int i = 0; i < joinUsers.size(); i++) {	// ä�� ����
						PrintWriter sendChat = new PrintWriter(userInstance.getUserSocket(joinUsers.get(i)).getOutputStream());
						sendChat.println(outRoomStr[1] + "���� �����ϼ̽��ϴ�.");
						sendChat.flush();
					}
					outRoomStr = null;
					str = null;
				}

				// ä�� ����
				else if (str.contains("SEND_CHAT%$%")) {
					System.out.println(str);
					String[] chatStr = str.split("%$%"); // [0] : ��û �޼���, [1] : ���� �г���, [2] : �� ��ȣ, [3] : ä�� ����
					ArrayList<String> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(chatStr[2])).entryList; // ������ ���� ���� ����Ʈ
					for(int i = 0; i < joinUsers.size(); i++) {	// ä�� ����
						PrintWriter sendChat = new PrintWriter(userInstance.getUserSocket(joinUsers.get(i)).getOutputStream());
						sendChat.println(chatStr[3]);
						sendChat.flush();
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
