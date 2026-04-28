package com.example.gymtrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymtrack.ui.theme.CarbonBlack
import com.example.gymtrack.ui.theme.MahoganyRed
import com.example.gymtrack.ui.theme.Silver

enum class BottomNavTab { HISTORY, NEW_TRAINING, PROFILE }

private data class NavItemData(
    val tab: BottomNavTab,
    val label: String,
    val icon: ImageVector
)

private val navItems = listOf(
    NavItemData(BottomNavTab.HISTORY,      "History", Icons.Default.DateRange),
    NavItemData(BottomNavTab.NEW_TRAINING, "Add",     Icons.Default.Add),
    NavItemData(BottomNavTab.PROFILE,      "Profile", Icons.Default.AccountCircle)
)

@Composable
fun BottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    NavigationBar(
        containerColor = CarbonBlack
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                selected  = selectedTab == item.tab,
                onClick   = { onTabSelected(item.tab) },
                icon      = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label     = { Text(text = item.label) },
                colors    = NavigationBarItemDefaults.colors(
                    selectedIconColor   = MahoganyRed,
                    selectedTextColor   = MahoganyRed,
                    unselectedIconColor = Silver,
                    unselectedTextColor = Silver,
                    indicatorColor      = CarbonBlack
                )
            )
        }
    }
}