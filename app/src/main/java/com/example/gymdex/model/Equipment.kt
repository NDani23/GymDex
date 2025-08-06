package com.example.gymdex.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Equipment(
    @PrimaryKey val id: Int,
    val name: String
){
    companion object {
        fun mock(_id: Int = 3,
                 _name: String = "Dumbbell"
        ) = Equipment(
            id = _id,
            name = _name
        )
    }
}

