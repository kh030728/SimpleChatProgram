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
                    ControlStatusMsg("IP주소를 입력하여 주세요.",Colors.Red,true);
                    buttonExecuteFlag = false;
                    return;
                }
                if (tb_NickName.Text == "")
                {
                    ControlStatusMsg("닉네임을 입력하여 주세요.",Colors.Red,true);
                    buttonExecuteFlag = false;
                    return;
                }

                ns = NetworkSystem.Instance;
                string IPAddr = tb_IPAddr.Text;
                string NickName = tb_NickName.Text;

                ns.Connect(IPAddr, NickName);
                ControlStatusMsg("연결중...", Colors.RoyalBlue, true);
                //Thread reflectTheConnectResultThread = new Thread(ReflectTheConnectResultThread);
                //reflectTheConnectResultThread.Start();

                Thread task = new Thread(ReflectTheConnectResultThread);
                task.Start();
                
            }
        }
        private void ReflectTheConnectResultThread()
        {
            while (true)
            {
                Thread.Sleep(100);
                if(ns.ConnectThreadFinishFlag == true)
                {
                    break;
                }
            }
            if(ns.NetWorkConnectionStatus == true)
            {
                
                LOW_tB_statusMsg.Dispatcher.BeginInvoke(new InvokeDelegate(ChangeSuccess));
                List<Room> rooms = new List<Room>();
                ns.RequestRoom(rooms);
                buttonExecuteFlag = false;
                Thread thread = new Thread
                (
                    () =>
                    {
                        RoomListWindow roomListWindow = new RoomListWindow(rooms);
                        roomListWindow.Closed += (sender2, e2) => roomListWindow.Dispatcher.InvokeShutdown();
                        roomListWindow.Show();
                        System.Windows.Threading.Dispatcher.Run();
                    }
                );
                thread.SetApartmentState(ApartmentState.STA); // STA로 스레드를 실행시킨 다는것, UI나 Window에 접근 가능해진다는 것.
                thread.Start();
                this.Dispatcher.InvokeShutdown();
            }
            else
            {
                LOW_tB_statusMsg.Dispatcher.BeginInvoke(new InvokeDelegate(ChangeFail));
                ns.ConnectThreadFinishFlag = false;
            }
            buttonExecuteFlag = false;
        }
        private void ChangeSuccess() { ControlStatusMsg("연결 성공", Colors.RoyalBlue, true);}
        private void ChangeFail() { ControlStatusMsg("연결 실패", Colors.Red, true); }
        private void ControlStatusMsg(string msg, Color color,bool visible)
        {
            LOW_tB_statusMsg.Text = msg;
            LOW_tB_statusMsg.Background = new SolidColorBrush(color);
            if(visible ==true)
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
