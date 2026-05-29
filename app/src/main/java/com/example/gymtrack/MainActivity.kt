package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.gymtrack.ui.exercise.ExerciseDetailScreen
import com.example.gymtrack.ui.exercise.ExerciseListScreen
import com.example.gymtrack.ui.exercise.ExerciseViewModel
import com.example.gymtrack.ui.history.HistoryScreen
import com.example.gymtrack.ui.history.HistoryViewModel
import com.example.gymtrack.ui.profile.ProfileEditScreen
import com.example.gymtrack.ui.profile.ProfileScreen
import com.example.gymtrack.ui.profile.ProfileLoadState
import com.example.gymtrack.ui.profile.ProfileViewModel
import com.example.gymtrack.ui.theme.GymTrackTheme
import com.example.gymtrack.ui.welcome.WelcomeScreen
import com.example.gymtrack.ui.workout.WorkoutDetailScreen
import com.example.gymtrack.ui.workout.WorkoutDetailViewModel
import com.example.gymtrack.ui.workout.WorkoutEditScreen
import com.example.gymtrack.ui.workout.WorkoutSummaryScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as GymTrackApplication

        val profileViewModel  = ViewModelProvider(this, ProfileViewModel.Factory(app.userRepository))[ProfileViewModel::class.java]
        val historyViewModel  = ViewModelProvider(this, HistoryViewModel.Factory(app.workoutRepository))[HistoryViewModel::class.java]
        val exerciseViewModel = ViewModelProvider(this, ExerciseViewModel.Factory(app.exerciseRepository))[ExerciseViewModel::class.java]

        setContent {
            GymTrackTheme {
                val navController    = rememberNavController()
                val profileLoadState by profileViewModel.profileLoadState.collectAsStateWithLifecycle()

                // Pojedynczy sealed state — brak race condition
                LaunchedEffect(profileLoadState) {
                    when (profileLoadState) {
                        is ProfileLoadState.HasProfile -> navController.navigate("history") {
                            popUpTo(0) { inclusive = true }
                        }
                        is ProfileLoadState.NoProfile -> navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                        ProfileLoadState.Loading -> { /* czekamy */ }
                    }
                }

                NavHost(navController = navController, startDestination = "loading") {

                    composable("loading") { /* splash / pusty ekran podczas sprawdzania profilu */ }

                    composable("welcome") {
                        WelcomeScreen(onNextClick = { navController.navigate("profile_setup") })
                    }

                    composable("profile_setup") {
                        ProfileScreen(
                            viewModel      = profileViewModel,
                            onNextClick = { navController.navigate("history") }
                        )
                    }

                    composable("history") {
                        HistoryScreen(
                            viewModel      = historyViewModel,
                            onProfileClick = { navController.navigate("profile_edit") },
                            onWorkoutClick = { workoutId -> navController.navigate("workout_detail/$workoutId") }
                        )
                    }

                    composable("profile_edit") {
                        ProfileEditScreen(
                            viewModel      = profileViewModel,
                            onHistoryClick = { navController.popBackStack() },
                            onAddClick     = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route     = "workout_detail/{workoutId}",
                        arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
                        val viewModel = remember(workoutId) {
                            WorkoutDetailViewModel(workoutId, app.workoutRepository, app.workoutExerciseRepository)
                        }
                        WorkoutDetailScreen(
                            viewModel     = viewModel,
                            onBack        = { navController.popBackStack() },
                            onEdit        = { navController.navigate("workout_edit/$workoutId") },
                            onAddExercise = { navController.navigate("exercise_list/$workoutId/false") },
                            onComplete    = { calories -> navController.navigate("workout_summary/$calories") }
                        )
                    }

                    composable(
                        route     = "workout_edit/{workoutId}",
                        arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
                        val viewModel = remember(workoutId) {
                            WorkoutDetailViewModel(workoutId, app.workoutRepository, app.workoutExerciseRepository)
                        }
                        WorkoutEditScreen(
                            viewModel     = viewModel,
                            onBack        = { navController.popBackStack() },
                            onAddExercise = { navController.navigate("exercise_list/$workoutId/true") }
                        )
                    }

                    composable(
                        route     = "exercise_list/{workoutId}/{fromEdit}",
                        arguments = listOf(
                            navArgument("workoutId") { type = NavType.IntType },
                            navArgument("fromEdit")  { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getInt("workoutId") ?: 0
                        val fromEdit  = backStackEntry.arguments?.getBoolean("fromEdit") ?: false
                        ExerciseListScreen(
                            viewModel       = exerciseViewModel,
                            onBack          = { navController.popBackStack() },
                            onExerciseClick = { exerciseId ->
                                navController.navigate("exercise_detail/$workoutId/$exerciseId/$fromEdit")
                            }
                        )
                    }

                    composable(
                        route     = "exercise_detail/{workoutId}/{exerciseId}/{fromEdit}",
                        arguments = listOf(
                            navArgument("workoutId")  { type = NavType.IntType },
                            navArgument("exerciseId") { type = NavType.IntType },
                            navArgument("fromEdit")   { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        val workoutId  = backStackEntry.arguments?.getInt("workoutId") ?: 0
                        val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: 0
                        val fromEdit   = backStackEntry.arguments?.getBoolean("fromEdit") ?: false

                        val workoutViewModel = remember(workoutId) {
                            WorkoutDetailViewModel(workoutId, app.workoutRepository, app.workoutExerciseRepository)
                        }
                        ExerciseDetailScreen(
                            exerciseId    = exerciseId,
                            viewModel     = exerciseViewModel,
                            onBack        = { navController.popBackStack() },
                            onAddToWorkout = { exId ->
                                workoutViewModel.addExercise(exId)
                                val target = if (fromEdit) "workout_edit/$workoutId" else "workout_detail/$workoutId"
                                navController.popBackStack(target, inclusive = false)
                            }
                        )
                    }

                    composable(
                        route     = "workout_summary/{calories}",
                        arguments = listOf(navArgument("calories") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val calories = backStackEntry.arguments?.getInt("calories") ?: 0
                        WorkoutSummaryScreen(
                            caloriesBurned = calories,
                            onContinue     = {
                                navController.navigate("history") {
                                    popUpTo("history") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
