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
                //get Repository
                val repository = AppDependencies.appRepository
                val states = repository.getState()
                if(states.isEmpty()){
                    repository.incrementStreak()
                }
                else {
                    repository.zeroingStreak()
                }

                //Reset Checkboxes
                repository.checkboxReset()

                //Reload
                Runtime.getRuntime().exit(0)

                Result.success()
            }catch (e : Exception){
                Result.failure()
            }
        }
    }
}