package edu.ucne.kias_rent_car.domain.usecase.Ubicacion

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.syncworker.UbicacionSyncWorker
import javax.inject.Inject

class TriggerSyncUbicacionUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<UbicacionSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}