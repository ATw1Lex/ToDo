package com.atwilex.to_do

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DailyDao {
    @Insert(entity = DailyDbEntity::class)
    fun insertNewData(data: DailyDbEntity)

    @Query("DELETE FROM daily WHERE id = :tabId")
    fun deleteTabData(tabId : Long)

    @Query("SELECT * FROM daily ORDER BY id DESC")
    fun getTab() : List<DailyDbEntity>

    @Update
    fun updateTab(tabDbEntity: DailyDbEntity)
}