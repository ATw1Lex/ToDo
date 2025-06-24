package com.atwilex.to_do

import android.content.Context
import androidx.room.Room

object DependenciesDaily {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    fun isInit() : Boolean = ::applicationContext.isInitialized

    private val dailyDatabase: DailyDatabase by lazy {
        Room.databaseBuilder(applicationContext, DailyDatabase::class.java, "database.db").build()
    }

    val dailyDao : DailyDao by lazy { dailyDatabase.getDailyDao() }

    val dailyRepository : DailyRepository by lazy { DailyRepository(dailyDao) }
}