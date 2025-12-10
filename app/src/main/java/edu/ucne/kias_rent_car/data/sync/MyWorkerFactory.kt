package edu.ucne.kias_rent_car.data.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.hilt.work.HiltWorkerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyWorkerFactory @Inject constructor(
    private val hiltWorkerFactory: HiltWorkerFactory
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return hiltWorkerFactory.createWorker(appContext, workerClassName, workerParameters)
    }
}