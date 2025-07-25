package com.notesmanager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class NativeOperator: Operations, DefaultLifecycleObserver {

    private external fun create(): Long
    private external fun search( processHandle: Long ) : Boolean
    private external fun next( processHandle: Long ) : Boolean
    private external fun delete( processHandle: Long )
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
            Log.d("NativeWavetableSynthesizer", "onResume() called")
            createNativeHandleIfNotExists()
        }
     }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d("NativeWavetableSynthesizer", "onPause() called")
        synchronized(nativeOperatorMutex) {
            Log.d("NativeWavetableSynthesizer", "onPause() called")

            if (nativeOperatorMutex == 0L) {
                Log.e("NativeWavetableSynthesizer", "Attempting to destroy a null synthesizer.")
                return
            }

            // Destroy the synthesizer
            delete(nativeOperatorHandle  )
            nativeOperatorHandle = 0L
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.d("NativeWavetableSynthesizer", "onDestroy() called")
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
        nativeOperatorHandle = create()
        // create handle

    }

    override  fun search(searchString : String) {

        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            search(nativeOperatorHandle)
        }

//        Log.d("OperationsLogging", "search() called with searchString: $searchString")
    }

    override  fun delete( id : Int): Boolean {
//        Log.d("OperationsLogging", "delete() called with id: $id")
        return true
    }

    override fun previous(id : Int): String {
//        Log.d("OperationsLogging", "previous() called with id: $id")
        return "previous"
    }
    override  fun next(id : Int): String {
//        Log.d("OperationsLogging", "next() called with id: $id")

        synchronized(nativeOperatorMutex) {
            createNativeHandleIfNotExists()
            next(nativeOperatorHandle)
        }

        return "next"
    }

    override  fun updateOrAdd( id : Int, description : String) :Boolean {
//        Log.d("OperationsLogging", "updateOrAdd() called with id: $id, description: $description")
        return true
    }

    override fun exit() {
//        Log.d("OperationsLogging", "exit() called")
    }



}