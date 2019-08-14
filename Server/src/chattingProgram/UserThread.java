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
		roomInfo.addRoom("1번 방인데수웅");
		roomInfo.addRoom("2번 방인거시야");
		roomInfo.addRoom("3번 방이지로옹");

		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // 출력 스트림

			while (true) {
				System.out.println("while 시작");
				String str = Breader.readLine(); // 입력된 데이타를 한 줄 단위로 읽음
				System.out.println(str); // 입력 확인용

				// 클라이언트 종료
				// if(str.equals("bye")) break;

				// 방정보 전송(형식 : "RNo방번호RNa방이름RPN인원수")
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("방정보 if 시작");
					for (int i = 0; i < roomInfo.sizeRoom(); i++) {
						System.out.println("방정보 if 내부의 for 시작" + roomInfo.nameRoom(i));
						Pwriter.println("RNo" + (i + 1) + "RNa" + roomInfo.nameRoom(i) + "RPN" + roomInfo.peopleNumber(i) + "\r\n");
						Pwriter.flush();
						Thread.sleep(20);
						System.out.println("방정보 if 내부의 for 끝");
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
					System.out.println("방정보 if 끝");
					str = null;
				}

				// 유저 닉네임 저장
				else if (str.contains("NICKNAME_")) {
					System.out.println("유저닉네임저장 if 시작");
					str = str.substring(9, str.length());
					System.out.println(str);
					userInfo.addUser(str);
					System.out.println("유저닉네임저장 if 끝");
					str = null;
				}

				// 방 생성
				else if (str.contains("REQUEST_CREATE_ROOM_")) {
					System.out.println("방생성 if 시작");
					str = str.substring(20, str.length()); // 입력 값에서 방 제목
					roomInfo.addRoom(str);
					System.out.println(str);
					int roomNu = roomInfo.searchRoom(str);
					System.out.println(roomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					// 참여자 목록 보내기
					/* 현재 구현할 필요 없는 기능
					 * for(int j = 0;j < userInfo.userList.size();j++) {
					 * if(userInfo.userJoinRoomNu.get(j) == roomNu) {
					 * Pwriter.println(userInfo.userList.get(j)); Pwriter.flush();
					 * System.out.println(userInfo.userList.get(j));
					 * Thread.sleep(20); } }
					 */
					System.out.println("방생성 if 끝");
					str = null;
				}

				// 채팅 전송
				else if (str.contains("%CHAT%&;_%$")) {
					String[] chatStr = str.split("&;_%$");
					int userNu = userInfo.searchUser(chatStr[1]);
					Pwriter.println(
							"RNu" + userInfo.userJoinRoomNu.get(userNu) + "UNa" + chatStr[1] + "Chat" + chatStr[2]);
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
