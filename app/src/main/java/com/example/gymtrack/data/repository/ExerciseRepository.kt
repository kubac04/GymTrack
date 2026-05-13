package com.example.gymtrack.data.repository

import com.example.gymtrack.data.local.dao.ExerciseDao
import com.example.gymtrack.data.local.entity.ExerciseEntity
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseDao: ExerciseDao) {
    fun getAllExercises(): Flow<List<ExerciseEntity>> = exerciseDao.getAllExercises()
    fun searchExercises(query: String): Flow<List<ExerciseEntity>> = exerciseDao.searchExercises(query)
    fun getExerciseById(id: Int): Flow<ExerciseEntity?> = exerciseDao.getExerciseById(id)
}
