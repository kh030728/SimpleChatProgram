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
    class NetworkSystem
    {
        private static NetworkSystem instance = null;
        private NetworkStream stream;
        private static TcpClient tcpc = null;
        private readonly int PACKET_SIZE = 1024;
        private bool netWorkConnectionStaust = false;
        public bool NetWorkConnectionStatus
        {
            get
            {
                return netWorkConnectionStaust;
            }
        }
        private bool connectThreadFinishFlag = false;
        public bool ConnectThreadFinishFlag
        {
            get
            {
                return connectThreadFinishFlag;
            }
        }
        private NetworkSystem() { }
        private string _ipAddr = "";
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
        public void Connect(string ipAddr, string nickName)
        {
            if (tcpc != null)
                return;
            connectThreadFinishFlag = false;
            _ipAddr = ipAddr;
            _nickName = nickName;

            Thread connectThread = new Thread(ConnectThread);
            connectThread.Start();
            return;
        }
        private void ConnectThread()
        {
            int port = 6000;
            Console.WriteLine("Start ConnectThread...\n IP Address : {0} Port : {1} NickName : {2}",_ipAddr,port,_nickName);
            try
            {
                tcpc = new TcpClient(_ipAddr, port);
            }
            catch
            {
                Console.WriteLine("Failed");
                return;
            }
            finally
            {
                connectThreadFinishFlag = true;
            }
            Console.WriteLine("Successed");
            netWorkConnectionStaust = true;
            stream = tcpc.GetStream();
            return;
        }
        // 방정보를 요청하는 메소드
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
                try
                {
                    stream.Read(inbuf, 0, inbuf.Length);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Receive Failed\n{0}", e);
                }
                inMsg = System.Text.Encoding.Default.GetString(inbuf).Trim(new char[] { '\0', '\n', '\r' });
                if (inMsg == "GoodBye")
                    break;
                string[] seperator = { "RNo", "RNa", "RPN" };
                string[] array = inMsg.Split(seperator, StringSplitOptions.RemoveEmptyEntries);
                input.Add(new Room(array[1],int.Parse(array[0]),int.Parse(array[2])));
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
