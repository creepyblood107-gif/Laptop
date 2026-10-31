package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreConfigDao {
    @Query("SELECT * FROM store_config WHERE id = 1 LIMIT 1")
    fun getStoreConfig(): Flow<StoreConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setStoreConfig(config: StoreConfigEntity)

    @Update
    suspend fun updateStoreConfig(config: StoreConfigEntity)
}
