package com.atwilex.to_do

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [DailyDbEntity::class]
)
abstract class DailyDatabase : RoomDatabase(){
    abstract fun getDailyDao() : DailyDao
}