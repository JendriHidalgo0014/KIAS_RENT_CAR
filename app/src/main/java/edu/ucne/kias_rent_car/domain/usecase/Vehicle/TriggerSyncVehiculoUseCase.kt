package edu.ucne.kias_rent_car.domain.usecase.Vehicle

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.syncworker.VehicleSyncWorker
import javax.inject.Inject

class TriggerSyncVehiculoUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<VehicleSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}