package com.atwilex.to_do

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val dailyDao : DailyDao, private val disposableDao : DisposableDao,
                    private val additionalDao: AdditionalDao) {

    //Daily Tab's functions
    suspend fun insertNewDailyData(dailyDbEntity : DailyDbEntity) : Long{
        return withContext(Dispatchers.IO) { dailyDao.insertNewData(dailyDbEntity) }
    }

    suspend fun insertListOfTasks (taskList : List<DailyDbEntity>) {
        withContext(Dispatchers.IO) { dailyDao.insertListOfTasks(taskList) }
    }

    suspend fun removeDailyDataById(id : Long){
        withContext(Dispatchers.IO) { dailyDao.removeData(id) }
    }

    suspend fun updateDailyData(dailyDbEntity: DailyDbEntity){
        withContext(Dispatchers.IO) { dailyDao.updateTab(dailyDbEntity) }
    }

    suspend fun getDailyTab() : List<DailyDbEntity>{
        return withContext(Dispatchers.IO) { dailyDao.getTab() }
    }

    suspend fun getAnyDailyTasks(day : String) : List<DailyDbEntity>{
        return withContext(Dispatchers.IO) { dailyDao.getAnyDailyTasks(day) }
    }

    //Fun for streak system
    suspend fun getState(day : String) : List<Boolean> {
        return withContext(Dispatchers.IO) { dailyDao.getState(day) }
    }


    //Additional functions

    suspend fun getToday() : String? {
        return withContext(Dispatchers.IO) { additionalDao.getToday() }
    }

    suspend fun insertStreak(additionalDbEntity: AdditionalDbEntity){
        withContext(Dispatchers.IO) { additionalDao.insertStreak(additionalDbEntity) }
    }

    suspend fun getStreak() : Int {
        return withContext(Dispatchers.IO) { additionalDao.getStreak() }
    }

    //Disposable Tab's functions
    suspend fun insertNewDisposableData(disposableDbEntity: DisposableDbEntity) : Long{
        withContext(Dispatchers.IO) { disposableDao.insertNewData(disposableDbEntity) }
        return disposableDbEntity.id
    }

    suspend fun removeDisposableDataById(id : Long){
        withContext(Dispatchers.IO) { disposableDao.removeData(id) }
    }

    suspend fun getDisposableTab() : List<DisposableDbEntity>{
        return withContext(Dispatchers.IO) { disposableDao.getTab() }
    }
}