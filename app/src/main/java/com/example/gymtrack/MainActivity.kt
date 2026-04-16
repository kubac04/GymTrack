package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.ui.history.HistoryScreen
import com.example.gymtrack.ui.history.HistoryViewModel
import com.example.gymtrack.ui.profile.ProfileScreen
import com.example.gymtrack.ui.profile.ProfileEditScreen
import com.example.gymtrack.ui.profile.ProfileViewModel
import com.example.gymtrack.ui.welcome.WelcomeScreen
import com.example.gymtrack.ui.theme.GymTrackTheme

class MainActivity : ComponentActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as GymTrackApplication

        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModel.Factory(app.userRepository)
        )[ProfileViewModel::class.java]

        historyViewModel = ViewModelProvider(
            this,
            HistoryViewModel.Factory(app.workoutRepository)
        )[HistoryViewModel::class.java]

        setContent {
            GymTrackTheme {
                val isProfileChecked by profileViewModel.isProfileChecked.collectAsStateWithLifecycle()
                val userProfile      by profileViewModel.userProfile.collectAsStateWithLifecycle()

                var currentScreen by remember { mutableStateOf<String?>(null) }

                // Czekamy aż baza odpowie, wtedy decydujemy gdzie iść ( żeby nie było że ekran powitalny ładuje sie przed baza , czyli zawsze
                LaunchedEffect(isProfileChecked) {
                    if (isProfileChecked && currentScreen == null) {
                        currentScreen = if (userProfile != null) "history" else "welcome"
                    }
                }

                when (currentScreen) {
                    "welcome" -> WelcomeScreen(
                        onNextClick = { currentScreen = "profile" }
                    )
                    "profile" -> ProfileScreen(
                        viewModel   = profileViewModel,
                        onNextClick = { currentScreen = "history" }
                    )
                    "history" -> HistoryScreen(
                        viewModel      = historyViewModel,
                        onProfileClick = { currentScreen = "profile_edit" }
                    )
                    "profile_edit" -> ProfileEditScreen(
                        viewModel      = profileViewModel,
                        onHistoryClick = { currentScreen = "history" },
                        onAddClick     = { currentScreen = "history" }
                    )

                }
            }
        }
    }
}
