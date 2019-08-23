namespace SimpleChatClient.ViewModels
{
    using System;
    using System.ComponentModel;
    using System.Windows;
    using System.Windows.Input;
    using System.Net.Sockets;
    using System.Threading;

    using SimpleChatClient.Models;

    internal class LogOnWindowViewModel
    {
        public LogOnWindowViewModel()
        {
            textData = MyInfo.Instance;
            ConnectCommand = new SimpleChatClient.Commands.ConnectCommand(this);
        }
        private MyInfo textData;

        public MyInfo TextData
        {
            get
            {
                return textData;
            }
        }

        public void Connect()
        {
            Console.WriteLine("=================================================");
            Console.WriteLine("NickName : {0} IP Address : {1}", TextData.NickName, TextData.Address);
            if (TextData.NickName.Contains("%$%"))
            {
                MessageBox.Show("\"%$%\"와 동일한 연속된 문자열은 사용할 수 없습니다.", "사용할 수 없는 문자열", MessageBoxButton.OK, MessageBoxImage.Error);
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            NetworkSystem networkSystem = NetworkSystem.Instance;
            #region Connecting part
            try
            {
                TcpClient tmp = new TcpClient(TextData.Address, 6000);
                if (!tmp.Connected)  // 연결되지 않으면 종료
                    return;
                networkSystem.tcpc = tmp;
            }
            catch
            {
                MessageBox.Show("서버와 연결할 수 없습니다.\n주소를 확인해주세요.", "연결 오류", MessageBoxButton.OK, MessageBoxImage.Error);
                Console.WriteLine("Socket Creating failed");
                Console.WriteLine("-------------------------------------------------");
                return;
            }
            Console.WriteLine("Socket Creating successed");
            #endregion
            #region Send NickName Message
            try
            {
                byte[] buff = new byte[1024];
                buff = System.Text.Encoding.UTF8.GetBytes("NICKNAME%$%" + TextData.NickName + "\r\n");
                networkSystem.Stream.Write(buff, 0, buff.Length);
            }
            catch
            {
                MessageBox.Show("서버와 연결할 수 없습니다.\n주소를 확인해주세요.", "연결 오류", MessageBoxButton.OK, MessageBoxImage.Error);
                Console.WriteLine("A message send failed");
                Console.WriteLine("-------------------------------------------------");
            }
            Console.WriteLine("A message send Successed");
            //try catch안해야지...
            byte[] inbuf = new byte[1024];
            networkSystem.Stream.Read(inbuf, 0, inbuf.Length);
            string msg = System.Text.Encoding.UTF8.GetString(inbuf).Trim(new char[] { '\n', '\0', '\r' });
            if (msg != "NICKNAME_OK")
            {
                networkSystem.tcpc = null;
                networkSystem.NickName = null;
                return;
            }
            #endregion
            networkSystem.NickName = TextData.NickName;
            Console.WriteLine("Connection Successed, NickName : {0}", networkSystem.NickName);
            OpenRoomListWindowAction();
            Thread.Sleep(10);
            CloseAction();

            Console.WriteLine("-------------------------------------------------");
        }
        public Action OpenRoomListWindowAction { get; set; }
        public Action CloseAction { get; set; }

        public ICommand ConnectCommand { get; private set; }
    }

}
