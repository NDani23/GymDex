package com.example.gymdex.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class MuscleGroup(
    @PrimaryKey val id: Int,
    val name: String
){
    companion object {
        fun mock(_id: Int = 8,
                 _name: String = "Arms"
        ) = MuscleGroup(
            id = _id,
            name = _name
        )
    }
}
