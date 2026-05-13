package com.example.gymtrack.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.entity.WorkoutEntity
import com.example.gymtrack.data.local.entity.WorkoutExerciseEntity
import com.example.gymtrack.data.local.entity.WorkoutExerciseItem
import com.example.gymtrack.data.repository.WorkoutExerciseRepository
import com.example.gymtrack.data.repository.WorkoutRepository
import com.example.gymtrack.util.CalorieCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkoutDetailViewModel(
    private val workoutId: Int,
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) : ViewModel() {

    val workout: StateFlow<WorkoutEntity?> = workoutRepository
        .getWorkoutById(workoutId)
        .catch { emit(null) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val exercises: StateFlow<List<WorkoutExerciseItem>> = workoutExerciseRepository
        .getExercisesForWorkout(workoutId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleExercise(item: WorkoutExerciseItem) {
        viewModelScope.launch {
            val entity = workoutExerciseRepository.getById(item.workoutExerciseId) ?: return@launch
            workoutExerciseRepository.updateExercise(entity.copy(isCompleted = !item.isCompleted))
        }
    }

    fun addExercise(exerciseId: Int, sets: Int = 3, reps: Int = 10, weightKg: Float = 0f) {
        viewModelScope.launch {
            workoutExerciseRepository.addExercise(
                WorkoutExerciseEntity(
                    workoutId = workoutId,
                    exerciseId = exerciseId,
                    sets = sets,
                    reps = reps,
                    weightKg = weightKg
                )
            )
        }
    }

    fun removeExercise(item: WorkoutExerciseItem) {
        viewModelScope.launch {
            val entity = workoutExerciseRepository.getById(item.workoutExerciseId) ?: return@launch
            workoutExerciseRepository.removeExercise(entity)
        }
    }

    fun updateExercise(item: WorkoutExerciseItem, sets: Int, reps: Int, weightKg: Float) {
        viewModelScope.launch {
            val entity = workoutExerciseRepository.getById(item.workoutExerciseId) ?: return@launch
            workoutExerciseRepository.updateExercise(entity.copy(sets = sets, reps = reps, weightKg = weightKg))
        }
    }

    fun completeWorkout(): Int {
        val calories = CalorieCalculator.calculate(exercises.value)
        viewModelScope.launch {
            workout.value?.let { w ->
                workoutRepository.updateWorkout(w.copy(isCompleted = true))
            }
        }
        return calories
    }
}
