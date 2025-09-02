package com.atwilex.to_do

import androidx.room.*

@Dao
interface AdditionalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStreak(additionalDbEntity: AdditionalDbEntity)

    //Get streak
    @Query("SELECT * FROM additional WHERE id = 1")
    fun getStreak() : AdditionalDbEntity

    //Update streak when you need set 0 to streak
    @Query("UPDATE additional SET streak = 0")
    fun zeroingStreak()

    //Update streak when you need set 0 to streak
    @Query("UPDATE additional SET streak = streak + 1 WHERE id = 1")
    fun incrementStreak()
}