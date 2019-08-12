package chattingProgram;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//서버는 ServerSocket을 가짐
//ServerSocket이 Socket을 생성
public class Server {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 소켓 생성
		ServerSocket serverSocket = null;
		Socket socket = null;
		RoomInfo roomInfo = new RoomInfo();

		try {
			serverSocket = new ServerSocket(6000); // port번호 6000로 서버소켓 생성
			socket = serverSocket.accept(); // 소켓생성 메소드 accept();
			Thread th1 = new UserThread(socket);
			th1.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally { // 서버 종료시 소켓을 해제
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
