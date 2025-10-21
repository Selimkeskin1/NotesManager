//
// Created by TR24798 on 25.07.2025.
//
#include "Notes.h"

Notes::Notes() {
    std::string file_path = {};
    file_path.append(ROOT_PATH);
    file_path.append("notes.txt");


    LOGD("Notes Constructor called");



    if  ( ! ( std::filesystem::exists(ROOT_PATH) ) ){
        std::filesystem::create_directories(ROOT_PATH);
    }


    if (!(std::filesystem::exists(file_path))) {
        notestream = new std::fstream(file_path, std::ios_base::out | std::ios_base::app);
        notestream->close();
    }

    notestream = new std::fstream(file_path,
                                  std::ios_base::in | std::ios_base::out | std::ios_base::binary);

}

Notes::~Notes() {
    LOGD("Notes DeConstructor called");
    if (notestream != nullptr) {
        notestream->close();
        delete notestream;
    }

}

std::string Notes::getNext() {
    std::string line = {};

    while (!(notestream->eof())) {
        std::getline(*(notestream), line);
        if (!(line.empty())) {
            std::string row = {};
            std::stringstream ss(line);
            int index = 0;

            while (std::getline(ss, row, '\t')) {
                if (index == 0) {
                    if ((row == "X"))
                        break;

                } else if (index == 2) {
                    return row;
                }
                index++;
            }
        }
    }

    if (notestream->eof()) {
        throw std::out_of_range("EOF reached");
        return "Last Record!";
    }
    return line;
}

std::string Notes::getPrevious() {
    std::tuple<int, int> pos;
    std::string line = {};
    notestream->clear();

    if (notestream->tellg() == 0)
        //        return "First Record!";
        throw std::out_of_range("BOF reached");
    do {
        pos = {};
        pos = getBeginOfLinePosition(3, 5);

        if (notestream->seekg(std::get<0>(pos))) {
            std::string prevLine = {};
            std::getline((*notestream), prevLine);
            if (std::get<1>(pos) <= 0)
                notestream->seekg(0);
            std::stringstream ss(prevLine);
            int index = 0;
            std::string row = {};
            while (std::getline(ss, row, '\t')) {
                if ((index == 0) && (row == "X")) //  not deleted!
                    break;
                else if (index == 2)
                    return row;

                index++;
            }
        }
    } while (std::get<0>(pos) > 0);

    throw std::out_of_range("bof reached");
}


std::string Notes::getCurrent() {
    std::string currentLine = {};
    notestream->clear();
    auto currentPos = notestream->tellg();
    auto pos = getBeginOfLinePosition(2, 5);
    notestream->seekg(std::get<0>(pos));
    std::getline(*notestream, currentLine);
    notestream->seekg(currentPos);
    return currentLine;
}

bool Notes::newNote(std::string &newnote) {

    notestream->clear();
    if (notestream->is_open()) {
        notestream->seekp(0, std::ios_base::end);
        (*notestream) << newnote;
        (*notestream) << std::endl;
        notestream->flush();
        return true;
    } else {
        return false;
    }

    return false;
}

