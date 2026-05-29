package com.example.gymtrack

import android.app.Application
import com.example.gymtrack.data.local.AppDatabase
import com.example.gymtrack.data.repository.ExerciseRepository
import com.example.gymtrack.data.repository.UserRepository
import com.example.gymtrack.data.repository.WorkoutExerciseRepository
import com.example.gymtrack.data.repository.WorkoutRepository

class GymTrackApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }
    val workoutRepository: WorkoutRepository by lazy { WorkoutRepository(database.workoutDao()) }
    val exerciseRepository: ExerciseRepository by lazy { ExerciseRepository(database.exerciseDao()) }
    val workoutExerciseRepository: WorkoutExerciseRepository by lazy { WorkoutExerciseRepository(database.workoutExerciseDao()) }
}
