package com.notesmanager

interface Operations {
    suspend fun play()
    suspend fun stop()
    suspend fun isPlaying() : Boolean
    suspend fun setFrequency(frequencyInHz: Float)
    suspend fun setVolume(volumeInDb: Float)

    fun search(searchString : String)
    fun delete( id : Int) : Boolean
    fun next(id : Int): String
    fun previous(id : Int) : String
    fun updateOrAdd( id : Int, description : String) :Boolean

    fun exit()

}