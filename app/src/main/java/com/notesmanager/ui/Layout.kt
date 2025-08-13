package com.notesmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.notesmanager.R
import com.notesmanager.MainScreenViewModel
import  androidx.compose.ui.text.*
import  androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextDirection

@Composable
fun MainLayout(
    modifier: Modifier,
    viewModel: MainScreenViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

//             WavetableSelectionPanel(modifier, viewModel)
//            ControlsPanel(modifier, viewModel)
            MainScreen(modifier, viewModel)

        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier, appViewModel: MainScreenViewModel
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = appViewModel.description,
        onValueChange = { appViewModel.updateDescription(it, 1) },
        label = { Text(stringResource(R.string.description)) },
        minLines = 15,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )

// arama butonu
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { appViewModel.search(appViewModel.description) }
    )
    {
        Text(
            text = stringResource(R.string.search_tr),
            style = TextStyle(color = Color.Black, textDirection = TextDirection.Content)
        )
    }

// İleri butonu
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { appViewModel.next(10) }
    )
    {
        Text(
            text = stringResource(R.string.next_tr),
            style = TextStyle(color = Color.Black, textDirection = TextDirection.Content)
        )
    }


// Geri butonu
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { appViewModel.previous(10) }
    )
    {
        Text(
            text = stringResource(R.string.previous_tr),
            style = TextStyle(color = Color.Black, textDirection = TextDirection.Content)
        )
    }


    // güncelleme ekleme butonu
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
//        onClick = {appViewModel.updateOrAdd(0, appViewModel.description)}
        onClick = { appViewModel.setUserCommand("UPDATE", appViewModel.description) }
    )
    {
        Text(
            text = stringResource(R.string.update_tr),
            style = TextStyle(color = Color.Black, textDirection = TextDirection.Content)
        )
    }

    // silme butonu
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
//        onClick = {appViewModel.delete(10)}
        onClick = { appViewModel.setUserCommand("DELETE", appViewModel.description) }
    )
    {
        Text(
            text = stringResource(R.string.delete_tr),
            style = TextStyle(color = Color.Black, textDirection = TextDirection.Content)
        )
    }

    // çıkış butonu
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = {}
    )
    {
        Text(
            text = stringResource(R.string.exit_tr),
            style = TextStyle(color = Color.Black, textDirection = TextDirection.Content)
        )
    }





    if (!(appViewModel.userCommand.isEmpty())) {
        when (appViewModel.userCommand) {
            "DELETE" -> {
                popUpToContinue(
                    onConfirm = { appViewModel.delete(10) },
                    onDismiss = { appViewModel.refreshMessageAndUserCommand() }
                )
            }

            "UPDATE" -> {
                popUpToContinue(
                    onConfirm = { appViewModel.updateOrAdd(10, appViewModel.description) },
                    onDismiss = { appViewModel.refreshMessageAndUserCommand() }
                )
            }
        }
    }

    if (!(appViewModel.alertMessage.isEmpty())) {
        AlertDialog(
            onDismissRequest = { appViewModel.refreshMessageAndUserCommand() },
            confirmButton = {
                TextButton(onClick = { appViewModel.refreshMessageAndUserCommand() })
                { Text(text = stringResource(R.string.continue_tr)) }
            },
            text = { Text(appViewModel.alertMessage) },
        )
    }


}

@Composable
private fun ControlsPanel(
    modifier: Modifier,
    appViewModel: MainScreenViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PitchControl(modifier, appViewModel)
            PlayControl(modifier, appViewModel)
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            VolumeControl(modifier, appViewModel)
        }
    }
}

@Composable
private fun PlayControl(modifier: Modifier, appViewModel: MainScreenViewModel) {
    // The label of the play button is now an observable state, an instance of State<Int?>.
    // State<Int?> is used because the label is the id value of the resource string.
    // Thanks to the fact that the composable observes the label,
    // the composable will be recomposed (redrawn) when the observed state changes.
    val playButtonLabel = appViewModel.playButtonLabel.observeAsState()

    PlayControlContent(
        modifier = modifier,
        // onClick handler now simply notifies the ViewModel that it has been clicked
        onClick = {
            appViewModel.playClicked()
        },
        // playButtonLabel will never be null; if it is, then we have a serious implementation issue
        buttonLabel = stringResource(playButtonLabel.value!!)
    )
}

@Composable
private fun PlayControlContent(modifier: Modifier, onClick: () -> Unit, buttonLabel: String) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(buttonLabel)
    }
}

@Composable
private fun PitchControl(
    modifier: Modifier,
    appViewModel: MainScreenViewModel
) {
    // if the frequency changes, recompose this composable
    val frequency = appViewModel.frequency.observeAsState()
    // the slider position state is hoisted by this composable; no need to embed it into
    // the ViewModel, which ideally, shouldn't be aware of the UI.
    // When the slider position changes, this composable will be recomposed as we explained in
    // the UI tutorial.


}


@Composable
private fun VolumeControl(modifier: Modifier, appViewModel: MainScreenViewModel) {
    // volume value is now an observable state; that means that the composable will be
    // recomposed (redrawn) when the observed state changes.
    val volume = appViewModel.volume.observeAsState()

    VolumeControlContent(
        modifier = modifier,
        // volume value should never be null; if it is, there's a serious implementation issue
        volume = volume.value!!,
        // use the value range from the ViewModel
        volumeRange = appViewModel.volumeRange,
        // on volume slider change, just update the ViewModel
        onValueChange = { appViewModel.setVolume(it) })
}

@Composable
private fun VolumeControlContent(
    modifier: Modifier,
    volume: Float,
    volumeRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    // The volume slider should take around 1/4 of the screen height
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sliderHeight = screenHeight / 4

    Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .offset(y = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    )
    {
        Slider(
            value = volume,
            onValueChange = onValueChange,
            modifier = modifier
                .width(sliderHeight.dp)
                .rotate(270f),
            valueRange = volumeRange
        )
    }
    Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
}

@Composable
private fun WavetableSelectionPanel(
    modifier: Modifier,
    appViewModel: MainScreenViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}

@Composable
fun popUpToContinue(onConfirm: () -> Unit, onDismiss: () -> Unit): Unit {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel_tr)) }
        },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onConfirm()

            }

            ) { Text(stringResource(R.string.continue_tr)) }
        },
        text = { Text(stringResource(R.string.question_continue_tr)) },
    )
}



