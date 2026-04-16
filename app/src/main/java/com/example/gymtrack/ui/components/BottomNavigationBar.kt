package com.example.gymtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtrack.ui.theme.*

enum class BottomNavTab { HISTORY, NEW_TRAINING, PROFILE }

@Composable
fun BottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CarbonBlack)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // History
        BottomNavItem(
            icon = Icons.Default.DateRange,
            label = "History",
            selected = selectedTab == BottomNavTab.HISTORY,
            onClick = { onTabSelected(BottomNavTab.HISTORY) }
        )

        // przycisk +  w środku
        Column(
            modifier = Modifier
                .width(80.dp)
                .clickable { onTabSelected(BottomNavTab.NEW_TRAINING) }
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = if (selectedTab == BottomNavTab.NEW_TRAINING) MahoganyRed else Silver,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Add",
                color = if (selectedTab == BottomNavTab.NEW_TRAINING) MahoganyRed else Silver,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        // Profil
        BottomNavItem(
            icon = Icons.Default.AccountCircle,
            label = "Profile",
            selected = selectedTab == BottomNavTab.PROFILE,
            onClick = { onTabSelected(BottomNavTab.PROFILE) }
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (selected) MahoganyRed else Silver

    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
