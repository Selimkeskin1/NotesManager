//
// Created by TR24798 on 28.07.2025.
//

#pragma once


#define DAY_TO_SECOND 86400
#define DAY_TO_MINUTE 1440
#define DAY_TO_HOUR 24
#define HOUR_TO_SECOND 3600
#define MINUTE_TO_SECOND 60
#define HOUR4_TO_SECOND 14400
#define HOUR4_TO_SECOND_DOUBLE 14400.00
#define HOUR3_5_TO_SECOND 12600
#define HOUR3_5_TO_SECOND_DOUBLE 12600.00
#define HOUR0_5_TO_SECOND 1800
#define MOUNTH3_TO_SECOND 7776000
#define MOUNTH1_TO_SECOND 2592000
#define TARGET_BS 120
#define ISF_VALUE 40
#define ISF_VALUE_FLOAT 40.00
#define CARB_RATIO 10
#define CARB_RATIO_FLOAT 10.00
#if defined(_WIN32) || defined(_WIN64)
#define ROOT_PATH "C:\\Users\\tr24798\\projects\\helloworld\\prod\\16031195646\\measurements\\test\\"
#elif defined(__ANDROID__)
#define ROOT_PATH "/data/data/com.notesmanager/files/"
#endif
