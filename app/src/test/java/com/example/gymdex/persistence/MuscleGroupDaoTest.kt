package com.example.gymdex.persistence

import com.example.gymdex.MainCoroutinesRule
import com.example.gymdex.MockTestUtil.mockMuscleGroupList
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
class MuscleGroupDaoTest : LocalDatabase() {

    private lateinit var muscleGroupDao: MuscleGroupDao

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun init() {
        muscleGroupDao = db.muscleGroupDao()
    }

    @Test
    fun insertAndLoadMuscleGroupListTest() = runTest {
        val mockDataList = mockMuscleGroupList()
        muscleGroupDao.insertMuscleGroupList(mockDataList)

        val loadFromDB = muscleGroupDao.getMuscleGroupList()
        assertThat(loadFromDB.size, `is`(mockDataList.size))
        assertThat(loadFromDB.containsAll(mockDataList), `is`(true))
    }

    @Test
    fun getMuscleGroupNameTest() = runTest {
        val mockDataList = mockMuscleGroupList()
        muscleGroupDao.insertMuscleGroupList(mockDataList)

        val name = muscleGroupDao.getMuscleGroupName(id = 12) // Back
        assertThat(name, `is`("Back"))
    }

    @Test
    fun getMuscleGroupNameNotFoundTest() = runTest {
        val mockDataList = mockMuscleGroupList()
        muscleGroupDao.insertMuscleGroupList(mockDataList)

        val name = muscleGroupDao.getMuscleGroupName(id = 99) // Non-existent ID
        assertThat(name == null, `is`(true))
    }

}