bool Notes::updateNote(std::string &update) {
    if (deleteNote(update)) {
        if (newNote(update)) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

bool Notes::deleteNote(std::string &del) {
    auto pos = getBeginOfLinePosition(2, 5);
    std::cout << std::get<0>(pos) << std::endl;
    notestream->clear();
    auto writepos = std::get<0>(pos) > 0 ? std::get<0>(pos) + 2 : 0;
    //Changing the current write position.
    notestream->seekp(std::get<0>(pos) > 0 ? std::get<0>(pos) : 0, std::ios_base::beg);
    (*notestream) << "X";
    notestream->flush();
    return true;
}

std::tuple<int, int> Notes::getBeginOfLinePosition(int times, int bufferSize) {
    std::string line = {};

    int pos = 0;
    int newline = 0;
    int findpos = 0;

//    char buffer[bufferSize + 1] = {};
    char buffer[bufferSize + 1];
    for (auto &c: buffer) c = '\0';

    notestream->clear();

    pos = (notestream->tellg());

    /*
        if (!(notestream->eof()))
        {
            pos = (notestream->tellg());
        }
        else
        {
            notestream->clear();
            std::streampos pos2 = notestream->tellg();
            notestream->seekg(0, (notestream->end));

            pos = (notestream->tellg());
        }

        */
    pos -= bufferSize;

    while (pos > 0) {
        int posAdd = 0;
        if (notestream->seekg(pos)) {
            //          notestream->get(buffer, ( bufferSize + 1 ),  '\xE' );
            notestream->get(buffer, (bufferSize + 1), '\x0'); // NULL
            //          notestream->get(buffer, ( bufferSize + 1 ) );
            for (auto c: buffer) {
                posAdd++;
                if ((c == '\n') || c == '\r') {
                    newline++;
                    break;
                }
            }
        } else {
            break;
        }
        if (newline == times) {
            findpos = pos + posAdd;
            //            findpos = pos + posAdd;
            break;
        }
        pos -= bufferSize;
    }
    return std::make_tuple(findpos, pos);
    //    return findpos;
}

std::optional<std::string> Notes::search(std::string &search) {
    notestream->clear();
    auto lastPos = notestream->tellg();
    notestream->seekg(0, notestream->beg);
    auto nextRecord = next(search);
    if (nextRecord == std::nullopt) {
        notestream->clear();
        notestream->seekg(lastPos);
        return std::nullopt;
    } else return nextRecord;
}

std::optional<std::string> Notes::next(std::string &search) {
    while (!notestream->eof()) {
        try {
            auto next = getNext();
            if (Helper::str_tolower(next).find(Helper::str_tolower(search)) != std::string::npos)
                return next;
        }
        catch (const std::exception &ex) {
            return std::nullopt;
        }
    }
    return std::nullopt;
}

std::optional<std::string> Notes::previous(std::string &search) {
    notestream->clear();
    while (notestream->tellg() > 0) {
        try {
            auto previous = getPrevious();
            if (Helper::str_tolower(previous).find(Helper::str_tolower(search)) !=
                std::string::npos) {
                return previous;
            }
        }
        catch (const std::exception &exc) {
            return std::nullopt;
        }
    }
    return std::nullopt;
}

std::optional<std::string> Notes::current(std::string &search) {
    return getCurrent();
}

int Notes::getId() {
    std::string line = {};
    notestream->clear();
    auto currentPos = notestream->tellg();
    notestream->seekg(0, notestream->end);
    auto read_pos = getBeginOfLinePosition(2, 5);
    notestream->seekg(std::get<0>(read_pos));
    std::getline(*notestream, line);

    if (!(line.empty())) {
        notestream->seekg(currentPos);
        std::stringstream ss(line);
        std::string row = {};
        int index = 0;
        while (std::getline(ss, row, '\t')) {
            if (index == 1) {
                return (std::stoi(row) + 1);
            }
            ++index;
        }
    }
    return 0;
}


bool Notes::synchronize(std::string &&ip) {
//    return synchronizeFile(std::move(ip));
   return testSecureConnection();
}

bool Notes::synchronizeFile(std::string &&ip) {

    int ConnectSocket = -1;
    struct sockaddr_in serv_addr;
    int iResult = 0;
    struct sockaddr_in my_addr1;
    struct addrinfo *result = NULL, *ptr = NULL, hints;

    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;

    memset(&my_addr1, 0, sizeof(my_addr1));

    my_addr1.sin_family = AF_INET;
    my_addr1.sin_addr.s_addr = INADDR_ANY;
    my_addr1.sin_port = htons(27016);



//    iResult = getaddrinfo("tcpbin.com", "4242", &hints, &result);
    iResult = getaddrinfo(ip.c_str(), DEFAULT_PORT, &hints, &result);
    if (iResult != 0) {
        LOGD("getaddrinfo failed with error: %d\n", iResult);
        return false;
    }



    // Create socket
    for (ptr = result; ptr != NULL; ptr = ptr->ai_next) {
        ConnectSocket = socket(ptr->ai_family, ptr->ai_socktype, ptr->ai_protocol);


        if (ConnectSocket < 0) {
            continue;
        }



        if (bind(ConnectSocket, (struct sockaddr *) &my_addr1, sizeof(struct sockaddr_in)) == 0) {
            printf("Binded Correctly\n");
        } else {
            continue;
        }




        // Connect to server.
        iResult = connect(ConnectSocket, ptr->ai_addr, (int) ptr->ai_addrlen);


        // Connect to server
        if (iResult < 0) {
//            std::cerr << "Connection Failed" << std::endl;
            LOGD("The last error message is: %s\n", strerror(errno));
            continue;
        } else if (iResult != -1)
            break;
        shutdown(ConnectSocket, SHUT_RDWR);
        close(ConnectSocket);
    }

    freeaddrinfo(result);

    if (ptr == nullptr) {
        LOGD("Could not connect\n");
        LOGD("ERR %d", stderr);
        shutdown(ConnectSocket, SHUT_RDWR);
        close(ConnectSocket);
        return false;
    }

    std::cout << "\nrecursive_directory_iterator:\n";
    std::string dir = {};
    std::string file = {};
    std::string command = {};

    const std::filesystem::path sandbox{ ROOT_PATH };


    for (auto const dir_entry: std::filesystem::recursive_directory_iterator{sandbox}) {

        if (dir_entry.is_directory()) {
            dir = dir_entry.path().string();
        } else if (dir_entry.is_regular_file()) {
            file = dir_entry.path().string();
        }
    }

//    dir = getRelatievePath(dir );

    dir = "notesmanager/";

    if (!dir.empty()) {
        command = {};
        command = "create_dir";
        command.append(1, '\t');
        command.append(dir);
        sendData(ConnectSocket, command);
        if (receiveData(ConnectSocket) == "NOK") {
        } else {
            for (auto const dir_entry: std::filesystem::recursive_directory_iterator{sandbox}) {
                if (dir_entry.is_regular_file()  ) {
                    command = {};
                    command = "create_file";
                    command.append(1, '\t');

                    std::string relatievePath = {};
                    relatievePath = getRelatievePath(dir_entry.path().string());
//                    command.append(dir_entry.path().string());
                    command.append(relatievePath);
                    command.append(1, '\v');
                    command.append(lastWriteTime(dir_entry.path().string()));
                    auto fs = std::to_string(std::filesystem::file_size(dir_entry.path()));
                    command.append(1, '\v');
                    command.append(fs);

                    sendData(ConnectSocket, command);
                    if (receiveData(ConnectSocket) == "NOK")
                        continue;
                    else {

                        std::ifstream copy_file(dir_entry.path().string(), std::ios_base::binary);
                        std::size_t index = {0};

                        if (copy_file.is_open()) {
                            std::string line{};
                            LOGD( "file transfer begins\n" );
                            command = {};
                            command = "new_line";
                            sendData(ConnectSocket, command);
                            receiveData(ConnectSocket);

                            while (!copy_file.eof()){
                                ++index;
                                /**/
                                char buffer[DEFAULT_BUFLEN] = {'\0'};
                                copy_file.read(buffer, DEFAULT_BUFLEN);
                                std::streamsize bytesRead = copy_file.gcount();
                                send_binary_data(ConnectSocket, buffer);
                                receiveData(ConnectSocket);
//                                std::this_thread::sleep_for(std::chrono::microseconds(300000));
                                /**/
                            }
                            if (copy_file.eof()) // check for EOF
                                LOGD( "[EoF reached]\n" );
                            else
                               LOGD("[error reading]\n " );
                            LOGD(" index = %d" ,  index );
                            copy_file.close();
                            command = {};
                            command = "finish";
                            sendData(ConnectSocket, command);
                            receiveData(ConnectSocket);
                        }
                    }
                }
            }
        }
    }


    shutdown(ConnectSocket, SHUT_RDWR);
    close(ConnectSocket);
    return true;
}


int Notes::sendData(int ConnectSocket, const std::string &message) {

    const char *sendData = nullptr;
    sendData = message.c_str();

//    return send(ConnectSocket, sendData, message.size(), 0);
   try {
       return write(ConnectSocket, sendData, message.size());
   }
   catch (const std::exception &ex) {
       LOGD("while socket->sendData exceptipn occured :%s", ex.what());
   }

}


std::string Notes::receiveData(int ConnectSocket) {
    int result = {0};
    std::string receivedData = {};

    // Receive until the peer closes the connection
    char recvbuf[DEFAULT_BUFLEN] = {'\0'};

//    result = recv(ConnectSocket, recvbuf, DEFAULT_BUFLEN, 0);
    try {
        result = read(ConnectSocket, recvbuf, DEFAULT_BUFLEN);
    }
    catch (const std::exception &ex) {
        LOGD("while socket->receiveData exceptipn occured :%s", ex.what());
    }

    if (result > 0) {
        LOGD("----------\n");
        LOGD("Bytes received: %d\n", result);
        LOGD("received data : %s\n", recvbuf);
        LOGD("----------\n");

        receivedData.append(recvbuf);
    } else if (result == 0)
        LOGD("Connection closed\n");
    else
        LOGD("recv failed with error: %s\n", strerror(errno));
    //   } while (result > 0);

    return receivedData;
}


std::string Notes::lastWriteTime(const std::string &file) {
    std::filesystem::file_time_type ftime = std::filesystem::last_write_time(file);
    auto timePoint = std::chrono::file_clock::to_sys(ftime);
    auto converted = std::chrono::system_clock::to_time_t(
            std::chrono::time_point_cast<std::chrono::system_clock::duration>(timePoint));
    return std::to_string(converted);
}

std::string  Notes::getRelatievePath(std::string path) {
    std::string root = {ROOT_PATH };
    std::string relPath = path;
    relPath.replace(relPath.find(root), root.length(), "notesmanager/");

    return relPath ;
}

int Notes::send_binary_data(int ConnectSocket, char *message) {
    return send(ConnectSocket, message, DEFAULT_BUFLEN, 0);
}

bool Notes::testSecureConnection() {
    SSL_library_init();
    SSL_load_error_strings();
    OpenSSL_add_all_algorithms();

    const SSL_METHOD* method = TLS_client_method();
    SSL_CTX* ctx = SSL_CTX_new(method);

    if (!ctx) {
        std::cerr << "SSL_CTX oluşturulamadı!\n";
        ERR_print_errors_fp(stderr);
        return false;
    }


    int sock = socket(AF_INET, SOCK_STREAM, 0);
    sockaddr_in server{};
    server.sin_family = AF_INET;
    server.sin_port = htons(27015);
    inet_pton(AF_INET, "192.168.1.204", &server.sin_addr);

    if (connect(sock, (struct sockaddr*)&server, sizeof(server)) < 0) {
        std::cerr << "Sunucuya bağlanılamadı!\n";
        printf("socket failed with error: %ld\n", stderr);
        return false;
    }


    SSL* ssl = SSL_new(ctx);
    SSL_set_fd(ssl, sock);

    if (SSL_connect(ssl) <= 0) {
        std::cerr << "TLS handshake hatası!\n";
        ERR_print_errors_fp(stderr);
    } else {
        std::cout << "TLS bağlantısı başarılı!\n";
        SSL_write(ssl, "Selam Sunucu!", 13);
        char buffer[1024] = {0};
        SSL_read(ssl, buffer, sizeof(buffer));
        std::cout << "Sunucudan: " << buffer << "\n";
    }


    SSL_shutdown(ssl);
    SSL_free(ssl);
    close(sock);
    SSL_CTX_free(ctx);


    return true;
}