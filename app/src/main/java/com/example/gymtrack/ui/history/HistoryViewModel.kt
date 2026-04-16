package com.example.gymtrack.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.entity.WorkoutEntity
import com.example.gymtrack.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

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

    class Factory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HistoryViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
