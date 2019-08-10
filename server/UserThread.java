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
			PrintWriter Pwriter = new PrintWriter(socket.getOutputStream()); //��� ��Ʈ��
			
			while(true) {
				System.out.println("while ����");
				String str = Breader.readLine(); //�Էµ� ����Ÿ�� �� �� ������ ����
				System.out.println(str); //�Է� Ȯ�ο�
				
				//Ŭ���̾�Ʈ ����
				if(str.equals("bye")) break;
				
				//Pwriter.println(str); //bye�� �ƴϸ� �޼��� ����
				//Pwriter.flush(); //���۳��� ����Ÿ �о��
				
				//������ ����(���� : "RNo���ȣRNa���̸�RPN�ο���")
				if(str.equals("REQUEST_ROOMINFO")) {
					System.out.println("������ if ����");
					for(int i = 0; i < roomInfo.sizeRoom();i++) {
						System.out.println("������ if ������ for ����");
						Pwriter.println("RNo"+(i+1)+"RNa"+roomInfo.nameRoom(i)+"RPN"+roomInfo.peopleNumber(i)+"\r\n");
						Pwriter.flush();
						Thread.sleep(10);;
						System.out.println("������ if ������ for ��");
					}
					Pwriter.println("COMEND");
					Pwriter.flush();
					System.out.println("������ if ��");
				}
				//���� �г��� ����
				else if(str.equals("NICKNAME_")) {
					System.out.println("�����г������� if ����");
					str = str.substring(10, str.length());
					userInfo.addUser(str);
					System.out.println("�����г������� if ��");
				}
				
				//�� ����
				else if(str.equals("")) {
					System.out.println("����� if ����");
					//str = str.substring(10, str.length()); // �Է� ������ �� ����
					roomInfo.addRoom(str);
					int i = roomInfo.searchRoom(str);
					roomInfo.joinRoom(i);
				}
			}
		} catch(Exception e) {
			System.out.println("userthread : "+e.getMessage());
		} finally { // ���� ����� ������ ����
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
