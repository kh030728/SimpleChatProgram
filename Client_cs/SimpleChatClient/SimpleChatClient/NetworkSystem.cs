using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;


namespace SimpleChatClient
{
    // 싱글톤 패턴으로 단하나의 객체를 갖는다.
    public class NetworkSystem
    {
        private static NetworkSystem instance = null;
        private NetworkStream stream;
        private static TcpClient tcpc = null;
        private readonly int PACKET_SIZE = 1024;
        private NetworkSystem() { }
        private string _nickName = "";
        public static NetworkSystem Instance
        {
            get
            {
                if (instance == null)
                    instance = new NetworkSystem();
                return instance;
            }
        }
        // 네트워크 연결을 수행하는 메소드

        /// <summary>
        /// STA가 아닌 다른 스레드에서 수행하세요.
        /// 연결이 된다면 해당 스레드 에서 RequestRoom또한 실행하세요.
        /// 연결에 성공한다면 0을 실패한다면 -1을 반환합니다.
        /// </summary>
        /// <param name="ipAddr"></param>
        /// <param name="nickName"></param>
        /// <returns></returns>
        public int Connect(string ipAddr, string nickName)
        {
            int port = 6000;
            _nickName = nickName;
            Console.WriteLine("NetworkSystem::Connect(string ipAddr,string nickName) Start ...");
            Console.Write("Generating Socket... ");
            try
            {
                tcpc = new TcpClient(ipAddr, port);
            }
            catch
            {
                Console.WriteLine("Failed");
                tcpc = null;
                return -1;
            }
            Console.WriteLine("OK");
            stream = tcpc.GetStream();
            Console.Write("Send NickName {0} ...", _nickName);
            try
            {
                byte[] buff = new byte[1024];
                buff = System.Text.Encoding.Default.GetBytes(_nickName + "\r\n");
                stream.Write(buff, 0, buff.Length);
                Thread.Sleep(10);
            }
            catch
            {
                Console.WriteLine("Failed");
                tcpc.Close();
                stream.Close();
                tcpc = null;
                stream = null;
                return -1;
            }
            Console.WriteLine("OK");
            return 0;
        }
        /// <summary>
        /// 채팅방에 대한 정보를 서버에 요청하여 받아오는 메소드입니다.
        /// </summary>
        /// <param name="input"></param>
        public void RequestRoom(List<Room> input)
        {   
            byte[] buf = new byte[PACKET_SIZE];
            string msg = "REQUEST_ROOMINFO\r\n";
            string inMsg = String.Empty;

            buf = System.Text.Encoding.Default.GetBytes(msg);
            if(tcpc.Connected == false)
            {
                Console.WriteLine("Socket closed. you can't send a message.");
                return;
            }
            try
            {
                Console.WriteLine("Socket Connected : {0}\nC >> {1}", tcpc.Connected ,msg);
                stream.Write(buf, 0, buf.Length);
            }
            catch (Exception e)
            {
                Console.WriteLine("Send Failed\n{0}",e);
            }
            Thread.Sleep(10);
            // recv 구현부
            while(true)
            {
                if (tcpc.Connected == false)
                {
                    Console.WriteLine("Socket closed. you can't send a message.");
                    return;
                }
                byte[] inbuf = new byte[PACKET_SIZE];
                int recvByteCount = 0;
                try
                {
                    recvByteCount = stream.Read(inbuf, 0, inbuf.Length);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Receive Failed\n{0}", e);
                }
                if (recvByteCount > 0)
                {
                    inMsg = System.Text.Encoding.Default.GetString(inbuf).Trim(new char[] { '\0', '\n', '\r' });
                    if (inMsg == "COMEND")
                        break;
                    string[] seperator = { "RNo", "RNa", "RPN" };
                    string[] array = inMsg.Split(seperator, StringSplitOptions.RemoveEmptyEntries);
                    input.Add(new Room(array[1], int.Parse(array[0]), int.Parse(array[2])));
                }
            }
            foreach(Room elem in input)
            {
                Console.WriteLine("방번호 {0} 이름 {1} 인원수 {2}", elem.ID, elem.NAME, elem.PEOPLE);
            }
            Console.WriteLine("End");
        }
        // 방에 접속을 요청하는 메소드
        public void RequestJoin()
        {

        }
        // 메시지를 전송하는 메소드
        public void SendMsg()
        {

        }
        // 메시지를 수신받는 메소드
        public void RecvMsg()
        {

        }
        ~NetworkSystem()
        {
            if (tcpc != null)
                tcpc.Close();
            if (stream != null)
                stream.Close();
        }

    }
    
}
