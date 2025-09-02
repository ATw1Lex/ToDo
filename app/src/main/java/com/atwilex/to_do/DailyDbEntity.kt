package com.atwilex.to_do

import androidx.room.*

//Entity for daily tab
@Entity(tableName = "daily")
data class DailyDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val name: String,
    val state: Long,

    @ColumnInfo(defaultValue = "0")
    val position: Int
)