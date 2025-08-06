package com.example.gymdex.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymdex.model.Exercise

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseList(exercises: List<Exercise>)
    @Query("SELECT * FROM Exercise")
    suspend fun getExerciseList(): List<Exercise>
    @Query("SELECT name FROM Exercise WHERE category = :muscleGroup")
    suspend fun getExerciseNames(muscleGroup : Int): List<String>
    @Query("SELECT * FROM Exercise WHERE id = :id")
    suspend fun getExerciseById(id: Int): Exercise?
    @Query("UPDATE Exercise SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, favorite: Boolean)
    @Query("UPDATE Exercise SET primaryImage = :primaryImage, secondaryImage = :secondaryImage WHERE id = :id")
    suspend fun updateImages(id: Int, primaryImage: String?, secondaryImage: String?)

    //Operations to support searching exercises by name and filtering
    @Query("SELECT * FROM Exercise WHERE name LIKE '%' || :query || '%'")
    suspend fun searchExercisesByName(query: String): List<Exercise>

    @Query("""
        SELECT * FROM Exercise
        WHERE (category = :muscleGroup)
        AND (:isFavorite IS NULL OR favorite = :isFavorite)
        AND (
            :noEquipmentNeeded = 1 AND (equipment IS NULL OR equipment = '[]')
            OR :noEquipmentNeeded = 0 AND (
                :equipmentIds IS NULL OR :equipmentIds = ''
                OR equipment LIKE '%' || :equipmentIds || '%'
            )
        )
    """)
    suspend fun filterExercises(
        muscleGroup: Int,
        equipmentIds: String?, // Comma-separated equipment IDs, e.g., "1,2"
        isFavorite: Boolean?,
        noEquipmentNeeded: Boolean
    ): List<Exercise>
}