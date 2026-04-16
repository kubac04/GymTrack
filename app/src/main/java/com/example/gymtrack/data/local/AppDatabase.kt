package com.example.gymtrack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymtrack.data.local.dao.*
import com.example.gymtrack.data.local.entity.*

// Pamiętaj o dodaniu ExerciseEntity i WorkoutExerciseEntity gdy je stworzysz
@Database(
    entities = [UserEntity::class, WorkoutEntity::class], // Docelowo dodaj tu resztę encji
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    // abstract fun exerciseDao(): ExerciseDao
    // abstract fun workoutExerciseDao(): WorkoutExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gymtrack_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}