package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.BannerEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.StoreConfigEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

data class CartItem(
    val product: ProductEntity,
    val quantity: Int = 1
)

data class UserProfile(
    val name: String = "Rohit Kumar",
    val phone: String = "9876543210",
    val email: String = "rohit.kumar@gmail.com",
    val isGoogleLinked: Boolean = true
)

class LaptopsRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val productDao = db.productDao()
    private val orderDao = db.orderDao()
    private val notificationDao = db.notificationDao()
    private val bannerDao = db.bannerDao()
    private val storeConfigDao = db.storeConfigDao()

    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val allNotifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val allBanners: Flow<List<BannerEntity>> = bannerDao.getAllActiveBanners()
    val allAdminBanners: Flow<List<BannerEntity>> = bannerDao.getAllBanners()
    val storeConfig: Flow<StoreConfigEntity?> = storeConfigDao.getStoreConfig()
    val productCount: Flow<Int> = productDao.getProductCount()
    val orderCount: Flow<Int> = orderDao.getOrderCount()
    val totalRevenue: Flow<Double?> = orderDao.getTotalRevenue()

    // In-memory cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // User profile state
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun updateProfile(name: String, phone: String, email: String) {
        _userProfile.value = UserProfile(
            name = name.trim(),
            phone = phone.trim(),
            email = email.trim(),
            isGoogleLinked = true
        )
    }

    fun addToCart(product: ProductEntity) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _cartItems.value = current
    }

    fun removeFromCart(productId: Long) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun updateCartQuantity(productId: Long, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val updatedQty = current[index].quantity + delta
            if (updatedQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = updatedQty)
            }
            _cartItems.value = current
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    suspend fun placeInstantOrder(
        product: ProductEntity,
        quantity: Int = 1,
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        pickupLocation: String,
        pickupDate: String,
        paymentMethod: String,
        paymentStatus: String,
        utrNumber: String = ""
    ): OrderEntity {
        val orderNum = "#LPS${Random.nextInt(1010, 9999)}"
        val order = OrderEntity(
            orderNumber = orderNum,
            productId = product.id,
            productName = product.name,
            productPrice = product.price,
            condition = product.condition,
            imageUrl = product.imageUrl,
            quantity = quantity,
            customerName = customerName.ifBlank { _userProfile.value.name },
            customerPhone = customerPhone.ifBlank { _userProfile.value.phone },
            customerEmail = customerEmail.ifBlank { _userProfile.value.email },
            pickupLocation = pickupLocation,
            pickupDate = pickupDate,
            orderTime = System.currentTimeMillis(),
            status = if (paymentStatus == "Approved") "Processing" else "Pending",
            paymentMethod = paymentMethod,
            paymentStatus = paymentStatus,
            utrNumber = utrNumber,
            guaranteeNote = "100% Material Ready Guarantee at Shop on Date"
        )
        val generatedId = orderDao.insertOrder(order)
        return order.copy(id = generatedId)
    }

    suspend fun placeCartOrders(
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        pickupLocation: String,
        pickupDate: String,
        paymentMethod: String,
        paymentStatus: String,
        utrNumber: String = ""
    ): List<OrderEntity> {
        val createdOrders = mutableListOf<OrderEntity>()
        for (item in _cartItems.value) {
            val orderNum = "#LPS${Random.nextInt(1010, 9999)}"
            val order = OrderEntity(
                orderNumber = orderNum,
                productId = item.product.id,
                productName = item.product.name,
                productPrice = item.product.price,
                condition = item.product.condition,
                imageUrl = item.product.imageUrl,
                quantity = item.quantity,
                customerName = customerName.ifBlank { _userProfile.value.name },
                customerPhone = customerPhone.ifBlank { _userProfile.value.phone },
                customerEmail = customerEmail.ifBlank { _userProfile.value.email },
                pickupLocation = pickupLocation,
                pickupDate = pickupDate,
                orderTime = System.currentTimeMillis(),
                status = if (paymentStatus == "Approved") "Processing" else "Pending",
                paymentMethod = paymentMethod,
                paymentStatus = paymentStatus,
                utrNumber = utrNumber,
                guaranteeNote = "100% Material Ready Guarantee at Shop on Date"
            )
            val generatedId = orderDao.insertOrder(order)
            createdOrders.add(order.copy(id = generatedId))
        }
        clearCart()
        return createdOrders
    }

    suspend fun cancelOrder(orderId: Long): Boolean {
        val order = orderDao.getOrderById(orderId) ?: return false
        val hoursPassed = (System.currentTimeMillis() - order.orderTime) / (1000.0 * 60 * 60)
        return if (hoursPassed < 2.0 && order.status != "Cancelled" && order.status != "Delivered") {
            orderDao.updateOrderStatus(orderId, "Cancelled")
            true
        } else {
            false
        }
    }

    suspend fun addNewProduct(product: ProductEntity): Long {
        return productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(id: Long) {
        productDao.deleteProductById(id)
    }

    suspend fun updateStockStatus(id: Long, inStock: Boolean) {
        productDao.updateStockStatus(id, inStock)
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun broadcastNotification(title: String, message: String, targetAudience: String) {
        notificationDao.insertNotification(
            NotificationEntity(
                title = title,
                message = message,
                targetAudience = targetAudience
            )
        )
    }

    suspend fun insertBanner(banner: BannerEntity): Long {
        return bannerDao.insertBanner(banner)
    }

    suspend fun updateBanner(banner: BannerEntity) {
        bannerDao.updateBanner(banner)
    }

    suspend fun deleteBanner(id: Long) {
        bannerDao.deleteBannerById(id)
    }

    suspend fun saveStoreConfig(config: StoreConfigEntity) {
        storeConfigDao.setStoreConfig(config)
    }
}
