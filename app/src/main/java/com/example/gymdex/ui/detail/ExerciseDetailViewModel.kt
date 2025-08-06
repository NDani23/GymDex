package com.example.gymdex.ui.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.gymdex.model.Exercise
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.gymdex.persistence.EquipmentDao
import com.example.gymdex.persistence.ExerciseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val repository: ExerciseDetailRepository,
    private val exerciseDao : ExerciseDao,
    private val equipmentDao: EquipmentDao
) : ViewModel() {
    private val _exercise = mutableStateOf<Exercise?>(null)
    val exercise: State<Exercise?> get() = _exercise

    private val _equipmentNames = mutableStateOf<List<String>>(emptyList())
    val equipmentNames: State<List<String>> get() = _equipmentNames

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> get() = _error

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun loadExercise(id: Int) {
        repository.getExerciseDetails(id, onError = { _error.value = it })
            .onEach { exercise: Exercise? -> // Explicit type
                _exercise.value = exercise
                if (exercise != null && exercise.equipment.isNotEmpty()) {
                    viewModelScope.launch {
                        val names = exercise.equipment.mapNotNull { equipmentId ->
                            equipmentDao.getEquipmentById(equipmentId)?.name
                        }
                        _equipmentNames.value = names
                    }
                } else {
                    _equipmentNames.value = emptyList()
                }
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    fun toggleFavorite() {
        val currentExercise = _exercise.value ?: return
        viewModelScope.launch {
            val newFavoriteStatus = !currentExercise.favorite
            exerciseDao.updateFavorite(currentExercise.id, newFavoriteStatus)
            _exercise.value = currentExercise.copy(favorite = newFavoriteStatus)
        }

        // Naplózzuk az eseményt Firebase Analytics-szel
        firebaseAnalytics.logEvent("add_to_favorites") {
            param(FirebaseAnalytics.Param.ITEM_ID, currentExercise.id.toString())
            param(FirebaseAnalytics.Param.ITEM_NAME, currentExercise.name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "exercise")
        }
    }
}