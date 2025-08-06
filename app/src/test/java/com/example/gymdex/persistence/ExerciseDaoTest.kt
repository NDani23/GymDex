package com.example.gymdex.persistence

import com.example.gymdex.MainCoroutinesRule
import com.example.gymdex.model.Exercise
import com.example.gymdex.MockTestUtil.mockExerciseList
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
class ExerciseDaoTest : LocalDatabase() {

    private lateinit var exerciseDao: ExerciseDao

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun init() {
        exerciseDao = db.exerciseDao()
    }

    @Test
    fun insertAndLoadExerciseListTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val loadFromDB = exerciseDao.getExerciseList()
        assertThat(loadFromDB.size, `is`(mockDataList.size))
        assertThat(loadFromDB.containsAll(mockDataList), `is`(true))
    }

    @Test
    fun getExerciseNamesTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val names = exerciseDao.getExerciseNames(muscleGroup = 11) // Chest
        assertThat(names.size, `is`(1))
        assertThat(names, `is`(listOf("Benchpress dumbbells")))
    }

    @Test
    fun getExerciseByIdTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val exercise = exerciseDao.getExerciseById(181) // Chin-ups
        assertThat(exercise?.id, `is`(181))
        assertThat(exercise?.name, `is`("Chin-ups"))
    }

    @Test
    fun getExerciseByIdNotFoundTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val exercise = exerciseDao.getExerciseById(999) // Non-existent ID
        assertThat(exercise == null, `is`(true))
    }

    @Test
    fun updateFavoriteTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        exerciseDao.updateFavorite(id = 97, favorite = true)
        val updatedExercise = exerciseDao.getExerciseById(97)
        assertThat(updatedExercise?.favorite, `is`(true))
    }

    @Test
    fun searchExercisesByNameTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val results = exerciseDao.searchExercisesByName("Chin")
        assertThat(results.size, `is`(1))
        assertThat(results[0].name, `is`("Chin-ups"))
    }

    @Test
    fun searchExercisesByNameNoMatchTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val results = exerciseDao.searchExercisesByName("Yoga")
        assertThat(results.size, `is`(0))
    }

    @Test
    fun filterExercisesByMuscleGroupTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val results = exerciseDao.filterExercises(
            muscleGroup = 12, // Back
            equipmentIds = null,
            isFavorite = null,
            noEquipmentNeeded = false
        )
        assertThat(results.size, `is`(1))
        assertThat(results[0].name, `is`("Chin-ups"))
    }

    @Test
    fun filterExercisesByEquipmentTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        val results = exerciseDao.filterExercises(
            muscleGroup = 11, // Chest
            equipmentIds = "3,8", // Barbell, Bench
            isFavorite = null,
            noEquipmentNeeded = false
        )
        assertThat(results.size, `is`(1))
        assertThat(results[0].name, `is`("Benchpress dumbbells"))
    }

    @Test
    fun filterExercisesByFavoriteTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        // Update one exercise to be favorite
        exerciseDao.updateFavorite(id = 82, favorite = true)

        val results = exerciseDao.filterExercises(
            muscleGroup = 8, // Triceps
            equipmentIds = null,
            isFavorite = true,
            noEquipmentNeeded = false
        )
        assertThat(results.size, `is`(1))
        assertThat(results[0].name, `is`("Dips"))
    }

    @Test
    fun filterExercisesMultipleParamTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)

        // Update one exercise to be favorite
        exerciseDao.updateFavorite(id = 181, favorite = true)

        val results = exerciseDao.filterExercises(
            muscleGroup = 12,
            equipmentIds = "6",
            isFavorite = true,
            noEquipmentNeeded = false
        )
        assertThat(results.size, `is`(1))
        assertThat(results[0].name, `is`("Chin-ups"))
    }

    @Test
    fun filterExercisesNoMatchTest() = runTest {
        val mockDataList = mockExerciseList()
        exerciseDao.insertExerciseList(mockDataList)


        val results = exerciseDao.filterExercises(
            muscleGroup = 8,
            equipmentIds = "6",
            isFavorite = true,
            noEquipmentNeeded = false
        )
        assertThat(results.size, `is`(0))
    }
}
