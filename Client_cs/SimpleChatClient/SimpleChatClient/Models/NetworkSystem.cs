using System;
using System.Net.Sockets;

namespace SimpleChatClient
{
    /// <summary>
    /// This class create only one instance.
    /// </summary>
    public class NetworkSystem
    {
        private static NetworkSystem instance = null;
        private NetworkSystem() { Console.WriteLine("networkSystem 생성자실행"); }
        public static NetworkSystem Instance
        {
            get
            {
                if (instance == null)
                    instance = new NetworkSystem();
                return instance;
            }
        }

        public NetworkStream Stream { get; private set; }
        private TcpClient _tcpc;
        public int joiningRoomNumber { get; set; }
        public TcpClient tcpc
        {
            get { return _tcpc; }
            set
            {
                if (_tcpc == null)
                {
                    _tcpc = value;
                    Stream = tcpc.GetStream();
                }
            }
        }
        public int PACKET_SIZE { get; }
        public string NickName { get; set; }
       
        ~NetworkSystem()
        {
            if (tcpc != null)
            {
                tcpc.Close();
            }
            if (Stream != null)
            {
                Stream.Close();
            }
        }
    }

}
