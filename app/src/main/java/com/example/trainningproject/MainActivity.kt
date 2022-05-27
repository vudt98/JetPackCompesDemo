package com.example.trainningproject

import android.graphics.Typeface
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.trainningproject.ui.theme.ColorMessage
import com.example.trainningproject.ui.theme.MyButton
import com.example.trainningproject.utils.LocaleUtils


class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            SetupNavGraph(navHostController = navController)
        }
    }
}

@Composable
fun MyScreen(navHostController: NavHostController) {
    val viewModel: MainViewModel = viewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    val tts = TextToSpeedUtils.getInstance(LocalContext.current)

    LaunchedEffect(key1 = tts, block = {
        viewModel.initial(tts)
    })

    LocaleUtils.setLocale(LocalContext.current, viewModel.language)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                viewModel.cancelTTS()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical
                ),
        ) {
            MyTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp), title = "Jetpack Demo"
            )
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
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                SubmitButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(5.dp),
                    title = "Speed"
                ) {
                    if (!TextUtils.isEmpty(viewModel.message) || !TextUtils.isEmpty(viewModel.title)) {
                        viewModel.showMessage(true)
                    }
                    if (viewModel.isShow) {
                        val mess = "${viewModel.title} ${viewModel.message}"
                        viewModel.speak(mess)
                    }
                }


                SubmitButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(5.dp),
                    title = "Hide Message"
                ) {
                    viewModel.showMessage(false)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AnimatedVisibility(
                    visible = viewModel.isShow,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    ContentMessage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        content = "${viewModel.title} ${viewModel.message}",
                        viewModel.isShow
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            Row(
                modifier = Modifier
                    .padding(bottom = 15.dp),
            ) {
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.Blue
                    ),
                    onClick = {
                        viewModel.setLanguageText("vi")
                        navHostController.navigate(route = Screen.Second.passId(viewModel.message))
                    }) {
                    Text(text = "Load VN")
                }
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.Blue
                    ),
                    onClick = {
                        viewModel.setLanguageText("en")
                        navHostController.navigate(route = Screen.Second.passId(viewModel.message))
                    }) {
                    Text(text = "Load EN")
                }
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
    modifier: Modifier,
    title: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
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
    modifier: Modifier,
    title: String
) {
    Text(
        modifier = modifier,
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
    modifier: Modifier,
    content: String,
    visibility: Boolean
) {
    if (visibility) {
        Text(
            modifier = modifier,
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
    MyScreen(navHostController = rememberNavController())
}