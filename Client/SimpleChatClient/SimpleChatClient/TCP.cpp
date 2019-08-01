#include "TCP.h"

std::vector<RoomInfo> TCP::requestRoomList()
{
	std::vector<RoomInfo> output;
	toRoomInfo = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	SOCKADDR_IN roomInfoAddr = {};
	roomInfoAddr.sin_family = AF_INET;
	roomInfoAddr.sin_port = ROOM_PORT;
	roomInfoAddr.sin_addr.S_un.S_addr = _SERVER_IP;
	std::cout << "�ּ� ���� ��" << std::endl;
	connect(toRoomInfo, (SOCKADDR*)&roomInfoAddr, sizeof(roomInfoAddr));
	std::cout << "����Ϸ�" << std::endl;
	char cBuffer[PACKET_SIZE] = "REQUEST_ROOMINFO";
	send(toRoomInfo, cBuffer, PACKET_SIZE, 0);
	std::cout << "������ �޽��� : " << cBuffer << std::endl;
	std::string info = "";
	recv(toRoomInfo, cBuffer, PACKET_SIZE, 0);
	info = cBuffer;
	std::cout << "���� �޽��� : " << info.c_str() << std::endl;

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
