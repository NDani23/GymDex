package com.example.gymdex.network
import com.google.gson.annotations.SerializedName
import com.example.gymdex.model.MuscleGroup

data class MuscleGroupResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val muscleGroups: List<MuscleGroup>
)