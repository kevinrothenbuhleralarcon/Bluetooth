package ch.kra.bluetooth.bluetooth.presentation.device_selection.screen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.kra.bluetooth.R
import ch.kra.bluetooth.core.UIEvent
import ch.kra.bluetooth.bluetooth.presentation.device_selection.DeviceSelectionListEvent
import ch.kra.bluetooth.bluetooth.presentation.device_selection.DeviceSelectionViewModel


@SuppressLint("MissingPermission")
@Composable
fun DeviceSelectionScreen(
    viewModel: DeviceSelectionViewModel = hiltViewModel(),
    navigate: (UIEvent.Navigate) -> Unit
) {
    val pairedDevices = viewModel.pairedDevices.value
    val devicesState = viewModel.devices.value

    val context = LocalContext.current

    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.onEvent(DeviceSelectionListEvent.BluetoothActivationResult(result.resultCode == ComponentActivity.RESULT_OK))
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.RequestBluetoothActivation -> {
                    val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    activityResultLauncher.launch(enableBluetoothIntent)
                }

                is UIEvent.RequestGPSActivation -> {
                    val enableGPSIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(enableGPSIntent)
                }

                is UIEvent.Navigate -> {
                    navigate(event)
                }
                else -> {}
            }
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.device_selection_header),
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
                    text = stringResource(R.string.scan),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.available_devices),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (devicesState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                devicesState.devices.forEach { device ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .clickable {
                                viewModel.onEvent(DeviceSelectionListEvent.PairedDeviceClicked(device.address))
                            },
                        elevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = device.name ?: stringResource(R.string.undefined))
                            Text(text = device.address)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.paired_devices),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(10.dp))
            pairedDevices.forEach { device ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .clickable {
                            viewModel.onEvent(DeviceSelectionListEvent.PairedDeviceClicked(device.address))
                        },
                    elevation = 10.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = device.name ?: stringResource(R.string.undefined))
                        Text(text = device.address)
                    }
                }
            }
        }
    }
}
