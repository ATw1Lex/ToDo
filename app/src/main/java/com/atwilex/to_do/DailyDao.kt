package com.atwilex.to_do

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

//Dao for daily tab
@Dao
interface DailyDao {
    @Insert()
    fun insertNewData(dailyDbEntity: DailyDbEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTasks(tasks : List<DailyDbEntity>)

    @Query("DELETE FROM daily WHERE id = :tabId")
    fun removeData(tabId : Long)

    @Query("SELECT * FROM daily ORDER BY position ASC")
    fun getTab() : List<DailyDbEntity>

    @Update
    fun updateTab(dailyDbEntity: DailyDbEntity)

    @Query("UPDATE daily SET state = 0")
    fun checkboxReset()

    //Query for streak system
    @Query("SELECT * FROM daily WHERE day = :day ORDER BY position ASC")
    fun getOldTasks(day : String) : List<DailyDbEntity>

    @Query("SELECT state FROM daily WHERE state = 0 AND day = :day")
    fun getState(day : String): List<Boolean>
}