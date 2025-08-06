package com.example.gymdex.network

import com.example.gymdex.model.Equipment
import com.example.gymdex.model.MuscleGroup
import com.google.gson.annotations.SerializedName

data class EquipmentResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    @SerializedName("results") val equipments: List<Equipment>
)