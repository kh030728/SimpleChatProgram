namespace SimpleChatClient.ViewModels
{
    using System.Collections.ObjectModel;
    using System.Threading;
    using System.Windows.Input;
    using System;

    using SimpleChatClient.Models;
    using System.Windows.Threading;

    internal class RoomListWindowViewModel
    {
        #region Define valuables
        private readonly RoomItemHandler _roomItemHandler;
        public Collection<Room> Items { get { return _roomItemHandler; } }
        private Thread Readthread;
        public string NickName { get; set; }
        public Room SelectedRoom { get; set; }
        #endregion
        /// <summary>
        /// Initializes a new instance of the RoomListWinodwViewModel class;
        /// </summary>
        public RoomListWindowViewModel(string nickName)
        {
            Console.Write("RoomListWindowViewModel 생성자 실행");
            #region Initialize Models
            NickName = nickName; // 사용자의 별칭 저장 및 UI에 반영
            _roomItemHandler = new RoomItemHandler(); // 방 목록에 정보가 갱신될 때에 처리를 위한 Handler
            #endregion
            #region Initialize Commands
            RefreshCommand = new SimpleChatClient.Commands.RefreshCommand(this); // 새로고침 커맨드
            CreateCommand = new SimpleChatClient.Commands.CreateCommand(this); // 방 생성 커맨드
            JoinCommand = new SimpleChatClient.Commands.JoinCommand(this); // 방 참여 커맨드
            #endregion
            #region Readthread Define
            Readthread = new Thread( () =>
            {
                NetworkSystem ns = NetworkSystem.Instance;
                while (true)
                {
                    byte[] readOnlyBuff = new byte[1024];
                    try
                    {
                        int recvBytes = ns.Stream.Read(readOnlyBuff, 0, readOnlyBuff.Length);
                        if (recvBytes == 0)
                        {
                            Console.WriteLine("ReadThread :: a Message is empty ");
                            continue;
                        }
                        else if (recvBytes < 0)
                        {
                            Console.WriteLine("ReadThread :: Socket is close");
                        }
                        Console.WriteLine("ReadThread :: a Message arrived ");
                    }
                    catch { Console.WriteLine("ReadThread :: An error was occured in running the Read method");
                    }
                    string msg = System.Text.Encoding.UTF8.GetString(readOnlyBuff).Trim(new char[] { '\n', '\r', '\0' });
                    if (msg.Contains("NOTIFY_ADD_ROOM%$%"))
                    {
                        string[] room = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                        Console.WriteLine("ReadThread :: Notify_Add_Room func");
                        try
                        {
                            Console.WriteLine("메시지확인 {0}", msg);
                            Console.WriteLine("방번호 : {0} 방이름 {1} 방인원수 {2}", room[1], room[2], room[3]);
                            try
                            {
                                int num = int.Parse(room[1]);
                                int peo = int.Parse(room[3]);
                                _roomItemHandler.Add(num,room[2],peo);
                            }catch(Exception e) { Console.WriteLine(e); }
                        }
                        catch
                        {
                            Console.WriteLine("ReadThread :: A task to add an item in list was failed.");
                            continue;
                        }
                        Console.WriteLine("ReadThread :: A room was added in a list");
                    }
                    else if (msg.Contains("NOTIFY_REMOVE_ROOM%$%"))
                    {
                        string[] room = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                        try
                        {
                            _roomItemHandler.RemoveRoom(int.Parse(room[1]));
                            Console.WriteLine("ReadThread :: The item was removed in list.");
                        }
                        catch
                        {
                            Console.WriteLine("ReadThread :: A task to remove an item in list was failed.");
                            continue;
                        }
                    }
                    else if (msg.Contains("NOTIFY_CHANGE_ROOM%$%"))
                    {
                        string [] room = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                        try
                        {
                            _roomItemHandler.ChangeRoom(int.Parse(room[1]),int.Parse(room[2]));
                            Console.WriteLine("ReadThread :: The item was removed in list.");
                        }
                        catch
                        {
                            Console.WriteLine("ReadThread :: A task to revise an item in list was failed.");
                            continue;
                        }
                    }
                    else
                    {
                        Console.WriteLine("ReadThread :: This message is invaild : {0}", msg);
                    }
                }
            });
            #endregion
            Readthread.Start();
            DownloadRoomData(); // 방목록을 받아옴
            Console.WriteLine("-----> 완료");
        }
        #region Define Commands
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
        #endregion
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
            buf = System.Text.Encoding.UTF8.GetBytes("REQUEST_ROOMINFO\r\n");
            #endregion
            #region Checks Network Connection Status
            if (networkSystem.tcpc.Connected == false)
            {
                Console.WriteLine("Socket closed. you can't send a message.");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            if (networkSystem.Stream == null)
            {
                Console.WriteLine("NetworkStream is null. ");
                Console.WriteLine("-------------------------------------------------");
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
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            Console.WriteLine("REQUEST_ROOMINFO Send Success");
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
            {
                Console.WriteLine("NeworkStream is null");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            if (!networkSystem.tcpc.Connected)
            {
                Console.WriteLine("Socket is not connected");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            #region Send Message for a new Room
            try
            {
                byte[] buf = new byte[1024];
                buf = System.Text.Encoding.UTF8.GetBytes("REQUEST_CREATE_ROOM%$%" + roomName + "%$%" + NickName + "\r\n");
                networkSystem.Stream.Write(buf, 0, buf.Length);
                Console.WriteLine("a message was send");
            }
            catch
            {
                Console.WriteLine("방생성 메시지 송신 오류");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            #endregion
            /*
            #region Receive Message for a confirm of Request
            byte[] buff = new byte[1024];
            try
            {
                networkSystem.Stream.Read(buff, 0, buff.Length);
                Msg = System.Text.Encoding.UTF8.GetString(buff).Trim(new char[] { '\0', '\n', '\r' });
                Console.WriteLine("The received Message : {0}", Msg);
            }
            catch
            {
                Console.WriteLine("방생성 메시지 수신 오류");
                Console.WriteLine("-------------------------------------------------");
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
            #endregion
            Console.WriteLine("-------------------------------------------------");
            */
        }
        #endregion
        #region Methods for the JoinCommand class
        public void JoinRoom()
        {

            Console.WriteLine("=================================================");
            Console.WriteLine("Start RoomListWindowViewModel::JoinRoom(void)"); 
            if (SelectedRoom == null)
            {
                Console.WriteLine("Selected Item is null");
                return;
            }
            Readthread.Abort();
            NetworkSystem ns = NetworkSystem.Instance;
            byte[] buff = new byte[1024];
            Console.WriteLine("REQUEST_JOIN_ROOM%$%" + ns.NickName + "%$%" + SelectedRoom.Number);
            buff = System.Text.Encoding.UTF8.GetBytes("REQUEST_JOIN_ROOM%$%" + ns.NickName + "%$%" + SelectedRoom.Number);
            try
            {
                ns.Stream.Write(buff, 0, buff.Length);
                Console.WriteLine("전송완료");
            }
            catch
            {
                Console.WriteLine("전송실패");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            buff = new byte[1024];
            try
            {
                ns.Stream.Read(buff, 0, buff.Length);
                Console.WriteLine("수신완료");
            }
            catch
            {
                Console.WriteLine("수신실패");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            string msg = System.Text.Encoding.UTF8.GetString(buff);
            if (msg.Contains("USERS%$%"))
            {
                string[] user = msg.Split(new string[] { "%$%" }, StringSplitOptions.RemoveEmptyEntries);
                foreach (string elem in user)
                {
                    Console.WriteLine("참여자 : {0}", elem);
                }
            }
            
            Console.WriteLine("-------------------------------------------------");
        }
        #endregion
    }
}
