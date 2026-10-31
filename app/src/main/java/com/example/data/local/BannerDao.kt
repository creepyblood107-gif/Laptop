package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BannerDao {
    @Query("SELECT * FROM banners WHERE isActive = 1 ORDER BY displayOrder ASC, slotNumber ASC, id ASC")
    fun getAllActiveBanners(): Flow<List<BannerEntity>>

    @Query("SELECT * FROM banners ORDER BY slotNumber ASC, displayOrder ASC, id ASC")
    fun getAllBanners(): Flow<List<BannerEntity>>

    @Query("SELECT * FROM banners WHERE slotNumber = :slot LIMIT 1")
    suspend fun getBannerBySlot(slot: Int): BannerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: BannerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanners(banners: List<BannerEntity>)

    @Update
    suspend fun updateBanner(banner: BannerEntity)

    @Delete
    suspend fun deleteBanner(banner: BannerEntity)

    @Query("DELETE FROM banners WHERE id = :id")
    suspend fun deleteBannerById(id: Long)
}
