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
		Room r1 = new Room("1번방인데수", "ㄱㄱㅎ");
		roomInstance.addRoom(r1);
		Room r2 = new Room("2번방이지요", "kkh");
		roomInstance.addRoom(r2);
		
		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // 출력 스트림
			String nickRecieve = Breader.readLine();
			String nickStr = new String(nickRecieve);
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
					for (int i = 0; i < roomInstance.getRoomAll(); i++) {
						System.out.println("방정보 if 내부의 for 시작 ");
						Pwriter.println("RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN"	+ roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						System.out.println("RNo" + (i + 1) + "RNa" + roomInstance.getRoomInfo(i).roomNa + "RPN"	+ roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
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
					System.out.println(str);
					Room room = new Room(str, nickStr);
					roomInstance.addRoom(room);
					Pwriter.println("SUCCESS_CREATE_ROOM");
					Pwriter.flush();
					System.out.println("방생성 if 끝");
					str = null;
				}
				
				// 방 참여
				else if(str.contains("REQUEST_JOIN_ROOM_")) {
					System.out.println("방 참여 if 시작");
					str = str.substring(19, str.length());
					//참여할 방의 (번호 or 제목)과 유저 닉네임 받아오고 스플릿 이용해서 각각 구분하기
					int roomNu = Integer.parseInt("");
					String userNick = "";
					roomInstance.getRoomInfo(roomNu).setAddEntry(userNick); // 방 정보에 참여 유저 넣기
					for(int i = 0; i < userInstance.getSizeInfo(); i++) { // 유저 정보에 참여한 방 넣기
						if(userInstance.getUserInfo(i).nickName == userNick) {
							userInstance.getUserInfo(i).joinRoomNo = roomNu;
						}
					}
					// 참여자 목록 보내기
					for(int i = 0; i < roomInstance.getRoomInfo(roomNu).entryList.size(); i++) {
						Pwriter.println(roomInstance.getRoomInfo(roomNu).entryList.get(i));
						Pwriter.flush();
						System.out.println("참여자 1 : " + roomInstance.getRoomInfo(roomNu).entryList.get(i));
						Thread.sleep(20);
					}
					str = null;
				}
				
				// 방 나가기
				else if(str.contains("REQUEST_OUT_ROOM_")) {
					System.out.println("방 나가기 if 시작");
					str = str.substring(18, str.length());
					System.out.println(str);
					roomInstance.removeRoom(Integer.parseInt(str));
					str = null;
				}

				// 채팅 전송
				else if (str.contains("%CHAT%&;_%$")) {
					String[] chatStr = str.split("&;_%$");
					int roomNu = 0;
					for(int i = 0; i < userInstance.getSizeInfo(); i++) {
						if(userInstance.getUserInfo(i).nickName == chatStr[1]) { // 방 번호 구하기
							roomNu = userInstance.getUserInfo(i).joinRoomNo;
						}
						break;
					}
					for(int i = 0; i < userInstance.getSizeInfo(); i++) { // 채팅 보내기
						if(userInstance.getUserInfo(i).joinRoomNo == roomNu) {
							PrintWriter sendChat = new PrintWriter(userInstance.getUserInfo(i).getSocket().getOutputStream());
							sendChat.println(chatStr[2]);
							sendChat.flush();
						}
					}
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
