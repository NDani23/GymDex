package com.example.gymdex.ui.filter

import androidx.annotation.WorkerThread
import com.example.gymdex.model.Equipment
import com.example.gymdex.model.MuscleGroup
import com.example.gymdex.network.ApiService
import javax.inject.Inject
import com.example.gymdex.persistence.MuscleGroupDao
import com.example.gymdex.persistence.EquipmentDao
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class FilterRepository @Inject constructor(
    private val apiService: ApiService,
    private val equipmentDao : EquipmentDao,
    private val muscleGroupDao: MuscleGroupDao
){
    @WorkerThread
    fun loadEquipments(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val localEquipments: List<Equipment> = equipmentDao.getEquipmentList()
        if (localEquipments.isEmpty()) {
            apiService.fetchEquipmentList()
                .suspendOnSuccess {
                    val equipments = data.equipments
                    equipmentDao.insertEquipmentList(equipments)
                    emit(equipments)
                }
                .onFailure { onError(message()) }
        } else {
            emit(localEquipments)
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)

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
                    muscleGroupDao.insertMuscleGroupList(muscleGroups)
                    emit(muscleGroups)
                }
                .onFailure { onError(message()) }
        } else {
            emit(localMuscleGroups)
        }
    }.onStart { onStart() }.onCompletion { onCompletion() }.flowOn(Dispatchers.IO)
}