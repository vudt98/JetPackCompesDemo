package com.example.trainningproject

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    var title by mutableStateOf("")

    var message by mutableStateOf("")

    var isShow by mutableStateOf(false)

    private lateinit var textToSpeechEngine: TextToSpeech

    fun onTitleChange(title: String) {
        this.title = title
    }

    fun onMessageChange(mess: String) {
        message = mess
    }

    fun initial(engine: TextToSpeech) = viewModelScope.launch {
        textToSpeechEngine = engine
    }

    fun speak(mess: String) = viewModelScope.launch {
        textToSpeechEngine.speak(mess, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun showMessage(show: Boolean) {
        isShow = show
    }


}