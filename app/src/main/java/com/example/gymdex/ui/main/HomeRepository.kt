package com.example.gymdex.ui.main
import androidx.annotation.WorkerThread
import com.example.gymdex.model.MuscleGroup
import com.example.gymdex.network.ApiService
import com.example.gymdex.persistence.MuscleGroupDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.sandwich.message
import javax.inject.Inject
import android.util.Log // Add this import

class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    private val muscleGroupDao : MuscleGroupDao
){
    @WorkerThread
    fun loadMuscleGroups(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val localMuscleGroups: List<MuscleGroup> = muscleGroupDao.getMuscleGroupList()
        if (localMuscleGroups.isEmpty()) {
            apiService.fetchMuscleGroupList()
                .suspendOnSuccess {
                    val muscleGroups = data.muscleGroups
                    //Log.d("HomeRepository", "Muscle Groups: $muscleGroups")
                    muscleGroupDao.insertMuscleGroupList(muscleGroups)
                    emit(muscleGroups)
                }
                .onFailure { onError(message()) }
        } else {
            emit(localMuscleGroups)
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
}