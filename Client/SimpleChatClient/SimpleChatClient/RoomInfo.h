/*
 * This header file was first written in 2019-07-30 11:12 by kh030728.
 * This file has some data about SimpleChatClient's Room.
 * There are three member variables RoomName, Port and NumPeople.
 *----------------------------------------------------------------------
 * �� ��� ������ kh030728�� ���� 2019-07-30 11:12�� ���ʷ� �ۼ��Ǿ����ϴ�.
 * �� ������ SimpleChatClient���� ����ϴ� ��� �����͸� ������ �ֽ��ϴ�.
 * ��� ������ RoomName, Port, NumPeople 3���� ������ �ֽ��ϴ�.
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

