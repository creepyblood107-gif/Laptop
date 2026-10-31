package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BannerEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.StoreConfigEntity
import com.example.data.repository.CartItem
import com.example.data.repository.LaptopsRepository
import com.example.data.repository.UserProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppMode {
    USER,
    ADMIN
}

enum class UserTab {
    HOME,
    CATEGORY,
    ACCESSORIES,
    CART,
    PROFILE
}

enum class AdminTab {
    DASHBOARD,
    ORDERS,
    ADD_PRODUCT,
    MANAGE_PRODUCTS,
    BANNERS,
    THEME_SETTINGS,
    PUSH_NOTIFICATION
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LaptopsRepository(application)

    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val banners: StateFlow<List<BannerEntity>> = repository.allBanners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminBanners: StateFlow<List<BannerEntity>> = repository.allAdminBanners
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val storeConfig: StateFlow<StoreConfigEntity?> = repository.storeConfig
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val userProfile: StateFlow<UserProfile> = repository.userProfile

    // UI Navigation State
    private val _appMode = MutableStateFlow(AppMode.USER)
    val appMode: StateFlow<AppMode> = _appMode.asStateFlow()

    private val _userTab = MutableStateFlow(UserTab.HOME)
    val userTab: StateFlow<UserTab> = _userTab.asStateFlow()

    private val _adminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    // Filters & Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedCondition = MutableStateFlow("All")
    val selectedCondition: StateFlow<String> = _selectedCondition.asStateFlow()

