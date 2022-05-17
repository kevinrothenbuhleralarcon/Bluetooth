package ch.kra.bluetooth.domain.repository

import android.bluetooth.BluetoothDevice
import ch.kra.bluetooth.core.Resource
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {
    fun scanDevices(): Flow<Resource<Set<BluetoothDevice>>>
    fun getPairedDevices(): Set<BluetoothDevice>
    fun isBlutoothEnabeled(): Boolean
}