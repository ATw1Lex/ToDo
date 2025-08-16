package com.atwilex.to_do

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResetWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val repository = AppDependencies.appRepository

                repository.checkboxReset()

                Result.success()
            }catch (e : Exception){
                Result.failure()
            }
        }
    }
}