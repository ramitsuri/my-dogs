package com.ramitsuri.mydogs.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramitsuri.mydogs.data.db.DogEntity
import com.ramitsuri.mydogs.data.model.SizeClass
import com.ramitsuri.mydogs.data.repository.DogRepository
import com.ramitsuri.mydogs.domain.DogAgeCalculator
import com.ramitsuri.mydogs.domain.DogAgeResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.ramitsuri.mydogs.domain.formatAge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.max

data class DogUiModel(
    val entity: DogEntity,
    val ageResult: DogAgeResult,
    val formattedBirthday: String,
    val formattedDogAge: String,
    val formattedMythAge: String,
    val formattedBreedAdjustedAge: String
)

data class HomeUiState(
    val dogs: List<DogUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val selectedDogForDetails: DogUiModel? = null,
    val isAddEditDialogVisible: Boolean = false,
    val dogToEdit: DogEntity? = null
)

class HomeViewModel(
    private val repository: DogRepository,
    private val calculator: DogAgeCalculator = DogAgeCalculator()
) : ViewModel() {

    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val _isAddEditDialogVisible = MutableStateFlow(false)
    private val _dogToEdit = MutableStateFlow<DogEntity?>(null)
    private val _selectedDogForDetails = MutableStateFlow<DogUiModel?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        repository.allDogs,
        _isAddEditDialogVisible,
        _dogToEdit,
        _selectedDogForDetails
    ) { dogs, isAddEditVisible, dogToEdit, selectedDogForDetails ->
        val now = System.currentTimeMillis()
        val dogUiModels = dogs.map { dog ->
            val diffMillis = now - dog.birthdayTimestamp
            val diffDays = diffMillis / (1000.0 * 60 * 60 * 24)
            val dogAgeYears = max(0.0, diffDays / 365.25)
            val sizeClass = SizeClass.fromString(dog.fallbackSizeClass)
            val ageResult = calculator.calculateDogAge(
                dogAgeYears = dogAgeYears,
                breedName = dog.breed,
                fallbackSizeClass = sizeClass
            )

            val formattedBirthday = dateFormatter.format(Date(dog.birthdayTimestamp))
            val formattedDogAge = formatAge(dogAgeYears)

            val formattedMythAge = "${formatAge(ageResult.mythAge)} human years (7x myth)"
            val formattedBreedAdjustedAge = "${formatAge(ageResult.breedAdjustedHumanAge)} human years"

            DogUiModel(
                entity = dog,
                ageResult = ageResult,
                formattedBirthday = formattedBirthday,
                formattedDogAge = formattedDogAge,
                formattedMythAge = formattedMythAge,
                formattedBreedAdjustedAge = formattedBreedAdjustedAge
            )
        }

        HomeUiState(
            dogs = dogUiModels,
            isLoading = false,
            selectedDogForDetails = selectedDogForDetails,
            isAddEditDialogVisible = isAddEditVisible,
            dogToEdit = dogToEdit
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun showAddDogDialog() {
        _dogToEdit.value = null
        _isAddEditDialogVisible.value = true
    }

    fun showEditDogDialog(dog: DogEntity) {
        _dogToEdit.value = dog
        _isAddEditDialogVisible.value = true
    }

    fun hideAddEditDialog() {
        _isAddEditDialogVisible.value = false
        _dogToEdit.value = null
    }

    fun showDogDetails(dog: DogUiModel?) {
        _selectedDogForDetails.value = dog
    }

    fun deleteDog(dog: DogEntity) {
        viewModelScope.launch {
            repository.delete(dog)
            if (_selectedDogForDetails.value?.entity?.id == dog.id) {
                _selectedDogForDetails.value = null
            }
        }
    }
}
