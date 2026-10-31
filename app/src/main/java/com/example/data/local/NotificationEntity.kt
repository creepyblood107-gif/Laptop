package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val message: String,
    val targetAudience: String = "All Users",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
