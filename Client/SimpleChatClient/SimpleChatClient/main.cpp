#include "RoomInfo.h"
#include "TCP.h"

int main()
{
	std::string ip = "127.0.0.1";
	TCP tcp(ip);
	if (tcp.Net_Connect()) // 성공하였나요?
	{
		tcp.requestRoomList();
	}
	return 0;
}