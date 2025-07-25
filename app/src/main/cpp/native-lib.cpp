#include <jni.h>
#include <string>
#include <memory>
#include "Log.h"
#include "Notes.h"


extern "C" JNIEXPORT jstring JNICALL
Java_com_notesmanager_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C"
JNIEXPORT jlong JNICALL
Java_com_notesmanager_NativeOperator_create(JNIEnv *env, jobject thiz) {
    auto notes = std::make_unique<Notes>();
    if (not notes) {
        LOGD("Failed to create notes");
        notes.reset();
    }
    return reinterpret_cast<jlong>(notes.release());
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_notesmanager_NativeOperator_search(JNIEnv *env, jobject thiz, jlong process_handle) {
    auto *handle = reinterpret_cast<Notes *>( process_handle );
    return true;
}