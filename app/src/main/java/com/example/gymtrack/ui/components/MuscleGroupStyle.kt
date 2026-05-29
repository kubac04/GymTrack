package com.example.gymtrack.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class MuscleGroupStyle(
    val color: Color,
    val abbr: String,
    val icon: ImageVector
)

fun muscleGroupStyle(group: String): MuscleGroupStyle = when (group.lowercase()) {
    "chest"     -> MuscleGroupStyle(Color(0xFF1565C0), "CH", Icons.Default.Accessibility)
    "legs"      -> MuscleGroupStyle(Color(0xFF2E7D32), "LG", Icons.Default.DirectionsRun)
    "back"      -> MuscleGroupStyle(Color(0xFF6A1B9A), "BK", Icons.Default.AccessibilityNew)
    "arms"      -> MuscleGroupStyle(Color(0xFFBF360C), "AR", Icons.Default.SelfImprovement)
    "shoulders" -> MuscleGroupStyle(Color(0xFFE65100), "SH", Icons.Default.EmojiPeople)
    else        -> MuscleGroupStyle(Color(0xFF37474F), "EX", Icons.Default.Person)
}
