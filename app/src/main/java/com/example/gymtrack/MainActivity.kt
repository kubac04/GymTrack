package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.gymtrack.ui.screens.ProfileScreen
import com.example.gymtrack.ui.screens.WelcomeScreen
import com.example.gymtrack.ui.theme.GymTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymTrackTheme {
                var currentScreen by remember { mutableStateOf("welcome") }
                when (currentScreen) {
                    "welcome" -> {
                        WelcomeScreen(onNextClick = {
                            currentScreen = "profile"
                        })
                    }
                    "profile" -> {
                        ProfileScreen(onNextClick = {
                            // Narazie pętla , potem to trzeba zmienić do innego obrazu ( Main Menu)
                            currentScreen = "welcome"
                        })
                    }
                }
            }
        }
    }
}