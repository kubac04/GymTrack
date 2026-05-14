package com.example.gymtrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.example.gymtrack.ui.theme.*

enum class BottomNavTab { HISTORY, NEW_TRAINING, PROFILE }

@Composable
fun BottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    NavigationBar(containerColor = CarbonBlack) {
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.HISTORY,
            onClick  = { onTabSelected(BottomNavTab.HISTORY) },
            icon     = { Icon(Icons.Default.DateRange, contentDescription = "History") },
            label    = { Text("History") },
            colors   = navItemColors()
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.NEW_TRAINING,
            onClick  = { onTabSelected(BottomNavTab.NEW_TRAINING) },
            icon     = { Icon(Icons.Default.Add, contentDescription = "Add") },
            label    = { Text("Add") },
            colors   = navItemColors()
        )
        NavigationBarItem(
            selected = selectedTab == BottomNavTab.PROFILE,
            onClick  = { onTabSelected(BottomNavTab.PROFILE) },
            icon     = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label    = { Text("Profile") },
            colors   = navItemColors()
        )
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor   = MahoganyRed,
    selectedTextColor   = MahoganyRed,
    indicatorColor      = MahoganyRed.copy(alpha = 0.15f),
    unselectedIconColor = Silver,
    unselectedTextColor = Silver
)
