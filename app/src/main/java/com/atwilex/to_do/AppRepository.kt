package com.atwilex.to_do

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val dailyDao : DailyDao, private val disposableDao : DisposableDao,
                    private val additionalDao: AdditionalDao) {

    //Daily Tab's functions
    suspend fun insertNewDailyData(dailyDbEntity : DailyDbEntity) : Long{
        withContext(Dispatchers.IO) { dailyDao.insertNewData(dailyDbEntity) }
        return dailyDbEntity.id
    }

    suspend fun removeDailyDataById(id : Long){
        withContext(Dispatchers.IO) { dailyDao.removeData(id) }
    }

    suspend fun updateDailyData(dailyDbEntity: DailyDbEntity){
        withContext(Dispatchers.IO) { dailyDao.updateTab(dailyDbEntity) }
    }

    suspend fun checkboxReset(){
        withContext(Dispatchers.IO) { dailyDao.checkboxReset() }
    }

    suspend fun getDailyTab() : List<DailyDbEntity>{
        return withContext(Dispatchers.IO) { dailyDao.getTab() }
    }

    //Fun for streak system
    suspend fun getState() : List<Boolean> {
        return withContext(Dispatchers.IO) { dailyDao.getState() }
    }


    //Additional functions

    suspend fun insertStreak(additionalDbEntity: AdditionalDbEntity){
        withContext(Dispatchers.IO) { additionalDao.insertStreak(additionalDbEntity) }
    }

    suspend fun getStreak() : AdditionalDbEntity{
        return withContext(Dispatchers.IO) { additionalDao.getStreak() }
    }

    suspend fun zeroingStreak(){
        withContext(Dispatchers.IO) { additionalDao.zeroingStreak() }
    }

    suspend fun incrementStreak(){
        withContext(Dispatchers.IO) { additionalDao.incrementStreak() }
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