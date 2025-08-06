package com.example.gymdex.network

import com.google.gson.annotations.SerializedName

data class ExerciseImage(
    val id: Int,
    val exercise: Int,
    val image: String,
    @SerializedName("is_main") val isMain: Boolean
)

data class ExerciseImageResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val images: List<ExerciseImage>
)