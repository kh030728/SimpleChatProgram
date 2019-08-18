namespace SimpleChatClient.Models
{
    using System.ComponentModel;
    public class LogOnInfo : INotifyPropertyChanged
    {
        public LogOnInfo()
        {

        }

        #region INotifyPropertyChanged Member
        public event PropertyChangedEventHandler PropertyChanged;
        #endregion
    }
}
