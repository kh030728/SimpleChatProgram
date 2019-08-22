using System.Windows;
using System.Windows.Input;

namespace SimpleChatClient
{
    /// <summary>
    /// ChatWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class ChatWindow : Window
    {
        public ChatWindow(int RoomNumber)
        { 
            InitializeComponent();
            ViewModels.ChatWindowViewModel vm = new ViewModels.ChatWindowViewModel();
            DataContext = vm;
        }

    }
}
