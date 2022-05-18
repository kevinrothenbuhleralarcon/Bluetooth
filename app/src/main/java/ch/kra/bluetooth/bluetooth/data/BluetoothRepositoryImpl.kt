package ch.kra.bluetooth.bluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.location.LocationManager
import android.util.Log
import ch.kra.bluetooth.core.Constant.GPS_REQUIRED
import ch.kra.bluetooth.core.Constant.SCAN_TIME
import ch.kra.bluetooth.core.Resource
import ch.kra.bluetooth.core.Tag.BLUETOOTH
import ch.kra.bluetooth.bluetooth.data.remote.BluetoothScanReceiver
import ch.kra.bluetooth.bluetooth.domain.repository.BluetoothRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BluetoothRepositoryImpl(
    private val bluetoothAdapter: BluetoothAdapter,
    private val bluetoothScanReceiver: BluetoothScanReceiver,
    private val locationManager: LocationManager
) : BluetoothRepository {

    @SuppressLint("MissingPermission")
    override fun scanDevices(): Flow<Resource<Set<BluetoothDevice>>> = flow {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            emit(Resource.Loading())
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            Log.i(BLUETOOTH, "Start Scanning device: ${bluetoothAdapter.startDiscovery()}")

            delay(SCAN_TIME)
            bluetoothAdapter.cancelDiscovery()
            emit(
                Resource.Success(
                    data = bluetoothScanReceiver.getDiscoveredDevices()
                )
            )
        } else {
            emit(Resource.Error(message = GPS_REQUIRED))
        }
    }

    @SuppressLint("MissingPermission")
    override fun getPairedDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter.bondedDevices
    }

    override fun isBlutoothEnabeled(): Boolean {
        return bluetoothAdapter.isEnabled
    }
}