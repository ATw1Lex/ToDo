package com.atwilex.to_do

import androidx.room.*

@Dao
interface AdditionalDao {
    //Insert new day
    @Insert
    fun insertStreak(additionalDbEntity: AdditionalDbEntity)

    //Require today
    @Query("SELECT day FROM additional ORDER BY day DESC LIMIT 1")
    fun getToday() : String?

    //Require streak
    @Query("SELECT streak FROM additional ORDER BY day DESC LIMIT 1")
    fun getStreak() : Int
}