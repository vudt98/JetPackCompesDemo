package com.example.trainningproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun MySecondScreen(
    text: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .align(Alignment.Center)) {
            MyTitle(
                modifier = Modifier
                    .fillMaxWidth(), title = text
            )
            MyTitle(modifier = Modifier.fillMaxWidth(), title = stringResource(id = R.string.content_2))
        }
    }
}