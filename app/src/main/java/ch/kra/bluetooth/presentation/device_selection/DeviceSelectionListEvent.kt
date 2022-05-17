package ch.kra.bluetooth.presentation.device_selection

import android.bluetooth.BluetoothDevice

sealed class DeviceSelectionListEvent {
    object ScanForDevices: DeviceSelectionListEvent()
    data class BluetoothActivationResult(val isActive: Boolean): DeviceSelectionListEvent()
}
