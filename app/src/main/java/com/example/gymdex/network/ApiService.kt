package com.example.gymdex.network

import com.example.gymdex.model.MuscleGroup
import retrofit2.http.GET
import retrofit2.http.Query
import com.skydoves.sandwich.ApiResponse

interface ApiService {

    @GET("exercisecategory")
    suspend fun fetchMuscleGroupList(): ApiResponse<MuscleGroupResponse>

    @GET("equipment")
    suspend fun fetchEquipmentList(): ApiResponse<EquipmentResponse>
    @GET("exercise")
    suspend fun fetchExerciseList(
        @Query("status") status: Int = 2,
        @Query("language") language: Int = 2,
        @Query("page") page: Int? = null
    ): ApiResponse<ExerciseResponse>

    @GET("exerciseimage")
    suspend fun fetchExerciseImagesById(
        @Query("exercise") exerciseId: Int
    ): ApiResponse<ExerciseImageResponse>
}