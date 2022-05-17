package ch.kra.bluetooth.presentation.device_selection.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.bluetooth.core.Tag
import ch.kra.bluetooth.core.Tag.BLUETOOTH
import ch.kra.bluetooth.core.UIEvent
import ch.kra.bluetooth.presentation.device_selection.DeviceSelectionListEvent
import ch.kra.bluetooth.presentation.device_selection.DeviceSelectionViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect


@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun DeviceSelectionScreen(
    viewModel: DeviceSelectionViewModel = hiltViewModel(),
    requestBluetoothActivation: () -> Unit,
    bluetoothResultFlow: Flow<Boolean>
) {
    val pairedDevices = viewModel.pairedDevices.value
    val devices = viewModel.devices.value

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect{ event ->
            when (event) {
                is UIEvent.RequestBluetoothActivation -> {
                    requestBluetoothActivation()
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        bluetoothResultFlow.collect{
            viewModel.onEvent(DeviceSelectionListEvent.BluetoothActivationResult(it))
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bluetooth Connected List",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.onEvent(DeviceSelectionListEvent.ScanForDevices) }
            ) {
                Text(
                    text = "Scan",
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Available Devices",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(10.dp))
            devices.forEach { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    elevation = 10.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = device.name)
                        Text(text = device.address)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Paired Devices",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(10.dp))
            pairedDevices.forEach { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    elevation = 10.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = device.name)
                        Text(text = device.address)
                    }
                }
            }
        }
    }
}
