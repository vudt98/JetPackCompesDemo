package com.example.trainningproject

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.trainningproject.ui.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val viewModel: MainViewModel by viewModels()

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(this, this, TextToSpeech.Engine.EXTRA_SAMPLE_TEXT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MyScreen(viewModel)
            }
        }

        textToSpeech.let { viewModel.initial(it) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.language.collectLatest {
                    textToSpeech.language = it
                    val result = textToSpeech.setLanguage(it)
                    Toast.makeText(
                        this@MainActivity,
                        "${it.language} - $result",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(viewModel.language.value)
            textToSpeech.setSpeechRate(0.8f)
            textToSpeech.setPitch(1.3f)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("Ahihi", "The Language not supported!")
            } else {
                Log.e("Ahihi", "The Language supported!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}

@Composable
fun MyScreen(viewModel: MainViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        MyEditText(
            label = stringResource(id = R.string.title),
            message = viewModel.title,
            onMessageChange = { viewModel.onTitleChange(it) })
        MyEditText(
            label = stringResource(id = R.string.enter_message),
            message = viewModel.message,
            onMessageChange = { viewModel.onMessageChange(it) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SubmitButton(title = "Speed") {
                val mess = "${viewModel.title} ${viewModel.message}"
                viewModel.speak(mess)
            }
            SubmitButton(title = "Change Language") {
                viewModel.onLanguageChange()
            }
        }
    }
}

@Composable
fun MyEditText(
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    message: String,
    onMessageChange: (String) -> Unit
) {

    OutlinedTextField(
        value = message,
        onValueChange = onMessageChange,
        label = { Text(label) },
        textStyle = TextStyle(fontSize = 18.sp, color = Color.Blue),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
    )
}

@Composable
fun SubmitButton(
    title: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(text = title)
    }
}