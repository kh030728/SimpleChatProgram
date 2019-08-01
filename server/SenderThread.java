package chattingProgram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//송신 thread 객체
public class SenderThread extends Thread {
	//필드
	Socket socket;
	
	//생성자
	public SenderThread(Socket socket) {
		this.socket = socket;
	}
	
	//메소드
	@Override
	public void run() {
		try {
			//입력으로 들어오는 스트림의 속도 향상
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter writer = new PrintWriter(socket.getOutputStream()); //출력 스트림
			while(true) {
				String str = reader.readLine(); //입력된 데이타를 한 줄 단위로 읽음
				if(str.equals("bye")) break; //입력값이 bye면 클라이언트 종료
				writer.println(str); //bye가 아니면 메세지 전송
				writer.flush(); //버퍼내의 데이타 밀어내기
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close(); //클라이언트 종료시 반드시 소켓 해제
			} catch(Exception e) {
			}
		}
	}
}
