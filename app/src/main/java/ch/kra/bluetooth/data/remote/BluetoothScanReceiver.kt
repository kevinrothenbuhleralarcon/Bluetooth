package ch.kra.bluetooth.data.remote

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ch.kra.bluetooth.core.Tag

class BluetoothScanReceiver: BroadcastReceiver() {

    private var discoveredDevices: Set<BluetoothDevice> = emptySet()

    fun getDiscoveredDevices(): Set<BluetoothDevice> {
        return discoveredDevices
    }

    override fun onReceive(context: Context?, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                Log.i(Tag.BLUETOOTH, "onReceive: Device found")
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.let {
                    Log.i(Tag.BLUETOOTH, "onReceive: Device not null")
                    discoveredDevices = discoveredDevices.plus(it)
                }
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                Log.i(Tag.BLUETOOTH, "onReceive: Started Discovery")
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                Log.i(Tag.BLUETOOTH, "onReceive: Finished Discovery")
            }
        }
    }
}