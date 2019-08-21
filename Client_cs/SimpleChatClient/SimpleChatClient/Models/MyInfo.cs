namespace SimpleChatClient.Models
{
    using System;
    using System.ComponentModel;
    class MyInfo :INotifyPropertyChanged
    {
        private MyInfo()
        {
            NickName = "NickName";
            Address = "127.0.0.1";
        }
        private static MyInfo _Instance = null;
        public static MyInfo Instance
        {
            get
            {
                if (_Instance == null)
                {
                    return new MyInfo();
                }
                return _Instance;
            }
        }
        private string address;
        public string Address
        {
            get { return address; }
            set
            {
                address = value;
                OnPropertyChanged("Address_TextBox");
            }
        }
        private string nickName;
        public string NickName {
            get { return nickName; }
            set
            {
                nickName = value;
                OnPropertyChanged("NickName_TextBox");
            }
        }

        #region INotifyPropertyChanged Member

        public event PropertyChangedEventHandler PropertyChanged;

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;

            if(handler != null)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        #endregion
    }
}
