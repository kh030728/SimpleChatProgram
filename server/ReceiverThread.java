package chattingProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
		RoomInfo roomInfo = new RoomInfo();
		BufferedReader reader = null;
		BufferedWriter Bwriter = null;
		PrintWriter Pwriter = null;
		

		roomInfo.addRoom("����� ���̽ô�","1");
		
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			Pwriter = new PrintWriter(socket.getOutputStream()); //��� ��Ʈ��
		} catch(Exception e) {
			System.out.println("���� ���� : "+e.getMessage());
		}
		try {
			while(true) {
				String str = reader.readLine(); //����Ÿ�� �� �� ���� \r\n
				System.out.println(str);
				if(str == null) break; //�Է¹��� ���� ������ �ݺ��� ����
				if(str.equals("REQUEST_ROOMINFO")) {
					System.out.println("����� if ��");
					for(int i = 0; i < roomInfo.sizeRoom();i++) {
						System.out.println("����� for ��");
						//String pnp = "test\r\n";
						//Bwriter.write(pnp);
						//Pwriter.println(pnp);
						//Bwriter.write("RNo"+i+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i));
						Pwriter.println("RNo"+(i+1)+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i)+"\r\n");
						System.out.println("����� for ������");
						Pwriter.flush();
					}
				}
				//Pwriter.println("RNo1"+"RNa"+roomInfo.nameRoom(0)+"RPN"+roomInfo.peopleNumber(0));
				System.out.println("���� > " + str); //���Ÿ޼��� ���
			}
		} catch(Exception e) {
			System.out.println("�����޼��� : "+e.getMessage());
		}
	}
}
