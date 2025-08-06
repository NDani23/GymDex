package com.example.gymdex.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymdex.model.MuscleGroup

@Dao
interface MuscleGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuscleGroupList(muscleGroups: List<MuscleGroup>)
    @Query("SELECT * FROM MuscleGroup")
    suspend fun getMuscleGroupList(): List<MuscleGroup>
    @Query("SELECT name FROM MuscleGroup Where id = :id")
    suspend fun getMuscleGroupName(id: Int): String
}