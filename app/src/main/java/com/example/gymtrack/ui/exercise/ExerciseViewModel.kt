package com.example.gymtrack.ui.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.entity.ExerciseEntity
import com.example.gymtrack.data.repository.ExerciseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class ExerciseViewModel(private val exerciseRepository: ExerciseRepository) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val exercises: StateFlow<List<ExerciseEntity>> = searchQuery
        .debounce(200)
        .flatMapLatest { query ->
            if (query.isBlank()) exerciseRepository.getAllExercises()
            else exerciseRepository.searchExercises(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(q: String) { searchQuery.value = q }

    fun getExerciseById(id: Int): Flow<ExerciseEntity?> = exerciseRepository.getExerciseById(id)

    class Factory(private val repository: ExerciseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(repository) as T
        }
    }
}
