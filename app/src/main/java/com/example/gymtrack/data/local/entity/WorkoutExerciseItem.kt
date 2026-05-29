package com.example.gymtrack.data.local.entity

data class WorkoutExerciseItem(
    val workoutExerciseId: Int,
    val exerciseId: Int,
    val exerciseName: String,
    val exerciseDescription: String,
    val muscleGroup: String,
    val sets: Int,
    val reps: Int,
    val weightKg: Float,
    val isCompleted: Boolean,
    val kcalPerRep: Double
)
