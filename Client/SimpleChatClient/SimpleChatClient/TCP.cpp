#include "TCP.h"

std::vector<RoomInfo> TCP::requestRoomList()
{
	std::vector<RoomInfo> output;
	toRoomInfo = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	SOCKADDR_IN roomInfoAddr = {};
	roomInfoAddr.sin_family = AF_INET;
	roomInfoAddr.sin_port = ROOM_PORT;
	roomInfoAddr.sin_addr.S_un.S_addr = _SERVER_IP;

	connect(toRoomInfo, (SOCKADDR*)&roomInfoAddr, sizeof(roomInfoAddr));

	char cBuffer[PACKET_SIZE] = {};
	std::string info = "";
	int flag = 1;
	while (!((flag != 0) ^ (flag != -1)))
	{
		flag = recv(toRoomInfo, cBuffer, PACKET_SIZE, 0);
		info = cBuffer;
	}
	return output;
}

bool TCP::requestRoomAccess()
{
	return false;
}

bool TCP::requestSendChat()
{
	return false;
}

TCP::TCP(std::string IPAddr)
{
	WSAStartup(MAKEWORD(2, 2), &wsaData);
	_SERVER_IP = inet_addr(IPAddr.c_str());
}


TCP::~TCP()
{
	WSACleanup();
}

bool TCP::send(Purpose purpose, std::string input)
{
	switch (purpose)
	{
	case Purpose::REQUEST_ROOMLIST:
		std::cout << "Request room list" << std::endl;
	default:
		return false;
	}
}
