package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        OrderEntity::class,
        NotificationEntity::class,
        BannerEntity::class,
        StoreConfigEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao
    abstract fun bannerDao(): BannerDao
    abstract fun storeConfigDao(): StoreConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "laptops_store_master.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabasePrepopulateCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabasePrepopulateCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    populateSeedData(database)
                }
            }
        }

        private suspend fun populateSeedData(db: AppDatabase) {
            val productDao = db.productDao()
            val orderDao = db.orderDao()
            val notificationDao = db.notificationDao()

            val initialProducts = listOf(
                ProductEntity(
                    id = 1,
                    name = "Dell Latitude 7490",
                    brand = "Dell",
                    price = 21999.0,
                    originalPrice = 32999.0,
                    category = "Laptop",
                    condition = "Used",
                    imageUrl = "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600",
                    processor = "Intel Core i5 8th Gen",
                    ram = "8GB DDR4",
                    storage = "256GB SSD",
                    display = "14\" FHD Anti-Glare",
                    rating = 4.3f,
                    reviewCount = 84,
                    isInStock = true,
                    stockCount = 6,
                    description = "Enterprise grade business laptop in pristine condition. Super fast SSD boot, lightweight chassis, backlit keyboard, and healthy battery life.",
                    keyFeatures = "Intel Core i5 8th Gen | 8GB RAM | 256GB SSD | 14\" FHD | Tested & Verified"
                ),
                ProductEntity(
                    id = 2,
                    name = "Dell XPS 13",
                    brand = "Dell",
                    price = 54999.0,
                    originalPrice = 75000.0,
                    category = "Laptop",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=600",
                    processor = "Intel Core i7 10th Gen",
                    ram = "16GB LPDDR4x",
                    storage = "512GB NVMe SSD",
                    display = "13.3\" InfinityEdge 4K UHD",
                    rating = 4.8f,
                    reviewCount = 134,
                    isInStock = true,
                    stockCount = 4,
                    description = "Flagship ultrabook with ultra-thin bezels, aluminum machined body, and stunning 4K display. Ideal for executives, creators, and programmers.",
                    keyFeatures = "Intel Core i7 | 16GB RAM | 512GB SSD | 4K InfinityEdge | Factory Sealed"
                ),
                ProductEntity(
                    id = 3,
                    name = "HP Pavilion 15",
                    brand = "HP",
                    price = 42999.0,
                    originalPrice = 58000.0,
                    category = "Laptop",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=600",
                    processor = "AMD Ryzen 5 5600H",
                    ram = "16GB DDR4",
                    storage = "512GB PCIe SSD",
                    display = "15.6\" IPS 144Hz FHD",
                    rating = 4.6f,
                    reviewCount = 98,
                    isInStock = true,
                    stockCount = 7,
                    description = "High performance laptop with Radeon graphics, micro-edge display, dual speakers by B&O, and rapid charging support.",
                    keyFeatures = "Ryzen 5 5600H | 16GB RAM | 512GB SSD | 15.6\" 144Hz | Brand New"
                ),
                ProductEntity(
                    id = 4,
                    name = "Lenovo ThinkPad T480",
                    brand = "Lenovo",
                    price = 28999.0,
                    originalPrice = 42000.0,
                    category = "Laptop",
                    condition = "Used",
                    imageUrl = "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=600",
                    processor = "Intel Core i5 8th Gen",
                    ram = "16GB DDR4",
                    storage = "512GB SSD",
                    display = "14\" IPS Matte FHD",
                    rating = 4.4f,
                    reviewCount = 76,
                    isInStock = true,
                    stockCount = 5,
                    description = "Legendary military-tested durability, spill-resistant keyboard, dual hot-swap battery technology, and legendary ThinkPad typing experience.",
                    keyFeatures = "Intel Core i5 | 16GB RAM | 512GB SSD | Dual Battery | Grade A Condition"
                ),
                ProductEntity(
                    id = 5,
                    name = "Apple MacBook Air M1",
                    brand = "Apple",
                    price = 68999.0,
                    originalPrice = 89900.0,
                    category = "Laptop",
                    condition = "Refurbished",
                    imageUrl = "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600",
                    processor = "Apple M1 8-Core CPU",
                    ram = "8GB Unified Memory",
                    storage = "256GB SSD",
                    display = "13.3\" Retina True Tone",
                    rating = 4.7f,
                    reviewCount = 112,
                    isInStock = true,
                    stockCount = 3,
                    description = "Silent fanless architecture, all-day 18 hour battery life, crystal clear Retina display with P3 wide color, certified refurbished with warranty.",
                    keyFeatures = "Apple M1 Chip | 8GB Unified | 256GB SSD | 18h Battery | 100% Tested"
                ),
                ProductEntity(
                    id = 6,
                    name = "HP ProBook 640 G5",
                    brand = "HP",
                    price = 22499.0,
                    originalPrice = 35000.0,
                    category = "Laptop",
                    condition = "Refurbished",
                    imageUrl = "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600",
                    processor = "Intel Core i5 8th Gen",
                    ram = "8GB DDR4",
                    storage = "256GB SSD",
                    display = "14\" Anti-Glare FHD",
                    rating = 4.4f,
                    reviewCount = 55,
                    isInStock = true,
                    stockCount = 8,
                    description = "Reliable business laptop with sturdy metal hinge, USB Type-C, HDMI port, and crisp audio. Excellent choice for office and academic use.",
                    keyFeatures = "Intel Core i5 | 8GB RAM | 256GB SSD | Type-C & HDMI | Ready for Pickup"
                ),
                // Accessories matching screenshots
                ProductEntity(
                    id = 7,
                    name = "Samsung 870 EVO SSD 250GB",
                    brand = "Samsung",
                    price = 3499.0,
                    originalPrice = 4500.0,
                    category = "SSD/HDD",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=600",
                    processor = "SATA III 6Gb/s",
                    ram = "V-NAND TLC",
                    storage = "250GB Capacity",
                    display = "Sequential Read 560MB/s",
                    rating = 4.6f,
                    reviewCount = 58,
                    isInStock = true,
                    stockCount = 15,
                    description = "The world's favorite SSD. Unrivaled reliability, up to 560MB/s sequential read speeds to speed up your laptop.",
                    keyFeatures = "250GB SATA SSD | 560MB/s Read | 5 Year Warranty | Brand New"
                ),
                ProductEntity(
                    id = 8,
                    name = "WD 1TB Hard Disk (HDD)",
                    brand = "Western Digital",
                    price = 3199.0,
                    originalPrice = 4200.0,
                    category = "SSD/HDD",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600",
                    processor = "5400 RPM",
                    ram = "128MB Cache",
                    storage = "1TB (1000GB)",
                    display = "2.5\" Laptop Form Factor",
                    rating = 4.4f,
                    reviewCount = 72,
                    isInStock = true,
                    stockCount = 12,
                    description = "High capacity internal 2.5\" mobile storage for photos, movies, games, and large files. Reliable WD Blue series.",
                    keyFeatures = "1TB Storage | 2.5 Inch Internal | WD Blue Series | Brand New"
                ),
                ProductEntity(
                    id = 9,
                    name = "Type-C to HDMI Dongle Hub",
                    brand = "Generic",
                    price = 1299.0,
                    originalPrice = 1999.0,
                    category = "Dongles",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1622445262464-84b1456045b6?w=600",
                    processor = "4K @ 60Hz Video",
                    ram = "USB 3.0 Port",
                    storage = "PD 100W Pass-through",
                    display = "Aluminum Grey Alloy",
                    rating = 4.3f,
                    reviewCount = 56,
                    isInStock = true,
                    stockCount = 20,
                    description = "Connect your laptop to any external 4K monitor, TV, or projector. Supports USB 3.0 data transfer and fast pass-through charging.",
                    keyFeatures = "4K HDMI | USB 3.0 Hub | 100W PD Charging | Plug & Play"
                ),
                ProductEntity(
                    id = 10,
                    name = "Laptop Cooling Pad Stand",
                    brand = "TechGear",
                    price = 899.0,
                    originalPrice = 1499.0,
                    category = "Accessories",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600",
                    processor = "Dual High-Speed Fans",
                    ram = "Blue LED Glow",
                    storage = "5 Angle Ergonomic",
                    display = "Fits up to 17\" Laptops",
                    rating = 4.2f,
                    reviewCount = 48,
                    isInStock = true,
                    stockCount = 14,
                    description = "Prevents laptop thermal throttling during heavy multitasking or gaming. Ultra quiet dual fans with ergonomic height adjustments.",
                    keyFeatures = "Dual Silent Fans | 5 Height Angles | Blue LED | Extra USB Port"
                ),
                ProductEntity(
                    id = 11,
                    name = "Dell Wireless Mouse",
                    brand = "Dell",
                    price = 749.0,
                    originalPrice = 1199.0,
                    category = "Accessories",
                    condition = "New",
                    imageUrl = "https://images.unsplash.com/photo-1527814050087-3793815479db?w=600",
                    processor = "1600 DPI Optical",
                    ram = "2.4GHz Wireless",
                    storage = "Plug & Play Nano Receiver",
                    display = "Matte Black",
                    rating = 4.5f,
                    reviewCount = 63,
                    isInStock = true,
                    stockCount = 25,
                    description = "Ergonomic wireless mouse with 18-month battery life, ambidextrous shape, and smooth optical tracking on almost any surface.",
                    keyFeatures = "2.4GHz Wireless | 1600 DPI | 18 Month Battery | Plug & Play"
                )
            )
            productDao.insertProducts(initialProducts)

            val now = System.currentTimeMillis()
            val initialOrders = listOf(
                OrderEntity(
                    orderNumber = "#LPS1001",
                    productId = 1,
                    productName = "Dell Latitude 7490",
                    productPrice = 21999.0,
                    condition = "Used",
                    imageUrl = "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600",
                    quantity = 1,
                    customerName = "Rahul Sharma",
                    customerPhone = "9876543210",
                    customerEmail = "rahul.sharma@gmail.com",
                    pickupLocation = "Liluah Station Pickup",
                    pickupDate = "Tomorrow (11:00 AM)",
                    orderTime = now - (30 * 60 * 1000), // 30 minutes ago
                    status = "Processing",
                    paymentMethod = "Cash on Delivery",
                    paymentStatus = "Pending",
                    guaranteeNote = "100% Tested Laptop Ready at Liluah Station"
                ),
                OrderEntity(
                    orderNumber = "#LPS1000",
                    productId = 6,
                    productName = "HP ProBook 640 G5",
                    productPrice = 22499.0,
                    condition = "Refurbished",
                    imageUrl = "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600",
                    quantity = 1,
                    customerName = "Amit Kumar Verma",
                    customerPhone = "9830123456",
                    customerEmail = "amitverma@gmail.com",
                    pickupLocation = "Direct Shop Pickup",
                    pickupDate = "Today (05:00 PM)",
                    orderTime = now - (4 * 60 * 60 * 1000), // 4 hours ago
                    status = "Delivered",
                    paymentMethod = "Online UPI",
                    paymentStatus = "Approved",
                    utrNumber = "UPI43091876521",
                    guaranteeNote = "Delivered & Verified by customer"
                ),
                OrderEntity(
                    orderNumber = "#LPS0999",
                    productId = 4,
                    productName = "Lenovo ThinkPad T480",
                    productPrice = 28999.0,
                    condition = "Used",
                    imageUrl = "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=600",
                    quantity = 1,
                    customerName = "Sneha Mondal",
                    customerPhone = "7003456789",
                    customerEmail = "sneha.mondal@gmail.com",
                    pickupLocation = "Direct Shop Pickup",
                    pickupDate = "Day After Tomorrow",
                    orderTime = now - (15 * 60 * 1000), // 15 mins ago
                    status = "Pending",
                    paymentMethod = "Cash on Delivery",
                    paymentStatus = "Pending",
                    guaranteeNote = "Stock reserved for pickup"
                ),
                OrderEntity(
                    orderNumber = "#LPS0998",
                    productId = 1,
                    productName = "Dell Latitude 7490",
                    productPrice = 21999.0,
                    condition = "Used",
                    imageUrl = "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600",
                    quantity = 1,
                    customerName = "Vikram Singh",
                    customerPhone = "8123456780",
                    customerEmail = "vikram.singh@gmail.com",
                    pickupLocation = "Liluah Station Pickup",
                    pickupDate = "24 Oct 2026",
                    orderTime = now - (24 * 60 * 60 * 1000),
                    status = "Delivered",
                    paymentMethod = "Cash on Delivery",
                    paymentStatus = "Paid",
                    guaranteeNote = "100% Guaranteed Fulfilled"
                )
            )
            for (ord in initialOrders) {
                orderDao.insertOrder(ord)
            }

            val initialNotifications = listOf(
                NotificationEntity(
                    title = "🎉 New Laptop Stock Arrived!",
                    message = "Dell Latitude 7490 and Lenovo ThinkPad T480 now in stock with guaranteed testing & 6 months warranty.",
                    timestamp = now - 3600000
                ),
                NotificationEntity(
                    title = "🛡️ 100% Pickup Guarantee",
                    message = "Book any laptop with Cash on Delivery — your unit will be packed, tested, and waiting for you at the shop or Liluah Station on your chosen date!",
                    timestamp = now - 7200000
                )
            )
            for (notif in initialNotifications) {
                notificationDao.insertNotification(notif)
            }

            val bannerDao = db.bannerDao()
            val initialBanners = listOf(
                // 6 Custom Slots (Owner can post/edit)
                BannerEntity(
                    id = 1,
                    title = "Best Laptops, Best Prices",
                    subtitle = "New & Used Laptops With Trusted Quality Guarantee",
                    badgeText = "100% QUALITY TESTED",
                    imageUrl = "",
                    actionText = "Explore Now",
                    targetCategory = "Laptop",
                    gradientStartHex = "#1E3A8A",
                    gradientEndHex = "#312E81",
                    isActive = true,
                    displayOrder = 1,
                    bannerType = "CUSTOM",
                    adUnitId = "",
                    slotNumber = 1
                ),
                BannerEntity(
                    id = 2,
                    title = "⚡ Supercharge Speed: SSD & RAM Upgrades",
                    subtitle = "Samsung EVO & WD NVMe at lowest wholesale price with free installation",
                    badgeText = "BEST UPGRADE DEALS",
                    imageUrl = "",
                    actionText = "View Upgrades",
                    targetCategory = "SSD/HDD",
                    gradientStartHex = "#064E3B",
                    gradientEndHex = "#0F766E",
                    isActive = true,
                    displayOrder = 2,
                    bannerType = "CUSTOM",
                    adUnitId = "",
                    slotNumber = 2
                ),
                BannerEntity(
                    id = 3,
                    title = "Certified ThinkPads & MacBooks",
                    subtitle = "Enterprise durability tested with 6 Months Comprehensive Store Warranty",
                    badgeText = "LIMITED TIME OFFER",
                    imageUrl = "",
                    actionText = "Grab Deal",
                    targetCategory = "Laptop",
                    gradientStartHex = "#701A75",
                    gradientEndHex = "#4C0519",
                    isActive = true,
                    displayOrder = 3,
                    bannerType = "CUSTOM",
                    adUnitId = "",
                    slotNumber = 3
                ),
                BannerEntity(
                    id = 4,
                    title = "Gaming & Creator Series Laptops",
                    subtitle = "Dedicated RTX Graphics & High Refresh Rate Displays Ready",
                    badgeText = "HIGH PERFORMANCE",
                    imageUrl = "",
                    actionText = "Explore Deals",
                    targetCategory = "Laptop",
                    gradientStartHex = "#92400E",
                    gradientEndHex = "#451A03",
                    isActive = true,
                    displayOrder = 4,
                    bannerType = "CUSTOM",
                    adUnitId = "",
                    slotNumber = 4
                ),
                BannerEntity(
                    id = 5,
                    title = "Essential Laptop Dongles & Hubs",
                    subtitle = "Type-C Multiport Adapters, 4K HDMI & Fast Charging Hubs",
                    badgeText = "HOT ACCESSORIES",
                    imageUrl = "",
                    actionText = "Shop Hubs",
                    targetCategory = "Dongles",
                    gradientStartHex = "#0F172A",
                    gradientEndHex = "#1E293B",
                    isActive = true,
                    displayOrder = 5,
                    bannerType = "CUSTOM",
                    adUnitId = "",
                    slotNumber = 5
                ),
                BannerEntity(
                    id = 6,
                    title = "Liluah Station Express Pickup",
                    subtitle = "Test any laptop in person before paying. Zero risk guarantee!",
                    badgeText = "100% STORE PICKUP",
                    imageUrl = "",
                    actionText = "Check Location",
                    targetCategory = "All",
                    gradientStartHex = "#1E3A8A",
                    gradientEndHex = "#172554",
                    isActive = true,
                    displayOrder = 6,
                    bannerType = "CUSTOM",
                    adUnitId = "",
                    slotNumber = 6
                ),
                // 4 Auto / Ad Unit Slots (7 to 10)
                BannerEntity(
                    id = 7,
                    title = "Google AdSense • Tech Network",
                    subtitle = "300x250 Medium Rectangle Certified Partner Advertisement",
                    badgeText = "SPONSORED AD",
                    imageUrl = "",
                    actionText = "Visit Sponsor",
                    targetCategory = "All",
                    gradientStartHex = "#0C4A6E",
                    gradientEndHex = "#082F49",
                    isActive = true,
                    displayOrder = 7,
                    bannerType = "AUTO_AD",
                    adUnitId = "ca-app-pub-3940256099942544/6300978111",
                    slotNumber = 7
                ),
                BannerEntity(
                    id = 8,
                    title = "AdMob Media • Brand Placement",
                    subtitle = "Interactive 300x250 High eCPM Auto Rotating Placement",
                    badgeText = "AUTO AD UNIT #2",
                    imageUrl = "",
                    actionText = "Learn More",
                    targetCategory = "All",
                    gradientStartHex = "#14532D",
                    gradientEndHex = "#052E16",
                    isActive = true,
                    displayOrder = 8,
                    bannerType = "AUTO_AD",
                    adUnitId = "ca-app-pub-3940256099942544/1033173712",
                    slotNumber = 8
                ),
                BannerEntity(
                    id = 9,
                    title = "AI Smart Ad Network • Electronics",
                    subtitle = "Contextual Targeted Tech Campaigns for Indian Shoppers",
                    badgeText = "AUTO AD UNIT #3",
                    imageUrl = "",
                    actionText = "Open Partner",
                    targetCategory = "All",
                    gradientStartHex = "#581C87",
                    gradientEndHex = "#3B0764",
                    isActive = true,
                    displayOrder = 9,
                    bannerType = "AUTO_AD",
                    adUnitId = "ca-app-pub-3940256099942544/8691691433",
                    slotNumber = 9
                ),
                BannerEntity(
                    id = 10,
                    title = "Premium Sponsor • Cloud & Software",
                    subtitle = "Official 300x250 Premium Auto Placement Slot",
                    badgeText = "AUTO AD UNIT #4",
                    imageUrl = "",
                    actionText = "Explore",
                    targetCategory = "All",
                    gradientStartHex = "#7F1D1D",
                    gradientEndHex = "#450A0A",
                    isActive = true,
                    displayOrder = 10,
                    bannerType = "AUTO_AD",
                    adUnitId = "ca-app-pub-3940256099942544/5224354917",
                    slotNumber = 10
                )
            )
            bannerDao.insertBanners(initialBanners)

            val storeConfigDao = db.storeConfigDao()
            storeConfigDao.setStoreConfig(
                StoreConfigEntity(
                    id = 1,
                    storeTitle = "Laptops",
                    storeTagline = "Your Trusted Laptop Store",
                    searchPlaceholder = "Search laptops, accessories, specs...",
                    themeName = "BLUE",
                    bannerIntervalSeconds = 5
                )
            )
        }
    }
}
