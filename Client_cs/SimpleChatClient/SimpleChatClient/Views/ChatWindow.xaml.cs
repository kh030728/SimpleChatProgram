namespace SimpleChatClient
{
    using SimpleChatClient.Views;
    using System;
    using System.Threading;
    using System.Windows;
    using System.Windows.Input;

    /// <summary>
    /// ChatWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class ChatWindow : Window
    {
        ViewModels.ChatWindowViewModel vm;
        public ChatWindow(int RoomNumber)
        { 
            InitializeComponent();
            vm = new ViewModels.ChatWindowViewModel();
            DataContext = vm;
            vm.RoomNumber = RoomNumber;
            if (vm.ChatLogAdd == null)
                vm.ChatLogAdd = new System.Action<string,string>((nickname,context) =>
                {
                    Console.WriteLine(nickname, context);
                });
            if (vm.ChatMessageClear == null)
                vm.ChatMessageClear = new Action( ()=> { ChatMessage_TextBox.Text =""; });
            if (vm.ScrolltoBottom == null)
                vm.ScrolltoBottom = new Action(() => { chatScroll.ScrollToBottom(); });
            if(vm.ShowRoomListWindow == null)
                vm.ShowRoomListWindow = new Action(() => { RoomListWindow roomListWindow = new RoomListWindow(NetworkSystem.Instance.NickName); roomListWindow.Show(); });
            if (vm.CloseWindow == null)
                vm.CloseWindow = new Action(() =>
                {
                    this.Close();
                });
        }

        private void ChatPressEnter(object sender, KeyEventArgs e)
        {
            if(e.Key == Key.Return)
            vm.Chat();
        }

        private void CloseWindow(object sender, RoutedEventArgs e)
        {
            byte[] buff = new byte[1024];
            buff = System.Text.Encoding.UTF8.GetBytes("REQUEST_OUT_ROOM%$%" + NetworkSystem.Instance.NickName + "\r\n");
            NetworkSystem.Instance.Stream.Write(buff, 0, buff.Length);
            Console.WriteLine("Click close window");
        }
    }
}
