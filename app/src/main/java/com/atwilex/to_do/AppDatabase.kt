package com.atwilex.to_do

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//database init
@Database(
    version = 3,
    exportSchema = true,
    entities = [DailyDbEntity::class, DisposableDbEntity::class],
    autoMigrations = [AutoMigration(from = 2, to = 3)]
)
abstract class AppDatabase : RoomDatabase(){
    //Database migration
    companion object{
        val MIGRATION23 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS disposable (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)")
            }
        }
    }
    abstract fun getDailyDao() : DailyDao
    abstract fun getDisposableDao() : DisposableDao
}