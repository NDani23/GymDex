package com.example.gymdex.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Exercise(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val equipment: List<Int> = emptyList(),
    val category: Int,
    val favorite: Boolean = false,
    val primaryImage: String? = null,
    val secondaryImage: String? = null
) {
    companion object {

        fun mock(_id: Int = 97,
                 _name: String = "Benchpress dumbbells",
                 _description: String = "The movement is very similar to benchpressing with a barbell, however, the weight is brought down to the chest at a lower point.</p>\n<p>Hold two dumbbells and lay down on a bench. Hold the weights next to the chest, at the height of your nipples and press them up till the arms are stretched. Let the weight slowly and controlled down.",
                 _equipment: List<Int> = listOf(3,8),
                 _category: Int = 11,
                 _primaryImage: String? = "http://exercise.hellogym.io/media/exercise-images/97/Dumbbell-bench-press-2.png",
                 _secondaryImage: String? = "http://exercise.hellogym.io/media/exercise-images/97/Dumbbell-bench-press-1.png"
        ) = Exercise(
            id = _id,
            name = _name,
            description = _description,
            equipment = _equipment,
            category = _category,
            primaryImage = _primaryImage,
            secondaryImage = _secondaryImage
        )
    }
}

