package com.example.gymtrack.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtrack.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WorkoutTile(
    title: String,
    dateMillis: Long,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    onTitleClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        .format(Date(dateMillis))

    val checkColor by animateColorAsState(
        targetValue = if (isCompleted) Color(0xFF2ECC71) else Silver,
        animationSpec = tween(durationMillis = 300),
        label = "CheckmarkColor"
    )

    ElevatedCard(
        onClick   = onTitleClick,
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.elevatedCardColors(containerColor = MahoganyRed),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(checkColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint               = White,
                    modifier           = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text       = title,
                color      = White,
                fontWeight = FontWeight.Bold,
                fontSize   = 17.sp,
                modifier   = Modifier.weight(1f),
                style      = MaterialTheme.typography.titleMedium.copy(color = White, fontWeight = FontWeight.Bold)
            )

            Text(
                text       = dateFormatted,
                color      = White,
                style      = MaterialTheme.typography.bodySmall.copy(color = White),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
