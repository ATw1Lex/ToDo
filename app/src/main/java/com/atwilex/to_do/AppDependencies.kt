package com.atwilex.to_do

import android.content.Context
import androidx.room.Room

object AppDependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    //checking
    fun isInit() : Boolean = ::applicationContext.isInitialized

    //Database builder
    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").addMigrations(
            AppDatabase.MIGRATION).build()
    }

    val dailyDao : DailyDao by lazy { appDatabase.getDailyDao() }

    val disposableDao: DisposableDao by lazy { appDatabase.getDisposableDao() }

    val streakDao : StreakDao by lazy { appDatabase.getAdditionalDao() }

    val appRepository : AppRepository by lazy { AppRepository(dailyDao, disposableDao, streakDao) }
}