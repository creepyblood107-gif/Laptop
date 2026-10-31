package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderTime DESC")
    fun getOrdersByStatus(status: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    suspend fun getOrderById(id: Long): OrderEntity?

    @Query("SELECT * FROM orders WHERE customerPhone = :phone ORDER BY orderTime DESC")
    fun getOrdersByPhone(phone: String): Flow<List<OrderEntity>>

    @Query("SELECT COUNT(*) FROM orders")
    fun getOrderCount(): Flow<Int>

    @Query("SELECT SUM(productPrice * quantity) FROM orders WHERE status != 'Cancelled'")
    fun getTotalRevenue(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long

    @Update
    suspend fun updateOrder(order: OrderEntity)

    @Query("UPDATE orders SET status = :status WHERE id = :id")
    suspend fun updateOrderStatus(id: Long, status: String)

    @Query("UPDATE orders SET paymentStatus = :paymentStatus, utrNumber = :utr WHERE id = :id")
    suspend fun updateOrderPayment(id: Long, paymentStatus: String, utr: String)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)
}
