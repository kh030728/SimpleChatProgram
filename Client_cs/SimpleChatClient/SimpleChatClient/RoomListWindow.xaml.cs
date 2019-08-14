using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using System.Windows;

namespace SimpleChatClient
{
    /// <summary>
    /// RoomListWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    /// 
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
            if (ns.RequestRoom(rooms) < 0)
            { // 실패한경우
                RoomListView.Items.Refresh();
                Console.WriteLine("Failed");
                return;
            }
            else
            {
                RoomListView.Items.Refresh();
            }
            Console.WriteLine("OK");
            btn_refresh.IsEnabled = true;
            btn_createRoom.IsEnabled = true;
        }

        private void Btn_createRoom_Click(object sender, RoutedEventArgs e)
        {
            CreateRoomDialog createRoomDialog = new CreateRoomDialog();
            if(createRoomDialog.ShowDialog() == true)
            {
                Console.WriteLine("True");
                Console.WriteLine("Room Name : {0}",CreateRoomDialog.ReturnValue);
                if(ns.RequestCreate(CreateRoomDialog.ReturnValue) == 0)
                {
                    // 성공한 경우
                    Thread.Sleep(50);
                    Console.Write("방 생성 완료 목록 갱신...");
                    ns.RequestRoom(rooms);
                    RoomListView.Items.Refresh();
                    Console.WriteLine("완료");
                }
                else
                {
                    // 실패한 경우
                    MessageBox.Show("방을 생성하는데 실패하였습니다.","생성 실패",MessageBoxButton.OK,MessageBoxImage.Error);
                }
            }
            else
            {
                Console.WriteLine("false");
            }
            
        }
        
    }
}
