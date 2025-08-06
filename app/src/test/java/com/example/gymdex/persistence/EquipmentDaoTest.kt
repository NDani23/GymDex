package com.example.gymdex.persistence

import com.example.gymdex.MainCoroutinesRule
import com.example.gymdex.MockTestUtil.mockEquipmentList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [23])
class EquipmentDaoTest : LocalDatabase() {

    private lateinit var equipmentDao: EquipmentDao

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun init() {
        equipmentDao = db.equipmentDao()
    }

    @Test
    fun insertAndLoadEquipmentListTest() = runTest {
        val mockDataList = mockEquipmentList()
        equipmentDao.insertEquipmentList(mockDataList)

        val loadFromDB = equipmentDao.getEquipmentList()
        assertThat(loadFromDB.size, `is`(mockDataList.size))
        assertThat(loadFromDB.containsAll(mockDataList), `is`(true))
    }

    @Test
    fun getEquipmentByIdTest() = runTest {
        val mockDataList = mockEquipmentList()
        equipmentDao.insertEquipmentList(mockDataList)

        val equipment = equipmentDao.getEquipmentById(6) // Pull-up bar
        assertThat(equipment != null, `is`(true))
        assertThat(equipment!!.id, `is`(6))
        assertThat(equipment.name, `is`("Pull-up bar"))
    }

    @Test
    fun getEquipmentByIdNotFoundTest() = runTest {
        val mockDataList = mockEquipmentList()
        equipmentDao.insertEquipmentList(mockDataList)

        val equipment = equipmentDao.getEquipmentById(999) // Non-existent ID
        assertThat(equipment == null, `is`(true))
    }

}
