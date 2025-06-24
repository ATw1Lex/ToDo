package com.atwilex.to_do

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyRepository(private val dailyDao : DailyDao) {

    suspend fun insertNewData(dailyDbEntity : DailyDbEntity) : Long{
        withContext(Dispatchers.IO) { dailyDao.insertNewData(dailyDbEntity) }
        return dailyDbEntity.id
    }

    suspend fun removeDataById(id : Long){
        withContext(Dispatchers.IO) { dailyDao.deleteTabData(id) }
    }

    suspend fun updateData(dailyDbEntity: DailyDbEntity){
        withContext(Dispatchers.IO) { dailyDao.updateTab(dailyDbEntity) }
    }

    suspend fun getDailyTab() : List<DailyDbEntity>{
        return withContext(Dispatchers.IO) { dailyDao.getTab() }
    }
}