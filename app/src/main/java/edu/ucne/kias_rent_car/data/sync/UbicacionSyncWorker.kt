package edu.ucne.kias_rent_car.data.syncworker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.kias_rent_car.domain.repository.UbicacionRepository

@HiltWorker
class UbicacionSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val ubicacionRepository: UbicacionRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            ubicacionRepository.refreshUbicaciones()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}