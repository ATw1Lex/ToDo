package com.atwilex.to_do

import androidx.room.*

//Dao for disposable tab
@Dao
interface DisposableDao {
    @Insert(entity = DisposableDbEntity::class)
    fun insertNewData(data : DisposableDbEntity)

    @Query("DELETE FROM disposable WHERE id =:tabId")
    fun removeData(tabId : Long)

    @Query("SELECT * FROM disposable ORDER BY id DESC")
    fun getTab() : List<DisposableDbEntity>
}