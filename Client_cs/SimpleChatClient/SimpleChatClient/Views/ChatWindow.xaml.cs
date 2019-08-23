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
        }

        private void ChatPressEnter(object sender, KeyEventArgs e)
        {
            if(e.Key == Key.Return)
            vm.Chat();
        }

        private void CloseWindow(object sender, RoutedEventArgs e)
        {
            vm.readThread.Join();
            vm.ShowRoomListWindow();
            this.Close();
        }
    }
}
