package edu.ucne.kias_rent_car.domain.usecase.Usuario

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.kias_rent_car.data.syncworker.UsuarioSyncWorker
import javax.inject.Inject

class TriggerSyncUsuarioUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<UsuarioSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}