package com.example.gymtrack.util

import com.example.gymtrack.data.local.entity.WorkoutExerciseItem

object CalorieCalculator {
    fun calculate(exercises: List<WorkoutExerciseItem>): Int =
        exercises.sumOf { item ->
            val weightFactor = 1.0 + item.weightKg * 0.005
            item.sets * item.reps * item.kcalPerRep * weightFactor
        }.toInt().coerceAtLeast(0)
}
