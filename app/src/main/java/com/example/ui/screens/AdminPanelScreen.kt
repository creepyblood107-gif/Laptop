package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewCarousel
import com.example.data.local.BannerEntity
import com.example.data.local.StoreConfigEntity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.ui.AdminTab
import com.example.ui.AppMode
import com.example.ui.MainViewModel
import com.example.ui.components.ConditionBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.formatInr
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenBright
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.BgDeep
import com.example.ui.theme.BorderDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TechBlue
import com.example.ui.theme.TechBlueBright
import com.example.ui.theme.TechBlueLight
import com.example.ui.theme.TechCyan
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val adminTab by viewModel.adminTab.collectAsState()
    val products by viewModel.products.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val adminBanners by viewModel.adminBanners.collectAsState()
    val storeConfig by viewModel.storeConfig.collectAsState()
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val masterPin by viewModel.masterPin.collectAsState()

    BackHandler {
        viewModel.setAppMode(AppMode.USER)
    }

    if (!isAdminAuthenticated) {
        AdminLoginView(
            onLogin = { email, pass -> viewModel.loginAdmin(email, pass) },
            onQuickLogin = { viewModel.quickAdminLogin() },
            onBackToStore = { viewModel.setAppMode(AppMode.USER) }
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BgDeep,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDark,
                    titleContentColor = TextPrimary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.setAppMode(AppMode.USER) },
                        modifier = Modifier.testTag("admin_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Store", tint = Color.White)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Admin Shield",
                            tint = AccentRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Panel",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = AccentRed
                        )
                    }
                },
                actions = {
                    Surface(
                        color = Color(0x33EF4444),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentRedDark),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = "Live Active",
                            color = AccentRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.logoutAdmin() },
                        modifier = Modifier.testTag("admin_logout_btn")
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "Logout Admin", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                contentColor = TextSecondary,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = adminTab == AdminTab.DASHBOARD,
                    onClick = { viewModel.setAdminTab(AdminTab.DASHBOARD) },
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentRed,
                        selectedTextColor = AccentRed,
                        indicatorColor = AccentRed.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("admin_nav_dashboard")
                )
                NavigationBarItem(
                    selected = adminTab == AdminTab.BANNERS,
                    onClick = { viewModel.setAdminTab(AdminTab.BANNERS) },
                    icon = { Icon(Icons.Default.ViewCarousel, contentDescription = "Banners") },
                    label = { Text("Banners", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentRed,
                        selectedTextColor = AccentRed,
                        indicatorColor = AccentRed.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("admin_nav_banners")
                )
                NavigationBarItem(
                    selected = adminTab == AdminTab.ADD_PRODUCT,
                    onClick = { viewModel.setAdminTab(AdminTab.ADD_PRODUCT) },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    label = { Text("Add Item", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentRed,
                        selectedTextColor = AccentRed,
                        indicatorColor = AccentRed.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("admin_nav_add")
                )
                NavigationBarItem(
                    selected = adminTab == AdminTab.MANAGE_PRODUCTS,
                    onClick = { viewModel.setAdminTab(AdminTab.MANAGE_PRODUCTS) },
                    icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Products") },
                    label = { Text("Products", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentRed,
                        selectedTextColor = AccentRed,
                        indicatorColor = AccentRed.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("admin_nav_products")
                )
                NavigationBarItem(
                    selected = adminTab == AdminTab.THEME_SETTINGS,
                    onClick = { viewModel.setAdminTab(AdminTab.THEME_SETTINGS) },
                    icon = { Icon(Icons.Default.Palette, contentDescription = "Theme") },
                    label = { Text("Theme", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentRed,
                        selectedTextColor = AccentRed,
                        indicatorColor = AccentRed.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("admin_nav_theme")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (adminTab) {
                AdminTab.DASHBOARD -> AdminDashboardView(
                    products = products,
                    orders = orders,
                    banners = adminBanners,
                    storeConfig = storeConfig,
                    currentMasterPin = masterPin,
                    onUpdateMasterPin = { viewModel.updateMasterPin(it) },
                    onNavigateToOrders = { viewModel.setAdminTab(AdminTab.ORDERS) },
                    onNavigateToAdd = { viewModel.setAdminTab(AdminTab.ADD_PRODUCT) },
                    onNavigateToBanners = { viewModel.setAdminTab(AdminTab.BANNERS) },
                    onNavigateToTheme = { viewModel.setAdminTab(AdminTab.THEME_SETTINGS) },
                    onNavigateToBroadcast = { viewModel.setAdminTab(AdminTab.PUSH_NOTIFICATION) }
                )
                AdminTab.BANNERS -> AdminBannersView(
                    banners = adminBanners,
                    storeConfig = storeConfig,
                    onAddBanner = { title, sub, img, cat, badge, act, g1, g2, slot ->
                        viewModel.addBanner(title, sub, img, cat, badge, act, g1, g2, slot)
                    },
                    onUpdateAdUnit = { id, unitId, title, sub, active ->
                        viewModel.updateBannerAdUnit(id, unitId, title, sub, active)
                    },
                    onUpdateBanner = { banner ->
                        viewModel.updateBanner(banner)
                    },
                    onDeleteBanner = { id -> viewModel.deleteBanner(id) },
                    onToggleActive = { banner, active -> viewModel.toggleBannerActive(banner, active) },
                    onUpdateInterval = { sec -> viewModel.updateBannerSlideInterval(sec) }
                )
                AdminTab.THEME_SETTINGS -> AdminThemeAndBrandingView(
                    storeConfig = storeConfig,
                    currentMasterPin = masterPin,
                    onSelectTheme = { theme -> viewModel.setThemePalette(theme) },
                    onSaveBranding = { title, tagline, placeholder ->
                        viewModel.updateStoreBranding(title, tagline, placeholder)
                    },
                    onUpdateMasterPin = { viewModel.updateMasterPin(it) }
                )
                AdminTab.ORDERS -> AdminOrdersView(
                    orders = orders,
                    onUpdateStatus = { id, status -> viewModel.updateOrderStatus(id, status) }
                )
                AdminTab.ADD_PRODUCT -> AdminAddProductView(
                    onAddProduct = { name, price, origPrice, cat, cond, img, proc, ram, storage, disp, desc, stock ->
                        viewModel.addProduct(name, price, origPrice, cat, cond, img, proc, ram, storage, disp, desc, stock)
                    }
                )
                AdminTab.MANAGE_PRODUCTS -> AdminManageProductsView(
                    products = products,
                    onToggleStock = { id, inStock -> viewModel.toggleProductStock(id, inStock) },
                    onDelete = { viewModel.deleteProduct(it) },
                    onNavigateToAdd = { viewModel.setAdminTab(AdminTab.ADD_PRODUCT) }
                )
                AdminTab.PUSH_NOTIFICATION -> AdminPushNotificationView(
                    onSendBroadcast = { title, msg, target ->
                        viewModel.broadcastAnnouncement(title, msg, target)
                    }
                )
            }
        }
    }
}

// ----------------- ADMIN DASHBOARD VIEW -----------------
@Composable
fun AdminDashboardView(
    products: List<ProductEntity>,
    orders: List<OrderEntity>,
    banners: List<BannerEntity>,
    storeConfig: StoreConfigEntity?,
    currentMasterPin: String,
    onUpdateMasterPin: (String) -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToBanners: () -> Unit,
    onNavigateToTheme: () -> Unit,
    onNavigateToBroadcast: () -> Unit
) {
    val totalRevenue = orders.filter { it.status != "Cancelled" }.sumOf { it.productPrice * it.quantity }
    val pendingOrders = orders.filter { it.status == "Pending" }.size
    val currentInterval = storeConfig?.bannerIntervalSeconds ?: 5
    val currentTheme = storeConfig?.themeName ?: "Blue"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Dashboard Overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: Total Products
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Products", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${products.size}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    }
                }

                // Card 2: Total Orders
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Orders", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${orders.size}", color = AccentGreenBright, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 3: Pending Orders
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Pending Action", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$pendingOrders", color = TechCyan, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    }
                }

                // Card 4: Total Revenue
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Revenue", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(formatInr(totalRevenue), color = TechBlueLight, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                }
            }
        }

        // Live Carousel & Theme Status Highlights
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ViewCarousel, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Carousel Swipe Speed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Auto-swiping every $currentInterval seconds (${banners.size} active banners)", color = TextSecondary, fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToBanners,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Edit Banners", fontSize = 11.sp, color = TechBlueLight)
                    }
                }
            }
        }

        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Customer App Theme", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Active theme: $currentTheme (${storeConfig?.storeTitle ?: "Laptops"})", color = TextSecondary, fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToTheme,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Change Theme", fontSize = 11.sp, color = TechBlueLight)
                    }
                }
            }
        }

        // Quick Actions Grid
        item {
            Text("Quick Management Actions", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateToAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Product", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onNavigateToOrders,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Live Orders", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onNavigateToBanners,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ViewCarousel, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Banner Slider", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = onNavigateToBroadcast,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Broadcast", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Master PIN Security Setting Card
        item {
            var showPinChangeDialog by remember { mutableStateOf(false) }

            Surface(
                color = Color(0xFF131D33),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TechBlue.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Secret Master Security PIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Active PIN: $currentMasterPin (Tap 5x on Logo to open)", color = TextSecondary, fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { showPinChangeDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Change PIN", fontSize = 11.sp, color = TechBlueLight)
                    }
                }
            }

            if (showPinChangeDialog) {
                var newPinInput by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { showPinChangeDialog = false },
                    title = { Text("Set New Master PIN", color = Color.White) },
                    text = {
                        Column {
                            Text("Enter new 4-digit Master PIN for secret admin access:", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = newPinInput,
                                onValueChange = { if (it.length <= 6 && it.all { ch -> ch.isDigit() }) newPinInput = it },
                                label = { Text("New PIN (4 digits)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newPinInput.length >= 4) {
                                    onUpdateMasterPin(newPinInput)
                                    showPinChangeDialog = false
                                }
                            }
                        ) {
                            Text("Save PIN")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPinChangeDialog = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                    },
                    containerColor = CardDark
                )
            }
        }

        // Recent Orders Preview
        item {
            Text(
                text = "Recent Customer Orders",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(orders.take(4)) { ord ->
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ord.orderNumber, color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(ord.productName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("ग्राहक: ${ord.customerName} (${ord.customerPhone})", color = TextSecondary, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(formatInr(ord.productPrice), color = TechBlueLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        StatusBadge(status = ord.status)
                    }
                }
            }
        }
    }
}

