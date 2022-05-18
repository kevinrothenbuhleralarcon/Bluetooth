package ch.kra.bluetooth.core

sealed class UIEvent {
    object RequestBluetoothActivation: UIEvent()
    object RequestGPSActivation: UIEvent()
    data class Navigate(val route: String): UIEvent()
    object NavigateBack: UIEvent()
}
