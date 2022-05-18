package ch.kra.bluetooth.presentation.device_selection

import android.bluetooth.BluetoothDevice

data class DeviceState(
    val devices: Set<BluetoothDevice> = emptySet(),
    val isLoading: Boolean = false
)
