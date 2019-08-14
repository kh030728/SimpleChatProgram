package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
	Socket socket;

	public UserThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		RoomInfo roomInfo = new RoomInfo();
		UserInfo userInfo = new UserInfo();
		roomInfo.addRoom("1�� ���ε�����");
		roomInfo.addRoom("2�� ���ΰŽþ�");
		roomInfo.addRoom("3�� �������ο�");

		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // ��� ��Ʈ��

			while (true) {
				System.out.println("while ����");
				String str = Breader.readLine(); // �Էµ� ����Ÿ�� �� �� ������ ����
				System.out.println(str); // �Է� Ȯ�ο�

				// Ŭ���̾�Ʈ ����
				// if(str.equals("bye")) break;

				// ������ ����(���� : "RNo���ȣRNa���̸�RPN�ο���")
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("������ if ����");
					for (int i = 0; i < roomInfo.sizeRoom(); i++) {
						System.out.println("������ if ������ for ����" + roomInfo.nameRoom(i));
						Pwriter.println("RNo" + (i + 1) + "RNa" + roomInfo.nameRoom(i) + "RPN" + roomInfo.peopleNumber(i) + "\r\n");
						Pwriter.flush();
						Thread.sleep(20);
						System.out.println("������ if ������ for ��");
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
					System.out.println("������ if ��");
					str = null;
				}

				// ���� �г��� ����
				else if (str.contains("NICKNAME_")) {
					System.out.println("�����г������� if ����");
					str = str.substring(9, str.length());
					System.out.println(str);
					userInfo.addUser(str);
					System.out.println("�����г������� if ��");
					str = null;
				}

				// �� ����
				else if (str.contains("REQUEST_CREATE_ROOM_")) {
					System.out.println("����� if ����");
					str = str.substring(20, str.length()); // �Է� ������ �� ����
					roomInfo.addRoom(str);
					System.out.println(str);
					int roomNu = roomInfo.searchRoom(str);
					System.out.println(roomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					// ������ ��� ������
					/* ���� ������ �ʿ� ���� ���
					 * for(int j = 0;j < userInfo.userList.size();j++) {
					 * if(userInfo.userJoinRoomNu.get(j) == roomNu) {
					 * Pwriter.println(userInfo.userList.get(j)); Pwriter.flush();
					 * System.out.println(userInfo.userList.get(j));
					 * Thread.sleep(20); } }
					 */
					System.out.println("����� if ��");
					str = null;
				}

				// ä�� ����
				else if (str.contains("%CHAT%&;_%$")) {
					String[] chatStr = str.split("&;_%$");
					int userNu = userInfo.searchUser(chatStr[1]);
					Pwriter.println(
							"RNu" + userInfo.userJoinRoomNu.get(userNu) + "UNa" + chatStr[1] + "Chat" + chatStr[2]);
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
