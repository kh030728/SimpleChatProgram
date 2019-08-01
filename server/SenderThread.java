package chattingProgram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
			//�Է����� ������ ��Ʈ���� �ӵ� ���
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter writer = new PrintWriter(socket.getOutputStream()); //��� ��Ʈ��
			while(true) {
				String str = reader.readLine(); //�Էµ� ����Ÿ�� �� �� ������ ����
				if(str.equals("bye")) break; //�Է°��� bye�� Ŭ���̾�Ʈ ����
				writer.println(str); //bye�� �ƴϸ� �޼��� ����
				writer.flush(); //���۳��� ����Ÿ �о��
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