// ----------------- ADMIN ADD PRODUCT VIEW -----------------
@Composable
fun AdminAddProductView(
    onAddProduct: (name: String, price: Double, origPrice: Double, cat: String, cond: String, img: String, proc: String, ram: String, storage: String, disp: String, desc: String, stock: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var originalPriceStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Laptop") }
    var condition by remember { mutableStateOf("Used") }
    var selectedImageUri by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("") }
    var processor by remember { mutableStateOf("Intel Core i5") }
    var ram by remember { mutableStateOf("8GB RAM") }
    var storage by remember { mutableStateOf("256GB SSD") }
    var display by remember { mutableStateOf("14\" FHD") }
    var description by remember { mutableStateOf("") }
    var stockStr by remember { mutableStateOf("5") }

    val context = LocalContext.current

    // Android Modern Photo Picker Launcher!
    // Directly opens device gallery to pick image, no manual permission hassle on Android 13+ / backward compatible!
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            Toast.makeText(context, "Gallery se image select ho gayi!", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Add New Product",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Fill laptop or accessory details and select image directly from gallery",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        // Image Selection Section (DIRECT FROM PHONE GALLERY!)
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Product Image (फोन गैलरी से फोटो चुनें)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Image preview if selected
                    val activeImage = selectedImageUri.ifBlank { imageUrlInput }
                    if (activeImage.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                        ) {
                            AsyncImage(
                                model = activeImage,
                                contentDescription = "Selected Product Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = {
                                    selectedImageUri = ""
                                    imageUrlInput = ""
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Gallery Launcher Button
                    Button(
                        onClick = {
                            // Launches system Photo Picker
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("admin_gallery_picker_btn")
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Gallery", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (activeImage.isNotBlank()) "Change Image From Phone Gallery" else "Select Image from Phone Gallery",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Or paste Image Web URL:",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    OutlinedTextField(
                        value = imageUrlInput,
                        onValueChange = { imageUrlInput = it },
                        placeholder = { Text("https://example.com/laptop.jpg", fontSize = 11.sp, color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Product Name
        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name (e.g. Dell Latitude 7490)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_prod_name_input"),
                singleLine = true
            )
        }

        // Price & Original Price
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Offer Price (₹)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = originalPriceStr,
                    onValueChange = { originalPriceStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("MRP / Cut Price (₹)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }

        // Category Selection
        item {
            Text("Category (श्रेणी)", color = TextSecondary, fontSize = 11.sp)
            val cats = listOf("Laptop", "Accessories", "SSD/HDD", "Dongles")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cats) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TechBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Condition Selection
        item {
            Text("Condition (स्थिति)", color = TextSecondary, fontSize = 11.sp)
            val conds = listOf("Used", "Refurbished", "New")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                conds.forEach { cond ->
                    FilterChip(
                        selected = condition == cond,
                        onClick = { condition = cond },
                        label = { Text(cond) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TechBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Specifications
        item {
            Text("Specifications (विनिर्देश)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = processor,
                    onValueChange = { processor = it },
                    label = { Text("Processor") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = ram,
                    onValueChange = { ram = it },
                    label = { Text("RAM") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = storage,
                    onValueChange = { storage = it },
                    label = { Text("Storage") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = display,
                    onValueChange = { display = it },
                    label = { Text("Display") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Stock & Description
        item {
            OutlinedTextField(
                value = stockStr,
                onValueChange = { stockStr = it.filter { ch -> ch.isDigit() } },
                label = { Text("In Stock Units (स्टॉक संख्या)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Product Description / Details (विवरण)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        // Save Button
        item {
            val finalImg = selectedImageUri.ifBlank { imageUrlInput }.ifBlank {
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600"
            }

            Button(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 0.0
                    val origPrice = originalPriceStr.toDoubleOrNull() ?: (price * 1.25)
                    val stock = stockStr.toIntOrNull() ?: 1
                    if (name.isNotBlank() && price > 0) {
                        onAddProduct(name, price, origPrice, category, condition, finalImg, processor, ram, storage, display, description, stock)
                    } else {
                        Toast.makeText(context, "कृपया प्रोडक्ट का नाम और कीमत भरें!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("admin_save_product_button")
            ) {
                Text("Add Product to Inventory", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ----------------- ADMIN MANAGE PRODUCTS VIEW -----------------
@Composable
fun AdminManageProductsView(
    products: List<ProductEntity>,
    onToggleStock: (Long, Boolean) -> Unit,
    onDelete: (Long) -> Unit,
    onNavigateToAdd: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }

    val filtered = products.filter { p ->
        val matchesQuery = searchQuery.isBlank() || p.name.contains(searchQuery, ignoreCase = true) || p.category.contains(searchQuery, ignoreCase = true)
        val matchesCat = when (selectedCategoryFilter) {
            "All" -> true
            "Laptops" -> p.category == "Laptop"
            "Accessories" -> p.category == "Accessories" || p.category == "SSD/HDD" || p.category == "Dongles"
            "SSD/HDD" -> p.category == "SSD/HDD"
            "Dongles" -> p.category == "Dongles"
            else -> p.category.equals(selectedCategoryFilter, ignoreCase = true)
        }
        matchesQuery && matchesCat
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Manage Inventory (${products.size})",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Laptops, SSD, HDD, Dongles & Accessories", color = TextSecondary, fontSize = 11.sp)
                }

                Button(
                    onClick = onNavigateToAdd,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Item", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by name, SSD, Dongle, Brand...", color = TextSecondary, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Category Filter Chips
            val cats = listOf("All", "Laptops", "Accessories", "SSD/HDD", "Dongles")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(cats) { cat ->
                    FilterChip(
                        selected = selectedCategoryFilter == cat,
                        onClick = { selectedCategoryFilter = cat },
                        label = { Text(cat, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TechBlue,
                            selectedLabelColor = Color.White,
                            containerColor = CardDark,
                            labelColor = TextSecondary
                        )
                    )
                }
            }
        }

        items(filtered) { p ->
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceDark)
                        ) {
                            AsyncImage(
                                model = p.imageUrl,
                                contentDescription = p.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ConditionBadge(condition = p.condition)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(formatInr(p.price), color = TechBlueLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        // In stock switch
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(if (p.isInStock) "In Stock" else "Out", color = if (p.isInStock) AccentGreenBright else AccentRed, fontSize = 10.sp)
                            Switch(
                                checked = p.isInStock,
                                onCheckedChange = { onToggleStock(p.id, it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = AccentGreenBright,
                                    checkedTrackColor = Color(0xFF064E3B)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { productToDelete = p },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    productToDelete?.let { prod ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Delete Product?", color = Color.White) },
            text = { Text("Are you sure you want to delete '${prod.name}' from inventory?", color = TextBody) },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(prod.id)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CardDark
        )
    }
}

// ----------------- ADMIN ORDERS VIEW -----------------
@Composable
fun AdminOrdersView(
    orders: List<OrderEntity>,
    onUpdateStatus: (Long, String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Pending", "Processing", "Delivered", "Cancelled")

    val filtered = if (selectedFilter == "All") orders else orders.filter { it.status == selectedFilter }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Customer Orders (Live Sync)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { f ->
                    FilterChip(
                        selected = selectedFilter == f,
                        onClick = { selectedFilter = f },
                        label = { Text(f) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentRed,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            item {
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("कोई आर्डर नहीं मिला।", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(filtered) { ord ->
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(ord.orderNumber, color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            StatusBadge(status = ord.status)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(ord.productName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Text(formatInr(ord.productPrice), fontWeight = FontWeight.ExtraBold, color = TechBlueLight, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "ग्राहक: ${ord.customerName}", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)

                        // Direct Call and WhatsApp row
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "मोबाइल: ${ord.customerPhone}", color = TextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(8.dp))

                            // Call Intent Button
                            OutlinedButton(
                                onClick = {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ord.customerPhone}"))
                                    context.startActivity(dialIntent)
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(26.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = AccentGreenBright, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call", fontSize = 10.sp, color = AccentGreenBright)
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // WhatsApp Intent Button
                            OutlinedButton(
                                onClick = {
                                    val clean = ord.customerPhone.filter { it.isDigit() }
                                    val waUrl = "https://api.whatsapp.com/send?phone=91$clean&text=Hello%20${ord.customerName},%20regarding%20your%20Laptops%20Store%20order%20${ord.orderNumber}"
                                    val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl))
                                    context.startActivity(waIntent)
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(26.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                            ) {
                                Text("WhatsApp", fontSize = 10.sp, color = TechCyan)
                            }
                        }

                        Text(text = "पिकअप स्थान: ${ord.pickupLocation}", color = TextBody, fontSize = 11.sp)
                        Text(text = "पिकअप तारीख: ${ord.pickupDate}", color = TextBody, fontSize = 11.sp)
                        Text(text = "भुगतान: ${ord.paymentMethod} (${ord.paymentStatus})", color = if (ord.paymentStatus == "Approved") AccentGreenBright else TextSecondary, fontSize = 11.sp)
                        if (ord.utrNumber.isNotBlank()) {
                            Text(text = "UTR Ref: ${ord.utrNumber}", color = TechCyan, fontSize = 10.sp)
                        }

                        val timeStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(ord.orderTime))
                        Text(text = "आर्डर समय: $timeStr", color = TextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))

                        Spacer(modifier = Modifier.height(10.dp))

                        // Status update buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (ord.status != "Processing" && ord.status != "Delivered" && ord.status != "Cancelled") {
                                Button(
                                    onClick = { onUpdateStatus(ord.id, "Processing") },
                                    colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(32.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Mark Processing", fontSize = 10.sp)
                                }
                            }

                            if (ord.status != "Delivered" && ord.status != "Cancelled") {
                                Button(
                                    onClick = { onUpdateStatus(ord.id, "Delivered") },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(32.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Mark Delivered", fontSize = 10.sp)
                                }
                            }

                            if (ord.status != "Cancelled") {
                                OutlinedButton(
                                    onClick = { onUpdateStatus(ord.id, "Cancelled") },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .height(32.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Cancel", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ----------------- ADMIN BROADCAST PUSH NOTIFICATION -----------------
@Composable
fun AdminPushNotificationView(
    onSendBroadcast: (title: String, message: String, target: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("All Users") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Broadcast Push Notification",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Send real-time alerts & new stock arrivals directly to customer applets",
            fontSize = 11.sp,
            color = TextSecondary
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (शीर्षक)") },
            placeholder = { Text("e.g. New Laptop Arrivals!") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message (संदेश)") },
            placeholder = { Text("Check out latest laptops and special discounts in our store.") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Text("Target Audience (किसको भेजना है):", color = TextSecondary, fontSize = 12.sp)
        val targets = listOf("All Users", "Only Customers", "Specific Users")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            targets.forEach { t ->
                FilterChip(
                    selected = target == t,
                    onClick = { target = t },
                    label = { Text(t) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentRed,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (title.isNotBlank() && message.isNotBlank()) {
                    onSendBroadcast(title, message, target)
                    Toast.makeText(context, "Notification Sent Successfully!", Toast.LENGTH_SHORT).show()
                    title = ""
                    message = ""
                } else {
                    Toast.makeText(context, "कृपया टाइटल और मैसेज लिखें!", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("send_broadcast_button")
        ) {
            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send Broadcast Now", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

// ----------------- ADMIN BANNERS & CAROUSEL VIEW (10 SLOTS: 6 CUSTOM + 4 AUTO AD UNITS) -----------------
@Composable
fun AdminBannersView(
    banners: List<BannerEntity>,
    storeConfig: StoreConfigEntity?,
    onAddBanner: (title: String, subtitle: String, imageUrl: String, targetCategory: String, badgeText: String, actionText: String, gradientStartHex: String, gradientEndHex: String, slotNumber: Int) -> Unit,
    onUpdateAdUnit: (bannerId: Long, adUnitId: String, title: String, subtitle: String, isActive: Boolean) -> Unit,
    onUpdateBanner: (BannerEntity) -> Unit,
    onDeleteBanner: (Long) -> Unit,
    onToggleActive: (BannerEntity, Boolean) -> Unit,
    onUpdateInterval: (Int) -> Unit
) {
    var bannerSectionTab by remember { mutableStateOf(0) } // 0 = 6 Custom Slots, 1 = 4 Auto Ad Units
    var selectedCustomSlot by remember { mutableStateOf(1) } // 1 to 6

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var badgeText by remember { mutableStateOf("FEATURED DEAL") }
    var actionText by remember { mutableStateOf("Explore Now") }
    var targetCategory by remember { mutableStateOf("Laptop") }
    var selectedImageUri by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("") }
    var selectedGradientPreset by remember { mutableStateOf("Blue") }

    val currentInterval = storeConfig?.bannerIntervalSeconds ?: 5
    var customIntervalInput by remember { mutableStateOf(currentInterval.toString()) }
    var bannerToDelete by remember { mutableStateOf<BannerEntity?>(null) }

    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri.toString()
            Toast.makeText(context, "गैलरी से 300x250 बैनर इमेज चुन ली गई!", Toast.LENGTH_SHORT).show()
        }
    }

    val gradientPresets = listOf(
        Triple("Blue", "#1E3A8A", "#312E81"),
        Triple("Crimson", "#991B1B", "#450A0A"),
        Triple("Green", "#065F46", "#064E3B"),
        Triple("Unicorn", "#701A75", "#3B0764"),
        Triple("Gold", "#92400E", "#451A03"),
        Triple("Midnight", "#0F172A", "#1E293B")
    )

    val activePreset = gradientPresets.find { it.first == selectedGradientPreset } ?: gradientPresets[0]

    val customBanners = banners.filter { it.bannerType != "AUTO_AD" && it.slotNumber in 1..6 }
    val autoAdBanners = banners.filter { it.bannerType == "AUTO_AD" || it.slotNumber in 7..10 }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Title & 10 Slot Architecture Overview
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ViewCarousel, contentDescription = null, tint = AccentRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "10 Banners & 300x250 Ad System",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Total 10 Slots: 6 Custom Store Banners + 4 Auto Ad Unit IDs (300x250 Medium Rectangle format). Snapping bug fixed so cards never freeze halfway.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        // Card 1: Slide Speed / Timer Selector
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = TechCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Auto-Swipe Timer (स्लाइडर टाइमर)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                        Surface(
                            color = TechBlue.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TechBlueBright)
                        ) {
                            Text(
                                text = "Active: ${currentInterval}s",
                                color = TechBlueBright,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Slide moves after configured duration. Select preset or enter custom seconds (e.g. 5s, 10s, 60s):",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val presets = listOf(3, 5, 8, 10, 15, 30, 60)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(presets) { sec ->
                            val isSel = currentInterval == sec
                            Surface(
                                color = if (isSel) TechBlue else SurfaceDark,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) TechBlueBright else BorderDark
                                ),
                                modifier = Modifier.clickable {
                                    onUpdateInterval(sec)
                                    customIntervalInput = sec.toString()
                                }
                            ) {
                                Text(
                                    text = if (sec == 5) "5s (Standard)" else "${sec}s",
                                    color = if (isSel) Color.White else TextSecondary,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customIntervalInput,
                            onValueChange = { customIntervalInput = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Set Custom Seconds (2s - 600s)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                val sec = customIntervalInput.toIntOrNull() ?: 5
                                onUpdateInterval(sec)
                                Toast.makeText(context, "स्वाइप टाइम $sec सेकंड पर सेट!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Text("Save Speed")
                        }
                    }
                }
            }
        }

        // Subtabs: 6 Custom Banners vs 4 Auto Ad Units
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark, RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                Surface(
                    color = if (bannerSectionTab == 0) TechBlue else Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { bannerSectionTab = 0 }
                ) {
                    Text(
                        text = "6 Custom Banners (1 - 6)",
                        color = if (bannerSectionTab == 0) Color.White else TextSecondary,
                        fontWeight = if (bannerSectionTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                Surface(
                    color = if (bannerSectionTab == 1) AccentRed else Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { bannerSectionTab = 1 }
                ) {
                    Text(
                        text = "4 Auto Ad Units (7 - 10)",
                        color = if (bannerSectionTab == 1) Color.White else TextSecondary,
                        fontWeight = if (bannerSectionTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }

        // TAB 0: 6 CUSTOM BANNERS (Owner can edit/upload/create)
        if (bannerSectionTab == 0) {
            item {
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Configure Custom Banner (Slot 1 to 6)",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Select a slot to edit or create, choose photo from gallery (300x250 recommended) or enter details.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Slot Selector Chips (1 to 6)
                        Text("Select Slot Number:", color = TechCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            (1..6).forEach { slotNum ->
                                val isSel = selectedCustomSlot == slotNum
                                val existingForSlot = customBanners.find { it.slotNumber == slotNum }
                                Surface(
                                    color = if (isSel) TechBlue else SurfaceDark,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSel) TechBlueBright else BorderDark
                                    ),
                                    modifier = Modifier.clickable {
                                        selectedCustomSlot = slotNum
                                        if (existingForSlot != null) {
                                            title = existingForSlot.title
                                            subtitle = existingForSlot.subtitle
                                            badgeText = existingForSlot.badgeText
                                            actionText = existingForSlot.actionText
                                            targetCategory = existingForSlot.targetCategory
                                            imageUrlInput = existingForSlot.imageUrl
                                            selectedImageUri = ""
                                        }
                                    }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "Slot $slotNum",
                                            color = if (isSel) Color.White else TextSecondary,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = if (existingForSlot?.isActive == true) "Active" else if (existingForSlot != null) "Off" else "Empty",
                                            color = if (existingForSlot?.isActive == true) AccentGreenBright else TextMuted,
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Image Picker Section
                        val activeImage = selectedImageUri.ifBlank { imageUrlInput }
                        if (activeImage.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceDark)
                            ) {
                                AsyncImage(
                                    model = activeImage,
                                    contentDescription = "Selected Banner Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = {
                                        selectedImageUri = ""
                                        imageUrlInput = ""
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                        .size(28.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = TechCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Choose Photo from Gallery", fontSize = 11.sp, color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = imageUrlInput,
                            onValueChange = { imageUrlInput = it },
                            placeholder = { Text("Or paste image link (300x250)...", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Banner Title (e.g. Dell & HP Mega Sale)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = subtitle,
                            onValueChange = { subtitle = it },
                            label = { Text("Subtitle / Guarantee Description") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = badgeText,
                                onValueChange = { badgeText = it },
                                label = { Text("Badge Text") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = actionText,
                                onValueChange = { actionText = it },
                                label = { Text("Button Text") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Target Category Chips
                        Text("Target Category upon Click:", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        val targetCats = listOf("All", "Laptop", "Accessories", "SSD/HDD", "Dongles")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(targetCats) { cat ->
                                FilterChip(
                                    selected = targetCategory == cat,
                                    onClick = { targetCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TechBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Gradient Presets
                        Text("Background Gradient Color:", color = TextSecondary, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(gradientPresets) { preset ->
                                val isSel = selectedGradientPreset == preset.first
                                Surface(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSel) 2.dp else 1.dp,
                                        if (isSel) Color.White else BorderDark
                                    ),
                                    modifier = Modifier.clickable { selectedGradientPreset = preset.first }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                                    colors = listOf(
                                                        Color(android.graphics.Color.parseColor(preset.second)),
                                                        Color(android.graphics.Color.parseColor(preset.third))
                                                    )
                                                )
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(preset.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 300x250 Preview Box
                        Text("Customer 300x250 Live Preview (Slot #$selectedCustomSlot):", color = TechCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 150.dp, max = 180.dp)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            colors = listOf(
                                                Color(android.graphics.Color.parseColor(activePreset.second)),
                                                Color(android.graphics.Color.parseColor(activePreset.third))
                                            )
                                        )
                                    )
                                    .padding(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(badgeText.ifBlank { "FEATURED DEAL" }, color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        Text("Slot #$selectedCustomSlot • 300x250", color = Color.White.copy(alpha = 0.8f), fontSize = 9.sp)
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(title.ifBlank { "Preview Title" }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                                            Text(subtitle.ifBlank { "Preview Subtitle" }, color = TextBody, fontSize = 10.sp, maxLines = 1)
                                        }
                                        if (activeImage.isNotBlank()) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(50.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color.Black.copy(alpha = 0.3f))
                                            ) {
                                                AsyncImage(model = activeImage, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(color = TechBlue, shape = RoundedCornerShape(6.dp)) {
                                            Text(actionText.ifBlank { "Explore Now" }, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                        }
                                        Text("Swipes every ${currentInterval}s", color = Color.White.copy(alpha = 0.6f), fontSize = 9.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val existing = customBanners.find { it.slotNumber == selectedCustomSlot }
                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    if (existing != null) {
                                        val updated = existing.copy(
                                            title = title,
                                            subtitle = subtitle,
                                            imageUrl = activeImage,
                                            targetCategory = targetCategory,
                                            badgeText = badgeText,
                                            actionText = actionText,
                                            gradientStartHex = activePreset.second,
                                            gradientEndHex = activePreset.third,
                                            slotNumber = selectedCustomSlot,
                                            isActive = true
                                        )
                                        onUpdateBanner(updated)
                                        Toast.makeText(context, "स्लॉट #$selectedCustomSlot बैनर अपडेट हो गया!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        onAddBanner(
                                            title,
                                            subtitle,
                                            activeImage,
                                            targetCategory,
                                            badgeText,
                                            actionText,
                                            activePreset.second,
                                            activePreset.third,
                                            selectedCustomSlot
                                        )
                                        Toast.makeText(context, "स्लॉट #$selectedCustomSlot बैनर पब्लिश हो गया!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "कृपया बैनर का शीर्षक (Title) लिखें!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (existing != null) "Update Slot #$selectedCustomSlot Banner" else "Publish to Slot #$selectedCustomSlot",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Existing Custom Banners (1 to 6)
            item {
                Text(
                    text = "Configured Custom Slots (${customBanners.size}/6)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            items(customBanners) { banner ->
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            colors = listOf(
                                                try { Color(android.graphics.Color.parseColor(banner.gradientStartHex)) } catch (_: Exception) { Color(0xFF1E3A8A) },
                                                try { Color(android.graphics.Color.parseColor(banner.gradientEndHex)) } catch (_: Exception) { Color(0xFF312E81) }
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (banner.imageUrl.isNotBlank()) {
                                    AsyncImage(model = banner.imageUrl, contentDescription = banner.title, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                } else {
                                    Icon(Icons.Default.ViewCarousel, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(24.dp))
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(color = TechBlue.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                        Text("Slot #${banner.slotNumber}", color = TechBlueLight, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(banner.badgeText, color = TechCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(banner.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(banner.subtitle, color = TextSecondary, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (banner.isActive) "Active" else "Hidden", color = if (banner.isActive) AccentGreenBright else TextMuted, fontSize = 9.sp)
                                Switch(
                                    checked = banner.isActive,
                                    onCheckedChange = { onToggleActive(banner, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = AccentGreenBright,
                                        checkedTrackColor = Color(0xFF064E3B)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    selectedCustomSlot = banner.slotNumber
                                    title = banner.title
                                    subtitle = banner.subtitle
                                    badgeText = banner.badgeText
                                    actionText = banner.actionText
                                    targetCategory = banner.targetCategory
                                    imageUrlInput = banner.imageUrl
                                    selectedImageUri = ""
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Edit in Form", fontSize = 10.sp, color = TechBlueLight)
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            OutlinedButton(
                                onClick = { bannerToDelete = banner },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(28.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: 4 AUTO AD UNIT SLOTS (Slots 7 to 10 for Ad Unit IDs)
        if (bannerSectionTab == 1) {
            item {
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TechCyan.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "4 Auto Ad Unit Slots (Slot 7 - 10)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Yeh 4 slots Auto Banner Ads ke liye hain. Yahan aap ya AI editor apna Banner Ad Unit ID paste kar sakte hain. Banner standard 300x250 Medium Rectangle me har ${currentInterval} second par auto-rotate hota rahega.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Render all 4 Auto Ad Unit Slots (7, 8, 9, 10)
            items((7..10).toList()) { slotNum ->
                val existing = autoAdBanners.find { it.slotNumber == slotNum }
                var adUnitInput by remember(existing?.adUnitId) {
                    mutableStateOf(existing?.adUnitId ?: "ca-app-pub-3940256099942544/630097811$slotNum")
                }
                var adTitleInput by remember(existing?.title) {
                    mutableStateOf(existing?.title ?: "Auto Tech Ad Unit #$slotNum")
                }
                var isSlotActive by remember(existing?.isActive) {
                    mutableStateOf(existing?.isActive ?: true)
                }

                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSlotActive) TechCyan.copy(alpha = 0.4f) else BorderDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = if (isSlotActive) AccentAmber.copy(alpha = 0.2f) else SurfaceDark,
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSlotActive) AccentAmber else BorderDark)
                                ) {
                                    Text(
                                        text = "AUTO AD SLOT #$slotNum",
                                        color = if (isSlotActive) AccentAmber else TextMuted,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0x3338BDF8),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("300x250 Medium Rect", color = TechCyan, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(if (isSlotActive) "Live" else "Off", color = if (isSlotActive) AccentGreenBright else TextMuted, fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Switch(
                                    checked = isSlotActive,
                                    onCheckedChange = {
                                        isSlotActive = it
                                        if (existing != null) {
                                            onToggleActive(existing, it)
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = AccentGreenBright,
                                        checkedTrackColor = Color(0xFF064E3B)
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Ad Unit ID Field
                        OutlinedTextField(
                            value = adUnitInput,
                            onValueChange = { adUnitInput = it },
                            label = { Text("Banner Ad Unit ID (यूनिट आईडी)") },
                            placeholder = { Text("e.g. ca-app-pub-3940256099942544/6300978111") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = adTitleInput,
                            onValueChange = { adTitleInput = it },
                            label = { Text("Sponsor / Network Title (प्रायोजक नाम)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Ad Format & Preview Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF091E3A))
                                .border(1.dp, TechCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(color = Color(0x33F59E0B), shape = RoundedCornerShape(4.dp)) {
                                            Text("Ad • 300x250", color = AccentAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(adTitleInput.ifBlank { "Auto Ad Unit" }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1)
                                    }
                                    Text("Rotates every ${currentInterval}s", color = TextSecondary, fontSize = 9.sp)
                                }

                                Text(
                                    text = "Unit ID: ${adUnitInput.ifBlank { "Unassigned" }}",
                                    color = TechCyan,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Surface(
                                    color = TechBlue.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text("Interactive Ad Placement Active", color = TechBlueLight, fontSize = 8.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Quick Auto-generate unit ID
                            OutlinedButton(
                                onClick = {
                                    adUnitInput = "ca-app-pub-3940256099942544/${1000000000 + slotNum * 123456}"
                                    Toast.makeText(context, "Test Ad Unit ID set!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(38.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Auto Test Unit ID", fontSize = 10.sp, color = TechCyan)
                            }

                            Button(
                                onClick = {
                                    if (existing != null) {
                                        onUpdateAdUnit(
                                            existing.id,
                                            adUnitInput.trim(),
                                            adTitleInput.trim(),
                                            "300x250 Medium Rectangle Certified Partner Advertisement",
                                            isSlotActive
                                        )
                                    } else {
                                        onAddBanner(
                                            adTitleInput.trim(),
                                            "300x250 Medium Rectangle Certified Partner Advertisement",
                                            "",
                                            "All",
                                            "SPONSORED AD",
                                            "Visit Partner",
                                            "#0C4A6E",
                                            "#082F49",
                                            slotNum
                                        )
                                    }
                                    Toast.makeText(context, "Slot #$slotNum Ad Unit ID सेव हो गई!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(38.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Save Ad Unit ID", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    bannerToDelete?.let { b ->
        AlertDialog(
            onDismissRequest = { bannerToDelete = null },
            title = { Text("Delete Banner?", color = Color.White) },
            text = { Text("Are you sure you want to delete '${b.title}'?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteBanner(b.id)
                        bannerToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { bannerToDelete = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CardDark
        )
    }
}

// ----------------- ADMIN THEME & BRANDING VIEW -----------------
private data class ThemeOption(
    val id: String,
    val name: String,
    val hindiTitle: String,
    val primaryColor: Color,
    val accentColor: Color,
    val description: String
)

@Composable
fun AdminThemeAndBrandingView(
    storeConfig: StoreConfigEntity?,
    currentMasterPin: String,
    onSelectTheme: (String) -> Unit,
    onSaveBranding: (title: String, tagline: String, placeholder: String) -> Unit,
    onUpdateMasterPin: (String) -> Unit
) {
    val activeTheme = storeConfig?.themeName ?: "Blue"
    var titleInput by remember(storeConfig) { mutableStateOf(storeConfig?.storeTitle ?: "Laptops") }
    var taglineInput by remember(storeConfig) { mutableStateOf(storeConfig?.storeTagline ?: "Your Trusted Laptop Store") }
    var placeholderInput by remember(storeConfig) { mutableStateOf(storeConfig?.searchPlaceholder ?: "Search laptops, accessories, specs...") }
    var showPinChangeDialog by remember { mutableStateOf(false) }

    val themeOptions = listOf(
        ThemeOption("Blue", "Tech Blue", "क्लासिक ब्लू", Color(0xFF2563EB), Color(0xFF38BDF8), "Professional blue & cyan tech palette"),
        ThemeOption("Red", "Gaming Crimson", "पावर गेमिंग रेड", Color(0xFFEF4444), Color(0xFFF87171), "High-energy bold gaming and performance red"),
        ThemeOption("Green", "Emerald Eco", "इको ग्रीन मिंट", Color(0xFF10B981), Color(0xFF34D399), "Clean, certified refurbished and eco aesthetic"),
        ThemeOption("Unicorn", "Neon Unicorn", "नियॉन पर्पल & मैजेंटा", Color(0xFFA855F7), Color(0xFFEC4899), "Vibrant RGB Cyberpunk styling for gamer stores"),
        ThemeOption("Gold", "Luxury Gold", "रॉयल शैम्पेन गोल्ड", Color(0xFFF59E0B), Color(0xFFFBBF24), "Prestige gold & amber premium laptop showcase"),
        ThemeOption("Cyan", "Clean Cyan", "इलेक्ट्रिक क्यान", Color(0xFF06B6D4), Color(0xFF22D3EE), "Ultra-modern, crisp aqua digital layout")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Title
        item {
            Text(
                text = "Store Branding & App Theme",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Switch live theme colors (Red, Green, Blue, Unicorn, Gold, Cyan) and customize store header and search text.",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        // Section 1: Dynamic Theme Selector
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = TechCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Customer App Color Theme (थीम रंग बदलें)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "1-Tap instantly updates entire customer app colors, buttons, icons, and badges:",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        themeOptions.forEach { opt ->
                            val isSelected = activeTheme.equals(opt.id, ignoreCase = true)
                            Surface(
                                color = if (isSelected) opt.primaryColor.copy(alpha = 0.15f) else SurfaceDark,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) opt.primaryColor else BorderDark
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectTheme(opt.id) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        // Color Swatches
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(opt.primaryColor)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(opt.accentColor)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "${opt.name} (${opt.hindiTitle})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = opt.description,
                                                fontSize = 10.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Surface(
                                            color = opt.primaryColor,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "ACTIVE",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    } else {
                                        OutlinedButton(
                                            onClick = { onSelectTheme(opt.id) },
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text("Apply", fontSize = 11.sp, color = opt.primaryColor)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Store Header & Search Bar Customizer
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = TechCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Header & Search Bar Text (हेडर व सर्च बार एडिट करें)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Customize the brand name, tagline, and search placeholder visible to all customers:",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Store Header Title (e.g. Laptops or Rohit Laptop Hub)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = taglineInput,
                        onValueChange = { taglineInput = it },
                        label = { Text("Store Tagline (e.g. Your Trusted Laptop Store)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = placeholderInput,
                        onValueChange = { placeholderInput = it },
                        label = { Text("Search Bar Placeholder Text") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            onSaveBranding(titleInput, taglineInput, placeholderInput)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Branding & Text Updates", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Section 3: Master Security PIN Manager
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Secret Master Access PIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Active PIN: $currentMasterPin (Tap 5x on Logo to open)", color = TextSecondary, fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = { showPinChangeDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Change PIN", fontSize = 11.sp, color = TechBlueLight)
                    }
                }
            }

            if (showPinChangeDialog) {
                var newPinInput by remember { mutableStateOf("") }
                AlertDialog(
                    onDismissRequest = { showPinChangeDialog = false },
                    title = { Text("Set New Master PIN", color = Color.White) },
                    text = {
                        Column {
                            Text("Enter new 4-digit Master PIN for secret admin access:", color = TextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = newPinInput,
                                onValueChange = { if (it.length <= 6 && it.all { ch -> ch.isDigit() }) newPinInput = it },
                                label = { Text("New PIN (4 digits)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (newPinInput.length >= 4) {
                                    onUpdateMasterPin(newPinInput)
                                    showPinChangeDialog = false
                                }
                            }
                        ) {
                            Text("Save PIN")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPinChangeDialog = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                    },
                    containerColor = CardDark
                )
            }
        }
    }
}

// ----------------- ADMIN LOGIN SCREEN (SCREEN 8) -----------------
@Composable
fun AdminLoginView(
    onLogin: (email: String, pass: String) -> Boolean,
    onQuickLogin: () -> Unit,
    onBackToStore: () -> Unit
) {
    var email by remember { mutableStateOf("admin@laptops.com") }
    var password by remember { mutableStateOf("admin123") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Admin Logo Badge
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E293B))
                    .border(1.dp, Color(0xFF7F1D1D), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Admin Shield",
                    tint = AccentRed,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Laptops",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Admin Panel Authentication",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentRed
            )
            Text(
                text = "Secure store control for inventory, orders & broadcasts",
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Login Card
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Sign in with Admin Credentials",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Admin Email") },
                        placeholder = { Text("admin@laptops.com") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_email_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TechBlueBright,
                            unfocusedBorderColor = BorderDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Admin Password / PIN") },
                        placeholder = { Text("admin123 or 1234") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TechBlueBright,
                            unfocusedBorderColor = BorderDark,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { onLogin(email, password) },
                        colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("admin_submit_login_btn")
                    ) {
                        Text("Login to Admin Panel", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Return to Customer Store
            TextButton(
                onClick = onBackToStore,
                modifier = Modifier.testTag("back_to_customer_store_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TechBlueLight, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Return to Customer Storefront", color = TechBlueLight, fontSize = 12.sp)
            }
        }
    }
}

