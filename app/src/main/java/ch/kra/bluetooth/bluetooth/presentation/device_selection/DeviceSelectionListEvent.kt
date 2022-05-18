package ch.kra.bluetooth.bluetooth.presentation.device_selection

sealed class DeviceSelectionListEvent {
    object ScanForDevices: DeviceSelectionListEvent()
    data class BluetoothActivationResult(val isActive: Boolean): DeviceSelectionListEvent()
    data class PairedDeviceClicked(val address: String): DeviceSelectionListEvent()
}
