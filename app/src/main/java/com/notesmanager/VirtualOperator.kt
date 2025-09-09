package com.notesmanager

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver


class VirtualOperator : Operations, DefaultLifecycleObserver {

    private var isPlaying = false

    override suspend fun isPlaying(): Boolean {
        Log.d("OperationsLogging", "isPlaying() called. isPlaying: $isPlaying")
        return isPlaying

    }

    override suspend fun play() {
        Log.d("OperationsLogging", "play() called")
        isPlaying = true

    }

    override suspend fun stop() {
        Log.d("OperationsLogging", "stop() called")
        isPlaying = false

    }

    override suspend fun setFrequency(frequencyInHz: Float) {
        Log.d("OperationsLogging", "setFrequency() called with frequency: $frequencyInHz")

    }

    override suspend fun setVolume(volumeInDb: Float) {
        Log.d("OperationsLogging", "setVolume() called with volume: $volumeInDb")
    }

    override fun search(searchString: String): String {

        return searchString
        Log.d("OperationsLogging", "search() called with searchString: $searchString")
    }

    override fun delete(id: Int): Boolean {
        Log.d("OperationsLogging", "delete() called with id: $id")
        return true
    }

    override fun previous(id: Int, searchString: String): String {
        Log.d("OperationsLogging", "previous() called with id: $id")
        return "previous"
    }

    override fun next(id: Int, searchString: String): String {
        Log.d("OperationsLogging", "next() called with id: $id")
        return "next"
    }

    override fun updateOrAdd(id: Int, description: String, isNew: Boolean): Boolean {
        Log.d("OperationsLogging", "updateOrAdd() called with id: $id, description: $description")
        return true
    }

    override fun exit() {
        Log.d("OperationsLogging", "exit() called")
    }

    override fun synchronize( id: Int, ipAdress: String): Boolean{
        return true;
    }


}