package ch.kra.bluetooth.bluetooth.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import ch.kra.bluetooth.R
import ch.kra.bluetooth.core.isPermanentlyDenied
import ch.kra.bluetooth.bluetooth.data.remote.BluetoothScanReceiver
import ch.kra.bluetooth.ui.theme.BluetoothTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var bReceiver: BluetoothScanReceiver

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(bReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        registerReceiver(bReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
        registerReceiver(bReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))

        setContent {
            BluetoothTheme {
                val lifeCycleOwner = LocalLifecycleOwner.current

                val permissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

                DisposableEffect(
                    key1 = lifeCycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                permissionState.launchMultiplePermissionRequest()
                            }
                        }
                        lifeCycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifeCycleOwner.lifecycle.removeObserver(observer)
                        }
                    })

                var showDialog by remember {
                    mutableStateOf(false)
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    when {
                        permissionState.allPermissionsGranted -> {
                            val navController = rememberNavController()
                            Navigation(navController = navController)
                        }
                        permissionState.shouldShowRationale -> {
                            showDialog = true
                            ShowPermissionDialog(
                                showDialog = showDialog,
                                message = getString(R.string.rational)
                            ) {
                                showDialog = false
                                permissionState.launchMultiplePermissionRequest()
                            }
                        }

                        permissionState.isPermanentlyDenied() -> {
                            showDialog = true
                            ShowPermissionDialog(
                                showDialog = showDialog,
                                message = getString(R.string.permanently_denied)
                            ) {
                                showDialog = false
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bReceiver)
    }
}

@Composable
fun ShowPermissionDialog(
    showDialog: Boolean,
    message: String,
    onClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            title = {
                Text(text = stringResource(R.string.permission_title))
            },
            text = {
                Text(text = message)
            },
            confirmButton = {
                Button(onClick = { onClick() }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )
    }
}


