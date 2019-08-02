using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
namespace SimpleChatClient
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class LogOnWindow : Window
    {
        TCPCli tcpCli = null;
        bool buttonExecuteFlag = false;
        public LogOnWindow()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (buttonExecuteFlag == false)
            {
                buttonExecuteFlag = true;
                if (tb_IPAddr.Text == "")
                {
                    LOW_tB_statusMsg.Text = "IP주소를 입력해주세요.";
                    LOW_tB_statusMsg.Background = new SolidColorBrush(Colors.Red);
                    buttonExecuteFlag = false;
                    return;
                }
                if (tb_NickName.Text == "")
                {
                    LOW_tB_statusMsg.Text = "닉네임을 입력해주세요.";
                    LOW_tB_statusMsg.Background = new SolidColorBrush(Colors.Red);
                    buttonExecuteFlag = false;
                    return;
                }
                LOW_tB_statusMsg.Text = "연결 시도중";
                LOW_tB_statusMsg.Background = new SolidColorBrush(Colors.RoyalBlue);
                tcpCli = new TCPCli();
                string IPAddr = tb_IPAddr.Text;
                string NickName = tb_NickName.Text;
                tcpCli.ServerAddr = IPAddr;
                if (tcpCli.NetworkConnect(LOW_tB_statusMsg))
                {
                    tcpCli.requestRoomInfo();
                    LOW_tB_statusMsg.Text = "연결 완료";
                    LOW_tB_statusMsg.Background = new SolidColorBrush(Colors.RoyalBlue);
                }
                else
                {

                    LOW_tB_statusMsg.Text = "연결 실패";
                    LOW_tB_statusMsg.Background = new SolidColorBrush(Colors.Red);
                }
                buttonExecuteFlag = false;
            }
        }
        private void Tb_IPAddr_GotFocus(object sender, RoutedEventArgs e)
        {
            LOW_tB_statusMsg.Text = "";
            LOW_tB_statusMsg.Background = new SolidColorBrush(Colors.RoyalBlue);
        }
    }
}
