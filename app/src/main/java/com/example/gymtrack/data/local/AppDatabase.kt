package com.example.gymtrack.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymtrack.data.local.dao.TrainingDao
import com.example.gymtrack.data.local.dao.UserDao
import com.example.gymtrack.data.local.entity.TrainingEntity
import com.example.gymtrack.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, TrainingEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun trainingDao(): TrainingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gymtrack_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}