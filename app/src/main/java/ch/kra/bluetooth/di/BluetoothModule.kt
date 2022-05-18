package ch.kra.bluetooth.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import ch.kra.bluetooth.data.BluetoothRepositoryImpl
import ch.kra.bluetooth.data.remote.BluetoothScanReceiver
import ch.kra.bluetooth.domain.repository.BluetoothRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {

    @RequiresApi(Build.VERSION_CODES.M)
    @Provides
    @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        return bluetoothManager.adapter
    }

    @Provides
    @Singleton
    fun provideBluetoothScanReceiver(): BluetoothScanReceiver {
        return BluetoothScanReceiver()
    }

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideBluetoothRepository(
        bluetoothAdapter: BluetoothAdapter,
        bluetoothScanReceiver: BluetoothScanReceiver,
        locationManager: LocationManager
    ): BluetoothRepository {
        return BluetoothRepositoryImpl(
            bluetoothAdapter,
            bluetoothScanReceiver,
            locationManager
        )
    }
}