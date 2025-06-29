package com.atwilex.to_do

import androidx.room.*

//Entity for disposable tab
@Entity(tableName = "disposable")
data class DisposableDbEntity(
    @PrimaryKey(autoGenerate = true) val id : Long,
    val name : String
)