package com.example.gymtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gymtrack.data.local.entity.WorkoutExerciseEntity
import com.example.gymtrack.data.local.entity.WorkoutExerciseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {
    @Insert
    suspend fun insert(entity: WorkoutExerciseEntity)

    @Update
    suspend fun update(entity: WorkoutExerciseEntity)

    @Delete
    suspend fun delete(entity: WorkoutExerciseEntity)

    @Query("""
        SELECT
            we.id          AS workoutExerciseId,
            we.exerciseId,
            e.name         AS exerciseName,
            e.description  AS exerciseDescription,
            e.muscleGroup  AS muscleGroup,
            we.sets,
            we.reps,
            we.weightKg,
            we.isCompleted,
            e.kcalPerRep
        FROM WorkoutExercises we
        INNER JOIN Exercises e ON we.exerciseId = e.id
        WHERE we.workoutId = :workoutId
    """)
    fun getExercisesForWorkout(workoutId: Int): Flow<List<WorkoutExerciseItem>>

    @Query("SELECT * FROM WorkoutExercises WHERE id = :id")
    suspend fun getById(id: Int): WorkoutExerciseEntity?
}
