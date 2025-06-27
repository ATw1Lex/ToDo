package com.atwilex.to_do

import androidx.room.*

@Entity(tableName = "daily")
data class DailyDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val name: String,
    val state: Long
)