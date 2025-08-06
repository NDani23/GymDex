package com.example.gymdex.ui.search

import com.example.gymdex.model.Exercise
import javax.inject.Inject
import com.example.gymdex.persistence.ExerciseDao;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepository @Inject constructor(
    private val exerciseDao : ExerciseDao
){
    fun searchExercises(
        query: String,
        onError: (String) -> Unit
    ): Flow<List<Exercise>> = flow {
        try {
            val exercises = exerciseDao.searchExercisesByName(query)
            emit(exercises)
        } catch (e: Exception) {
            onError(e.message ?: "Error searching exercises")
        }
    }.flowOn(Dispatchers.IO)
}