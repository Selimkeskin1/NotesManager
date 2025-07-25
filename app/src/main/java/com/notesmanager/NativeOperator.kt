package com.notesmanager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver


class NativeOperator: Operations, DefaultLifecycleObserver {
    private var isPlaying = false

    private var nativeOperatorHandle: Long = 0
    private val nativeOperatorMutex = Object()


    companion object {
        init {
            System.loadLibrary("notesmanager")
        }
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
    override  suspend fun setVolume(volumeInDb: Float) {
        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
//            setVolume(nativeOperatorHandle, volumeInDb)
        }

    }


    private fun createNativeHandleIfNotExists() {
        if (nativeOperatorHandle != 0L) {
            return
        }
//        synthesizerHandle = create()
        // create the synthesizer

    }

    override  fun search(searchString : String) {
        Log.d("OperationsLogging", "search() called with searchString: $searchString")
    }

    override  fun delete( id : Int): Boolean {
        Log.d("OperationsLogging", "delete() called with id: $id")
        return true
    }

    override fun previous(id : Int): String {
        Log.d("OperationsLogging", "previous() called with id: $id")
        return "previous"
    }
    override  fun next(id : Int): String {
        Log.d("OperationsLogging", "next() called with id: $id")
        return "next"
    }

    override  fun updateOrAdd( id : Int, description : String) :Boolean {
        Log.d("OperationsLogging", "updateOrAdd() called with id: $id, description: $description")
        return true
    }

    override fun exit() {
        Log.d("OperationsLogging", "exit() called")
    }



}