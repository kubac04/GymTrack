package com.example.gymtrack.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gymtrack.data.local.entity.UserEntity
import com.example.gymtrack.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class ProfileLoadState {
    object Loading : ProfileLoadState()
    object NoProfile : ProfileLoadState()
    data class HasProfile(val user: UserEntity) : ProfileLoadState()
}

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Jeden flow zamiast dwóch osobnych — eliminuje race condition
    val profileLoadState: StateFlow<ProfileLoadState> = userRepository
        .getUserProfile()
        .map { user ->
            if (user != null) ProfileLoadState.HasProfile(user)
            else ProfileLoadState.NoProfile
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfileLoadState.Loading)

    // Skrót dla ekranów które potrzebują tylko danych użytkownika
    val userProfile: StateFlow<UserEntity?> = userRepository
        .getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Zachowane dla kompatybilności wstecznej
    val isProfileChecked: StateFlow<Boolean> = profileLoadState
        .map { it !is ProfileLoadState.Loading }
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
