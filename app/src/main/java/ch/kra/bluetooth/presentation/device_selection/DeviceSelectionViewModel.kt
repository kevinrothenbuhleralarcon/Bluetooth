package ch.kra.bluetooth.presentation.device_selection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.kra.bluetooth.core.Constant.GPS_REQUIRED
import ch.kra.bluetooth.core.Resource
import ch.kra.bluetooth.core.Tag.BLUETOOTH
import ch.kra.bluetooth.core.UIEvent
import ch.kra.bluetooth.domain.repository.BluetoothRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class DeviceSelectionViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) : ViewModel() {

    private val _devices: MutableState<DeviceState> = mutableStateOf(DeviceState())
    val devices: State<DeviceState> = _devices

    private val _pairedDevices: MutableState<Set<BluetoothDevice>> = mutableStateOf(emptySet())
    val pairedDevices: State<Set<BluetoothDevice>> = _pairedDevices

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        if (bluetoothRepository.isBlutoothEnabeled()) {
            _pairedDevices.value =  bluetoothRepository.getPairedDevices()
        } else {
            sendEvent(UIEvent.RequestBluetoothActivation)
        }
    }

    fun onEvent(event: DeviceSelectionListEvent) {
        when (event) {
            is DeviceSelectionListEvent.ScanForDevices -> {
                startDeviceScan()
            }

            is DeviceSelectionListEvent.BluetoothActivationResult -> {
                if (event.isActive) {
                    _pairedDevices.value = bluetoothRepository.getPairedDevices()
                } else {
                    sendEvent(UIEvent.RequestBluetoothActivation)
                }
            }
        }
    }

    private fun startDeviceScan() {
        viewModelScope.launch {
            bluetoothRepository.scanDevices().onEach { result ->
                when(result) {
                    is Resource.Success -> {
                        Log.i(BLUETOOTH, "Devices ${result.data}")
                        _devices.value = devices.value.copy(
                            devices = result.data,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> {
                        if (result.message == GPS_REQUIRED) {
                            sendEvent(UIEvent.RequestGPSActivation)
                        }
                    }

                    is Resource.Loading -> {
                        _devices.value = devices.value.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}