package com.atwilex.to_do

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//database init
@Database(
    version = 6,
    exportSchema = true,
    entities = [DailyDbEntity::class, DisposableDbEntity::class, AdditionalDbEntity::class],
    autoMigrations = [AutoMigration(from = 5, to = 6)]
)
abstract class AppDatabase : RoomDatabase(){
    //Database migration
    companion object{
        val MIGRATION34 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS additional (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, streak INTEGER NOT NULL DEFAULT 0)")
            }
        }
    }
    abstract fun getDailyDao() : DailyDao
    abstract fun getDisposableDao() : DisposableDao
    abstract fun getAdditionalDao() : AdditionalDao
}