using System.Windows;
using System.Windows.Media;
namespace SimpleChatClient
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class LogOnWindow : Window
    {
        NetworkSystem ns = null;
        private delegate void InvokeDelegate();
        public LogOnWindow()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            btn_access.IsEnabled = false;
            #region TextBox 검사 작업
            if (tb_IPAddr.Text == "")
            {
                ControlStatusMsg("IP주소를 입력하여 주세요.", Colors.Red, true);
                btn_access.IsEnabled = true;
                return;
            }
            if (tb_NickName.Text == "")
            {
                ControlStatusMsg("닉네임을 입력하여 주세요.", Colors.Red, true);
                btn_access.IsEnabled = true;
                return;
            }
            if (tb_NickName.Text.Contains(" "))
            {
                ControlStatusMsg("닉네임에 공백을 포함할 수 없습니다.", Colors.Red, true);
                btn_access.IsEnabled = true;
                return;
            }
            char[] invalidCharacter = { '%', '_', '&', ';' };
            if (tb_NickName.Text.IndexOfAny(invalidCharacter) > 0)
            {
                ControlStatusMsg("'%', '_', '&', ';' 문자는 닉네임에 포함될 수 없습니다.", Colors.Red, true);
                btn_access.IsEnabled = true;
                return;
            }
            #endregion
            ns = NetworkSystem.Instance;
            if(ns.Connect(tb_IPAddr.Text, tb_NickName.Text) < 0)
            {
                ControlStatusMsg("연결에 실패하였습니다.", Colors.Red, true);
                btn_access.IsEnabled = true;
                return;
            }

            RoomListWindow roomListWindow = new RoomListWindow();
            roomListWindow.Show();
            this.Close();

        }
        private void Button_Click1(object sender, RoutedEventArgs e)
        {
            ChatWindow chatWindow = new ChatWindow();
            chatWindow.Show();
            this.Close();
        }
        private void ControlStatusMsg(string msg, Color color, bool visible)
        {
            LOW_tB_statusMsg.Text = msg;
            LOW_tB_statusMsg.Background = new SolidColorBrush(color);
            if (visible == true)
                LOW_tB_statusMsg.Visibility = Visibility.Visible;
            else
                LOW_tB_statusMsg.Visibility = Visibility.Hidden;
        }
        private void ChangeSuccess() { ControlStatusMsg("연결 성공", Colors.RoyalBlue, true); }
        private void ChangeFail() { ControlStatusMsg("연결 실패", Colors.Red, true); }
        private void Tb_IPAddr_GotFocus(object sender, RoutedEventArgs e)
        {
            LOW_tB_statusMsg.Text = "";
            LOW_tB_statusMsg.Visibility = Visibility.Hidden;
        }
    }
}
