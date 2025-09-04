//
// Created by TR24798 on 25.07.2025.
//

#pragma once
#include <string>
#include <iostream>
#include "UnitOfMeasure.h"
#include "Log.h"
#include "Constants.h"
#include <android/multinetwork.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <zlib.h>
#include <fcntl.h>
#include <sys/stat.h>


class Helper
{
private:
public:
    Helper();
    ~Helper();
    static std::string str_tolower( std::string s  );

};