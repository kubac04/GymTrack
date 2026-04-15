package com.example.gymtrack.data.local
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(user: User)

    @Query("SELECT * FROM UserProfile WHERE uid = 1")
    fun getUserProfile(): Flow<User?>
}
