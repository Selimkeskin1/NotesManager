//
// Created by TR24798 on 8/18/2025.
//

#include "Ftp.h"
#include <cstdio>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>

#include <utility>


Ftp::Ftp( std::string && host_name , int port ) {
    connectToServer(std::move(host_name), port);
};

Ftp::~Ftp() {
    disconnectFromServer();
}

int Ftp::connectToServer( std::string && host_name , int port) {

    struct sockaddr_in addr{};
    if ((sockets = socket(AF_INET, SOCK_STREAM, 0 )) < 0) {
        return -1;
    }

    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);

    if (inet_pton(AF_INET, host_name.c_str(), &addr.sin_addr) <= 0) {
        return -1;
    }

    if (connect(sockets, (struct sockaddr *) &addr, sizeof(addr)) < 0) {
        return -1;
    }
    return sockets;

}


void Ftp::disconnectFromServer() {
    if (sockets  > 0 )
        close(sockets);
}

std::string Ftp::receiveData() {
    return "Ftp data ";
}

void Ftp::sendData() {

}









