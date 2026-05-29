package com.example.gymtrack.data.repository

import com.example.gymtrack.data.local.dao.WorkoutExerciseDao
import com.example.gymtrack.data.local.entity.WorkoutExerciseEntity
import com.example.gymtrack.data.local.entity.WorkoutExerciseItem
import kotlinx.coroutines.flow.Flow

class WorkoutExerciseRepository(private val dao: WorkoutExerciseDao) {
    fun getExercisesForWorkout(workoutId: Int): Flow<List<WorkoutExerciseItem>> =
        dao.getExercisesForWorkout(workoutId)

    suspend fun addExercise(entity: WorkoutExerciseEntity) = dao.insert(entity)
    suspend fun updateExercise(entity: WorkoutExerciseEntity) = dao.update(entity)
    suspend fun removeExercise(entity: WorkoutExerciseEntity) = dao.delete(entity)
    suspend fun getById(id: Int): WorkoutExerciseEntity? = dao.getById(id)
}
