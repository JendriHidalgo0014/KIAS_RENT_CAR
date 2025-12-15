package edu.ucne.kias_rent_car.domain.usecase.Mensaje

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.syncworker.MensajeSyncWorker
import javax.inject.Inject

class TriggerSyncMensajeUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<MensajeSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}