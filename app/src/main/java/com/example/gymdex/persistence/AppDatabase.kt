package com.example.gymdex.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gymdex.model.Exercise
import com.example.gymdex.model.MuscleGroup
import com.example.gymdex.model.Equipment

@Database(entities = [Exercise::class, MuscleGroup::class, Equipment::class], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun muscleGroupDao(): MuscleGroupDao
    abstract fun equipmentDao(): EquipmentDao
}