package chattingProgram;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//서버는 ServerSocket을 가짐
//ServerSocket이 Socket을 생성
public class Server {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 소켓 생성
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(6000); // port번호 6000로 서버소켓 생성
			UsersInfo users = UsersInfo.getInstance();
			RoomsInfo rooms = RoomsInfo.getInstance();
			
			while (true) {
				socket = serverSocket.accept(); // 소켓생성 메소드 accept();
				InetAddress inetAddress = socket.getInetAddress(); //접속자 IP
				String ip = inetAddress.getHostAddress();
				Thread th = new UserThread(socket, ip);
				th.start();
			}
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
