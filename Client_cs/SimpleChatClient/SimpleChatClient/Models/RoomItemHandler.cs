namespace SimpleChatClient.Models
{
    using System.Collections.ObjectModel;
    using System.Collections.Specialized;
    using System.Windows.Threading;
    using System;

    public class RoomItemHandler : ObservableCollection<Room>
    {
        /// <summary>
        /// Initializes a new instance of the RoomItemHandler class;
        /// </summary>
        public RoomItemHandler()
        {
            
        }
        public override event NotifyCollectionChangedEventHandler CollectionChanged;
        protected override void OnCollectionChanged(NotifyCollectionChangedEventArgs e)
        {
            NotifyCollectionChangedEventHandler CollectionChanged = this.CollectionChanged;
            if(CollectionChanged != null)
            {
                foreach(NotifyCollectionChangedEventHandler nh in CollectionChanged.GetInvocationList())
                {
                    DispatcherObject dispObj = nh.Target as DispatcherObject;
                    if(dispObj != null)
                    {
                        Dispatcher dispatcher = dispObj.Dispatcher;
                        if(dispatcher != null)
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

        public void Add(string number, string name, string people)
        {
            this.Add(new Room(name, int.Parse(number), int.Parse(people)));
        }
        public bool RemoveRoom(int roomIndex)
        {
            foreach(Room elem in this)
            {
                if(elem.Number == roomIndex)
                {
                    Remove(elem);
                    return true;
                }
            }
            return false;
        }
        public bool ChangeRoom(int roomIndex, int roomPeople)
        {
            foreach (Room elem in this)
            {
                if (elem.Number == roomIndex)
                {
                    elem.NumberOfPeople = roomPeople;
                    return true;
                }
            }
            return false;
        }
    }

    public class Room
    {
        /// <summary>
        /// Initializes a new instance of the Room class;
        /// </summary>
        public Room(string name, int number, int numberOfPeople)
        {
            Name = name;
            Number = number;
            NumberOfPeople = numberOfPeople;
        }

        private string _Name;
        /// <summary>
        /// Gets or Sets the Room's Name;
        /// </summary>
        public string Name { get => _Name; set => _Name = value; }
        private int _Number;
        /// <summary>
        /// Gets or Sets the Room's Number;
        /// </summary>
        public int Number{ get => _Number; set => _Number = value; }
        private int _NumberOfPeople;
        /// <summary>
        /// Gets or Sets the Room's NumberOfPeople;
        /// </summary>
        public int NumberOfPeople { get => _NumberOfPeople; set => _NumberOfPeople = value; }
    }
}