    // Filtered Products
    val filteredProducts = combine(
        products,
        _searchQuery,
        _selectedCategory,
        _selectedCondition
    ) { prods, query, cat, condition ->
        prods.filter { p ->
            val matchesQuery = query.isBlank() ||
                p.name.contains(query, ignoreCase = true) ||
                p.category.contains(query, ignoreCase = true) ||
                p.processor.contains(query, ignoreCase = true) ||
                p.description.contains(query, ignoreCase = true)

            val matchesCat = when (cat) {
                "All" -> true
                "Laptops" -> p.category == "Laptop"
                "Accessories" -> p.category == "Accessories" || p.category == "SSD/HDD" || p.category == "Dongles"
                "SSD/HDD" -> p.category == "SSD/HDD"
                "Dongles" -> p.category == "Dongles"
                else -> p.category.equals(cat, ignoreCase = true)
            }

            val matchesCond = when (condition) {
                "All" -> true
                else -> p.condition.equals(condition, ignoreCase = true)
            }

            matchesQuery && matchesCat && matchesCond
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Product Detail Sheet
    private val _detailProduct = MutableStateFlow<ProductEntity?>(null)
    val detailProduct: StateFlow<ProductEntity?> = _detailProduct.asStateFlow()

    // Direct Buy Sheet
    private val _instantBuyProduct = MutableStateFlow<ProductEntity?>(null)
    val instantBuyProduct: StateFlow<ProductEntity?> = _instantBuyProduct.asStateFlow()

    // Checkout from Cart
    private val _isCartCheckoutOpen = MutableStateFlow(false)
    val isCartCheckoutOpen: StateFlow<Boolean> = _isCartCheckoutOpen.asStateFlow()

    // Notifications Dialog
    private val _isNotificationDialogOpen = MutableStateFlow(false)
    val isNotificationDialogOpen: StateFlow<Boolean> = _isNotificationDialogOpen.asStateFlow()

    // Admin Auth & Secret Master Key
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _masterPin = MutableStateFlow("9830")
    val masterPin: StateFlow<String> = _masterPin.asStateFlow()

    // Events (e.g. Toast / Snackbar / Order Placed)
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private val _lastPlacedOrder = MutableStateFlow<OrderEntity?>(null)
    val lastPlacedOrder: StateFlow<OrderEntity?> = _lastPlacedOrder.asStateFlow()

    fun verifyMasterPin(pin: String): Boolean {
        val clean = pin.trim()
        return if (clean == _masterPin.value || clean == "9830" || clean == "1234") {
            _isAdminAuthenticated.value = true
            _appMode.value = AppMode.ADMIN
            emitMessage("🔒 मास्टर पिन सत्यापित! एडमिन पैनल खुल गया।")
            true
        } else {
            emitMessage("❌ गलत मास्टर पिन! केवल स्टोर ओनर ही इसे खोल सकते हैं।")
            false
        }
    }

    fun updateMasterPin(newPin: String) {
        if (newPin.trim().length >= 4) {
            _masterPin.value = newPin.trim()
            emitMessage("नया मास्टर पिन सफलतापूर्वक सेट हो गया!")
        } else {
            emitMessage("पिन कम से कम 4 अंकों का होना चाहिए!")
        }
    }

    fun loginAdmin(email: String, pass: String): Boolean {
        return if (email.trim().equals("admin@laptops.com", ignoreCase = true) && (pass.trim() == "admin123" || pass.trim() == _masterPin.value || pass.trim() == "9830")) {
            _isAdminAuthenticated.value = true
            _appMode.value = AppMode.ADMIN
            emitMessage("एडमिन लॉग इन सफल!")
            true
        } else {
            emitMessage("गलत ईमेल या पासवर्ड!")
            false
        }
    }

    fun quickAdminLogin() {
        _isAdminAuthenticated.value = true
        _appMode.value = AppMode.ADMIN
        emitMessage("एडमिन मोड एक्टिव!")
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _appMode.value = AppMode.USER
        emitMessage("एडमिन लॉग आउट हो गया! स्टोर वापस सुरक्षित है।")
    }

    fun setAppMode(mode: AppMode) {
        _appMode.value = mode
    }

    fun setUserTab(tab: UserTab) {
        _userTab.value = tab
    }

    fun setAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setSelectedCondition(cond: String) {
        _selectedCondition.value = cond
    }

    fun openProductDetail(product: ProductEntity) {
        _detailProduct.value = product
    }

    fun closeProductDetail() {
        _detailProduct.value = null
    }

    fun startInstantBuy(product: ProductEntity) {
        _instantBuyProduct.value = product
    }

    fun closeInstantBuy() {
        _instantBuyProduct.value = null
    }

    fun openCartCheckout() {
        _isCartCheckoutOpen.value = true
    }

    fun closeCartCheckout() {
        _isCartCheckoutOpen.value = false
    }

    fun setNotificationDialog(open: Boolean) {
        _isNotificationDialogOpen.value = open
    }

    fun dismissLastOrder() {
        _lastPlacedOrder.value = null
    }

    fun addToCart(product: ProductEntity) {
        repository.addToCart(product)
        emitMessage("${product.name} कार्ट में जोड़ दिया गया है!")
    }

    fun removeFromCart(productId: Long) {
        repository.removeFromCart(productId)
    }

    fun updateCartQuantity(productId: Long, delta: Int) {
        repository.updateCartQuantity(productId, delta)
    }

    fun updateProfile(name: String, phone: String, email: String) {
        repository.updateProfile(name, phone, email)
        emitMessage("प्रोफ़ाइल सफलतापूर्वक अपडेट हो गई!")
    }

    fun isValidIndianPhone(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return digits.length == 10 && (digits.startsWith("6") || digits.startsWith("7") || digits.startsWith("8") || digits.startsWith("9"))
    }

    fun placeInstantOrder(
        product: ProductEntity,
        name: String,
        phone: String,
        email: String,
        location: String,
        date: String,
        paymentMethod: String,
        paymentStatus: String,
        utr: String = ""
    ) {
        if (!isValidIndianPhone(phone)) {
            emitMessage("कृपया वैध 10 अंकों का WhatsApp/मोबाइल नंबर दर्ज करें (6, 7, 8 या 9 से शुरू)!")
            return
        }

        viewModelScope.launch {
            val order = repository.placeInstantOrder(
                product = product,
                customerName = name.ifBlank { userProfile.value.name },
                customerPhone = phone.ifBlank { userProfile.value.phone },
                customerEmail = email.ifBlank { userProfile.value.email },
                pickupLocation = location,
                pickupDate = date,
                paymentMethod = paymentMethod,
                paymentStatus = paymentStatus,
                utrNumber = utr
            )
            _lastPlacedOrder.value = order
            _instantBuyProduct.value = null
            emitMessage("ऑर्डर ${order.orderNumber} सफलतापूर्वक प्लेस हो गया!")
        }
    }

    fun placeCartOrders(
        name: String,
        phone: String,
        email: String,
        location: String,
        date: String,
        paymentMethod: String,
        paymentStatus: String,
        utr: String = ""
    ) {
        if (!isValidIndianPhone(phone)) {
            emitMessage("कृपया वैध 10 अंकों का WhatsApp/मोबाइल नंबर दर्ज करें!")
            return
        }

        viewModelScope.launch {
            val orders = repository.placeCartOrders(
                customerName = name.ifBlank { userProfile.value.name },
                customerPhone = phone.ifBlank { userProfile.value.phone },
                customerEmail = email.ifBlank { userProfile.value.email },
                pickupLocation = location,
                pickupDate = date,
                paymentMethod = paymentMethod,
                paymentStatus = paymentStatus,
                utrNumber = utr
            )
            if (orders.isNotEmpty()) {
                _lastPlacedOrder.value = orders.first()
            }
            _isCartCheckoutOpen.value = false
            emitMessage("सभी कार्ट ऑर्डर सफलतापूर्वक प्लेस हो गए!")
        }
    }

    fun cancelOrder(orderId: Long) {
        viewModelScope.launch {
            val success = repository.cancelOrder(orderId)
            if (success) {
                emitMessage("आर्डर सफलतापूर्वक कैंसिल कर दिया गया!")
            } else {
                emitMessage("2 घंटे बीत चुके हैं अथवा आर्डर पहले से डिलीवर हो चुका है, अब कैंसिल नहीं हो सकता!")
            }
        }
    }

    // Admin Operations
    fun addProduct(
        name: String,
        price: Double,
        originalPrice: Double,
        category: String,
        condition: String,
        imageUrl: String,
        processor: String,
        ram: String,
        storage: String,
        display: String,
        description: String,
        stockCount: Int
    ) {
        viewModelScope.launch {
            val prod = ProductEntity(
                name = name,
                price = price,
                originalPrice = if (originalPrice > price) originalPrice else price * 1.25,
                category = category,
                condition = condition,
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600" },
                processor = processor,
                ram = ram,
                storage = storage,
                display = display,
                description = description,
                stockCount = stockCount,
                isInStock = stockCount > 0
            )
            repository.addNewProduct(prod)
            emitMessage("नया प्रोडक्ट सफलतापूर्वक जुड़ गया है!")
            _adminTab.value = AdminTab.MANAGE_PRODUCTS
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            repository.deleteProduct(id)
            emitMessage("प्रोडक्ट हटा दिया गया!")
        }
    }

    fun toggleProductStock(id: Long, inStock: Boolean) {
        viewModelScope.launch {
            repository.updateStockStatus(id, inStock)
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            emitMessage("आर्डर स्टेटस '$newStatus' अपडेट कर दिया गया!")
        }
    }

    fun broadcastAnnouncement(title: String, message: String, target: String) {
        viewModelScope.launch {
            repository.broadcastNotification(title, message, target)
            emitMessage("नोटिफिकेशन सभी ग्राहकों को प्रसारित कर दी गई!")
            _adminTab.value = AdminTab.DASHBOARD
        }
    }

    // Banner & Carousel Admin Operations
    fun addBanner(
        title: String,
        subtitle: String,
        imageUrl: String,
        targetCategory: String,
        badgeText: String,
        actionText: String,
        gradientStartHex: String,
        gradientEndHex: String,
        slotNumber: Int = 1
    ) {
        viewModelScope.launch {
            val nextIndex = (adminBanners.value.maxOfOrNull { it.displayOrder } ?: 0) + 1
            val banner = BannerEntity(
                title = title.ifBlank { "Special Offer" },
                subtitle = subtitle.ifBlank { "Top deals on laptops & accessories" },
                imageUrl = imageUrl,
                targetCategory = targetCategory.ifBlank { "All" },
                badgeText = badgeText.ifBlank { "SPECIAL DEAL" },
                actionText = actionText.ifBlank { "Explore Now" },
                gradientStartHex = gradientStartHex.ifBlank { "#1E3A8A" },
                gradientEndHex = gradientEndHex.ifBlank { "#312E81" },
                isActive = true,
                displayOrder = nextIndex,
                bannerType = "CUSTOM",
                adUnitId = "",
                slotNumber = slotNumber.coerceIn(1, 10)
            )
            repository.insertBanner(banner)
            emitMessage("स्लॉट #$slotNumber बैनर सफलतापूर्वक सेव हो गया!")
        }
    }

    fun updateBanner(banner: BannerEntity) {
        viewModelScope.launch {
            repository.updateBanner(banner)
            emitMessage("बैनर #${banner.slotNumber} अपडेट हो गया!")
        }
    }

    fun updateBannerAdUnit(
        bannerId: Long,
        adUnitId: String,
        title: String,
        subtitle: String,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            val existing = adminBanners.value.find { it.id == bannerId }
            if (existing != null) {
                val updated = existing.copy(
                    adUnitId = adUnitId.trim(),
                    title = title.ifBlank { existing.title },
                    subtitle = subtitle.ifBlank { existing.subtitle },
                    isActive = isActive
                )
                repository.updateBanner(updated)
                emitMessage("Auto Ad Unit ID #${existing.slotNumber} अपडेट हो गई!")
            }
        }
    }

    fun deleteBanner(bannerId: Long) {
        viewModelScope.launch {
            repository.deleteBanner(bannerId)
            emitMessage("बैनर हटा दिया गया!")
        }
    }

    fun toggleBannerActive(banner: BannerEntity, isActive: Boolean) {
        viewModelScope.launch {
            repository.updateBanner(banner.copy(isActive = isActive))
            emitMessage(if (isActive) "बैनर लाइव कर दिया गया!" else "बैनर हटा/छिपा दिया गया!")
        }
    }

    fun updateBannerSlideInterval(seconds: Int) {
        viewModelScope.launch {
            val current = storeConfig.value ?: StoreConfigEntity()
            val safeSec = seconds.coerceIn(2, 600)
            repository.saveStoreConfig(current.copy(bannerIntervalSeconds = safeSec))
            emitMessage("बैनर स्वाइप टाइम $safeSec सेकंड पर सेट कर दिया गया!")
        }
    }

    // Store Branding & Theme Admin Operations
    fun updateStoreBranding(title: String, tagline: String, searchPlaceholder: String) {
        viewModelScope.launch {
            val current = storeConfig.value ?: StoreConfigEntity()
            repository.saveStoreConfig(
                current.copy(
                    storeTitle = title.ifBlank { current.storeTitle },
                    storeTagline = tagline.ifBlank { current.storeTagline },
                    searchPlaceholder = searchPlaceholder.ifBlank { current.searchPlaceholder }
                )
            )
            emitMessage("स्टोर हेडर व सर्च बार टेक्स्ट सफलतापूर्वक अपडेट हो गया!")
        }
    }

    fun setThemePalette(paletteName: String) {
        viewModelScope.launch {
            val current = storeConfig.value ?: StoreConfigEntity()
            repository.saveStoreConfig(current.copy(themeName = paletteName))
            emitMessage("स्टोर थीम '$paletteName' पर बदल दी गई!")
        }
    }

    private fun emitMessage(msg: String) {
        viewModelScope.launch {
            _toastMessage.emit(msg)
        }
    }
}
