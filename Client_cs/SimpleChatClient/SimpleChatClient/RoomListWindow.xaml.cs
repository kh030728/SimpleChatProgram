using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace SimpleChatClient
{
    /// <summary>
    /// RoomListWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class RoomListWindow : Window
    {
        private List<Room> rooms;
        private NetworkSystem ns;
        public RoomListWindow(List<Room> input)
        {
            rooms = input;
            ns = NetworkSystem.Instance;
            InitializeComponent();
            RoomListView.ItemsSource = rooms;
            TB_NickName.Text = ns.NICKNAME;
        }
        public RoomListWindow()
        {
            InitializeComponent();
        }



        private void Btn_exit_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        private void Btn_refresh_Click(object sender, RoutedEventArgs e)
        {
            btn_refresh.IsEnabled = false;
            btn_createRoom.IsEnabled = false;
            Console.WriteLine("Click refresh button");
            Task refreshTask = new Task(() =>
            {
                List<Room> tmp = new List<Room>();
                if(ns.RequestRoom(tmp) < 0)
                { // 실패한경우
                    btn_refresh.IsEnabled = true;
                    btn_createRoom.IsEnabled = true;
                    return;
                }
                else
                {
                    rooms = tmp;
                    RoomListView.Items.Refresh();
                }
                btn_refresh.IsEnabled = true;
                btn_createRoom.IsEnabled = true;
            });
            refreshTask.Start();
        }

        private void Btn_createRoom_Click(object sender, RoutedEventArgs e)
        {
            CreateRoomDialog createRoomDialog = new CreateRoomDialog();
            if(createRoomDialog.ShowDialog() == true)
            {
                Console.WriteLine("True");
                Console.WriteLine("Room Name : {0}",CreateRoomDialog.ReturnValue);
                ns.RequestCreate(CreateRoomDialog.ReturnValue);
            }
            else
            {
                Console.WriteLine("false");
            }
            
        }
        
    }
}
