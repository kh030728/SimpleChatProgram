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

        public RoomListWindow()
        {
            ns = NetworkSystem.Instance;
            rooms = new List<Room>();
            if(ns.RequestRoom(rooms) < 0) // 실패시
            {
                MessageBox.Show("방목록을 불러오는데 실패 하였습니다.\n새로 고침을 이용하여 주세요.", "연결 오류", MessageBoxButton.OK, MessageBoxImage.Error);
                InitializeComponent();
            }
            else
            {  //성공시
                InitializeComponent();
                RoomListView.ItemsSource = rooms;
            }
            TB_NickName.Text = ns.NICKNAME;
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
                RoomListView.ItemsSource = rooms;
                RoomListView.Items.Refresh();
                Console.WriteLine("Failed");
                return;
            }
            RoomListView.ItemsSource = rooms;
            RoomListView.Items.Refresh();
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
                    RoomListView.ItemsSource = rooms;
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
        private void Btn_joinRoom_Click(object sender, RoutedEventArgs e)
        {
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
        }
    }
}
