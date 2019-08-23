namespace SimpleChatClient
{
    using System;
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
        }

        private void ChatPressEnter(object sender, KeyEventArgs e)
        {
            if(e.Key == Key.Return)
            vm.Chat();
        }
    }
}
