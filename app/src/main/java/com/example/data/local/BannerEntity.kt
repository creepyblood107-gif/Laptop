package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subtitle: String,
    val badgeText: String = "100% QUALITY TESTED",
    val imageUrl: String = "",
    val actionText: String = "Explore Now",
    val targetCategory: String = "Laptop",
    val gradientStartHex: String = "#1E3A8A",
    val gradientEndHex: String = "#312E81",
    val isActive: Boolean = true,
    val displayOrder: Int = 0,
    val bannerType: String = "CUSTOM", // "CUSTOM" or "AUTO_AD"
    val adUnitId: String = "",         // 300x250 Banner Ad Unit ID (e.g. AdMob / AdSense / AI Network)
    val slotNumber: Int = 1            // 1 to 10 (1-6 Custom, 7-10 Auto Ad)
)
