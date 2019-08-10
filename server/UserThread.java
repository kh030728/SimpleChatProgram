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
		
		try {
			BufferedReader Breader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); //출력 스트림
			
			while(true) {
				System.out.println("while 시작");
				String str = Breader.readLine(); //입력된 데이타를 한 줄 단위로 읽음
				System.out.println(str); //입력 확인용
				
				//클라이언트 종료
				if(str.equals("bye")) break;
				
				//Pwriter.println(str); //bye가 아니면 메세지 전송
				//Pwriter.flush(); //버퍼내의 데이타 밀어내기
				
				//방정보 전송(형식 : "RNo방번호RNa방이름RPN인원수")
				if(str.equals("REQUEST_ROOMINFO")) {
					System.out.println("방정보 if 시작");
					for(int i = 0; i < roomInfo.sizeRoom();i++) {
						System.out.println("방정보 if 내부의 for 시작");
						Pwriter.println("RNo"+(i+1)+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i)+"\r\n");
						Pwriter.flush();
						Thread.sleep(10);;
						System.out.println("방정보 if 내부의 for 끝");
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
					System.out.println("방정보 if 끝");
				}
				//유저 닉네임 저장
				else if(str.equals("NICKNAME_")) {
					System.out.println("유저닉네임저장 if 시작");
					str = str.substring(10, str.length());
					userInfo.addUser(str);
					System.out.println("유저닉네임저장 if 끝");
				}
				
				//방 생성
				else if(str.equals("")) {
					System.out.println("방생성 if 시작");
					//str = str.substring(10, str.length()); // 입력 값에서 방 제목
					roomInfo.addRoom(str);
					int i = roomInfo.searchRoom(str);
					roomInfo.joinRoom(i);
				}
			}
		} catch(Exception e) {
			System.out.println("userthread : "+e.getMessage());
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
