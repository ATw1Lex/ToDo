package com.atwilex.to_do

import androidx.room.*

//Entity for daily tab
@Entity(tableName = "daily")
data class DailyDbEntity(
    @PrimaryKey(autoGenerate = true) var id : Long,
    var name: String,
    var state: Long,

    @ColumnInfo(defaultValue = "04-10-2025")
    val day : String,

    @ColumnInfo(defaultValue = "0")
    val position: Int
)