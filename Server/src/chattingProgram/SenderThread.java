/*package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
			BufferedReader Breader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); //��� ��Ʈ��
			while(true) {
				String str = Breader.readLine(); //�Էµ� ����Ÿ�� �� �� ������ ����
				System.out.println(str);
				if(str.equals("bye")) break; //�Է°��� bye�� Ŭ���̾�Ʈ ����
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
}*/