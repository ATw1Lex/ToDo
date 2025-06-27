package com.atwilex.to_do

import android.content.Context
import androidx.room.Room

object AppDependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    fun isInit() : Boolean = ::applicationContext.isInitialized

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db").addMigrations(
            AppDatabase.MIGRATION12).build()
    }

    val dailyDao : DailyDao by lazy { appDatabase.getDailyDao() }

    val appRepository : AppRepository by lazy { AppRepository(dailyDao) }
}