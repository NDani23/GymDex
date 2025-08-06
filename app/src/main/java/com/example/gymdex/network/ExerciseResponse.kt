package com.example.gymdex.network

import com.example.gymdex.model.Equipment
import com.example.gymdex.model.Exercise
import com.google.gson.annotations.SerializedName

class ExerciseResponse (
    val count: Int,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val exercises: List<Exercise>
)