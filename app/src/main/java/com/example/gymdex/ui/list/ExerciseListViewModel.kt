package com.example.gymdex.ui.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymdex.model.Equipment
import com.example.gymdex.model.Exercise
import com.example.gymdex.model.MuscleGroup
import com.example.gymdex.network.EquipmentResponse
import com.example.gymdex.persistence.MuscleGroupDao
import com.example.gymdex.ui.main.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val repository: ExerciseListRepository,
) : ViewModel() {

    private val _exercises = mutableStateOf<List<Exercise>>(emptyList())
    val exercises: State<List<Exercise>> get() = _exercises

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _muscleGroupId = mutableStateOf<Int?>(null)
    private val _equipmentIds = mutableStateOf<Set<Int>?>(null)
    private val _isFavorite = mutableStateOf<Boolean?>(null)
    private val _noEquipmentNeeded = mutableStateOf(false)

    private fun loadExercises() {
        repository.loadExercises(
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false },
            onError = { "Failed to load exercises" }
        ).onEach { exercises ->
            _exercises.value = exercises
        }.launchIn(viewModelScope)
    }

    fun filterExercises(
        muscleGroupId: Int,
        equipmentIds: Set<Int>? = null,
        isFavorite: Boolean? = null,
        noEquipmentNeeded: Boolean = false
    ) {
        _muscleGroupId.value = muscleGroupId
        _equipmentIds.value = equipmentIds
        _isFavorite.value = isFavorite
        _noEquipmentNeeded.value = noEquipmentNeeded

        repository.filterExercises(
            muscleGroup = muscleGroupId,
            equipmentIds = equipmentIds,
            isFavorite = isFavorite,
            noEquipmentNeeded = noEquipmentNeeded,
            onError = { /* Handle error */ }
        ).onEach { exercises ->
            _exercises.value = exercises
        }.launchIn(viewModelScope)
    }
    fun updateLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}