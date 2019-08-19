using System;
using System.Net.Sockets;
using System.Threading;

namespace SimpleChatClient
{
    using SimpleChatClient.Models;
    using System.Collections.ObjectModel;

    /// <summary>
    /// This class create only one instance.
    /// </summary>
    public class NetworkSystem
    {
        #region NetworkSystem members
        private static NetworkSystem instance = null;
        private NetworkStream _Stream;
        public NetworkStream Stream { get; private set; }
        private TcpClient _tcpc = null;
        public TcpClient tcpc { get; private set; }
        private readonly int _PACKET_SIZE = 1024;
        public int PACKET_SIZE { get; }
        private NetworkSystem() { }
        private string _NickName = "";
        public string NickName { get; private set; }
        public static NetworkSystem Instance
        {
            get
            {
                if (instance == null)
                    instance = new NetworkSystem();
                return instance;
            }
        }
        #endregion
        // 네트워크 연결을 수행하는 메소드
        #region NetworkSystem Methods

        #region NetworkSystem Methods on LogOnWindow
        public int Connect(string ipAddr, string nickName)
        {
            int port = 6000;
            NickName= nickName;
            if(tcpc != null)
            {
                if(tcpc.Connected ==true)
                {
                    Console.WriteLine("already Connedted");
                    return 0;
                }
            }
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
            Stream = tcpc.GetStream();
            Console.Write("Send NickName {0} ...", NickName);
            try
            {
                byte[] buff = new byte[1024];
                buff = System.Text.Encoding.ASCII.GetBytes("NICKNAME_"+NickName + "\r\n");
                Stream.Write(buff, 0, buff.Length);
                Thread.Sleep(10);
            }
            catch
            {
                Console.WriteLine("Failed");
                tcpc.Close();
                Stream.Close();
                tcpc = null;
                Stream = null;
                return -1;
            }
            Console.WriteLine("OK");
            return 0;
        }
        #endregion

        #region NetworkSystem Methods on RoomListWindow

        /// <summary>
        /// 채팅 방에 접속하는 메소드. 성공시 0 실패시 -1을 반환한다.
        /// </summary>
        /// <returns></returns>
        public int RequestJoin(int RoomNumber)
        {
            #region 사용할 변수 초기화
            byte[] buff = new byte[1024];
            string Msg = "JOIN_" + NickName+ "_" + RoomNumber.ToString() + "\r\n";
            #endregion
            
            #region 네트워크 통신
            Console.WriteLine("Message : {0}", Msg);
            Console.Write("Send ... ");
            try
            {
                Stream.Write(buff, 0, buff.Length);
                Console.WriteLine("OK");
                /*
                 * 방에대한 정보를 받아오는 자리
                 */
            }
            catch
            {
                Console.WriteLine("Failed");
                return -1;
            }
            #endregion

            return 0;
        }

        #endregion

        #region NetworkSystem Method on ChatWindow
        // 메시지를 전송하는 메소드
        public void SendMsg(string msg)
        {
            Console.Write("Send Message...");
            byte[] buff = new byte[1024];
            buff = System.Text.Encoding.ASCII.GetBytes("%CHAT%&;_%$"+NickName+"&;_%$"+msg+"\r\n");
            Stream.Write(buff,0,buff.Length);
            Console.WriteLine("OK");
        }
        // 메시지를 수신받는 메소드
        public String RecvMsg()
        {             
            byte[] buff = new byte[1024];
            Stream.Read(buff,0,buff.Length);
            //RNu방번호UNa유저닉네임Chat채팅내용
            String Msg = System.Text.Encoding.ASCII.GetString(buff).Trim(new char[] { '\0', '\n', '\r' });
            Console.WriteLine("Receive Message : {0}", Msg);
            return Msg;
        }
        ~NetworkSystem()
        {
            if (tcpc != null)
                tcpc.Close();
            if (Stream != null)
                Stream.Close();
        }
        #endregion

        #endregion
    }

}
