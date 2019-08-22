using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SimpleChatClient.ViewModels
{
    using System.Threading;

    class ChatWindowViewModel
    {
        private Models.ChatUsers _Users;
        public Models.ChatUsers Users
        {
            get
            {
                if (_Users == null)
                { _Users = new Models.ChatUsers(); }
                return _Users;
            }
        }
        private void ReadThread()
        {
            Console.WriteLine("ReadThread :: Start");
            while (true)
            {
                byte[] buff = new byte[1024];
                int recvBytes = NetworkSystem.Instance.Stream.Read(buff, 0, buff.Length);
                string msg = System.Text.Encoding.UTF8.GetString(buff).Trim(new char[] { '\n', '\r', '\0' });
                Console.WriteLine("ReadThread :: A received message : {0}", msg);
                if (msg.Contains("USERS%$%"))
                {
                    string[] userNameArray = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                    for(int i = 1; i < userNameArray.Length; i++)
                    {
                        Console.WriteLine("User : {0}", userNameArray[i]);
                    }
                }
            }
        }
        public ChatWindowViewModel()
        {
            Thread readThread = new Thread(new ThreadStart(ReadThread));
            readThread.IsBackground = true;
            readThread.Start();
            Thread.Sleep(50);
            byte[] buff = new byte[1024];
            buff = System.Text.Encoding.UTF8.GetBytes("READY_FOR_JOIN\r\n");
            NetworkSystem.Instance.Stream.Write(buff, 0, buff.Length);


        }

    }
}
