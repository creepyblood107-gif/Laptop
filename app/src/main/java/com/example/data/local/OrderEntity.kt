package com.example.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    indices = [
        Index(value = ["orderNumber"], unique = true),
        Index(value = ["status"]),
        Index(value = ["orderTime"]),
        Index(value = ["customerPhone"])
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val productId: Long = 0,
    val productName: String,
    val productPrice: Double,
    val condition: String = "Used",
    val imageUrl: String = "",
    val quantity: Int = 1,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String = "",
    val pickupLocation: String, // "Liluah Station Pickup", "Direct Shop Pickup"
    val pickupDate: String,
    val orderTime: Long = System.currentTimeMillis(),
    val status: String = "Pending", // "Pending", "Processing", "Delivered", "Cancelled"
    val paymentMethod: String = "Cash on Delivery", // "Cash on Delivery", "Online UPI"
    val paymentStatus: String = "Pending", // "Pending", "Approved", "Paid"
    val utrNumber: String = "",
    val guaranteeNote: String = "100% Material Ready Guarantee at Shop on Date"
)
