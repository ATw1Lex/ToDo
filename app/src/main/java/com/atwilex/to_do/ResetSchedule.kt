package com.atwilex.to_do

import android.content.Context
import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

fun resetSchedule(context: Context){

    val currentTime = Calendar.getInstance()
    val midNight = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    if(currentTime.timeInMillis > midNight.timeInMillis){
        midNight.add(Calendar.DAY_OF_YEAR, 1)
    }

    val initialDelay = midNight.timeInMillis - currentTime.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<ResetWorker>(
        repeatInterval = 1,
        repeatIntervalTimeUnit = TimeUnit.DAYS
    ).setInitialDelay(initialDelay, TimeUnit.MILLISECONDS).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork("daily_task_reset", ExistingPeriodicWorkPolicy.KEEP, workRequest)
}

