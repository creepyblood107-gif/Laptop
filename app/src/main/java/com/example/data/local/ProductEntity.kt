package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["category"]),
        Index(value = ["condition"]),
        Index(value = ["isInStock"])
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String = "Generic",
    val price: Double,
    val originalPrice: Double = price * 1.25,
    val category: String, // "Laptop", "Accessories", "SSD/HDD", "Dongles", "Others"
    val condition: String = "Used", // "New", "Used", "Refurbished"
    val imageUrl: String,
    val processor: String = "",
    val ram: String = "",
    val storage: String = "",
    val display: String = "",
    val rating: Float = 4.5f,
    val reviewCount: Int = 42,
    val isInStock: Boolean = true,
    val stockCount: Int = 5,
    val description: String = "",
    val keyFeatures: String = "Tested & Verified | 6 Months Warranty | Ready at Shop",
    val createdAt: Long = System.currentTimeMillis()
)
