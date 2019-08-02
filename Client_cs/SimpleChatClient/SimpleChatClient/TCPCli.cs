using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.Net;
using System.Windows.Controls;
using System.Windows.Threading;
using System.Windows.Media;

namespace SimpleChatClient
{
    class TCPCli
    {
        private Socket toServerSock = null;
        private string serverAddr="";
        public string ServerAddr
        {
            get
            {
                return serverAddr;
            }
            set
            {
                serverAddr = value;
            }
        }
        public bool NetworkConnect(TextBlock textBlock)
        {
            try
            {
                toServerSock = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                toServerSock.Connect(new IPEndPoint(IPAddress.Parse(serverAddr), 6000));
                //toServerSock.BeginConnect(new IPEndPoint(IPAddress.Parse(serverAddr), 6000), new AsyncCallback(ConnectCallback), toServerSock);
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return false;
            }
            return true;

        }
        public void requestRoomInfo()
        {
            string msg = "REQUEST_ROOMINFO";
            byte[] buff = new byte[1024];
            buff = Encoding.ASCII.GetBytes(msg);
            toServerSock.BeginSend(buff,0, buff.Length, SocketFlags.None, new AsyncCallback(SendCallback), toServerSock);
            byte[] inbuff = new byte[1024];
            toServerSock.Receive(inbuff);
            string print = Encoding.ASCII.GetString(inbuff);

        }
        public void SendMessage(string input)
        {

        }
        private static void SendCallback(IAsyncResult ar)
        {
            try
            {
                Socket client = (Socket)ar.AsyncState;
                int bytesSent = client.EndSend(ar);
                Console.WriteLine("Sent {0} bytes to server.", bytesSent);            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        ~TCPCli()
        {
            Console.WriteLine("소멸자실행");
            try
            {
                toServerSock.Close();
            }
            catch(Exception e)
            {
                Console.WriteLine(e);
            }
        }
    }
}
