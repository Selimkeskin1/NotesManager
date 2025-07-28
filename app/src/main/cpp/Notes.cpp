//
// Created by TR24798 on 25.07.2025.
//
#include "Notes.h"

Notes::Notes()
{
    std::string file_path = {};
    file_path.append(ROOT_PATH);
    file_path.append("notes.txt");


    LOGD("Notes Constructor called");


    if (!(std::filesystem::exists(file_path))){
        notestream = new std::fstream(file_path, std::ios_base::out |  std::ios_base::app);
        notestream->close();
    }

    notestream = new std::fstream(file_path, std::ios_base::in | std::ios_base::out | std::ios_base::binary);

}

Notes::~Notes()
{
    LOGD("Notes DeConstructor called");
    if (notestream != nullptr)
    {
        notestream->close();
        delete notestream;
    }

}

std::string Notes::getNext()
{
    std::string line = {};

    while (!(notestream->eof()))
    {
        std::getline(*(notestream), line);
        if (!(line.empty()))
        {
            std::string row = {};
            std::stringstream ss(line);
            int index = 0;
            while (std::getline(ss, row, '\t'))
            {
                if (index == 1)
                {
                    if ((row != "X"))
                    {
                        return line;
                    }
                }
                index++;
            }
        }
    }

    if (notestream->eof())
    {
        throw std::out_of_range("EOF reached");
        return "Last Record!";
    }
    return line;
}

std::string Notes::getPrevious()
{
    std::tuple<int, int> pos;
    std::string line = {};
    notestream->clear();

    if (notestream->tellg() == 0)
        //        return "First Record!";
        throw std::out_of_range("BOF reached");
    do
    {
        pos = {};
        pos = getBeginOfLinePosition(3, 5);

        if (notestream->seekg(std::get<0>(pos)))
        {
            std::string prevLine = {};
            std::getline((*notestream), prevLine);
            if (std::get<1>(pos) <= 0)
                notestream->seekg(0);
            std::stringstream ss(prevLine);
            int index = 0;
            std::string row = {};
            while (std::getline(ss, row, '\t'))
            {
                if ((index == 1) && (row != "X")) //  not deleted!
                    return prevLine;
                index++;
            }
        }
    } while (std::get<0>(pos) > 0);

    throw std::out_of_range("bof reached");
}


std::string Notes::getCurrent()
{
    std::string currentLine = {};
    notestream->clear();
    auto currentPos = notestream->tellg();
    auto pos = getBeginOfLinePosition(2, 5);
    notestream->seekg(std::get<0>(pos));
    std::getline(*notestream, currentLine);
    notestream->seekp(currentPos);
    return currentLine;
}

bool Notes::newNote(std::string &newnote)
{
    notestream->clear();
    if (notestream->is_open())
    {
        notestream->seekp(0, std::ios_base::end);
        (*notestream) << newnote;
        (*notestream) << std::endl;
        notestream->flush();
        return true;
    }
    else
    {
        return false;
    }

    return false;
}

bool Notes::updateNote(std::string &update)
{
    if (deleteNote(update))
    {
        if (newNote(update))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    else
    {
        return false;
    }
}

bool Notes::deleteNote(std::string &del)
{
    auto pos = getBeginOfLinePosition(2, 5);
    std::cout << std::get<0>(pos) << std::endl;
    notestream->clear();
    auto writepos = std::get<0>(pos) > 0 ? std::get<0>(pos) + 2 : 0;
    notestream->seekp(std::get<0>(pos) > 0 ? std::get<0>(pos) + 2 : 0, std::ios_base::beg);
    (*notestream) << "X";
    notestream->flush();
    return true;
}

std::tuple<int, int> Notes::getBeginOfLinePosition(int times, int bufferSize)
{
    std::string line = {};

    int pos = 0;
    int newline = 0;
    int findpos = 0;

//    char buffer[bufferSize + 1] = {};
    char buffer[bufferSize + 1];
    for( auto  c: buffer)  c = '\0';

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

    while (pos > 0)
    {
        int posAdd = 0;
        if (notestream->seekg(pos))
        {
            //          notestream->get(buffer, ( bufferSize + 1 ),  '\xE' );
            notestream->get(buffer, (bufferSize + 1), '\x0'); // NULL
            //          notestream->get(buffer, ( bufferSize + 1 ) );
            for (auto c : buffer)
            {
                posAdd++;
                if ((c == '\n') || c == '\r')
                {
                    newline++;
                    break;
                }
            }
        }
        else
        {
            break;
        }
        if (newline == times)
        {
            findpos = pos + posAdd;
            //            findpos = pos + posAdd;
            break;
        }
        pos -= bufferSize;
    }
    return std::make_tuple(findpos, pos);
    //    return findpos;
}

bool Notes::search(std::string &search)
{
    notestream->clear();
    auto currentPos = notestream->tellg();

    return true;
}

std::optional<std::string> Notes::next(std::string &search)
{
    while (!notestream->eof())
    {
        try
        {
            auto next = getNext();
            if (Helper::str_tolower(next).find(Helper::str_tolower(search)) != std::string::npos)
                return next;
        }
        catch (const std::exception &ex)
        {
            return std::nullopt;
        }
    }
    return  std::nullopt;
}

std::optional<std::string> Notes::previous(std::string &search)
{
    notestream->clear();
    while (notestream->tellg() > 0)
    {
        try
        {
            auto previous = getPrevious();
            if (Helper::str_tolower(previous).find(Helper::str_tolower(search)) != std::string::npos)
            {
                return previous;
            }
        }
        catch (const std::exception &exc)
        {
            return exc.what();
        }
    }
    return   std::nullopt;
}

std::optional<std::string> Notes::current(std::string &search)
{
    return getCurrent();
}
