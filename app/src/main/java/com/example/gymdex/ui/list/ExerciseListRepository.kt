package com.example.gymdex.ui.list

import androidx.annotation.WorkerThread
import com.example.gymdex.model.Exercise
import com.example.gymdex.network.ApiService
import javax.inject.Inject
import com.example.gymdex.persistence.ExerciseDao;
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class ExerciseListRepository @Inject constructor(
    private val apiService: ApiService,
    private val exerciseDao : ExerciseDao
){
    @WorkerThread
    fun loadExercises(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ): Flow<List<Exercise>> {
        return flow {
            var localExercises: List<Exercise> = exerciseDao.getExerciseList()
            if (localExercises.isEmpty()) {
                var nextPage: String? = ""
                var page = 1
                do {
                    apiService.fetchExerciseList(page = page)
                        .suspendOnSuccess {
                            val exercises = data.exercises
                            exerciseDao.insertExerciseList(exercises)
                            emit(exerciseDao.getExerciseList())
                            nextPage = data.next
                            page++
                        }.onFailure {
                            onError(message())
                        }
                } while (nextPage != null)

            } else {
                emit(localExercises)
            }
        }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
    }

    @WorkerThread
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


    @WorkerThread
    fun filterExercises(
        muscleGroup: Int,
        equipmentIds: Set<Int>?,
        isFavorite: Boolean?,
        noEquipmentNeeded: Boolean,
        onError: (String) -> Unit
    ): Flow<List<Exercise>> = flow {
        try {
            val equipmentString = equipmentIds?.joinToString(",")
            val exercises = exerciseDao.filterExercises(
                muscleGroup = muscleGroup,
                equipmentIds = equipmentString,
                isFavorite = isFavorite,
                noEquipmentNeeded = noEquipmentNeeded
            )
            emit(exercises)
        } catch (e: Exception) {
            onError(e.message ?: "Error filtering exercises")
        }
    }.flowOn(Dispatchers.IO)
}