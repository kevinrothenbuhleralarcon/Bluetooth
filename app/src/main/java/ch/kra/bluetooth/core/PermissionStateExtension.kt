package ch.kra.bluetooth.core

import com.google.accompanist.permissions.*

@ExperimentalPermissionsApi
fun MultiplePermissionsState.isPermanentlyDenied(): Boolean {
    return !allPermissionsGranted && ! shouldShowRationale
}

@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !status.isGranted && !status.shouldShowRationale
}