package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

//�۽� thread ��ü
public class SenderThread extends Thread {
	//�ʵ�
	Socket socket;
	
	//������
	public SenderThread(Socket socket) {
		this.socket = socket;
	}
	
	//�޼ҵ�
	@Override
	public void run() {
		try {
			RoomInfo roomInfo = new RoomInfo();
			
			//�Է����� ������ ��Ʈ���� �ӵ� ���
			BufferedReader Breader = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); //��� ��Ʈ��
			while(true) {
				String str = Breader.readLine(); //�Էµ� ����Ÿ�� �� �� ������ ����
				if(str.equals("bye")) break; //�Է°��� bye�� Ŭ���̾�Ʈ ����
				if(str.equals("REQUEST_ROOMINFO")) {
					for(int i = 0; i < roomInfo.sizeRoom();i++) {
						Bwriter.write("RNo"+i+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i));
					}
				}
				Pwriter.println(str); //bye�� �ƴϸ� �޼��� ����
				Pwriter.flush(); //���۳��� ����Ÿ �о��
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close(); //Ŭ���̾�Ʈ ����� �ݵ�� ���� ����
			} catch(Exception e) {
			}
		}
	}
}
