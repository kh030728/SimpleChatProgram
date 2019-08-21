
namespace SimpleChatClient.Views
{
    using System.Windows;

    using SimpleChatClient.Models;
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
        }

        private void Btn_exit_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        
        private void Btn_joinRoom_Click(object sender, RoutedEventArgs e)
        {
            /*
            Room room = (Room)RoomListView.SelectedItem;
            if (room == null)
                Console.WriteLine("No Select");
            else
            {
                Console.WriteLine("the room number of Selected item : ", room.ID);
                if(ns.RequestJoin(room.ID) < 0 )
                { // 실패의 경우
                    MessageBox.Show("해당 방에 접속하는 것이 실패하였습니다.", "접속 실패", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
            */
        }
    }
}
