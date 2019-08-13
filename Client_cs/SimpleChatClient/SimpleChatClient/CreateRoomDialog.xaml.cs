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
    /// CreateRoomDialog.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class CreateRoomDialog : Window
    {
        public static string ReturnValue = string.Empty;
        public CreateRoomDialog()
        {
            ReturnValue = string.Empty;
            InitializeComponent();
        }
        private void btnDialogOK_Click(object sender,RoutedEventArgs e)
        {
            if (txtAnswer.Text == "")
            {
                Console.WriteLine("방 이름을 입력하세요.");
                this.DialogResult = false;
                return;
            }
            this.DialogResult = true;
            ReturnValue = txtAnswer.Text;//txtAnswer.Text;
            this.Close();
        }

        public string Answer
        {
            get
            {
                return "aa";//txtAnswer.Text;
            }
        }
    }
}
