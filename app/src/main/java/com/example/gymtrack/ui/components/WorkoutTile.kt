package com.example.gymtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    modifier: Modifier = Modifier
) {
    val dateFormatted = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        .format(Date(dateMillis))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MahoganyRed)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Green checkmark circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isCompleted) androidx.compose.ui.graphics.Color(0xFF2ECC71) else Silver),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Title
        Text(
            text = title,
            color = White,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        // Date
        Text(
            text = dateFormatted,
            color = White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
