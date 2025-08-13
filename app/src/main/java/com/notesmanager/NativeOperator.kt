package com.notesmanager

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class NativeOperator : Operations, DefaultLifecycleObserver {
    private external fun create(): Long
    private external fun search(processHandle: Long, searchString: String): String
    private external fun next(processHandle: Long, searchString: String): String
    private external fun deleteObject(processHandle: Long)
    private external fun previous(processHandle: Long, searchString: String): String

    private external fun deleteNote(processHandle: Long, id: Int): Boolean

    private external fun updateOrAdd(
        processHandle: Long, id: Int, description: String, isNew: Boolean
    ): Boolean


    private external fun test(processHandle: Long): String
    private var isPlaying = false
    private var nativeOperatorHandle: Long = 0
    private val nativeOperatorMutex = Object()

    companion object {
        init {
            System.loadLibrary("notesmanager")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        synchronized(nativeOperatorMutex) {
            if (nativeOperatorHandle == 0L) {
                return
            }
            // Destroy the synthesizer
            deleteObject(nativeOperatorHandle)
            nativeOperatorHandle = 0L
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

    override suspend fun isPlaying(): Boolean {
        return isPlaying
    }

    override suspend fun play() {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
//            play(nativeOperatorHandle)
        }
    }

    override suspend fun stop() {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
//            stop(nativeOperatorHandle)
        }
    }

    override suspend fun setFrequency(frequencyInHz: Float) {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
//            setFrequency(nativeOperatorHandle, frequencyInHz)
        }
    }

    override suspend fun setVolume(volumeInDb: Float) {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
//            setVolume(nativeOperatorHandle, volumeInDb)
        }

    }


    private fun createNativeHandleIfNotExists() {
        if (nativeOperatorHandle != 0L) {
            return
        }
        nativeOperatorHandle = create()
        // create handle

    }

    override fun search(searchString: String): String {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            return search(nativeOperatorHandle, searchString)
        }

//        Log.d("OperationsLogging", "search() called with searchString: $searchString")
    }

    override fun delete(id: Int): Boolean {
//        Log.d("OperationsLogging", "delete() called with id: $id")
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            return deleteNote(nativeOperatorHandle, id)


        }

    }

    override fun previous(id: Int, searchString: String): String {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            return previous(nativeOperatorHandle, searchString)
        }
    }

    override fun next(id: Int, searchString: String): String {
//        Log.d("OperationsLogging", "next() called with id: $id")

        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            return next(nativeOperatorHandle, searchString)
        }
    }

    override fun updateOrAdd(id: Int, description: String, isNew: Boolean): Boolean {
//        Log.d("OperationsLogging", "updateOrAdd() called with id: $id, description: $description")
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            return updateOrAdd(nativeOperatorHandle, id, description, isNew)
        }

    }

    override fun exit() {
//        Log.d("OperationsLogging", "exit() called")
    }

}