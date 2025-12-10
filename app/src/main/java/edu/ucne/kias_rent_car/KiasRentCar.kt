package edu.ucne.kias_rent_car

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import edu.ucne.kias_rent_car.data.sync.SyncTrigger
import edu.ucne.kias_rent_car.domain.repository.UbicacionRepository
import edu.ucne.kias_rent_car.domain.repository.VehicleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class KiasRentCar : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var ubicacionRepository: UbicacionRepository

    @Inject
    lateinit var vehicleRepository: VehicleRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            try {
                ubicacionRepository.refreshUbicaciones()
                vehicleRepository.refreshVehicles()
            } catch (_: Exception) { }
        }
        SyncTrigger.schedulePeriodicSync(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}