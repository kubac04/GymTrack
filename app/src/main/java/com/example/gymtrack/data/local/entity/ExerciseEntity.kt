package com.example.gymtrack.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "kcal_per_rep") val kcalPerRep: Double,
    @ColumnInfo(name = "image_res_id") val imageResId: Int // Store R.drawable.image_name
)