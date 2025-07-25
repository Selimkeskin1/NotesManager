package com.notesmanager

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ViewModel : androidx.lifecycle.ViewModel() {

//    var operator: VirtualOperator? = null
    var operator: NativeOperator? = null
    private val _volume = MutableLiveData(-24f)
    val volume: LiveData<Float>
        get() {
            return _volume
        }
    val volumeRange = (-60f)..0f



    // NOTE !!!
    var _description by mutableStateOf("")
        private set

    val description: String
        get() {
            return _description
        }



    // Note ID ??
    var _noteId by mutableIntStateOf(0)
        private set

    val noteId :  Int
        get() {
            return _noteId
        }


    private val _frequency = MutableLiveData(300f)
    val frequency: LiveData<Float>
        get() {
            return _frequency
        }
    private val frequencyRange = 40f..3000f
    fun setVolume(volumeInDb: Float) {
        _volume.value = volumeInDb
        viewModelScope.launch {
            operator?.setVolume(volumeInDb)
        }
    }

    fun playClicked(){
        // play() and stop() are suspended functions => we must launch a coroutine
        viewModelScope.launch {
            if (operator?.isPlaying() == true) {
                operator?.stop()
            } else {
                operator?.play()
            }
            updatePlayButtonLabel()
        }
    }

    private val _playButtonLabel = MutableLiveData(R.string.play)
    val playButtonLabel: LiveData<Int>
        get() {
            return _playButtonLabel
        }

    private fun updatePlayButtonLabel() {
        viewModelScope.launch {
            if (operator?.isPlaying() == true) {
                _playButtonLabel.value = R.string.stop
            } else {
                _playButtonLabel.value = R.string.play
            }
        }
    }

    fun applyParameters() {
        viewModelScope.launch {
            operator?.setFrequency(frequency.value!!)
            operator?.setVolume(volume.value!!)
            updatePlayButtonLabel()
        }
    }

    fun updateDescription(description: String, type: Int) {
        if ( type == 1 ) {
            this._description = description
        }
    }

    fun search(searchString : String) {
//        operator?.search(searchString)
    }

    fun delete(id : Int) {
        operator?.delete(id)
    }

    fun updateOrAdd(id : Int, description : String) {
        operator?.updateOrAdd(id, description)
    }

    fun next(id: Int) : String  {
        operator?.next(id)
        return "Next"
    }

    fun previous(id : Int): String{
        operator?.previous(id)
        return "previous"
    }

}