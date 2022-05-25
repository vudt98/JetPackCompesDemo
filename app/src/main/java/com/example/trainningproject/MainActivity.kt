package com.example.trainningproject

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.trainningproject.ui.theme.ColorMessage
import com.example.trainningproject.ui.theme.MyButton
import com.example.trainningproject.utils.LocaleUtils
import java.util.*


class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val viewModel: MainViewModel by viewModels()

    lateinit var navController: NavHostController

    private val textToSpeech: TextToSpeech by lazy {
        TextToSpeech(this, this, TextToSpeech.Engine.EXTRA_SAMPLE_TEXT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val content = LocalContext.current
            changeLanguage(content, "en")

            navController = rememberNavController()
            SetupNavGraph(navHostController = navController, viewModel)

        }

        textToSpeech.let { viewModel.initial(it) }

    }

    private fun changeLanguage(context: Context, lang: String) {
        LocaleUtils.setLocale(context, lang)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)
            textToSpeech.setSpeechRate(1f)
//            textToSpeech.setPitch(1.3f)

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
fun MyScreen(viewModel: MainViewModel, navHostController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(modifier = Modifier.padding(top = 15.dp)) {
                MyTitle(title = "Jetpack Demo")
            }
            MyEditText(
                label = stringResource(id = R.string.content_1),
                message = viewModel.title,
                onMessageChange = { viewModel.onTitleChange(it) })
            MyEditText(
                label = stringResource(id = R.string.enter_message),
                message = viewModel.message,
                onMessageChange = { viewModel.onMessageChange(it) }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                ) {
                    SubmitButton(title = "Speed") {
                        if (!TextUtils.isEmpty(viewModel.message) || !TextUtils.isEmpty(viewModel.title)) {
                            viewModel.showMessage(true)
                        }
                        if (viewModel.isShow) {
                            val mess = "${viewModel.title} ${viewModel.message}"
                            viewModel.speak(mess)
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                ) {
                    SubmitButton(title = "Hide Message") {
                        viewModel.showMessage(false)
                    }
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.Center),
        ) {
            ContentMessage(content = "${viewModel.title} ${viewModel.message}", viewModel.isShow)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 15.dp),
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = Color.Blue
                ),
                onClick = {
                    navHostController.navigate(route = Screen.Second.passId(viewModel.message))
                }) {
                Text(text = "Open Second Screen")
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
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MyButton,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 1.dp
        ),
    ) {
        Text(text = title)
    }
}

@Composable
fun MyTitle(
    title: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        color = Color.Magenta,
        fontFamily = FontFamily(
            typeface = Typeface.DEFAULT_BOLD
        ),
        textAlign = TextAlign.Center,
        fontSize = 30.sp
    )
}

@Composable
fun ContentMessage(
    content: String,
    visibility: Boolean
) {
    if (visibility) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = content,
            color = ColorMessage,
            fontFamily = FontFamily(
                typeface = Typeface.DEFAULT_BOLD
            ),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ContentMessage(content = "Ahihi", false)
//    MyScreen(viewModel = MainViewModel())
}