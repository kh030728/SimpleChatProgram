
namespace SimpleChatClient.Models
{
    using System.Collections.ObjectModel;

    public class RoomItemHandler : ObservableCollection<Room>
    {
        /// <summary>
        /// Initializes a new instance of the RoomItemHandler class;
        /// </summary>
        public RoomItemHandler()
        {

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
