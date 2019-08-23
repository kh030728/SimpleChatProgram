using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SimpleChatClient.Models
{
    class ChatLogs : ObservableCollection<ChatLog>
    {

    }
    public class ChatLog
    {
        public string NickName { get; set; }
        public string Message { get; set; }
        public ChatLog(string nickname, string msg)
        {
            NickName = nickname;
            Message = msg;
        }
    }
}
