package com.example.gymdex

import com.example.gymdex.model.Exercise
import com.example.gymdex.model.Equipment
import com.example.gymdex.model.MuscleGroup


object MockTestUtil {
    fun mockExerciseList() = listOf(
        Exercise.mock(),
        Exercise.mock(
            _id = 181,
            _name = "Chin-ups",
            _category = 12,
            _description = "Like pull-ups but with a reverse grip.",
            _equipment = listOf(6),
            _primaryImage = "http://exercise.hellogym.io/media/exercise-images/181/Chin-ups-1.png",
            _secondaryImage = "http://exercise.hellogym.io/media/exercise-images/181/Chin-ups-2.png"
        ),
        Exercise.mock(
            _id = 82,
            _name = "Dips",
            _category = 8,
            _description = "Hold onto the bars at a narrow place (if they are not parallel) and press yourself up, but don't stretch the arms completely, so the muscles stay during the whole exercise under tension. Now bend the arms and go down as much as you can, keeping the elbows always pointing back, At this point, you can make a short pause before repeating the movement.",
            _equipment = listOf(),
            _primaryImage = "http://exercise.hellogym.io/media/exercise-images/82/Tricep-dips-2-2.png",
            _secondaryImage = "http://exercise.hellogym.io/media/exercise-images/82/Tricep-dips-2-1.png"
        )
    )

    fun mockEquipmentList() = listOf(
        Equipment.mock(),
        Equipment.mock(
            _id = 8,
            _name = "Bench"
        ),
        Equipment.mock(
            _id = 6,
            _name = "Pull-up bar"
        )
    )

    fun mockMuscleGroupList() = listOf(
        MuscleGroup.mock(),
        MuscleGroup.mock(
            _id = 12,
            _name = "Back"
        ),
        MuscleGroup.mock(
            _id = 11,
            _name = "Chest"
        )
    )
}
