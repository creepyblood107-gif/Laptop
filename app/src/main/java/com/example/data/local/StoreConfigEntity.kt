package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_config")
data class StoreConfigEntity(
    @PrimaryKey
    val id: Int = 1,
    val storeTitle: String = "Laptops",
    val storeTagline: String = "Your Trusted Laptop Store",
    val searchPlaceholder: String = "Search laptops, accessories, specs...",
    val themeName: String = "BLUE", // "BLUE", "RED", "GREEN", "UNICORN", "GOLD", "CYAN"
    val bannerIntervalSeconds: Int = 5
)
