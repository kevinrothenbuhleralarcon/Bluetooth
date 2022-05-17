package ch.kra.bluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Looper
import android.util.Log
import ch.kra.bluetooth.core.Resource
import ch.kra.bluetooth.core.Tag
import ch.kra.bluetooth.core.Tag.BLUETOOTH
import ch.kra.bluetooth.data.remote.BluetoothScanReceiver
import ch.kra.bluetooth.domain.repository.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BluetoothRepositoryImpl(
    private val bluetoothAdapter: BluetoothAdapter,
    private val bluetoothScanReceiver: BluetoothScanReceiver
) : BluetoothRepository {

    @SuppressLint("MissingPermission")
    override fun scanDevices(): Flow<Resource<Set<BluetoothDevice>>> = flow {
        emit(Resource.Loading())
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
        Log.i(BLUETOOTH, "Start Scanning device: ${bluetoothAdapter.startDiscovery()}")

        Thread.sleep(10000L)
        bluetoothAdapter.cancelDiscovery()
        emit(Resource.Success(
            data = bluetoothScanReceiver.getDiscoveredDevices()
        ))

        /*Handler(Looper.getMainLooper()).postDelayed({
            Log.i(BLUETOOTH, "timeout")
            bluetoothAdapter.cancelDiscovery()
            emit(Resource.Success(
                data = bluetoothScanReceiver.getDiscoveredDevices()
            ))
        }, 10000L)*/
    }

    @SuppressLint("MissingPermission")
    override fun getPairedDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter.bondedDevices
    }

    override fun isBlutoothEnabeled(): Boolean {
        return bluetoothAdapter.isEnabled
    }
}