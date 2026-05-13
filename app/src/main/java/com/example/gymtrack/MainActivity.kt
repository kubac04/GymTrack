package com.example.gymtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymtrack.ui.exercise.ExerciseDetailScreen
import com.example.gymtrack.ui.exercise.ExerciseListScreen
import com.example.gymtrack.ui.exercise.ExerciseViewModel
import com.example.gymtrack.ui.history.HistoryScreen
import com.example.gymtrack.ui.history.HistoryViewModel
import com.example.gymtrack.ui.profile.ProfileEditScreen
import com.example.gymtrack.ui.profile.ProfileScreen
import com.example.gymtrack.ui.profile.ProfileViewModel
import com.example.gymtrack.ui.theme.GymTrackTheme
import com.example.gymtrack.ui.welcome.WelcomeScreen
import com.example.gymtrack.ui.workout.WorkoutDetailScreen
import com.example.gymtrack.ui.workout.WorkoutDetailViewModel
import com.example.gymtrack.ui.workout.WorkoutEditScreen
import com.example.gymtrack.ui.workout.WorkoutSummaryScreen

class MainActivity : ComponentActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as GymTrackApplication

        profileViewModel = ViewModelProvider(
            this, ProfileViewModel.Factory(app.userRepository)
        )[ProfileViewModel::class.java]

        historyViewModel = ViewModelProvider(
            this, HistoryViewModel.Factory(app.workoutRepository)
        )[HistoryViewModel::class.java]

        exerciseViewModel = ViewModelProvider(
            this, ExerciseViewModel.Factory(app.exerciseRepository)
        )[ExerciseViewModel::class.java]

        setContent {
            GymTrackTheme {
                val isProfileChecked by profileViewModel.isProfileChecked.collectAsStateWithLifecycle()
                val userProfile by profileViewModel.userProfile.collectAsStateWithLifecycle()

                var currentScreen by remember { mutableStateOf<String?>(null) }
                var currentWorkoutId by remember { mutableStateOf(0) }
                var summaryCalories by remember { mutableStateOf(0) }

                val workoutDetailViewModel = remember(currentWorkoutId) {
                    if (currentWorkoutId > 0) {
                        WorkoutDetailViewModel(
                            workoutId = currentWorkoutId,
                            workoutRepository = app.workoutRepository,
                            workoutExerciseRepository = app.workoutExerciseRepository
                        )
                    } else null
                }

                LaunchedEffect(isProfileChecked) {
                    if (isProfileChecked && currentScreen == null) {
                        currentScreen = if (userProfile != null) "history" else "welcome"
                    }
                }

                when {
                    currentScreen == "welcome" -> WelcomeScreen(
                        onNextClick = { currentScreen = "profile" }
                    )
                    currentScreen == "profile" -> ProfileScreen(
                        viewModel = profileViewModel,
                        onNextClick = { currentScreen = "history" }
                    )
                    currentScreen == "history" -> HistoryScreen(
                        viewModel = historyViewModel,
                        onProfileClick = { currentScreen = "profile_edit" },
                        onWorkoutClick = { workoutId ->
                            currentWorkoutId = workoutId
                            currentScreen = "workout_detail"
                        }
                    )
                    currentScreen == "profile_edit" -> ProfileEditScreen(
                        viewModel = profileViewModel,
                        onHistoryClick = { currentScreen = "history" },
                        onAddClick = { currentScreen = "history" }
                    )
                    currentScreen == "workout_detail" && workoutDetailViewModel != null -> {
                        WorkoutDetailScreen(
                            viewModel = workoutDetailViewModel,
                            onBack = { currentScreen = "history" },
                            onEdit = { currentScreen = "workout_edit" },
                            onAddExercise = { currentScreen = "exercise_list_detail" },
                            onComplete = { calories ->
                                summaryCalories = calories
                                currentScreen = "workout_summary"
                            }
                        )
                    }
                    currentScreen == "workout_edit" && workoutDetailViewModel != null -> {
                        WorkoutEditScreen(
                            viewModel = workoutDetailViewModel,
                            onBack = { currentScreen = "workout_detail" },
                            onAddExercise = { currentScreen = "exercise_list" }
                        )
                    }
                    // Exercise list from WorkoutEditScreen
                    currentScreen == "exercise_list" -> {
                        ExerciseListScreen(
                            viewModel = exerciseViewModel,
                            onBack = { currentScreen = "workout_edit" },
                            onExerciseClick = { exerciseId ->
                                currentScreen = "exercise_detail_from_edit_$exerciseId"
                            }
                        )
                    }
                    // Exercise list from WorkoutDetailScreen (add directly)
                    currentScreen == "exercise_list_detail" -> {
                        ExerciseListScreen(
                            viewModel = exerciseViewModel,
                            onBack = { currentScreen = "workout_detail" },
                            onExerciseClick = { exerciseId ->
                                currentScreen = "exercise_detail_from_detail_$exerciseId"
                            }
                        )
                    }
                    currentScreen?.startsWith("exercise_detail_from_edit_") == true -> {
                        val exerciseId = currentScreen!!
                            .removePrefix("exercise_detail_from_edit_")
                            .toIntOrNull() ?: 0
                        ExerciseDetailScreen(
                            exerciseId = exerciseId,
                            viewModel = exerciseViewModel,
                            onBack = { currentScreen = "exercise_list" },
                            onAddToWorkout = { exId ->
                                workoutDetailViewModel?.addExercise(exId)
                                currentScreen = "workout_edit"
                            }
                        )
                    }
                    currentScreen?.startsWith("exercise_detail_from_detail_") == true -> {
                        val exerciseId = currentScreen!!
                            .removePrefix("exercise_detail_from_detail_")
                            .toIntOrNull() ?: 0
                        ExerciseDetailScreen(
                            exerciseId = exerciseId,
                            viewModel = exerciseViewModel,
                            onBack = { currentScreen = "exercise_list_detail" },
                            onAddToWorkout = { exId ->
                                workoutDetailViewModel?.addExercise(exId)
                                currentScreen = "workout_detail"
                            }
                        )
                    }
                    currentScreen == "workout_summary" -> {
                        WorkoutSummaryScreen(
                            caloriesBurned = summaryCalories,
                            onContinue = { currentScreen = "history" }
                        )
                    }
                }
            }
        }
    }
}
