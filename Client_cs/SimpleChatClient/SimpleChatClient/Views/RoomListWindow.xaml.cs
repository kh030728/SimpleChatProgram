namespace SimpleChatClient.Views
{
    using System.Windows;
    using System;
    
    using SimpleChatClient.ViewModels;

    /// <summary>
    /// RoomListWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class RoomListWindow : Window
    {
        /// <summary>
        /// Initializes a new Instance of the RoomListWindow class;
        /// </summary>
        public RoomListWindow(string NickName)
        {
            InitializeComponent();
            RoomListWindowViewModel vm = new RoomListWindowViewModel(NickName);
            DataContext = vm;
            if(vm.CloseAction == null) vm.CloseAction = new Action(this.Close);
            if(vm.OpenChatWindowAction == null)
                vm.OpenChatWindowAction = new Action(() => { ChatWindow chatWindow = new ChatWindow(vm.SelectedRoom.Number); chatWindow.Show(); });
            if (vm.OpenChatWindowActionWithCreate == null)
                vm.OpenChatWindowActionWithCreate = new Action(() => { ChatWindow chatWindow = new ChatWindow(vm.createRoomNumber); chatWindow.Show(); });
        }

        private void Btn_exit_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        
    }
}
