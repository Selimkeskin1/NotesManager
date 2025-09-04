//
// Created by TR24798 on 8/18/2025.
//
#pragma once
#include <string>


class Ftp{


private:
    int sockets =0;
    int connectToServer( std::string  && host_name , int port );
    void disconnectFromServer();

public:
     Ftp( std::string  && host_name , int port )  ;
    ~Ftp() ;

    void sendData();
    std::string receiveData();



};