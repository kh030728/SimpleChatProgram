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
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); // 출력 스트림
			String nickStr = Breader.readLine();
			System.out.println("닉네임 수신 " + nickStr);

			// 유저 닉네임 저장
			if (nickStr.contains("NICKNAME%$%")) {
				System.out.println("유저닉네임저장 if 시작");
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
				System.out.println("유저닉네임저장 if 끝");
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
				String str = Breader.readLine(); // 입력된 데이타를 한 줄 단위로 읽음
				System.out.println("받은 메세지 : " + str); // 입력 확인용

				// 클라이언트 종료
				// if(str.equals("bye")) break;

				// 방정보 전송(형식 : "RNo방번호RNa방이름RPN인원수")
				if (str.equals("REQUEST_ROOMINFO")) {
					System.out.println("방정보 요청 메세지 수신 완료 / 수신 메세지 : " + str);
					for (int i = 0; i < roomInstance.getRoomAll(); i++) {
						System.out.println("방정보 보내는중 " + i + "개 준비중");
						Pwriter.println("NOTIFY_ADD_ROOM%$%" + i + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entry.size() + "\r\n");
						System.out.println("NORIFY_ADD_ROOM%$%" + i + "%$%" + roomInstance.getRoomInfo(i).roomNa + "%$%" + roomInstance.getRoomInfo(i).entry.size() + "\r\n");
						Pwriter.flush();
						Thread.sleep(50);
						System.out.println("방정보 보내는중 " + i + "개 완료");
					}
					System.out.println("방정보 전송 끝");
					str = null;
				}

				// 방 생성
				else if (str.contains("REQUEST_CREATE_ROOM%$%")) { // (유저 닉네임 받을 필요 없음(수정 필요))
					System.out.println("방생성 요청 메세지 수신 완료 / 수신 메세지 : " + str);
					String[] createRoomStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 방 제목, [2] : 요청 유저
					System.out.println("메세지 분리 확인용 - 0 : " + createRoomStr[0] + " 1 : " + createRoomStr[1] + " 2 : " + createRoomStr[2]);
					Room room = new Room(createRoomStr[1], userInfo);
					roomInstance.addRoom(room);
					int addRoomNu = 0;
					for (int i = roomInstance.getRoomAll() - 1; i >= 0; i--) {
						System.out.println("생성한 방 번호 찾는 중 / 현재 탐색 시작할 방 번호 : " + i);
						if (roomInstance.getRoomInfo(i).roomNa == createRoomStr[1]) {
							addRoomNu = i;
							System.out.println("탐색 완료한 방 번호 : " + i);
							break;
						}
					}
					userInfo.roomNu = addRoomNu;
					System.out.println("생성한 방 번호 : " + addRoomNu);
					Pwriter.println("SUCCESS_CREATE_ROOM%$%" + addRoomNu);
					Pwriter.flush();
					str = null;
					str = Breader.readLine();
					System.out.println("추가 메세지 확인 : " + str);
					if(str.equals("READY_FOR_JOIN")) {
						Pwriter.println("USERS%$%" + userInfo.nickName);
						Pwriter.flush();
					}
					for (int i = 0; i < userInstance.getSizeInfo(); i++) { // 방 생성 후 갱신된 방 정보 재 전송
						System.out.println("생성 후 갱신된 방 정보 전송 준비중");
						PrintWriter NotifyAddRoom = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
						NotifyAddRoom.println("NOTIFY_ADD_ROOM%$%" + addRoomNu + "%$%" + createRoomStr[1] + "%$%" + roomInstance.getRoomInfo(addRoomNu).entry.size() +"\r\n");
						NotifyAddRoom.flush();
						System.out.println("갱신된 방 정보 전송 완료");
					}
					System.out.println("방 생성과 생성 후 후처리 완료");
					createRoomStr = null;
					str = null;
				}

				// 방 참여
				else if (str.contains("REQUEST_JOIN_ROOM%$%")) { // (참여할 유저 필요 없음(수정 필요))
					System.out.println("방 참여 메세지 수신 완료 / 수신 메세지 : " + str);
					String[] joinRoomStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 참여할 유저, [2] : 참여할 방 번호
					System.out.println("메세지 분리 확인용 - 0 : " + joinRoomStr[0] + " 1 : " + joinRoomStr[1] + " 2 : " + joinRoomStr[2]);
					int roomNu = Integer.parseInt(joinRoomStr[2]);  // 방 번호
					roomInstance.getRoomInfo(roomNu).AddEntry(userInfo); // 방 정보에 참여 유저 넣기
					userInfo.roomNu = roomNu; // 유저 정보의 현재 방 번호 변경
					Pwriter.println("FINISH_JOIN");
					Pwriter.flush();
					
					str = null;
					str = Breader.readLine();
					System.out.println("참여 확인용" + str);
					if(str.equals("READY_FOR_JOIN")) { // 후 처리 전 메세지 받기
						// 참여자 목록 보내기
						String users = "USERS";
						for (int i = 0; i < roomInstance.getRoomInfo(roomNu).entry.size(); i++) {
							System.out.println("참여자 목록 가져오는 중 / 현재 진행도 : " + users);
							users += "%$%";
							users += roomInstance.getRoomInfo(roomNu).entry.get(i).nickName;
							System.out.println("참여자 목록 가져오는 중 / 현재 완료된 진행 : " + users);
						}
						users += "\r\n";
						Pwriter.println(users);
						Pwriter.flush();
						
						// 참여한 방에 참여자 정보 전달
						ArrayList<User> joinUsers = roomInstance.getRoomInfo(roomNu).entry; // 참여한 방의 유저 리스트
						System.out.println("참여한 방의 갱신된 유저 목록 : " + joinUsers);
						for (int i = 0; i < joinUsers.size(); i++) { // 채팅 전송
							System.out.println("참여한 방의 유저들에게 참여자 정보 전송중 / 현재 " + i + "번 유저에게 전송 대기 중");
							PrintWriter sendChat = new PrintWriter(joinUsers.get(i).socket.getOutputStream());
							sendChat.println("JOIN_USER%$%" + userInfo.nickName);
							sendChat.flush();
							System.out.println("참여한 방의 유저들에게 참여자 정보 전송중 / 현재 " + i + "번 유저까지 전송 완료 상태");
						}
						System.out.println("참여한 방의 유저에게 새로운 참여자 전송 완료");
						
						System.out.println("전체 유저에게 인원수가 변경된 방 알림");
						for (int i = 0; i < userInstance.getSizeInfo(); i++) {
							System.out.println(i + "번 유저에게 전송 준비 중");
							PrintWriter sendChangePeople = new PrintWriter(userInstance.getUserInfo(i).socket.getOutputStream());
							sendChangePeople.println("NOTIFY_CHAGE_ROOM%$%" + roomNu + "%$%" + roomInstance.getRoomInfo(roomNu).entry.size());
							sendChangePeople.flush();
							System.out.println(i + "번 유저까지 전송 완료");
						}
					}
					System.out.println("방 참여와 후처리 완료");
					joinRoomStr = null;
					str = null;
				}

				// 방 나가기
				else if (str.contains("REQUEST_OUT_ROOM%$%")) { // (나간 유저 필요없음 (수정 필요))
					System.out.println("방 나가기 메세지 요청 수신 완료 / 수신된 메세지 : " + str);
					String[] outRoomStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 나간 유저
					System.out.println("메세지 분리 확인용 - 0 : " + outRoomStr[0] + " 1 : " + userInfo.nickName);
					int roomNu = userInfo.roomNu;
					userInfo.roomNu = 0;
					roomInstance.getRoomInfo(roomNu).RemoveEntry(userInfo.nickName); // 해당 방의 유저 목록에서 나간 유저 삭제
					Pwriter.println("OUT_ROOM_OK");
					Pwriter.flush();
					// 나간 유저 정보 전달
					ArrayList<User> joinUsers = roomInstance.getRoomInfo(roomNu).entry; // 참여한 방의 유저 리스트
					if (joinUsers.size() != 0) {
						System.out.println("해당 방에 남아있는 유저가 있다면 그 유저들에게 퇴장 메세지 전송");
						for (int i = 0; i < joinUsers.size(); i++) { // 채팅 전송
							System.out.println(i + "번 유저에게 전송 준비중");
							PrintWriter sendChat = new PrintWriter(joinUsers.get(i).socket.getOutputStream());
							sendChat.println("OUT_USER%$%" + userInfo.nickName);
							sendChat.flush();
							System.out.println(i + "번 유저까지 전송 완료");
						}
					}
					else {
						roomInstance.removeRoom(roomNu);
					}
					System.out.println("방 나가기 요청과 후처리 완료");
					outRoomStr = null;
					str = null;
				}

				// 채팅 전송
				else if (str.contains("SEND_CHAT%$%")) {
					System.out.println("채팅 전송 요청 메세지 / 받은 메세지 : " + str);
					String[] chatStr = str.split("\\%\\$\\%"); // [0] : 요청 메세지, [1] : 방 번호, [2] : 유저 닉네임, [3] : 채팅 내용
					System.out.println("받은 메세지 분리 확인 - [0] : " + chatStr[0] + " / [1] : " + chatStr[1] + " / [2] : " + chatStr[2] + " / [3] : " + chatStr[3]);
					ArrayList<User> userList = roomInstance.getRoomInfo(Integer.parseInt(chatStr[1])).entry;
					for (int i = 0; i < userList.size(); i++) { // 채팅 전송
						System.out.println("현재 " + i + "번 유저에게 채팅 전송 준비 중");
						PrintWriter sendChat = new PrintWriter(userList.get(i).socket.getOutputStream());
						sendChat.println(str + "\r\n");
						sendChat.flush();
						System.out.println("현재 " + i + "번 유저에게 채팅 전송 완료");
					}
				}
				
				// 프로그램 종료
				else if(str.contains("REQUEST_OUT_USER%$%")) { // (닉네임 받을 필요 없음(수정 필요))
					System.out.println("채팅 전송 요청 메세지 / 받은 메세지 : " + str);
					String[] outUser = str.split("\\%\\$\\%"); //[0] : 요청 메세지, [1] : 유저 닉네임 
					System.out.println("받은 메세지 분리 확인 - [0] : " + outUser[0] + " [1] : " + outUser[1]);
					userInstance.removeUser(userInfo.nickName);
				}
				
				// 강제 종료
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
							roomInstance.removeRoom(userInfo.roomNu);
						}
					}
					userInstance.removeUser(userInfo.nickName);
					
				}
				System.out.println("====================================");
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
