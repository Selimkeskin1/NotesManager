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
        notes.reset();
    }
    return reinterpret_cast<jlong>(notes.release());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_notesmanager_NativeOperator_search(JNIEnv *env, jobject thiz, jlong process_handle,
                                            jstring search_string) {
    auto *handle = reinterpret_cast<Notes *>( process_handle );

    std::string no_record = "No record found";
    std::string search = env->GetStringUTFChars(search_string, nullptr);
    auto nextRecord = handle->next( search );
    if ( nextRecord  == std::nullopt ) return env->NewStringUTF( no_record.c_str() ) ;
    return env->NewStringUTF( nextRecord->c_str() );

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_notesmanager_NativeOperator_next(JNIEnv *env, jobject thiz, jlong process_handle, jstring search_string) {
    auto *handle = reinterpret_cast<Notes *>( process_handle );
    std::string no_record = "No record found";
    std::string search = env->GetStringUTFChars(search_string, nullptr);
     auto nextRecord = handle->next( search );
    if ( nextRecord  == std::nullopt ) return env->NewStringUTF( no_record.c_str() ) ;
    return env->NewStringUTF( nextRecord->c_str() );
}

extern "C"
JNIEXPORT void JNICALL
Java_com_notesmanager_NativeOperator_deleteObject(JNIEnv *env, jobject thiz, jlong process_handle) {
    auto* handle = reinterpret_cast<Notes*>( process_handle);
    if (not handle) {
        return;
    }
    delete handle;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_notesmanager_NativeOperator_test(JNIEnv *env, jobject thiz, jlong process_handle) {
    // TODO: implement test()
    auto *handle = reinterpret_cast<Notes *>( process_handle );
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_notesmanager_NativeOperator_previous(JNIEnv *env, jobject thiz, jlong process_handle, jstring search_string) {
    // TODO: implement previous()
    auto *handle = reinterpret_cast<Notes *>( process_handle );
    std::string no_record = "No record found";
    std::string search = env->GetStringUTFChars(search_string, nullptr);
    auto nextRecord = handle->previous( search  );
    if ( nextRecord  == std::nullopt ) return env->NewStringUTF( no_record.c_str() ) ;
    return env->NewStringUTF( nextRecord->c_str() );
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_notesmanager_NativeOperator_deleteNote(JNIEnv *env, jobject thiz, jlong process_handle,
                                                jint id) {
    auto *handle = reinterpret_cast<Notes *>( process_handle );
    std::string search =  "";
    return  handle->deleteNote( search  );
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_notesmanager_NativeOperator_updateOrAdd(JNIEnv *env, jobject thiz, jlong process_handle,
                                                 jint id, jstring description, jboolean is_new) {
    auto *handle = reinterpret_cast<Notes *>( process_handle );
        if (is_new) {

            std::string line = {};
            auto line_id = handle->getId();
            line.append(std::to_string(  line_id ) );
            line.append(1,'\t');
            line.append(" ");
            line.append(1,'\t');
            line.append(  env->GetStringUTFChars(description, nullptr) );


            return handle->newNote( line  );
        } else {
            std::string update_note = env->GetStringUTFChars(description, nullptr);
            return handle->updateNote(update_note);
        }

}