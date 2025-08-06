package com.example.gymdex.ui.detail

import androidx.annotation.WorkerThread
import com.example.gymdex.model.Exercise
import com.example.gymdex.network.ApiService
import com.example.gymdex.persistence.ExerciseDao
import com.example.gymdex.persistence.EquipmentDao
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.skydoves.sandwich.message
import javax.inject.Inject

class ExerciseDetailRepository @Inject constructor(
    private val apiService: ApiService,
    private val exerciseDao : ExerciseDao,
    private val EquipmentDao : EquipmentDao
){
    @WorkerThread
    fun getExerciseDetails(id: Int, onError: (String) -> Unit): Flow<Exercise?> = flow {
        val exercise = exerciseDao.getExerciseById(id)
        if (exercise != null) {
            emit(exercise)
            // Fetch images if not already present
            if (exercise.primaryImage == null && exercise.secondaryImage == null) {
                apiService.fetchExerciseImagesById(exerciseId = id)
                    .suspendOnSuccess {
                        var primaryImage: String? = null
                        var secondaryImage: String? = null
                        data.images.forEach { image ->
                            if (image.isMain) primaryImage = image.image
                            else secondaryImage = image.image
                        }
                        exerciseDao.updateImages(id, primaryImage, secondaryImage)
                        emit(exerciseDao.getExerciseById(id)) // Emit updated exercise
                    }.onFailure {
                        onError(message())
                    }
            }
        } else {
            onError("Exercise not found")
        }
    }.flowOn(Dispatchers.IO)
}