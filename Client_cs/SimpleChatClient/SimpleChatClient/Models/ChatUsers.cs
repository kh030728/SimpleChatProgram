namespace SimpleChatClient.Models
{
    using System.Collections.ObjectModel;
    using System.Collections.Specialized;
    using System.Windows.Threading;
    using System;

    public class ChatUsers : ObservableCollection<User>
    {
        public ChatUsers()
        {

        }
        public void remove(string user)
        {
            for(int i = 0; i < this.Count; i++)
            {
                if(this[i].NickName == user)
                {
                    this.RemoveAt(i);
                    return;
                }
            }
        }
        #region ObservableCollection member
        public override event NotifyCollectionChangedEventHandler CollectionChanged;
        protected override void OnCollectionChanged(NotifyCollectionChangedEventArgs e)
        {
            NotifyCollectionChangedEventHandler CollectionChanged = this.CollectionChanged;
            if (CollectionChanged != null)
            {
                foreach (NotifyCollectionChangedEventHandler nh in CollectionChanged.GetInvocationList())
                {
                    DispatcherObject dispObj = nh.Target as DispatcherObject;
                    if (dispObj != null)
                    {
                        Dispatcher dispatcher = dispObj.Dispatcher;
                        if (dispatcher != null)
                        {
                            dispatcher.BeginInvoke(
                            (Action)(() => nh.Invoke(this,
                                new NotifyCollectionChangedEventArgs(NotifyCollectionChangedAction.Reset))),
                            DispatcherPriority.DataBind);
                        }
                    }
                }
            }
        }
        #endregion
    }

    public class User
    {
        public string NickName { get; set; }
        /// <summary>
        /// Initializes a new instance of The User class.
        /// </summary>
        public User(string nick)
        {
            NickName = nick;
        }
    }
}
