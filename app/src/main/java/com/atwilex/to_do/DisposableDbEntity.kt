package com.atwilex.to_do

import androidx.room.*

@Entity(tableName = "disposable",
    indices = [Index("id")])
data class DisposableDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val name: String
)
