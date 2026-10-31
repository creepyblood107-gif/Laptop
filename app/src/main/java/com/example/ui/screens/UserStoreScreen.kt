package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.data.local.BannerEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.StoreConfigEntity
import com.example.ui.AppMode
import com.example.ui.MainViewModel
import com.example.ui.UserTab
import com.example.ui.components.ConditionBadge
import com.example.ui.components.ProductThumbnail
import com.example.ui.components.StatusBadge
import com.example.ui.components.StoreGuaranteeBanner
import com.example.ui.components.UpiQrCodeCanvas
import com.example.ui.components.formatInr
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenBright
import com.example.ui.theme.AccentRed
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
fun UserStoreScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userTab by viewModel.userTab.collectAsState()
    val products by viewModel.filteredProducts.collectAsState()
    val allProducts by viewModel.products.collectAsState()
    val banners by viewModel.banners.collectAsState()
    val storeConfig by viewModel.storeConfig.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val detailProduct by viewModel.detailProduct.collectAsState()
    val instantBuyProduct by viewModel.instantBuyProduct.collectAsState()
    val isCartCheckoutOpen by viewModel.isCartCheckoutOpen.collectAsState()
    val isNotificationDialogOpen by viewModel.isNotificationDialogOpen.collectAsState()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedCondition by viewModel.selectedCondition.collectAsState()

    val currentStoreTitle = storeConfig?.storeTitle?.ifBlank { "Laptops" } ?: "Laptops"
    val currentStoreTagline = storeConfig?.storeTagline?.ifBlank { "Your Trusted Laptop Store" } ?: "Your Trusted Laptop Store"

    var showSecretPinDialog by remember { mutableStateOf(false) }
    var secretTopTapCount by remember { mutableStateOf(0) }
    var lastTopTapTime by remember { mutableStateOf(0L) }

    val totalCartCount = cartItems.sumOf { it.quantity }

    BackHandler(enabled = userTab != UserTab.HOME) {
        viewModel.setUserTab(UserTab.HOME)
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
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null // Zero visual feedback so regular users have no clue
                        ) {
                            val now = System.currentTimeMillis()
                            if (now - lastTopTapTime < 1500) {
                                secretTopTapCount++
                                if (secretTopTapCount >= 5) {
                                    secretTopTapCount = 0
                                    showSecretPinDialog = true
                                }
                            } else {
                                secretTopTapCount = 1
                            }
                            lastTopTapTime = now
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TechBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Laptop,
                                contentDescription = "Store Logo",
                                tint = TechBlueBright,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = currentStoreTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = currentStoreTagline,
                                fontSize = 10.sp,
                                color = TechBlueLight
                            )
                        }
                    }
                },
                actions = {
                    // Notification Bell for customer alerts
                    IconButton(
                        onClick = { viewModel.setNotificationDialog(true) },
                        modifier = Modifier.testTag("bell_icon_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (notifications.isNotEmpty()) {
                                    Badge(containerColor = TechBlueBright) {
                                        Text(notifications.size.toString(), fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = TextSecondary
                            )
                        }
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
                    selected = userTab == UserTab.HOME,
                    onClick = { viewModel.setUserTab(UserTab.HOME) },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TechBlueBright,
                        selectedTextColor = TechBlueBright,
                        indicatorColor = TechBlue.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_home")
                )
                NavigationBarItem(
                    selected = userTab == UserTab.CATEGORY,
                    onClick = { viewModel.setUserTab(UserTab.CATEGORY) },
                    icon = { Icon(Icons.Outlined.Category, contentDescription = "Category") },
                    label = { Text("Category", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TechBlueBright,
                        selectedTextColor = TechBlueBright,
                        indicatorColor = TechBlue.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_category")
                )
                NavigationBarItem(
                    selected = userTab == UserTab.ACCESSORIES,
                    onClick = { viewModel.setUserTab(UserTab.ACCESSORIES) },
                    icon = { Icon(Icons.Default.Store, contentDescription = "Accessories") },
                    label = { Text("Accessories", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TechBlueBright,
                        selectedTextColor = TechBlueBright,
                        indicatorColor = TechBlue.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_accessories")
                )
                NavigationBarItem(
                    selected = userTab == UserTab.CART,
                    onClick = { viewModel.setUserTab(UserTab.CART) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (totalCartCount > 0) {
                                    Badge(containerColor = TechBlueBright) {
                                        Text(totalCartCount.toString(), fontSize = 9.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TechBlueBright,
                        selectedTextColor = TechBlueBright,
                        indicatorColor = TechBlue.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_cart")
                )
                NavigationBarItem(
                    selected = userTab == UserTab.PROFILE,
                    onClick = { viewModel.setUserTab(UserTab.PROFILE) },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TechBlueBright,
                        selectedTextColor = TechBlueBright,
                        indicatorColor = TechBlue.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (userTab) {
                UserTab.HOME -> HomeView(
                    products = products,
                    banners = banners,
                    storeConfig = storeConfig,
                    searchQuery = searchQuery,
                    selectedCategory = selectedCategory,
                    selectedCondition = selectedCondition,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    onCategoryChange = { viewModel.setSelectedCategory(it) },
                    onConditionChange = { viewModel.setSelectedCondition(it) },
                    onProductClick = { viewModel.openProductDetail(it) },
                    onBuyNow = { viewModel.startInstantBuy(it) },
                    onAddToCart = { viewModel.addToCart(it) },
                    onExploreClick = { viewModel.setUserTab(UserTab.CATEGORY) },
                    onBannerAction = { category ->
                        if (category.isNotBlank() && category != "All") {
                            viewModel.setSelectedCategory(category)
                            if (category == "Accessories" || category == "SSD/HDD" || category == "Dongles") {
                                viewModel.setUserTab(UserTab.ACCESSORIES)
                            } else {
                                viewModel.setUserTab(UserTab.CATEGORY)
                            }
                        } else {
                            viewModel.setUserTab(UserTab.CATEGORY)
                        }
                    }
                )
                UserTab.CATEGORY -> CategoryView(
                    products = allProducts,
                    onProductClick = { viewModel.openProductDetail(it) },
                    onBuyNow = { viewModel.startInstantBuy(it) },
                    onAddToCart = { viewModel.addToCart(it) }
                )
                UserTab.ACCESSORIES -> AccessoriesView(
                    products = allProducts.filter { it.category == "Accessories" || it.category == "SSD/HDD" || it.category == "Dongles" },
                    onProductClick = { viewModel.openProductDetail(it) },
                    onBuyNow = { viewModel.startInstantBuy(it) },
                    onAddToCart = { viewModel.addToCart(it) }
                )
                UserTab.CART -> CartView(
                    cartItems = cartItems,
                    onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                    onRemove = { viewModel.removeFromCart(it) },
                    onCheckout = { viewModel.openCartCheckout() },
                    onStartShopping = { viewModel.setUserTab(UserTab.HOME) }
                )
                UserTab.PROFILE -> ProfileView(
                    userProfile = userProfile,
                    orders = orders,
                    onCancelOrder = { viewModel.cancelOrder(it) },
                    onTriggerSecretDoor = { showSecretPinDialog = true },
                    onUpdateProfile = { name, phone, email -> viewModel.updateProfile(name, phone, email) }
                )
            }
        }
    }

    // Product Detail Bottom Sheet
    detailProduct?.let { product ->
        ProductDetailModalSheet(
            product = product,
            onDismiss = { viewModel.closeProductDetail() },
            onBuyNow = {
                viewModel.closeProductDetail()
                viewModel.startInstantBuy(product)
            },
            onAddToCart = {
                viewModel.addToCart(product)
            }
        )
    }

    // Direct Instant Buy Sheet
    instantBuyProduct?.let { product ->
        InstantCheckoutSheet(
            product = product,
            defaultName = userProfile.name,
            defaultPhone = userProfile.phone,
            defaultEmail = userProfile.email,
            onDismiss = { viewModel.closeInstantBuy() },
            onConfirmOrder = { name, phone, email, loc, date, method, paymentStatus, utr ->
                viewModel.placeInstantOrder(
                    product = product,
                    name = name,
                    phone = phone,
                    email = email,
                    location = loc,
                    date = date,
                    paymentMethod = method,
                    paymentStatus = paymentStatus,
                    utr = utr
                )
            }
        )
    }

    // Cart Checkout Sheet
    if (isCartCheckoutOpen && cartItems.isNotEmpty()) {
        CartCheckoutSheet(
            cartItems = cartItems,
            defaultName = userProfile.name,
            defaultPhone = userProfile.phone,
            defaultEmail = userProfile.email,
            onDismiss = { viewModel.closeCartCheckout() },
            onConfirmOrder = { name, phone, email, loc, date, method, paymentStatus, utr ->
                viewModel.placeCartOrders(
                    name = name,
                    phone = phone,
                    email = email,
                    location = loc,
                    date = date,
                    paymentMethod = method,
                    paymentStatus = paymentStatus,
                    utr = utr
                )
            }
        )
    }

    // Notifications Dialog
    if (isNotificationDialogOpen) {
        NotificationsDialog(
            notifications = notifications,
            onDismiss = { viewModel.setNotificationDialog(false) }
        )
    }

    // Last Placed Order Confirmed Dialog
    lastPlacedOrder?.let { order ->
        OrderConfirmedDialog(
            order = order,
            onDismiss = {
                viewModel.dismissLastOrder()
                viewModel.setUserTab(UserTab.PROFILE)
            }
        )
    }

    // Secret Master PIN Dialog (Only opens when owner taps logo or version 5 times!)
    if (showSecretPinDialog) {
        SecretMasterPinDialog(
            onDismiss = { showSecretPinDialog = false },
            onSubmitPin = { pin ->
                val ok = viewModel.verifyMasterPin(pin)
                if (ok) {
                    showSecretPinDialog = false
                }
            }
        )
    }
}

// ----------------- HOME VIEW -----------------
@Composable
fun HomeView(
    products: List<ProductEntity>,
    banners: List<BannerEntity>,
    storeConfig: StoreConfigEntity?,
    searchQuery: String,
    selectedCategory: String,
    selectedCondition: String,
    onSearchChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onConditionChange: (String) -> Unit,
    onProductClick: (ProductEntity) -> Unit,
    onBuyNow: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit,
    onExploreClick: () -> Unit,
    onBannerAction: (String) -> Unit
) {
    val searchPlaceholderText = storeConfig?.searchPlaceholder?.ifBlank {
        "Search laptops, accessories, specs..."
    } ?: "Search laptops, accessories, specs..."

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input"),
                placeholder = { Text(searchPlaceholderText, color = TextSecondary, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TechBlueLight)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary)
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TechBlueBright,
                    unfocusedBorderColor = BorderDark,
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )
        }

        // Hero Auto-Swiping Carousel Banners (Supports up to 10 slots: 6 Custom + 4 Auto Ad Units in 300x250)
        item {
            val displayBanners = if (banners.isNotEmpty()) banners else listOf(
                BannerEntity(
                    id = 1L,
                    title = "Best Laptops, Best Prices",
                    subtitle = "New & Used Laptops With Trusted Quality Guarantee",
                    badgeText = "100% QUALITY TESTED",
                    actionText = "Explore Now",
                    targetCategory = "Laptop",
                    gradientStartHex = "#1E3A8A",
                    gradientEndHex = "#312E81",
                    bannerType = "CUSTOM",
                    slotNumber = 1
                )
            )

            val pagerState = rememberPagerState(pageCount = { displayBanners.size })
            val autoIntervalSec = (storeConfig?.bannerIntervalSeconds ?: 5).coerceIn(2, 600)
            val coroutineScope = rememberCoroutineScope()

            // Continuous auto-swipe loop that never gets cancelled mid-animation
            androidx.compose.runtime.LaunchedEffect(displayBanners.size, autoIntervalSec) {
                if (displayBanners.size > 1) {
                    while (true) {
                        delay(autoIntervalSec * 1000L)
                        if (!pagerState.isScrollInProgress) {
                            val next = (pagerState.currentPage + 1) % displayBanners.size
                            pagerState.animateScrollToPage(
                                page = next,
                                animationSpec = androidx.compose.animation.core.tween(
                                    durationMillis = 650,
                                    easing = androidx.compose.animation.core.FastOutSlowInEasing
                                )
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth(),
                    pageSpacing = 0.dp
                ) { page ->
                    val currentBanner = displayBanners[page]
                    val isAutoAd = currentBanner.bannerType == "AUTO_AD"
                    val startColor = try {
                        Color(android.graphics.Color.parseColor(currentBanner.gradientStartHex))
                    } catch (_: Exception) {
                        Color(if (isAutoAd) 0xFF0C4A6E else 0xFF1E3A8A)
                    }
                    val endColor = try {
                        Color(android.graphics.Color.parseColor(currentBanner.gradientEndHex))
                    } catch (_: Exception) {
                        Color(if (isAutoAd) 0xFF082F49 else 0xFF312E81)
                    }

                    Surface(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isAutoAd) TechCyan.copy(alpha = 0.5f) else BorderDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBannerAction(currentBanner.targetCategory) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 190.dp, max = 220.dp)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(startColor, endColor)
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Top row: Badge & Slot indicator
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isAutoAd) Icons.Default.Shield else Icons.Default.Verified,
                                            contentDescription = if (isAutoAd) "Ad Unit" else "Verified",
                                            tint = if (isAutoAd) AccentAmber else TechCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = currentBanner.badgeText.ifBlank {
                                                if (isAutoAd) "300x250 AUTO AD" else "FEATURED DEAL"
                                            },
                                            color = if (isAutoAd) AccentAmber else TechCyan,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Surface(
                                        color = Color.Black.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (isAutoAd) "Ad Slot #${currentBanner.slotNumber} • 300x250" else "Slot #${currentBanner.slotNumber}",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                // Middle row: Content and optional image
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f, fill = false)
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = currentBanner.title,
                                            color = Color.White,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = currentBanner.subtitle,
                                            color = TextBody,
                                            fontSize = 11.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(vertical = 3.dp)
                                        )

                                        if (isAutoAd && currentBanner.adUnitId.isNotBlank()) {
                                            Text(
                                                text = "Ad Unit ID: ${currentBanner.adUnitId}",
                                                color = TechCyan.copy(alpha = 0.85f),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    if (currentBanner.imageUrl.isNotBlank()) {
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color.Black.copy(alpha = 0.3f))
                                        ) {
                                            AsyncImage(
                                                model = currentBanner.imageUrl,
                                                contentDescription = currentBanner.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }

                                // Bottom row: CTA button & Slide countdown info
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = { onBannerAction(currentBanner.targetCategory) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isAutoAd) TechCyan.copy(alpha = 0.3f) else TechBlue
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                        modifier = Modifier
                                            .height(34.dp)
                                            .testTag("banner_action_button")
                                    ) {
                                        Text(
                                            text = currentBanner.actionText.ifBlank {
                                                if (isAutoAd) "Visit Partner" else "Explore Now"
                                            },
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                    }

                                    if (displayBanners.size > 1) {
                                        Text(
                                            text = "Auto-swipe: ${autoIntervalSec}s",
                                            color = Color.White.copy(alpha = 0.65f),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Dot Indicators
                if (displayBanners.size > 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        displayBanners.indices.forEach { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .size(if (isSelected) 18.dp else 6.dp, 6.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) TechBlueBright else BorderDark)
                                    .clickable {
                                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                    }
                            )
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            val categories = listOf("All", "Laptops", "Accessories", "SSD/HDD", "Dongles")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { onCategoryChange(cat) },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TechBlue,
                            selectedLabelColor = Color.White,
                            containerColor = CardDark,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory == cat,
                            borderColor = if (selectedCategory == cat) TechBlueBright else BorderDark
                        )
                    )
                }
            }
        }

        // Condition Filter Chips (New, Used, Refurbished)
        item {
            val conditions = listOf("All", "New", "Used", "Refurbished")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Condition:", color = TextSecondary, fontSize = 11.sp)
                for (cond in conditions) {
                    val isSel = selectedCondition == cond
                    Surface(
                        color = if (isSel) TechBlue.copy(alpha = 0.25f) else CardDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSel) TechBlueBright else BorderDark
                        ),
                        modifier = Modifier
                            .clickable { onConditionChange(cond) }
                            .padding(1.dp)
                    ) {
                        Text(
                            text = cond,
                            color = if (isSel) TechBlueLight else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // Guarantee Strip
        item {
            StoreGuaranteeBanner()
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Featured Products (${products.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "100% In Stock",
                    fontSize = 11.sp,
                    color = AccentGreenBright,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Products Grid
        if (products.isEmpty()) {
            item {
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("कोई प्रोडक्ट नहीं मिला।", color = TextSecondary, fontSize = 14.sp)
                        Text("कृपया अन्य कीवर्ड या फ़िल्टर आज़माएँ।", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }
        } else {
            // Render items in 2-column cards
            items(products.chunked(2)) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (product in pair) {
                        ProductCard(
                            product = product,
                            onCardClick = { onProductClick(product) },
                            onBuyNow = { onBuyNow(product) },
                            onAddToCart = { onAddToCart(product) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductEntity,
    onCardClick: () -> Unit,
    onBuyNow: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
        modifier = modifier
            .clickable { onCardClick() }
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Image with Condition Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                ConditionBadge(
                    condition = product.condition,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                )
                if (product.isInStock) {
                    Surface(
                        color = Color(0xCC064E3B),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "In Stock",
                            color = AccentGreenBright,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Specs brief
            if (product.processor.isNotBlank() || product.ram.isNotBlank()) {
                Text(
                    text = "${product.processor} ${if (product.ram.isNotBlank()) "• " + product.ram else ""}",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Price & MRP
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatInr(product.price),
                    color = TechBlueLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                if (product.originalPrice > product.price) {
                    Text(
                        text = formatInr(product.originalPrice),
                        color = TextMuted,
                        fontSize = 10.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = AccentAmber,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${product.rating} (${product.reviewCount})",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Direct Buy Button (Instant Checkout!)
            Button(
                onClick = onBuyNow,
                colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("buy_now_btn_${product.id}"),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Buy Now", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Add to Cart
            OutlinedButton(
                onClick = onAddToCart,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .testTag("add_to_cart_btn_${product.id}"),
                contentPadding = PaddingValues(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
            ) {
                Text("Add to Cart", color = TextBody, fontSize = 11.sp)
            }
        }
    }
}

// ----------------- CATEGORY VIEW -----------------
@Composable
fun CategoryView(
    products: List<ProductEntity>,
    onProductClick: (ProductEntity) -> Unit,
    onBuyNow: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit
) {
    var selectedTab by remember { mutableStateOf("All") }
    val categories = listOf("All", "Laptop", "Accessories", "SSD/HDD", "Dongles")

    val filtered = if (selectedTab == "All") products else products.filter { it.category == selectedTab }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Browse By Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedTab == cat,
                        onClick = { selectedTab = cat },
                        label = { Text(cat, fontSize = 12.sp) },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductClick(p) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ConditionBadge(condition = p.condition)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = p.category,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = p.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = p.keyFeatures,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatInr(p.price),
                            fontWeight = FontWeight.ExtraBold,
                            color = TechBlueLight,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Button(
                            onClick = { onBuyNow(p) },
                            colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp)
                        ) {
                            Text("Buy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// ----------------- ACCESSORIES VIEW -----------------
@Composable
fun AccessoriesView(
    products: List<ProductEntity>,
    onProductClick: (ProductEntity) -> Unit,
    onBuyNow: (ProductEntity) -> Unit,
    onAddToCart: (ProductEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Accessories & Upgrades",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "SSDs, HDDs, Type-C Dongles, Cooling Pads & Wireless Peripherals",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        items(products) { item ->
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductClick(item) }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceDark)
                    ) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Text(
                            text = item.description,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatInr(item.price),
                                fontWeight = FontWeight.Bold,
                                color = TechBlueLight,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "⭐ ${item.rating}",
                                fontSize = 10.sp,
                                color = AccentAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onBuyNow(item) },
                        colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Buy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ----------------- CART VIEW -----------------
@Composable
fun CartView(
    cartItems: List<com.example.data.repository.CartItem>,
    onUpdateQuantity: (Long, Int) -> Unit,
    onRemove: (Long) -> Unit,
    onCheckout: () -> Unit,
    onStartShopping: () -> Unit
) {
    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Empty Cart",
                    tint = TextSecondary,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "आपका कार्ट खाली है।",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "लैपटॉप या एक्सेसरीज़ चुनकर कार्ट में जोड़ें।",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onStartShopping,
                    colors = ButtonDefaults.buttonColors(containerColor = TechBlue)
                ) {
                    Text("Start Shopping")
                }
            }
        }
    } else {
        val subtotal = cartItems.sumOf { it.product.price * it.quantity }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Text(
                text = "My Cart (${cartItems.size} items)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cartItems) { item ->
                    Surface(
                        color = CardDark,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceDark)
                            ) {
                                AsyncImage(
                                    model = item.product.imageUrl,
                                    contentDescription = item.product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.product.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = formatInr(item.product.price),
                                    color = TechBlueLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            // Quantity Adjuster
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(SurfaceDark, RoundedCornerShape(6.dp))
                                    .padding(2.dp)
                            ) {
                                IconButton(
                                    onClick = { onUpdateQuantity(item.product.id, -1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                                Text(
                                    text = item.quantity.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                IconButton(
                                    onClick = { onUpdateQuantity(item.product.id, 1) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            IconButton(
                                onClick = { onRemove(item.product.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = AccentRed, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtotal Card & Checkout Button
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount:", color = TextSecondary, fontSize = 13.sp)
                        Text(formatInr(subtotal), color = TechBlueLight, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onCheckout,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("checkout_cart_button")
                    ) {
                        Text("Proceed to Checkout", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// ----------------- PROFILE VIEW -----------------
@Composable
fun ProfileView(
    userProfile: com.example.data.repository.UserProfile,
    orders: List<OrderEntity>,
    onCancelOrder: (Long) -> Unit,
    onTriggerSecretDoor: () -> Unit,
    onUpdateProfile: (String, String, String) -> Unit
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    val currentTime = System.currentTimeMillis()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Card
        item {
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(TechBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.firstOrNull()?.uppercase() ?: "U",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = userProfile.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "+91 ${userProfile.phone}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Google Account",
                            tint = AccentGreenBright,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Linked with Google (${userProfile.email})",
                            fontSize = 11.sp,
                            color = TechCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Text("Edit Name & WhatsApp Number", fontSize = 11.sp, color = TechBlueLight)
                    }
                }
            }
        }

        // Store Pickup Location Info Card
        item {
            Surface(
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TechBlue.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Store, contentDescription = "Shop", tint = TechBlueBright, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Shop Location & Liluah Station Pickup", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📍 Liluah Station Platform 1 Exit / Near Main Market, Liluah, Howrah.\n📞 Support: +91 98765 43210 (10:00 AM - 08:30 PM)",
                        color = TextBody,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Orders History Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Orders History (${orders.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Live Sync",
                    fontSize = 11.sp,
                    color = AccentGreenBright
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                Surface(
                    color = CardDark,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("अभी तक कोई आर्डर नहीं किया गया है।", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(orders) { ord ->
                val hoursPassed = (currentTime - ord.orderTime) / (1000.0 * 60 * 60)
                val canCancel = hoursPassed < 2.0 && ord.status != "Cancelled" && ord.status != "Delivered"

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
                            Text(
                                text = ord.orderNumber,
                                fontWeight = FontWeight.Bold,
                                color = TechCyan,
                                fontSize = 12.sp
                            )
                            StatusBadge(status = ord.status)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = ord.productName,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = formatInr(ord.productPrice),
                                fontWeight = FontWeight.ExtraBold,
                                color = TechBlueLight,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "पिकअप स्थान: ${ord.pickupLocation}", color = TextBody, fontSize = 11.sp)
                        Text(text = "पिकअप तारीख: ${ord.pickupDate}", color = TextBody, fontSize = 11.sp)
                        Text(
                            text = "भुगतान: ${ord.paymentMethod} (${ord.paymentStatus})",
                            color = if (ord.paymentStatus == "Approved") AccentGreenBright else TextSecondary,
                            fontSize = 11.sp
                        )
                        if (ord.utrNumber.isNotBlank()) {
                            Text(text = "UTR Ref: ${ord.utrNumber}", color = TechCyan, fontSize = 10.sp)
                        }

                        val timeStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(ord.orderTime))
                        Text(text = "समय: $timeStr", color = TextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))

                        // Guarantee notice
                        Surface(
                            color = Color(0x1F22C55E),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Text(
                                text = "🛡️ ${ord.guaranteeNote}",
                                color = AccentGreenBright,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(6.dp)
                            )
                        }

                        // 2-hour cancellation handling
                        if (ord.status == "Cancelled") {
                            Text("यह ऑर्डर कैंसिल कर दिया गया है।", color = AccentRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else if (canCancel) {
                            Button(
                                onClick = { onCancelOrder(ord.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(34.dp)
                                    .testTag("cancel_order_${ord.id}")
                            ) {
                                Text("Cancel Order (Within 2 Hours Allowed)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else if (ord.status != "Delivered") {
                            Text(
                                text = "⏳ Cancellation period expired (2 hours over - material is packed at shop)",
                                color = AccentAmber,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Discreet App Footer (Secret 5-Tap door for store owner to unlock admin!)
        item {
            var secretVersionTapCount by remember { mutableStateOf(0) }
            var lastVersionTapTime by remember { mutableStateOf(0L) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 24.dp)
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null // Silent with no visual ripple so regular users don't know
                    ) {
                        val now = System.currentTimeMillis()
                        if (now - lastVersionTapTime < 1500) {
                            secretVersionTapCount++
                            if (secretVersionTapCount >= 5) {
                                secretVersionTapCount = 0
                                onTriggerSecretDoor()
                            }
                        } else {
                            secretVersionTapCount = 1
                        }
                        lastVersionTapTime = now
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Laptops Store • Version 1.0.4",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Trusted Laptop Store • Liluah Pickup",
                        color = Color(0xFF334155),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }

    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(userProfile.name) }
        var editPhone by remember { mutableStateOf(userProfile.phone) }
        var editEmail by remember { mutableStateOf(userProfile.email) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Customer Profile", color = Color.White) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name (नाम)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it.filter { ch -> ch.isDigit() } },
                        label = { Text("10-Digit WhatsApp Mobile (नंबर)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email (ईमेल)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editName, editPhone, editEmail)
                        showEditProfileDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CardDark
        )
    }
}

// ----------------- PRODUCT DETAIL MODAL SHEET -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailModalSheet(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onBuyNow: () -> Unit,
    onAddToCart: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ConditionBadge(condition = product.condition)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            // Big Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0B0F19))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title & Price
            Text(
                text = product.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatInr(product.price),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TechBlueLight
                )
                Spacer(modifier = Modifier.width(10.dp))
                if (product.originalPrice > product.price) {
                    Text(
                        text = formatInr(product.originalPrice),
                        fontSize = 14.sp,
                        color = TextMuted,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val discount = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                    Surface(
                        color = Color(0xFF064E3B),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "$discount% OFF",
                            color = AccentGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // In Stock & Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = AccentAmber, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${product.rating} (${product.reviewCount} Reviews)",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "• In Stock: ${product.stockCount} units",
                    fontSize = 12.sp,
                    color = AccentGreenBright,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Specifications Grid
            Text(text = "Key Specifications", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            val specs = listOf(
                Pair("Processor", product.processor.ifBlank { "High-Speed CPU" }),
                Pair("Memory (RAM)", product.ram.ifBlank { "8GB / 16GB High Speed" }),
                Pair("Storage", product.storage.ifBlank { "Ultra Fast SSD" }),
                Pair("Display Screen", product.display.ifBlank { "Anti-Glare HD/FHD" }),
                Pair("Warranty", "6 Months Testing Warranty"),
                Pair("Pickup Ready", "100% Ready at Shop on Order Date")
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                specs.chunked(2).forEach { rowSpecs ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (spec in rowSpecs) {
                            Surface(
                                color = CardDark,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(spec.first, color = TextSecondary, fontSize = 10.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(spec.second, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Description
            if (product.description.isNotBlank()) {
                Text(text = "Description & Condition", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = product.description, fontSize = 12.sp, color = TextBody, lineHeight = 18.sp)
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Store Pickup Guarantee
            StoreGuaranteeBanner()

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Add to Cart", color = TextBody, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onBuyNow,
                    colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("detail_buy_now_button")
                ) {
                    Text("Buy Now (Direct)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ----------------- INSTANT CHECKOUT SHEET -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstantCheckoutSheet(
    product: ProductEntity,
    defaultName: String,
    defaultPhone: String,
    defaultEmail: String,
    onDismiss: () -> Unit,
    onConfirmOrder: (name: String, phone: String, email: String, loc: String, date: String, method: String, paymentStatus: String, utr: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf(defaultName) }
    var phone by remember { mutableStateOf(defaultPhone) }
    var email by remember { mutableStateOf(defaultEmail) }
    var location by remember { mutableStateOf("Direct Shop Pickup") }
    var pickupDate by remember { mutableStateOf("Tomorrow (11:00 AM - 08:00 PM)") }

    // Payment method: "COD" vs "ONLINE"
    var paymentMethod by remember { mutableStateOf("COD") } // Cash on Delivery is preferred!
    var utrNumber by remember { mutableStateOf("") }
    var onlinePaidApproved by remember { mutableStateOf(false) }

    // Validation state
    val cleanDigits = phone.filter { it.isDigit() }
    val isPhoneValid = cleanDigits.length == 10 && (cleanDigits.startsWith("6") || cleanDigits.startsWith("7") || cleanDigits.startsWith("8") || cleanDigits.startsWith("9"))
    val phoneError = phone.isNotBlank() && !isPhoneValid

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Instant Buy Checkout",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            // Product Summary Card
            Surface(
                color = CardDark,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceDark)
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(product.condition, color = TextSecondary, fontSize = 11.sp)
                    }
                    Text(
                        formatInr(product.price),
                        color = TechBlueLight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pre-filled Google Account verification
            Surface(
                color = Color(0x1A2563EB),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TechBlue.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Google Login",
                        tint = TechCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Linked With Google Account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("Name pre-filled: $name", color = TechBlueLight, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Customer Information", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))

            // Full Name input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name (ग्राहक का नाम)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("checkout_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TechBlueBright,
                    unfocusedBorderColor = BorderDark,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Phone number with strict validation
            OutlinedTextField(
                value = phone,
                onValueChange = { input ->
                    phone = input.filter { it.isDigit() }.take(10)
                },
                label = { Text("10-Digit Mobile / WhatsApp (मोबाइल नंबर)") },
                placeholder = { Text("e.g. 9876543210") },
                isError = phoneError,
                supportingText = {
                    if (phoneError) {
                        Text(
                            text = "❌ कृपया वैध 10-अंकों का WhatsApp नंबर दर्ज करें (6, 7, 8 या 9 से शुरू)",
                            color = AccentRed,
                            fontSize = 11.sp
                        )
                    } else if (isPhoneValid) {
                        Text(
                            text = "✓ सही WhatsApp नंबर (ऑर्डर अपडेट इस नंबर पर भेजा जाएगा)",
                            color = AccentGreenBright,
                            fontSize = 11.sp
                        )
                    } else {
                        Text("10 अंक डालें (उदाहरण: 9876543210)", color = TextSecondary, fontSize = 10.sp)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("checkout_phone_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (phoneError) AccentRed else TechBlueBright,
                    unfocusedBorderColor = if (phoneError) AccentRed else BorderDark,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Pickup Location Selection
            Text("Pickup Location (सामान कहाँ से लेना है)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))

            val locations = listOf(
                Pair("Direct Shop Pickup", "Direct Shop Pickup (दुकान से सीधे पिकअप - Liluah Market)"),
                Pair("Liluah Station Pickup", "Liluah Station Pickup (लिलुआ स्टेशन प्लेटफार्म 1 पर लें)")
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                locations.forEach { loc ->
                    val isSel = location == loc.first
                    Surface(
                        color = if (isSel) TechBlue.copy(alpha = 0.2f) else CardDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) TechBlueBright else BorderDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { location = loc.first }
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSel,
                                onClick = { location = loc.first },
                                colors = RadioButtonDefaults.colors(selectedColor = TechBlueBright)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(loc.second, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pickup Date Selection
            Text("Pickup Date (किस दिन सामान लेना चाहते हैं)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))

            val dates = listOf(
                "Today (आज ही शाम 5:00 PM)",
                "Tomorrow (कल सुबह 11:00 AM)",
                "Within 2 Days (2 दिन के अंदर)"
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                dates.forEach { dt ->
                    val isSel = pickupDate == dt
                    Surface(
                        color = if (isSel) TechBlue.copy(alpha = 0.25f) else CardDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) TechBlueBright else BorderDark),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { pickupDate = dt }
                    ) {
                        Text(
                            text = dt.substringBefore(" ("),
                            color = if (isSel) TechBlueLight else TextSecondary,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Options: COD (Preferred!) vs ONLINE
            Text("Payment Method (भुगतान का तरीका)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))

            // COD Option (Recommended)
            Surface(
                color = if (paymentMethod == "COD") Color(0xFF0F2E22) else CardDark,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (paymentMethod == "COD") AccentGreenBright else BorderDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { paymentMethod = "COD" }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = paymentMethod == "COD",
                            onClick = { paymentMethod = "COD" },
                            colors = RadioButtonDefaults.colors(selectedColor = AccentGreenBright)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Cash on Delivery (Pay at Pickup)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = AccentGreen,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "★ RECOMMENDED",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "दुकान/स्टेशन पर खुद टेस्ट करके कैश दें।",
                                color = TextBody,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // 100% guarantee reminder
                    Surface(
                        color = Color(0x2222C55E),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = "Guarantee", tint = AccentGreenBright, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "100% गारंटी: चुनी हुई तारीख ($pickupDate) पर आपका लैपटॉप पूरी तरह रेडी मिलेगा! कोई चक्कर नहीं काटना पड़ेगा।",
                                color = AccentGreenBright,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Online UPI Option
            Surface(
                color = if (paymentMethod == "ONLINE") Color(0xFF132247) else CardDark,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (paymentMethod == "ONLINE") TechBlueBright else BorderDark
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { paymentMethod = "ONLINE" }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = paymentMethod == "ONLINE",
                            onClick = { paymentMethod = "ONLINE" },
                            colors = RadioButtonDefaults.colors(selectedColor = TechBlueBright)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Online UPI / QR Code Payment",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "GPay, PhonePe, Paytm ya kisi bhi UPI se turant payment karein",
                                color = TextBody,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // When Online is selected, show authentic QR Code & UTR input
                    if (paymentMethod == "ONLINE") {
                        Spacer(modifier = Modifier.height(10.dp))
                        UpiQrCodeCanvas(
                            upiId = "laptops.store@okaxis",
                            amount = product.price,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = utrNumber,
                            onValueChange = { utrNumber = it },
                            label = { Text("UPI Reference / UTR Number (12 डिजिट UTR नंबर)") },
                            placeholder = { Text("e.g. 430918765219") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TechBlueBright,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = {
                                if (utrNumber.length >= 6) {
                                    onlinePaidApproved = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (onlinePaidApproved) AccentGreen else TechBlue
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (onlinePaidApproved) {
                                Icon(Icons.Default.Check, contentDescription = "Approved", tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Payment Verified & Approved!", fontWeight = FontWeight.Bold)
                            } else {
                                Text("Verify & Confirm UPI Payment", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm & Place Order Button
            Button(
                onClick = {
                    val finalPayMethod = if (paymentMethod == "COD") "Cash on Delivery" else "Online UPI"
                    val finalPayStatus = if (paymentMethod == "ONLINE" && onlinePaidApproved) "Approved" else "Pending"

                    onConfirmOrder(
                        name,
                        phone,
                        email,
                        location,
                        pickupDate,
                        finalPayMethod,
                        finalPayStatus,
                        utrNumber
                    )
                },
                enabled = isPhoneValid && name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (paymentMethod == "COD") AccentGreen else TechBlue
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_order_button")
            ) {
                Text(
                    text = "Confirm & Place Order (${formatInr(product.price)})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ----------------- CART CHECKOUT SHEET -----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutSheet(
    cartItems: List<com.example.data.repository.CartItem>,
    defaultName: String,
    defaultPhone: String,
    defaultEmail: String,
    onDismiss: () -> Unit,
    onConfirmOrder: (name: String, phone: String, email: String, loc: String, date: String, method: String, paymentStatus: String, utr: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val totalAmount = cartItems.sumOf { it.product.price * it.quantity }

    var name by remember { mutableStateOf(defaultName) }
    var phone by remember { mutableStateOf(defaultPhone) }
    var email by remember { mutableStateOf(defaultEmail) }
    var location by remember { mutableStateOf("Direct Shop Pickup") }
    var pickupDate by remember { mutableStateOf("Tomorrow (11:00 AM - 08:00 PM)") }
    var paymentMethod by remember { mutableStateOf("COD") }
    var utrNumber by remember { mutableStateOf("") }
    var onlinePaidApproved by remember { mutableStateOf(false) }

    val cleanDigits = phone.filter { it.isDigit() }
    val isPhoneValid = cleanDigits.length == 10 && (cleanDigits.startsWith("6") || cleanDigits.startsWith("7") || cleanDigits.startsWith("8") || cleanDigits.startsWith("9"))

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Cart Checkout (${cartItems.size} items)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Total Payable: ${formatInr(totalAmount)}", color = TechBlueLight, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name (नाम)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it.filter { ch -> ch.isDigit() }.take(10) },
                label = { Text("10-Digit Mobile / WhatsApp") },
                supportingText = {
                    if (isPhoneValid) Text("✓ Verified Mobile", color = AccentGreenBright, fontSize = 10.sp)
                    else Text("कृपया 10-अंकों का नंबर दर्ज करें (6-9)", color = TextSecondary, fontSize = 10.sp)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text("Pickup Location:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    color = if (location == "Direct Shop Pickup") TechBlue.copy(alpha = 0.25f) else CardDark,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { location = "Direct Shop Pickup" }
                        .padding(2.dp)
                ) {
                    Text("Direct Shop Pickup", color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                }
                Surface(
                    color = if (location == "Liluah Station Pickup") TechBlue.copy(alpha = 0.25f) else CardDark,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { location = "Liluah Station Pickup" }
                        .padding(2.dp)
                ) {
                    Text("Liluah Station Pickup", color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = {
                    onConfirmOrder(name, phone, email, location, pickupDate, if (paymentMethod == "COD") "Cash on Delivery" else "Online UPI", "Pending", utrNumber)
                },
                enabled = isPhoneValid && name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Confirm All Orders (${formatInr(totalAmount)})", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ----------------- NOTIFICATIONS DIALOG -----------------
@Composable
fun NotificationsDialog(
    notifications: List<NotificationEntity>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = "Bell", tint = TechBlueBright)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Store Notifications", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            if (notifications.isEmpty()) {
                Text("कोई नई नोटिफिकेशन नहीं है।", color = TextSecondary, fontSize = 13.sp)
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notifications) { notif ->
                        Surface(
                            color = CardDark,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(notif.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(notif.message, color = TextBody, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        containerColor = SurfaceDark
    )
}

// ----------------- ORDER CONFIRMED DIALOG -----------------
@Composable
fun OrderConfirmedDialog(
    order: OrderEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = AccentGreenBright,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("ऑर्डर सफलतापूर्वक प्लेस हो गया!", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Order ID: ${order.orderNumber}",
                    color = TechCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = order.productName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Total Amount: ${formatInr(order.productPrice)}",
                    color = TechBlueLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFF0F2E22),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AccentGreenBright),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("🛡️ 100% Pickup Guarantee", color = AccentGreenBright, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(
                            text = "पिकअप स्थान: ${order.pickupLocation}\nतारीख: ${order.pickupDate}\nसामान समय पर टेस्ट करके रेडी रखा जाएगा।",
                            color = TextBody,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View In My Orders")
            }
        },
        containerColor = SurfaceDark
    )
}

// ----------------- SECRET OWNER PIN DIALOG -----------------
@Composable
fun SecretMasterPinDialog(
    onDismiss: () -> Unit,
    onSubmitPin: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Master Key",
                    tint = TechCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Store Administration", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Owner Authentication", color = TextSecondary, fontSize = 11.sp)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "कृपया अपना 4-अंकों का गुप्त मास्टर पिन दर्ज करें:",
                    color = TextBody,
                    fontSize = 12.sp
                )
                OutlinedTextField(
                    value = pin,
                    onValueChange = { input ->
                        if (input.length <= 6 && input.all { it.isDigit() }) {
                            pin = input
                        }
                    },
                    label = { Text("Master PIN") },
                    placeholder = { Text("••••") },
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("secret_master_pin_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TechBlueBright,
                        unfocusedBorderColor = BorderDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Text(
                    text = "🔒 यह केवल स्टोर ओनर के लिए है। गलत पिन डालने पर यह तुरंत बंद हो जाएगा।",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmitPin(pin) },
                colors = ButtonDefaults.buttonColors(containerColor = TechBlue),
                modifier = Modifier.testTag("submit_master_pin_btn")
            ) {
                Text("Unlock Admin", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = SurfaceDark
    )
}

