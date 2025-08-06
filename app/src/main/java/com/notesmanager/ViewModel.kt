package com.notesmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.notesmanager.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
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



    var _newNote by mutableStateOf(true)
    private set

    val newNote: Boolean
        get() {
            return _newNote
        }



    // Note ID ??
    var _noteId by mutableIntStateOf(0)
        private set

    val noteId :  Int
        get() {
            return _noteId
        }



    var _searchString by mutableStateOf("")
        private set
     val searchString: String
        get() {
            return _searchString
        }

// işelm sonucunu kullanıcıya göster
    var _alertMessage by mutableStateOf("")
        private set
    val alertMessage: String
        get() {
            return _alertMessage
        }

// onaydan sonra işlemi yürüt
    var _userCommand by mutableStateOf("")
        private set
    val userCommand: String
        get() {
            return _userCommand
        }



    private val _frequency = MutableLiveData(300f)
    val frequency: LiveData<Float>
        get() {
            return _frequency
        }
    private val frequencyRange = 40f..3000f


    /*

    init {
        operator = NativeOperator()
    }

     */

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
            if ( ( description == "")  && (! _newNote)  ) {
                _newNote = true
            }

            if (this._description.isEmpty()){
                _searchString = ""
            }
        }
    }

    fun search(searchString : String) {

        _searchString = searchString

        val retunValue = operator?.search(searchString) ?: ""

        if ( retunValue!= "No record found") {
            this._description = retunValue
            _newNote = false
        }else{
            this._alertMessage = "Kayıt Bulunamadı!"
        }

    }

    fun delete(id : Int) {
        if (operator?.delete(id) == true ){
           this._alertMessage = "İşlem başarılı\nKayıt silindi"

            val retunValue = operator?.previous(id , searchString) ?: ""
            if ( retunValue!= "No record found") {
               this._description = retunValue
                this._newNote = false
            }else{
                val retunValue = operator?.next(id , searchString) ?: ""
                if ( retunValue!= "No record found") {
                    this._description = retunValue
                    this._newNote = false
                }else{
                    this._description = ""
                }
            }
        }else{
            this._alertMessage = "Tekrar deneyin\nİşlem başarısız"
        }
    }

    fun updateOrAdd(id : Int, description : String) {
      if ( operator?.updateOrAdd(id, description, _newNote) == true ){
          this._alertMessage = "İşlem başarılı\nKayıt güncellendi"
      }else{
          this._alertMessage = "Tekrar deneyin\nİşlem başarısız"
      }
    }

    fun next(id: Int)  {

        val retunValue = operator?.next(id , searchString) ?: ""
        if ( retunValue!= "No record found") {
          _newNote = false
            this._description = retunValue
        }else{
            this._alertMessage = "Son kayıttasınız!"
        }

    }

    fun previous(id : Int){
        val retunValue = operator?.previous(id , searchString) ?: ""

        if ( retunValue != "No record found") {
            _newNote = false
            this._description = retunValue
        }else {
            this._alertMessage = "İlk kayıttasınız!"

        }
    }

    fun setUserCommand(user_comm : String, note : String  ){
        if ( ( ( user_comm == "DELETE" ) || (user_comm == "UPDATE") )  && note.isEmpty())  {
            this._alertMessage = "Not boş olamaz!!!"
            return
        }
        this._userCommand = user_comm
    }

    fun refreshMessageAndUserCommand(){

        this._userCommand = ""
        this._alertMessage = ""

    }

}