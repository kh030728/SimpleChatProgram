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
        public Thread readThread;
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
        public ICommand SendChatButtonCommand { get; private set; }
        public ICommand SendChatKeyDownCommand { get; private set; }

        #region Actions
        public Action<string, string> ChatLogAdd { get; set; }
        public Action ChatMessageClear { get; set; }
        public Action ScrolltoBottom { get; set; }
        public Action ShowRoomListWindow { get; set; }
        public Action CloseWindow { get; set; }
        #endregion

        /// <summary>
        /// Method to perform the read task from a server;
        /// </summary>
        private void ReadThread()
        {
            Console.WriteLine("ReadThread :: Start");
            try
            {
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
                            chatLogs.Add(new ChatLog(userNameArray[1], userNameArray[2]));
                            STAthread.Invoke(ScrolltoBottom);
                            Console.WriteLine("ChatWindowViewModel:: ReadThread :: To add a chat Message have been Successed");
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine("ChatWindowViewModel:: ReadThread :: To add a chat Message have been Failed\n {0}", e);
                        }
                    }
                    else if (msg.Contains("JOIN_USER%$%"))
                    {
                        string[] joinUser = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                        for(int i = 0; i < Users.Count; i++)
                        {
                            if (Users[i].NickName == joinUser[1])
                            {
                                Console.WriteLine("ChatWindowViewModel:: ReadThread :: This user already joined");
                                return;
                            }
                        }
                        Users.Add(new User(joinUser[1]));
                        Console.WriteLine("ChatWindowViewModel:: ReadThread :: Join User ( {0} )", joinUser[1]);
                    }
                    else if (msg.Contains("OUT_USER%$%"))
                    {
                        string[] outUser = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                        Users.remove(outUser[1]);
                        Console.WriteLine("ChatWindowViewModel:: ReadThread :: Out User ( {0} )", outUser[1]);
                    }
                    else if(msg.Contains("OUT_ROOM_OK"))
                    {
                        Console.WriteLine("ChatWindowViewModel:: ReadThread :: OUT OK");
                        STAthread.Invoke(ShowRoomListWindow);
                        STAthread.Invoke(CloseWindow);
                        return;
                    }
                }
            }
            catch
            {
                Console.WriteLine("ChatWindowViewModel:: ReadThread :: end");
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
                buff = System.Text.Encoding.UTF8.GetBytes("SEND_CHAT%$%"+chatMessage.Message+"\r\n");
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
            SendChatButtonCommand = new SimpleChatClient.Commands.SendChatButtonCommand(this);
            SendChatKeyDownCommand = new SimpleChatClient.Commands.SendChatKeyDownCommand(this);
            chatMessage = new ChatMessage();
            chatLogs = new ChatLogs();
            _Users = new ChatUsers();
            STAthread = Dispatcher.CurrentDispatcher;
            readThread = new Thread(new ThreadStart(ReadThread));
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
