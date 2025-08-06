package com.example.gymdex.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymdex.model.Equipment

@Dao
interface EquipmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipmentList(equipments: List<Equipment>)
    @Query("SELECT * FROM Equipment")
    suspend fun getEquipmentList(): List<Equipment>
    @Query("SELECT * FROM Equipment where id = :id")
    suspend fun getEquipmentById(id: Int): Equipment?
}