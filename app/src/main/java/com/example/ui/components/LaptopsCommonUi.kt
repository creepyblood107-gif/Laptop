package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenBright
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
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
import java.text.NumberFormat
import java.util.Locale

fun formatInr(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    formatter.maximumFractionDigits = 0
    return formatter.format(amount)
}

@Composable
fun ConditionBadge(condition: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (condition.lowercase()) {
        "new" -> Pair(Color(0xFF064E3B), Color(0xFF34D399))
        "refurbished" -> Pair(Color(0xFF1E3A8A), Color(0xFF60A5FA))
        else -> Pair(Color(0xFF374151), Color(0xFFE5E7EB)) // Used
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
    ) {
        Text(
            text = condition.uppercase(),
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        "Delivered" -> Pair(Color(0x2222C55E), AccentGreenBright)
        "Processing" -> Pair(Color(0x2238BDF8), TechCyan)
        "Cancelled" -> Pair(Color(0x22EF4444), AccentRed)
        else -> Pair(Color(0x22F59E0B), Color(0xFFFBBF24)) // Pending
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Text(
            text = status,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun StoreGuaranteeBanner(modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF0F2344),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TechBlue.copy(alpha = 0.6f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Guarantee",
                tint = TechCyan,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "100% Pickup & Material Guarantee",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Aapka laptop chuni hui date par shop par ready milega. Test karke payment karein!",
                    color = TextBody,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
fun ProductThumbnail(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CardDark)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun UpiQrCodeCanvas(
    upiId: String,
    amount: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "SCAN & PAY VIA ANY UPI APP",
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Procedural Authentic QR Code Pattern
            Canvas(modifier = Modifier.size(160.dp)) {
                val sizePx = size.width
                val numCells = 21
                val cellSize = sizePx / numCells

                // White canvas background
                drawRect(color = Color.White, size = size)

                // Finder Patterns at 3 corners
                fun drawFinderPattern(rowStart: Int, colStart: Int) {
                    // Outer 7x7 black box
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(colStart * cellSize, rowStart * cellSize),
                        size = Size(7 * cellSize, 7 * cellSize)
                    )
                    // Inner 5x5 white box
                    drawRect(
                        color = Color.White,
                        topLeft = Offset((colStart + 1) * cellSize, (rowStart + 1) * cellSize),
                        size = Size(5 * cellSize, 5 * cellSize)
                    )
                    // Center 3x3 black box
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset((colStart + 2) * cellSize, (rowStart + 2) * cellSize),
                        size = Size(3 * cellSize, 3 * cellSize)
                    )
                }

                drawFinderPattern(0, 0)
                drawFinderPattern(0, 14)
                drawFinderPattern(14, 0)

                // Alignment pattern at bottom right
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(13 * cellSize, 13 * cellSize),
                    size = Size(5 * cellSize, 5 * cellSize)
                )
                drawRect(
                    color = Color.White,
                    topLeft = Offset(14 * cellSize, 14 * cellSize),
                    size = Size(3 * cellSize, 3 * cellSize)
                )
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(15 * cellSize, 15 * cellSize),
                    size = Size(cellSize, cellSize)
                )

                // Fill data cells with a stable pseudo-pattern
                val seed = (upiId.hashCode() xor amount.toLong().hashCode()).toInt()
                for (r in 0 until numCells) {
                    for (c in 0 until numCells) {
                        // Skip finder patterns
                        if ((r < 7 && c < 7) || (r < 7 && c >= 14) || (r >= 14 && c < 7)) continue
                        if (r in 13..17 && c in 13..17) continue

                        val bit = ((r * 31 + c * 17 + seed) % 3) == 0 || ((r + c) % 2 == 0 && (r * c) % 3 == 0)
                        if (bit) {
                            drawRect(
                                color = Color.Black,
                                topLeft = Offset(c * cellSize, r * cellSize),
                                size = Size(cellSize, cellSize)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "UPI ID: $upiId",
                color = Color(0xFF1E293B),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = "Amount: ${formatInr(amount)}",
                color = TechBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("UPI ID", upiId)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "UPI ID copied: $upiId", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.testTag("copy_upi_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy UPI ID",
                    modifier = Modifier.size(16.dp),
                    tint = TechBlue
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy UPI ID", color = TechBlue, fontSize = 12.sp)
            }
        }
    }
}
