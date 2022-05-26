package com.example.trainningproject

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.*

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var title by mutableStateOf("")

    var message by mutableStateOf("")

    var isShow by mutableStateOf(false)

    var language by mutableStateOf("en")

    private lateinit var textToSpeechEngine: TextToSpeech

    val data = flow {
        val data = savedStateHandle.get<String>("Language") ?: "en"
        savedStateHandle["Language"] = data
        emit(data)
    }.stateIn(
        scope = viewModelScope + Dispatchers.IO,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = "en"
    )

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

    fun setLanguageText(lang: String) {
        language = lang
    }
}