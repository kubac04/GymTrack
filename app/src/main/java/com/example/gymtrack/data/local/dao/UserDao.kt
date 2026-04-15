package com.example.gymtrack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymtrack.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertProfile(user: UserEntity)

    @Query("SELECT * FROM UserProfile WHERE uid = 1")
    fun getUserProfile(): Flow<UserEntity?>
}