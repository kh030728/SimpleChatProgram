namespace SimpleChatClient.ViewModels
{
    using System;
    using System.Threading;
    using System.Windows;
    using System.Windows.Input;
    using System.Windows.Threading;

    using SimpleChatClient.Models;

    class ChatWindowViewModel
    {
        #region variables
        private Models.ChatUsers _Users;
        /// <summary>
        /// variable to be connected the list of Users.
        /// </summary>
        public Models.ChatUsers Users { get { return _Users; } }
        /// <summary>
        /// A dispatcher about main thread
        /// </summary>
        public Dispatcher STAthread { get; set; }

        public int RoomNumber { get; set; }

        public ChatMessage chatMessage { get; private set;}
        private ChatLogs _chatLogs;
        /// <summary>
        /// variable to be connected the ChatScroll view.
        /// </summary>
        public ChatLogs chatLogs { get { return _chatLogs; } private set { _chatLogs = value; } }
        #endregion

        /// <summary>
        /// The Command class for ChatButton
        /// </summary>
        public ICommand ChatButton { get; private set; }

        #region Actions
        public Action<string, string> ChatLogAdd { get; set; }
        public Action ChatMessageClear { get; set; }
        public Action ScrolltoBottom { get; set; }
        #endregion

        /// <summary>
        /// Method to perform the read task from a server;
        /// </summary>
        private void ReadThread()
        {
            Console.WriteLine("ReadThread :: Start");
            while (true)
            {
                byte[] buff = new byte[1024];
                int recvBytes = NetworkSystem.Instance.Stream.Read(buff, 0, buff.Length);
                string msg = System.Text.Encoding.UTF8.GetString(buff).Trim(new char[] { '\n', '\r', '\0' });
                Console.WriteLine("ChatWindowViewModel:: ReadThread :: A received message : {0}", msg);
                if (msg.Contains("USERS%$%"))
                {
                    string[] userNameArray = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                    for (int i = 1; i < userNameArray.Length; i++)
                    {
                        Console.WriteLine("ChatWindowViewModel:: ReadThread :: {0}", userNameArray[i]);
                        Users.Add(new User(userNameArray[i]));
                    }
                }
                else if (msg.Contains("SEND_CHAT%$%"))
                {
                    try
                    {
                        string[] userNameArray = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                        chatLogs.Add(new ChatLog(userNameArray[2], userNameArray[3]));
                        STAthread.Invoke(ScrolltoBottom);
                        Console.WriteLine("ChatWindowViewModel:: ReadThread :: To add a chat Message have been Successed");
                    }
                    catch(Exception e)
                    {
                        Console.WriteLine("ChatWindowViewModel:: ReadThread :: To add a chat Message have been Failed\n {0}",e);
                    }
                }
            }
        }

        /// <summary>
        /// Sends chat message to a server;
        /// </summary>
        public void Chat()
        {
            byte[] buff = new byte[1024];
            if(chatMessage.Message =="")
            {
                Console.WriteLine("Please input a message.");
                return;
            }
            if (chatMessage.Message.Contains("%$%"))
            {
                MessageBox.Show("문자열 \"%$%\"을 포함할 수 없습니다.", "입력 제한", MessageBoxButton.OK, MessageBoxImage.Asterisk);
                return;
            }
            try
            {
                buff = System.Text.Encoding.UTF8.GetBytes("SEND_CHAT%$%"+RoomNumber+"%$%"+NetworkSystem.Instance.NickName+"%$%"+chatMessage.Message+"\r\n");
                NetworkSystem.Instance.Stream.Write(buff, 0, buff.Length);
                Console.WriteLine("A sending process have been completed.");
                ChatMessageClear();

            }
            catch
            {
                Console.WriteLine("A sending Process have been failed.");
            }
        }
        /// <summary>
        /// Initializes a new instance of the ChatWindowViewModel class;
        /// </summary>
        public ChatWindowViewModel()
        {
            Console.WriteLine("ChatWindowViewModel :: start");
            #region Initializes variables
            // Initializes the command for chatButton.
            ChatButton = new SimpleChatClient.Commands.ChatButton(this);
            chatMessage = new ChatMessage();
            chatLogs = new ChatLogs();
            _Users = new ChatUsers();
            STAthread = Dispatcher.CurrentDispatcher;
            Thread readThread = new Thread(new ThreadStart(ReadThread));
            readThread.IsBackground = true;
            readThread.ApartmentState = ApartmentState.STA;
            #endregion
            readThread.Start();
            Thread.Sleep(50);
            #region Send READY_FOR_JOIN to the server
            byte[] buff = new byte[1024];
            buff = System.Text.Encoding.UTF8.GetBytes("READY_FOR_JOIN\r\n");
            NetworkSystem.Instance.Stream.Write(buff, 0, buff.Length);
            #endregion
            Console.WriteLine("ChatWindowViewModel :: send Ready for join");


        }

    }
}
