package com.atwilex.to_do

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val dailyDao : DailyDao, private val disposableDao : DisposableDao) {

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

    suspend fun getDailyTab() : List<DailyDbEntity>{
        return withContext(Dispatchers.IO) { dailyDao.getTab() }
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