using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Threading.Tasks;
using System.Threading;
using System.Collections.Generic;
namespace SimpleChatClient
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class LogOnWindow : Window
    {
        NetworkSystem ns = null;
        bool buttonExecuteFlag = false;
        private delegate void InvokeDelegate();
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
                    ControlStatusMsg("IP주소를 입력하여 주세요.", Colors.Red, true);
                    buttonExecuteFlag = false;
                    return;
                }
                if (tb_NickName.Text == "")
                {
                    ControlStatusMsg("닉네임을 입력하여 주세요.", Colors.Red, true);
                    buttonExecuteFlag = false;
                    return;
                }

                ns = NetworkSystem.Instance;
                string IPAddr = tb_IPAddr.Text;
                string NickName = tb_NickName.Text;
                ControlStatusMsg("연결중...", Colors.RoyalBlue, true);
                Thread thread = new Thread( //네트워크 연결을 다루는 스레드
                    () =>
                    {
                        if (ns.Connect(IPAddr, NickName) < 0) // 실패한경우
                        {
                            LOW_tB_statusMsg.Dispatcher.Invoke(() => { ControlStatusMsg("연결 실패", Colors.Red, true); });
                            buttonExecuteFlag = false;
                            return;
                        }   //성공한경우
                        Console.WriteLine("방정보 받아오기 성공");
                        LOW_tB_statusMsg.Dispatcher.Invoke(() => { ControlStatusMsg("연결 성공", Colors.RoyalBlue, true); });
                        List<Room> rooms = new List<Room>();
                        ns.RequestRoom(rooms);
                        Thread thread2 = new Thread
                        (
                            () =>
                            {
                                Console.WriteLine("thread2 Start (RoomListWindow show)");
                                RoomListWindow roomListWindow = new RoomListWindow(rooms);
                                roomListWindow.Closed += (sender2, e2) => roomListWindow.Dispatcher.InvokeShutdown();
                                roomListWindow.Show();
                                System.Windows.Threading.Dispatcher.Run();
                            }
                        );
                        thread2.SetApartmentState(ApartmentState.STA);
                        thread2.Start();
                        this.Dispatcher.InvokeShutdown();
                        buttonExecuteFlag = false;

                    });
                thread.Start();

            }
        }
        private void ChangeSuccess() { ControlStatusMsg("연결 성공", Colors.RoyalBlue, true); }
        private void ChangeFail() { ControlStatusMsg("연결 실패", Colors.Red, true); }
        private void ControlStatusMsg(string msg, Color color, bool visible)
        {
            LOW_tB_statusMsg.Text = msg;
            LOW_tB_statusMsg.Background = new SolidColorBrush(color);
            if (visible == true)
                LOW_tB_statusMsg.Visibility = Visibility.Visible;
            else
                LOW_tB_statusMsg.Visibility = Visibility.Hidden;
        }
        private void Tb_IPAddr_GotFocus(object sender, RoutedEventArgs e)
        {
            LOW_tB_statusMsg.Text = "";
            LOW_tB_statusMsg.Visibility = Visibility.Hidden;
        }
    }
}
