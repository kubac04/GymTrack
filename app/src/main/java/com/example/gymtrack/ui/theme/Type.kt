package com.example.gymtrack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.example.gymtrack.R
import androidx.compose.ui.text.font.Font

// Set of Material typography styles to start with
val Rubik = FontFamily(
    Font(R.font.rubik_light, FontWeight.Light),
    Font(R.font.rubik_light_italic, FontWeight.Light, FontStyle.Italic),

    Font(R.font.rubik_regular, FontWeight.Normal),
    Font(R.font.rubik_italic, FontWeight.Normal, FontStyle.Italic),

    Font(R.font.rubik_medium, FontWeight.Medium),
    Font(R.font.rubik_medium_italic, FontWeight.Medium, FontStyle.Italic),

    Font(R.font.rubik_semi_bold, FontWeight.SemiBold),
    Font(R.font.rubik_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),

    Font(R.font.rubik_bold, FontWeight.Bold),
    Font(R.font.rubik_bold_italic, FontWeight.Bold, FontStyle.Italic),

    Font(R.font.rubik_extra_bold, FontWeight.ExtraBold),
    Font(R.font.rubik_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),

    Font(R.font.rubik_black, FontWeight.Black),
    Font(R.font.rubik_black_italic, FontWeight.Black, FontStyle.Italic),
)

val RubikMonoOne = FontFamily(
    Font(R.font.rubik_mono_one_regular, FontWeight.Normal)
)

val Typography = Typography(
    // NAJWIĘKSZE NAGŁÓWKI
    headlineLarge = TextStyle(
        fontFamily = RubikMonoOne,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),

    // Sekcje / podtytuły (np. "History", "Profile")
    headlineMedium = TextStyle(
        fontFamily = RubikMonoOne,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),

    // Tytuły kart / elementów
    titleLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),

    // Tekst przycisków
    labelLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Główny tekst (opisy, dane użytkownika)
    bodyLarge = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    // Mniejszy tekst (np. szczegóły ćwiczeń)
    bodyMedium = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Małe etykiety (np. reps, weight, serie)
    labelSmall = TextStyle(
        fontFamily = Rubik,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    )
)