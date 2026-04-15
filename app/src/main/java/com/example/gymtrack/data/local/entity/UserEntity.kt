package com.example.gymtrack.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "UserProfile")
data class UserEntity(
    @PrimaryKey val uid: Int = 1,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "profile_picture_uri") val profilePictureUri: String? = null,
)
