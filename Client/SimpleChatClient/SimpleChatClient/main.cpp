#include "RoomInfo.h"
#include "TCP.h"

int main()
{
	std::string ip = "127.0.0.1";
	TCP tcp(ip);
	tcp.requestRoomList();
	return 0;
}