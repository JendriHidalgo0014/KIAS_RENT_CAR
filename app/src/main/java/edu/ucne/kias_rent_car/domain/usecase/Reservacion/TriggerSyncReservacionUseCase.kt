package edu.ucne.kias_rent_car.domain.usecase.Reservacion

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.syncworker.ReservacionSyncWorker
import javax.inject.Inject

class TriggerSyncReservacionUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<ReservacionSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}