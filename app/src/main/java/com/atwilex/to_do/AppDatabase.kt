package com.atwilex.to_do

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 2,
    exportSchema = true,
    entities = [DailyDbEntity::class],
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabase : RoomDatabase(){
    companion object{
        val MIGRATION12 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS daily (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "name TEXT NOT NULL," +
                        "state INTEGER NOT NULL)")

            }
        }
    }
    abstract fun getDailyDao() : DailyDao
}