package ch.kra.bluetooth.bluetooth.presentation.device_control


sealed class DeviceControlListEvent {
    object OnOnClick: DeviceControlListEvent()
    object OnOffClick: DeviceControlListEvent()
    object OnDisconnectClick: DeviceControlListEvent()
    object OnBackPressed: DeviceControlListEvent()
}
