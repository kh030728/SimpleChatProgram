using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace SimpleChatClient
{
    /// <summary>
    /// Window1.xaml에 대한 상호 작용 논리
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
        }


        private void Btn_exit_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
        private void Btn_refresh_Click(object sender, RoutedEventArgs e)
        {
            
        }
    }
}
