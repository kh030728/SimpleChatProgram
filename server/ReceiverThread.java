package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ReceiverThread extends Thread {
	//필드
	Socket socket;
	
	//생성자
	public ReceiverThread(Socket socket) {
		this.socket = socket;
	}
	
	//메소드
	@Override
	public void run() {
		RoomInfo roomInfo = new RoomInfo();
		BufferedReader reader = null;
		BufferedWriter Bwriter = null;
		PrintWriter Pwriter = null;
		

		roomInfo.addRoom("여기는 방이시다","1");
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			Pwriter = new PrintWriter(socket.getOutputStream()); //출력 스트림
		} catch(Exception e) {
			System.out.println("위쪽 에러 : "+e.getMessage());
		}
		try {
			while(true) {
				String str = reader.readLine(); //데이타의 한 줄 구분 \r\n
				System.out.println(str);
				if(str == null) break; //입력받은 값이 없으면 반복문 종료
				if(str.equals("REQUEST_ROOMINFO")) {
					System.out.println("여기는 if 안");
					for(int i = 0; i < roomInfo.sizeRoom();i++) {
						System.out.println("여기는 for 안");
						//String pnp = "test\r\n";
						//Bwriter.write(pnp);
						//Pwriter.println(pnp);
						//Bwriter.write("RNo"+i+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i));
						Pwriter.println("RNo"+(i+1)+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i)+"\r\n");
						System.out.println("여기는 for 마지막");
						Pwriter.flush();
					}
				}
				//Pwriter.println("RNo1"+"RNa"+roomInfo.nameRoom(0)+"RPN"+roomInfo.peopleNumber(0));
				System.out.println("수신 > " + str); //수신메세지 출력
			}
		} catch(Exception e) {
			System.out.println("에러메세지 : "+e.getMessage());
		}
	}
}
