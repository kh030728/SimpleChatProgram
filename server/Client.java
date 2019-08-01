package chattingProgram;

import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket("127.0.0.1", 4098);
			Thread th1 = new SenderThread(socket);
			Thread th2 = new ReceiverThread(socket);
			th1.start();
			th2.start();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}