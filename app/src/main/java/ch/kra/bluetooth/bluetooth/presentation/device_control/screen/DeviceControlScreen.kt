package ch.kra.bluetooth.bluetooth.presentation.device_control.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.bluetooth.R
import ch.kra.bluetooth.bluetooth.presentation.device_control.DeviceControlViewModel
import ch.kra.bluetooth.core.UIEvent

@Composable
fun DeviceControlScreen(
    viewModel: DeviceControlViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val deviceAddress = viewModel.deviceAddress.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.NavigateBack -> {
                    navigateBack()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.device_control_header))
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Device address: $deviceAddress")
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(R.string.on))
            }

            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(R.string.off))
            }

            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(R.string.disconnect))
            }
        }
    }
}