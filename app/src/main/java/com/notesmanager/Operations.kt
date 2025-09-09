package com.notesmanager

interface Operations {
    suspend fun play()
    suspend fun stop()
    suspend fun isPlaying(): Boolean
    suspend fun setFrequency(frequencyInHz: Float)
    suspend fun setVolume(volumeInDb: Float)

    fun search(searchString: String): String
    fun delete(id: Int): Boolean
    fun next(id: Int, searchString: String): String
    fun previous(id: Int, searchString: String): String
    fun updateOrAdd(id: Int, description: String, isNew: Boolean): Boolean

    fun  synchronize(id: Int, ipAdress: String): Boolean


    fun exit()

}