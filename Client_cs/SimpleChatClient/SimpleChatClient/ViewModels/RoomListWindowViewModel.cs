namespace SimpleChatClient.ViewModels
{
    using System.Windows.Input;
    using System;
    
    using SimpleChatClient.Models;
    using System.Collections.ObjectModel;

    internal class RoomListWindowViewModel
    {
        private readonly RoomItemHandler _roomItemHandler;
        public Collection<Room> Items { get { return _roomItemHandler; } }
        /// <summary>
        /// Initializes a new instance of the RoomListWinodwViewModel class;
        /// </summary>
        public RoomListWindowViewModel()
        {
            _roomItemHandler = new RoomItemHandler();
            RefreshCommand = new SimpleChatClient.Commands.RefreshCommand(this);
            CreateCommand = new SimpleChatClient.Commands.CreateCommand(this);
            JoinCommand = new SimpleChatClient.Commands.JoinCommand(this);
            DownloadRoomData();
        }
        /// <summary>
        /// Gets or set the RoomListWindowViewModel's RefreshCommand
        /// </summary>
        public ICommand RefreshCommand { get; private set; }
        /// <summary>
        /// Gets or set the RoomListWindowViewModel's CreateCommand
        /// </summary>
        public ICommand CreateCommand { get; private set; }
        /// <summary>
        /// Gets or set the RoomListWindowViewModel's JoinCommand
        /// </summary>
        public ICommand JoinCommand { get; private set; }

        #region Methods for the RefreshCommand class
        /// <summary>
        /// Gets or sets a System.Boolean value indicating whether the RoomList Can be Update;
        /// </summary>
        public bool CanListUpdate
        {
            get
            {
                return true;
            }
        }
        /// <summary>
        /// Gets the RefreshCommand for the ViewModel;
        /// </summary>
        /// <summary>
        /// 방 데이터를 새로 고침하는데 사용되는 메소드
        /// </summary>
        public void DownloadRoomData()
        {
            #region Initializes valuables
            Console.WriteLine("=================================================");
            Console.WriteLine("Start RoomListWindowViewModel::DownloadRoomData(void)");
            NetworkSystem networkSystem = NetworkSystem.Instance;
            _roomItemHandler.Clear();
            byte[] buf = new byte[1024];
            buf = System.Text.Encoding.Default.GetBytes("REQUEST_ROOMINFO\r\n");
            #endregion
            #region Checks Network Connection Status
            if (networkSystem.tcpc.Connected == false)
            {
                Console.WriteLine("Socket closed. you can't send a message.");
                return;
            }
            if (networkSystem.Stream == null)
            {
                Console.WriteLine("NetworkStream is null. ");
                return;
            }
            #endregion
            #region Send "REQUEST_ROOMINFO" Message
            try
            {
                Console.WriteLine("Send Message ... ");
                networkSystem.Stream.Write(buf, 0, buf.Length);
            }
            catch (Exception e)
            {
                Console.WriteLine("Send Failed\n{0}", e);
                return;
            }
            Console.WriteLine("Send Success");
            Console.WriteLine("=================================================");
            #endregion
            #region Downloads data of Room;
            Console.WriteLine("Downloading data of Room ... ");
            
            while (true)
            {
                int recvByteCount = 0;
                try
                {
                    buf = new byte[1024];
                    recvByteCount = networkSystem.Stream.Read(buf, 0, buf.Length);
                    Console.WriteLine("The Received bytes {0}", recvByteCount);
                }
                catch (Exception e)
                {
                    Console.WriteLine("Receive Failed\n{0}", e);
                    return;
                }
                if (recvByteCount > 0)
                {
                    string inMsg = String.Empty;
                    inMsg = System.Text.Encoding.Default.GetString(buf).Trim(new char[] { '\0', '\n', '\r' });
                    Console.WriteLine("Message : {0}",inMsg);
                    if (inMsg == "COMEND")
                        break;
                    string[] seperator = { "RNo", "RNa", "RPN" };
                    string[] array = inMsg.Split(seperator, StringSplitOptions.RemoveEmptyEntries);
                    if (array.Length != 3)
                        break;
                    _roomItemHandler.Add(new Room(array[1], int.Parse(array[0]), int.Parse(array[2])));
                }
                else
                {
                    break;
                }
            }
            Console.WriteLine("OK");
            #endregion
            #region Prints data
            foreach (Room elem in _roomItemHandler)
            {
                Console.WriteLine("방번호 {0} 이름 {1} 인원수 {2}", elem.Number, elem.Name, elem.NumberOfPeople);
            }
            Console.WriteLine("End");
            #endregion
        }
        #endregion
        #region Methods for the CreateCommand class
        public void CreateRoom(string roomName)
        {
            Console.WriteLine("=================================================");
            Console.WriteLine("Start RoomListWindowViewModel::CreateRoom(string)");
            NetworkSystem networkSystem = NetworkSystem.Instance;
            string Msg;
            if (networkSystem.Stream == null)
                return;
            if (!networkSystem.tcpc.Connected)
                return;
            #region Send Message for a new Room
            try
            {
                byte[] buf = new byte[1024];
                buf = System.Text.Encoding.Default.GetBytes("REQUEST_CREATE_ROOM_" + roomName + "\r\n");
                networkSystem.Stream.Write(buf, 0, buf.Length);
            }
            catch
            {
                Console.WriteLine("방생성 메시지 송신 오류");
                return;
            }
            #endregion
            #region Receive Message for a confirm of Request
            byte[] buff = new byte[1024];
            try
            {
                networkSystem.Stream.Read(buff, 0, buff.Length);
                Msg = System.Text.Encoding.Default.GetString(buff).Trim(new char[] { '\0', '\n', '\r' });
                Console.WriteLine("The received Message : {0}", Msg);
            }
            catch
            {
                Console.WriteLine("방생성 메시지 수신 오류");
                return;
            }
            #endregion
            #region Check the message
            if (Msg == "SUCCESS_CREATE_ROOM")
            {
                Console.WriteLine("The request has successed");
            }
            else
            {
                Console.WriteLine("The request has failed");
            }
            DownloadRoomData();
            #endregion
            Console.WriteLine("=================================================");
        }
        #endregion
        #region Methods for the JoinCommand class

        #endregion
    }
}
