#include "RoomInfo.h"

//get and set method for Member variables 
std::string RoomInfo::GetRoomName() {	return RoomName;   }
void RoomInfo::SetRoomName(std::string input) {	RoomName = input; }
unsigned short RoomInfo::GetPort() { return Port; }
void RoomInfo::SetPort(unsigned short input) { Port = input; }
short RoomInfo::GetNumPeople() { return NumPeople; }
void RoomInfo::SetNumPeople(short input) { NumPeople = input; }

RoomInfo::RoomInfo(std::string RoomName, unsigned short Port, short NumPeople) 
{
	this->RoomName = RoomName;
	this->Port = Port;
	this->NumPeople = NumPeople;
}
RoomInfo::~RoomInfo() {}