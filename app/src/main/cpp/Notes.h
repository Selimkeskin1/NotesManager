//
// Created by TR24798 on 25.07.2025.
//


#pragma once

#include <iostream>
#include <string>
#include <fstream>
#include <clocale>
#include <memory>
#include <sstream>
#include <map>
#include <tuple>
#include "Helper.h"
#include <optional>



class Notes
{
private:
    std::fstream * notestream = nullptr;
    std::string getNext();
    std::string getPrevious();
    std::string getCurrent();
    bool synchronizeFile( std::string  && ip);

    int sendData(int ConnectSocket, std::string const &message);
    std::string  receiveData(int ConnectSocket);
    std::string lastWriteTime(const std::string &  file);
    std::string  getRelatievePath(std::string path);



public:
    Notes();
    ~Notes();

    std::optional<std::string> next( std::string & );
    std::optional<std::string> previous( std::string &);
    std::optional<std::string> current( std::string & );

    bool newNote(std::string &);
    bool deleteNote( std::string & );
    bool updateNote(std::string &);
    std::tuple<int, int> getBeginOfLinePosition(int,int);
    std::optional<std::string> search( std::string & );
    int getId();
    bool synchronize( std::string  && ip);


};