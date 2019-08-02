#include "TCP.h"

std::vector<RoomInfo> TCP::requestRoomList()
{
	std::vector<RoomInfo> output;
	char cBuffer[PACKET_SIZE] = "REQUEST_ROOMINFO";
	std::string info = "";
	try {
		send(toServerSock, cBuffer, PACKET_SIZE, 0);
	}
	catch (int e)
	{
		std::cout << "TCP send failed" << std::endl;
		return output;
	}
	std::cout << "전송한 메시지 : " << cBuffer << std::endl;
	try {
		recv(toServerSock, cBuffer, PACKET_SIZE, 0);
		info = cBuffer;
		std::cout << "받은 메시지 : " << info.c_str() << std::endl;
	}
	catch(int e)
	{
		std::cout << "RoomInfo를 받아오는데 실패" << std::endl;
	}

	return output;
}

bool TCP::Net_Connect()
{
	try
	{
		toServerSock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
		SOCKADDR_IN serverAddr = {};
		serverAddr.sin_family = AF_INET;
		serverAddr.sin_port = ROOM_PORT;
		serverAddr.sin_addr.S_un.S_addr = _SERVER_IP;
		connect(toServerSock, (SOCKADDR*)&serverAddr, sizeof(serverAddr));
		std::cout << "연결완료" << std::endl;
		return true;
	}
	catch(int e)
	{
		std::cout << "[Error] TCP Connection failed..." << std::endl;
		return false;
	}
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
