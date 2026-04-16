package com.example.gymtrack.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.entity.WorkoutEntity
import com.example.gymtrack.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
// Ekran dodawania treningów , działa jak narazie
class WorkoutViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

    val workouts: StateFlow<List<WorkoutEntity>> = workoutRepository
        .getAllWorkouts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addWorkout(title: String) {
        viewModelScope.launch {
            workoutRepository.insertWorkout(
                WorkoutEntity(
                    title = title,
                    date = System.currentTimeMillis(),
                    isCompleted = false
                )
            )
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workout)
        }
    }

    fun markCompleted(workout: WorkoutEntity) {
        viewModelScope.launch {
            workoutRepository.updateWorkout(workout.copy(isCompleted = true))
        }
    }

    class Factory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WorkoutViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
