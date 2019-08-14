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
    /// ChatWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class ChatWindow : Window
    {
        NetworkSystem ns;
        public ChatWindow()
        {
            ns = NetworkSystem.Instance;
            InitializeComponent();
        }

        private void SendButton_Click(object sender, RoutedEventArgs e)
        {
            sendButton.IsEnabled = false;
            string tmp = tbx_chatInput.Text;
            tbx_chatInput.Text = "";
            AddLog(tmp);
            sendButton.IsEnabled = true;
        }
        private void RecvThread()
        {
            while(true)
            {
                string Msg = ns.RecvMsg();
                if(Msg.Contains("") == true)
                {

                }
                else
                {

                }
            }
        }
        
        private void AddLog(string Msg)
        {
            chatitem _item = new chatitem();
            _item.nameTextBlock.Text = "김광현";
            _item.messageTextBlock.Text = Msg;
            chatStackPanel.Children.Add(_item);
            sv_Chatlog.ScrollToBottom();
        }

        private void Tbx_chatInput_EnterKeyDown(object sender, KeyEventArgs e)
        {
            if(e.Key == Key.Return)
            {
                string tmp = tbx_chatInput.Text;
                tbx_chatInput.Text = "";
                AddLog(tmp);
            }
        }
    }
}
