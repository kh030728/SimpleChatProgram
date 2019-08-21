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
		/*
		Room r1 = new Room("1번방인데수", "ㄱㄱㅎ");
		roomInstance.addRoom(r1);
		Room r2 = new Room("2번방이지요", "kkh");
		roomInstance.addRoom(r2);
		*/
		
		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // 출력 스트림
			String nickRecieve = Breader.readLine();
			String nickStr = new String(nickRecieve);
			System.out.println("닉네임 수신 " + nickStr);

			// 유저 닉네임 저장
			if (nickStr.contains("NICKNAME%$%")) {
				System.out.println("유저닉네임저장 if 시작");
				nickStr = nickStr.substring(11, nickStr.length());
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
						Pwriter.println("ROOM%$%" + (i + 1) + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						System.out.println("ROOM%$%" + (i + 1) + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entryList.size() + "\r\n");
						Pwriter.flush();
						Thread.sleep(50);
						System.out.println("방정보 if 내부의 for 끝");
					}
					Pwriter.println("SEND_FINISH\r\n");
					Pwriter.flush();
					System.out.println("방정보 if 끝");
					str = null;
				}

				// 방 생성
				else if (str.contains("REQUEST_CREATE_ROOM%$%")) {
					System.out.println("방생성 if 시작");
					System.out.println(str);
					String[] createRoomStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 방 제목, [2] : 요청 유저
					System.out.println(createRoomStr[0] + " " + createRoomStr[1] + " " + createRoomStr[2]);
					Room room = new Room(createRoomStr[1], createRoomStr[2]);
					roomInstance.addRoom(room);
					Pwriter.println("SUCCESS_CREATE_ROOM\r\n");
					Pwriter.flush();
					for (int i = 0; i < userInstance.getSizeInfo(); i++) { // 방 생성 후 갱신된 방 정보 재 전송
						PrintWriter NotifyAddRoom = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
						int addRoomNu = 0;
						for(int j = roomInstance.getRoomAll()-1; j >= 0 ; j--) {
							if(roomInstance.getRoomInfo(j).roomNa == createRoomStr[1]) {
								addRoomNu = j;
							}
							break;
						}
						NotifyAddRoom.println("NOTIFY_ADD_ROOM%$%" + addRoomNu +"%$%" + createRoomStr[1] + "\r\n");
						NotifyAddRoom.println("SEND_FINISH\r\n");
						NotifyAddRoom.flush();
					}
					System.out.println("방생성 if 끝");
					createRoomStr = null;
					str = null;
				}
				
				// 방 참여
				else if(str.contains("REQUEST_JOIN_ROOM%$%")) {
					System.out.println("방 참여 if 시작");
					String[] joinRoomStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 참여할 유저, [2] : 참여할 방 번호
					roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).setAddEntry(joinRoomStr[1]); // 방 정보에 참여 유저 넣기
					// 참여자 목록 보내기
					String users = "USERS";
					for(int i = 0; i < roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList.size(); i++) {
						users += "%$%";
						users += roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList.get(i);
					}
					users += "\r\n";
					//참여한 방에 참여자 정보 전달
					ArrayList<String> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(joinRoomStr[2])).entryList; // 참여한 방의 유저 리스트
					for(int i = 0; i < joinUsers.size(); i++) {	// 채팅 전송
						PrintWriter sendChat = new PrintWriter(userInstance.getUserSocket(joinUsers.get(i)).getOutputStream());
						Pwriter.println(users);
						Pwriter.flush();
						sendChat.println(joinRoomStr[1] + "님이 참여하셨습니다.\r\n");
						sendChat.flush();
					}
					joinRoomStr = null;
					str = null;
				}
				
				// 방 나가기
				else if(str.contains("REQUEST_OUT_ROOM%$%")) {
					System.out.println("방 나가기 if 시작");
					System.out.println(str);
					String[] outRoomStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 나간 유저, [2]: 나간 방 번호
					roomInstance.getRoomInfo(Integer.parseInt(outRoomStr[2])).setRemoveEntry(outRoomStr[1]); // 해당 방의 유저 목록에서 나간 유저 삭제
					//나간 유저 정보 전달
					ArrayList<String> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(outRoomStr[2])).entryList; // 참여한 방의 유저 리스트
					for(int i = 0; i < joinUsers.size(); i++) {	// 채팅 전송
						PrintWriter sendChat = new PrintWriter(userInstance.getUserSocket(joinUsers.get(i)).getOutputStream());
						sendChat.println(outRoomStr[1] + "님이 퇴장하셨습니다.");
						sendChat.flush();
					}
					outRoomStr = null;
					str = null;
				}

				// 채팅 전송
				else if (str.contains("SEND_CHAT%$%")) {
					System.out.println(str);
					String[] chatStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 유저 닉네임, [2] : 방 번호, [3] : 채팅 내용
					ArrayList<String> joinUsers = roomInstance.getRoomInfo(Integer.parseInt(chatStr[2])).entryList; // 참여한 방의 유저 리스트
					for(int i = 0; i < joinUsers.size(); i++) {	// 채팅 전송
						PrintWriter sendChat = new PrintWriter(userInstance.getUserSocket(joinUsers.get(i)).getOutputStream());
						sendChat.println(chatStr[3]);
						sendChat.flush();
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
