package ch.kra.bluetooth.core

sealed class UIEvent {
    object RequestBluetoothActivation: UIEvent()
    object RequestGPSActivation: UIEvent()
}
