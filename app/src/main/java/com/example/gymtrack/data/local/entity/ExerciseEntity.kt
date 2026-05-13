package com.example.gymtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val steps: String,
    val muscleGroup: String,
    val kcalPerRep: Double = 0.5,
    val videoUrl: String = ""
)
