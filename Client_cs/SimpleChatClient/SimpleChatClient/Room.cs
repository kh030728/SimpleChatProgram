using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SimpleChatClient
{
    class Room
    {
        private string name; public string NAME { get { return name; } set { name = value; } }
        private int id; public int ID { get { return id; } set { id = value; } }
        private int people; public int PEOPLE { get { return people; } set { people = value; } }

        Room(string _name, int _id, int _people)
        {
            name = _name;
            id = _id;
            people = _people;
        }
    }
}
