package com.example.gymdex.ui.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymdex.model.Equipment
import com.example.gymdex.model.MuscleGroup
import com.example.gymdex.ui.list.ExerciseListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val repository: FilterRepository
) : ViewModel() {
    private val _equipments = mutableStateOf<List<Equipment>>(emptyList())
    val equipments: State<List<Equipment>> get() = _equipments

    private val _muscleGroups = mutableStateOf<List<MuscleGroup>>(emptyList())
    val muscleGroups: State<List<MuscleGroup>> get() = _muscleGroups

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading


    private val _showFavoritesOnly = mutableStateOf(false)
    val showFavoritesOnly: State<Boolean> get() = _showFavoritesOnly

    private val _selectedMuscleGroupId = mutableStateOf<Int?>(null)
    val selectedMuscleGroupId: State<Int?> get() = _selectedMuscleGroupId

    private val _selectedEquipmentIds = mutableStateOf<Set<Int>>(emptySet())
    val selectedEquipmentIds: State<Set<Int>> get() = _selectedEquipmentIds

    private val _noEquipmentNeeded = mutableStateOf(false)
    val noEquipmentNeeded: State<Boolean> get() = _noEquipmentNeeded

    init {
        loadEquipments()
        loadMuscleGroups()
    }

    private fun loadEquipments() {
        repository.loadEquipments(
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false },
            onError = { "Failed to load equipments" }
        ).onEach { equipments ->
            _equipments.value = equipments
        }.launchIn(viewModelScope)
    }

    private fun loadMuscleGroups() {
        repository.loadMuscleGroups(
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false },
            onError = { /* Log error */ }
        ).onEach { muscleGroups ->
            _muscleGroups.value = muscleGroups
            if (muscleGroups.isNotEmpty() && _selectedMuscleGroupId.value == null) {
                _selectedMuscleGroupId.value = muscleGroups.first().id
            }
        }.launchIn(viewModelScope)
    }

    fun updateFavoritesOnly(showFavorites: Boolean) {
        _showFavoritesOnly.value = showFavorites
    }

    fun updateMuscleGroupId(muscleGroupId: Int) {
        _selectedMuscleGroupId.value = muscleGroupId
    }

    fun updateEquipmentIds(equipmentId: Int, isSelected: Boolean) {
        _selectedEquipmentIds.value = if (isSelected) {
            _selectedEquipmentIds.value + equipmentId
        } else {
            _selectedEquipmentIds.value - equipmentId
        }
        if (isSelected) {
            _noEquipmentNeeded.value = false
        }
    }

    fun updateNoEquipmentNeeded(isNeeded: Boolean) {
        _noEquipmentNeeded.value = isNeeded
        if (isNeeded) {
            _selectedEquipmentIds.value = emptySet()
        }
    }

    fun applyFilters(): String? {
        return _selectedMuscleGroupId.value?.let { muscleGroupId ->
            val equipmentString = if (_noEquipmentNeeded.value) "" else _selectedEquipmentIds.value.joinToString(",")
            val favorites = _showFavoritesOnly.value
            val noEquipment = _noEquipmentNeeded.value
            "list/$muscleGroupId?favorites=$favorites&noEquipment=$noEquipment&equipment=$equipmentString"
        }
    }
}