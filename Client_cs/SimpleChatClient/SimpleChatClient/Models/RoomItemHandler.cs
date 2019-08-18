using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SimpleChatClient.Models
{
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
        public string Name { get; set; }
        private int _Number;
        /// <summary>
        /// Gets or Sets the Room's Number;
        /// </summary>
        public int Number{ get; set; }
        private int _NumberOfPeople;
        /// <summary>
        /// Gets or Sets the Room's NumberOfPeople;
        /// </summary>
        public int NumberOfPeople { get; set; }
    }
}
