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
		Room room = new Room("1번방인데수", "ㄱㄱㅎ");
		roomInstance.addRoom(room);
		room = new Room("2번방이지요", "kkh");
		roomInstance.addRoom(room);

		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // 출력 스트림

			String nickStr = Breader.readLine();
			System.out.println("닉네임 수신 " + nickStr);

			// 유저 닉네임 저장
			if (nickStr.contains("NICKNAME_")) {
				System.out.println("유저닉네임저장 if 시작");
				nickStr = nickStr.substring(9, nickStr.length());
				System.out.println(nickStr);
				User userInfo = new User(nickStr, socket);
				userInstance.addUser(userInfo);
				System.out.println("유저닉네임저장 if 끝");
			} else {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
					}
				}
			}

			while (true) {
				System.out.println("while 시작");
				String str = Breader.readLine(); // 입력된 데이타를 한 줄 단위로 읽음
				System.out.println(str); // 입력 확인용

				// 클라이언트 종료
				// if(str.equals("bye")) break;

				// 방정보 전송(형식 : "RNo방번호RNa방이름RPN인원수")
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("방정보 if 시작");
					String receiveRoomInfo = null;
					for (int i = 0; i < roomInstance.roomAll(); i++) {
						System.out.println("방정보 if 내부의 for 시작 ");
						receiveRoomInfo = "RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n";
						Pwriter.println(receiveRoomInfo.getBytes("utf-8"));
						System.out.println("RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN"
								+ roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						Pwriter.flush();
						Thread.sleep(20);
						System.out.println("방정보 if 내부의 for 끝");
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
					System.out.println("방정보 if 끝");
					str = null;
				}

				// 방 생성
				else if (str.contains("REQUEST_CREATE_ROOM_")) {
					System.out.println("방생성 if 시작");
					str = str.substring(20, str.length()); // 입력 값에서 방 제목
					// roomInfo.addRoom(str);
					System.out.println(str);
					// int roomNu = roomInfo.searchRoom(str);
					// System.out.println(roomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					// 참여자 목록 보내기
					/*
					 * 현재 구현할 필요 없는 기능 for(int j = 0;j < userInfo.userList.size();j++) {
					 * if(userInfo.userJoinRoomNu.get(j) == roomNu) {
					 * Pwriter.println(userInfo.userList.get(j)); Pwriter.flush();
					 * System.out.println(userInfo.userList.get(j)); Thread.sleep(20); } }
					 */
					System.out.println("방생성 if 끝");
					str = null;
				}

				// 채팅 전송
				else if (str.contains("%CHAT%&;_%$")) {
					String[] chatStr = str.split("&;_%$");
					// int userNu = userInfo.searchUser(chatStr[1]);
					// Pwriter.println(
					// "RNu" + userInfo.userJoinRoomNu.get(userNu) + "UNa" + chatStr[1] + "Chat" +
					// chatStr[2]);
				}
				System.out.println("while 끝");
			}
		} catch (Exception e) {
			System.out.println("userthread : " + e.getMessage());
		} finally { // 서버 종료시 소켓을 해제
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
