package com.example.gymtrack.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDao {

    @Insert
    suspend fun insertTraining(training: Training)

    @Query("SELECT * FROM Trainings ORDER BY date DESC")
    fun getAllTrainings(): Flow<List<Training>>

}