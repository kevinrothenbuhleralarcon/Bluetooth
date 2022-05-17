package ch.kra.bluetooth.core

sealed class Routes(val route: String) {
    object DeviceSelection: Routes("device_selection")
}
