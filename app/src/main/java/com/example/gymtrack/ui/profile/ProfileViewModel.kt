package com.example.gymtrack.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.entity.UserEntity
import com.example.gymtrack.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Pobiera dane profilu z bazy
    val userProfile: StateFlow<UserEntity?> = userRepository
        .getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // true gdy baza już odpowiedziała (nawet jeśli jest pusta)
    // Dzięki temu null = "jeszcze ładuje" czy "naprawdę brak usera"
    val isProfileChecked: StateFlow<Boolean> = userRepository
        .getUserProfile()
        .map { true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Sygnał zapisano pomyślnie
    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess

    fun saveProfile(firstName: String, weight: String, height: String, photoUri: String? = null) {
        // Obsługa przecinka
        val normalizedWeight = weight.replace(',', '.')
        val weightFloat = normalizedWeight.toFloatOrNull() ?: 0f
        val heightInt   = height.toIntOrNull() ?: 0

        viewModelScope.launch {
            userRepository.saveUserProfile(
                UserEntity(
                    uid       = 1,
                    firstName = firstName,
                    weight    = weightFloat,
                    height    = heightInt,
                    profilePictureUri = photoUri ?: userProfile.value?.profilePictureUri
                )
            )
            _saveSuccess.emit(Unit) // powiadom UI że zapis się udał
        }
    }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
