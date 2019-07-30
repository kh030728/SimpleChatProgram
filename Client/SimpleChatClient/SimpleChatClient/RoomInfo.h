/*
 * This header file was first written in 2019-07-30 11:12 by kh030728.
 * This file has some data about SimpleChatClient's Room.
 * There are three member variables RoomName, Port and NumPeople.
 *----------------------------------------------------------------------
 * 이 헤더 파일은 kh030728에 의해 2019-07-30 11:12에 최초로 작성되었습니다.
 * 이 파일은 SimpleChatClient에서 사용하는 몇몇 데이터를 가지고 있습니다.
 * 멤버 변수로 RoomName, Port, NumPeople 3개를 가지고 있습니다.
 */

#pragma once
#include <iostream>
class RoomInfo
{
private :
	std::string RoomName = "";
	short NumPeople = 0;
	unsigned short Port;

public:
	std::string GetRoomName();
	void SetRoomName(std::string input);

	short GetNumPeople();
	void SetNumPeople(short input);

	unsigned short GetPort();
	void SetPort(unsigned short input);

	RoomInfo(std::string RoomName, unsigned short Port, short NumPeople);
	~RoomInfo();
};

