package com.example.gymtrack.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "UserProfile")
data class User(
    @PrimaryKey val uid: Int = 1,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "height") val height: Int?,
    @ColumnInfo(name = "weight") val weight: Float?,
    )

@Entity(tableName = "Trainings")
data class Training(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "date") val date: Long?,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean? = false,
)