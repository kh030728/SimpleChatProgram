#pragma once
#include <iostream>
#include <WinSock2.h>
#include "RoomInfo.h"
#include <vector>

constexpr auto ROOM_PORT = 1;

#define PACKET_SIZE 1024

class TCP
{
private:
	WSAData wsaData;
	std::vector<RoomInfo> requestRoomList();
	SOCKET toRoomInfo;
	unsigned short _SERVER_IP;
	bool requestRoomAccess();
	bool requestSendChat();
	
	
public :
	enum class Purpose {
		REQUEST_ROOMLIST,
		REQUEST_ROOMACCSESS,
		REQUEST_SENDCHAT
	};

	TCP(std::string IPAddr);
	~TCP();
	bool send(Purpose purpose, std::string input);
	
};

