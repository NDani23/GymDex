package com.example.gymdex.ui.search
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.gymdex.model.Exercise
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> get() = _searchQuery

    private val _searchResults = mutableStateOf<List<Exercise>>(emptyList())
    val searchResults: State<List<Exercise>> get() = _searchResults

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchExercises(query)
    }

    private fun searchExercises(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        repository.searchExercises(
            query = query,
            onError = { /* Handle error, e.g., log or show toast */ }
        ).onEach { exercises: List<Exercise> ->
            _searchResults.value = exercises
        }.launchIn(viewModelScope)
    }
}