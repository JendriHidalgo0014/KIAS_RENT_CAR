package edu.ucne.kias_rent_car.data.syncworker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.kias_rent_car.data.remote.Resource
import edu.ucne.kias_rent_car.domain.repository.ReservacionRepository

@HiltWorker
class ReservacionSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val reservacionRepository: ReservacionRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return when (reservacionRepository.postPendingReservaciones()) {
            is Resource.Success -> Result.success()
            is Resource.Error -> Result.retry()
            else -> Result.failure()
        }
    }
}