package com.example.trainningproject

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class TextToSpeedUtils private constructor(context: Context) : TextToSpeech.OnInitListener {

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(context, this, TextToSpeech.Engine.EXTRA_SAMPLE_TEXT)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            textToSpeech.setSpeechRate(1f)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("Ahihi", "The Language not supported!")
            } else {
                Log.e("Ahihi", "The Language supported!")
            }
        }
    }

    companion object{
        var INSTACE: TextToSpeech? = null
        fun getInstance(context: Context) : TextToSpeech {
            if (INSTACE == null) {
                INSTACE = TextToSpeedUtils(context).textToSpeech
            } else {
                INSTACE
            }
            return INSTACE as TextToSpeech
        }
    }
}