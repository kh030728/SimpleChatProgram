
namespace SimpleChatClient.Views
{
    using System.Windows;
    using System;

    using SimpleChatClient.ViewModels;

    public partial class LogOnWindow : Window
    {
        public LogOnWindow()
        {
            InitializeComponent();
            LogOnWindowViewModel vm = new LogOnWindowViewModel();
            DataContext = vm;
            if (vm.CloseAction == null)
                vm.CloseAction = new Action(this.Close);
            if (vm.OpenRoomListWindowAction == null)
                vm.OpenRoomListWindowAction = new Action(()=> { RoomListWindow roomListWindow = new RoomListWindow(vm.TextData.NickName);  roomListWindow.Show(); });
        }
    }
}
