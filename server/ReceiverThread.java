package chattingProgram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiverThread extends Thread {
	//�ʵ�
	Socket socket;
	
	//������
	public ReceiverThread(Socket socket) {
		this.socket = socket;
	}
	
	//�޼ҵ�
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true) {
				String str = reader.readLine(); //����Ÿ�� �� �� ���� \r\n
				if(str == null) break; //�Է¹��� ���� ������ �ݺ��� ����
				System.out.println("���� > " + str); //���Ÿ޼��� ���
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
