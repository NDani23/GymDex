package com.example.gymdex.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymdex.model.MuscleGroup
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.gymdex.ui.list.ExerciseListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val exerciseListRepository: ExerciseListRepository
) : ViewModel() {
    private val _muscleGroups = mutableStateOf<List<MuscleGroup>>(emptyList())
    val muscleGroups: State<List<MuscleGroup>> get() = _muscleGroups

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    init {
        loadMuscleGroups()
        loadExercisesInBackground()
    }

    private fun loadMuscleGroups() {

        repository.loadMuscleGroups(
            onStart = { _isLoading.value = true },
            onCompletion = { _isLoading.value = false },
            onError = { "Failed to load muscle Groups" }
        ).onEach { muscleGroups ->
            _muscleGroups.value = muscleGroups
        }.launchIn(viewModelScope)
    }

    //Start fetching exercises in the background because it takes a lot of time due to pagination
    private fun loadExercisesInBackground() {
        exerciseListRepository.loadExercises(
            onStart = { /* No UI update */ },
            onCompletion = { /* No UI update */ },
            onError = { /* Log error */ }
        ).launchIn(viewModelScope)
    }
}