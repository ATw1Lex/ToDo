package com.atwilex.to_do

import androidx.room.*


//Entity for additional tab
@Entity(tableName = "additional")
data class AdditionalDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Long,
    @ColumnInfo(defaultValue = "0")
    val streak: Int
)