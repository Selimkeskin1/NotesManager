//
// Created by TR24798 on 25.07.2025.
//
#include "Helper.h"
#include <jni.h>

Helper::Helper()
{
}

Helper::~Helper()
{
}

std::string Helper::str_tolower(std::string s)
{
    std::string str = "";
    char cont = ' ';



    setlocale(LC_ALL, "Turkish");
    for (int i = 0; i < s.length(); i++)
    {

        std::string turkishch = "";

        if (cont == 'X')
        {
            cont = ' ';
            continue;
        }

        turkishch.append(1, s[i]);
        turkishch.append(1, s[i + 1]);

        if (turkishch == "İ" || turkishch == "i")
        {
            str.append("i");
            cont = 'X';
        }
        else if (turkishch == "Ö" || turkishch == "ö")
        {
            str.append("ö");
            cont = 'X';
        }
        else if (turkishch == "Ç" || turkishch == "ç")
        {
            str.append("ç");
            cont = 'X';
        }
        else if (turkishch == "Ş" || turkishch == "ş")
        {
            str.append("ş");
            cont = 'X';
        }
        else if (turkishch == "Ü" || turkishch == "ü")
        {
            str.append("ü");
            cont = 'X';
        }
        else if (turkishch == "Ğ" || turkishch == "ğ")
        {
            str.append("ğ");
            cont = 'X';
        }
        else if (s[i] == 'I')
        {
            str.append("ı");
        }
        else
        {
            str.append(1, tolower(s[i]));
        }
        //        if ((s[i] == -60 )  && ( s[i + 1] == -80 )   ) { // Turkish İ
        //            str.append("i");
        //        }
        //        else if ( ( s[i] == -61 )   && ( s[i + 1] == -106) ) { // Turkish Ö
        //            str.append("ö");
        //        }
        //        else if( ( s[i] == -61)   && (s[i + 1] == -74)  )   { // Turkish ö
        //                str.append("ö");
        //        }
        //        else if(  ( s[i] == -61  )  && ( s[i + 1] == -121 )  ) { // Turkish  Ç
        //          str.append("ç");
        //        }
        //        else if( ( s[i] == -61 )   &&  ( s[i + 1] == -89  )){ // ç
        //            str.append("ç");
        //        }
        //        else if(  ( s[i] == -60 )   && ( s[i + 1] == -79 ) ){ // ı
        //            str.append("ı");
        //        }
        //        else if( s[i] == 'I' ){ // I
        //            str.append("ı");
        //        }
        //        else if( ( s[i] == -59 )   && ( s[i + 1] == -98 ) ){ // Ş
        //            str.append("ş");
        //        }
        //        else if( ( s[i] == -59 )   && ( s[i + 1] == -97 ) ){ // Ş
        //            str.append("ş");
        //        }
        //        else if( ( s[i] == -61 )   && ( s[i + 1] == -100 ) ){ // Ü
        //            str.append("ü");
        //        }
        //        else if( s[i] == -61   && s[i + 1] == -68 ){ // Ü
        //            str.append("ü");
        //        }
        //        else if( ( s[i] == -60  )   && ( s[i + 1] == -97 ) ){ // ğ
        //            str.append("ğ");
        //        }
        //        else if( ( s[i] == -60 )  && ( s[i + 1] == -98 ) ){ // Ğ
        //            str.append("ğ");
        //        }
        //        else if (s[i] >= 0  ){
        //            str.append(1, tolower(s[i]) );
        //        }
    }
    return str;
}

