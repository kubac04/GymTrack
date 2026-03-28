package com.example.gymtrack.navigation

sealed class Screen(val route: String) {
    object History : Screen("history")
    object NewTraining : Screen("new_training")
    object Profile : Screen("profile")
    object Welcome : Screen("welcome")
}