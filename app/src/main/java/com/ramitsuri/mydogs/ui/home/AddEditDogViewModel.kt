package com.ramitsuri.mydogs.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramitsuri.mydogs.data.db.DogEntity
import com.ramitsuri.mydogs.data.model.SizeClass
import com.ramitsuri.mydogs.data.repository.DogRepository
import com.ramitsuri.mydogs.domain.DogAgeCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditDogUiState(
    val dogId: String? = null,
    val name: String = "",
    val birthdayTimestamp: Long = System.currentTimeMillis(),
    val breed: String = "",
    val fallbackSizeClass: SizeClass = SizeClass.LARGE,
    val availableBreeds: List<String> = DogAgeCalculator.defaultDataset.breeds.map { it.name },
    val isNameError: Boolean = false,
    val isSaving: Boolean = false
)

class AddEditDogViewModel(
    private val repository: DogRepository,
    dogToEdit: DogEntity? = null
) : ViewModel() {

    private val dataset = DogAgeCalculator.defaultDataset

    private val _uiState = MutableStateFlow(
        AddEditDogUiState(
            dogId = dogToEdit?.id,
            name = dogToEdit?.name ?: "",
            birthdayTimestamp = dogToEdit?.birthdayTimestamp ?: System.currentTimeMillis(),
            breed = dogToEdit?.breed ?: "",
            fallbackSizeClass = SizeClass.fromString(dogToEdit?.fallbackSizeClass),
            availableBreeds = dataset.breeds.map { it.name }.sortedBy { it }
        )
    )
    val uiState: StateFlow<AddEditDogUiState> = _uiState.asStateFlow()

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName, isNameError = false) }
    }

    fun onBirthdayChanged(newTimestamp: Long) {
        _uiState.update { it.copy(birthdayTimestamp = newTimestamp) }
    }

    fun onBreedChanged(newBreed: String) {
        _uiState.update { it.copy(breed = newBreed) }
        val matched = dataset.breeds.find { it.name.equals(newBreed.trim(), ignoreCase = true) }
        if (matched != null) {
            _uiState.update { it.copy(fallbackSizeClass = SizeClass.fromString(matched.sizeClass)) }
        }
    }

    fun onSizeClassChanged(newSizeClass: SizeClass) {
        _uiState.update { it.copy(fallbackSizeClass = newSizeClass) }
    }

    private fun reset() {
        _uiState.update {
            AddEditDogUiState(
                dogId = null,
                name = "",
                birthdayTimestamp = System.currentTimeMillis(),
                breed = "",
                fallbackSizeClass = SizeClass.LARGE,
                availableBreeds = dataset.breeds.map { it.name }.sortedBy { it },
                isNameError = false,
                isSaving = false
            )
        }
    }

    fun save(onSaved: () -> Unit) {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(isNameError = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            if (currentState.dogId.isNullOrBlank()) {
                val newDog = DogEntity(
                    name = currentState.name.trim(),
                    birthdayTimestamp = currentState.birthdayTimestamp,
                    breed = currentState.breed.takeIf { it.isNotBlank() },
                    fallbackSizeClass = currentState.fallbackSizeClass.jsonKey
                )
                repository.insert(newDog)
            } else {
                val updatedDog = DogEntity(
                    id = currentState.dogId,
                    name = currentState.name.trim(),
                    birthdayTimestamp = currentState.birthdayTimestamp,
                    breed = currentState.breed.takeIf { it.isNotBlank() },
                    fallbackSizeClass = currentState.fallbackSizeClass.jsonKey
                )
                repository.update(updatedDog)
            }
            _uiState.update { it.copy(isSaving = false) }
            reset()
            onSaved()
        }
    }
}
