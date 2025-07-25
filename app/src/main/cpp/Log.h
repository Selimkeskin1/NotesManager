//
// Created by TR24798 on 25.07.2025.
//
#pragma once

#include <android/log.h>

#ifndef NDEBUG
#define LOGD(args...) \
__android_log_print(android_LogPriority::ANDROID_LOG_DEBUG, "Test App", args)
#else
#define LOGD(args...)
#endif