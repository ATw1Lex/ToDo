package com.atwilex.to_do

import android.content.Context
import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

fun resetSchedule(context: Context){
    val calendar = Calendar.getInstance()
    val currentTime = calendar.timeInMillis

    calendar.add(Calendar.DAY_OF_YEAR, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val midnightTime = calendar.timeInMillis

    val initialDelay = midnightTime - currentTime

    val workRequest = PeriodicWorkRequestBuilder<ResetWorker>(24, TimeUnit.HOURS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork("midnight_reset", ExistingPeriodicWorkPolicy.KEEP, workRequest)
}

