package com.example.gymtrack

import android.app.Application
import com.example.gymtrack.data.local.AppDatabase
import com.example.gymtrack.data.repository.UserRepository
import com.example.gymtrack.data.repository.WorkoutRepository
// Definicja Bazy Danych i repozytoriów ( nw do końca po co ten plik ale musi być tak jak cloude ci napisał wtedy xd)
class GymTrackApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val userRepository: UserRepository by lazy { UserRepository(database.userDao()) }
    val workoutRepository: WorkoutRepository by lazy { WorkoutRepository(database.workoutDao()) }
}
