// app/src/test/java/com/example/gymdex/network/ApiServiceTest.kt
package com.example.gymdex.network

import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ApiServiceTest : ApiAbstract<ApiService>() {

    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        apiService = createService(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchMuscleGroupListSuccessTest() = runTest {
        enqueueResponse("MuscleGroups.json")
        val response = apiService.fetchMuscleGroupList()
        if (response is ApiResponse.Success) {
            assertThat(response.data.muscleGroups.size, `is`(7))
            assertThat(response.data.muscleGroups[0].name, `is`("Abs"))
            assertThat(response.data.muscleGroups[1].name, `is`("Arms"))
        }
    }

    @Test
    fun fetchMuscleGroupListFailureTest() = runTest {
        enqueueResponse("Error.json", mapOf("Content-Type" to "application/json"))
        val response = apiService.fetchMuscleGroupList()
        if (response is ApiResponse.Failure.Error) {
            assertThat(response.response.message(), `is`("Not Found"))
            assertThat(response.response.code(), `is`(404))
        }
    }

    @Test
    fun fetchEquipmentListSuccessTest() = runTest {
        enqueueResponse("Equipments.json")
        val response = apiService.fetchEquipmentList()
        if (response is ApiResponse.Success) {
            assertThat(response.data.equipments.size, `is`(10))
            assertThat(response.data.equipments[0].name, `is`("Barbell"))
            assertThat(response.data.equipments[1].name, `is`("Bench"))
        }
    }

    @Test
    fun fetchEquipmentListFailureTest() = runTest {
        enqueueResponse("Error.json", mapOf("Content-Type" to "application/json"))
        val response = apiService.fetchEquipmentList()
        if (response is ApiResponse.Failure.Error) {
            assertThat(response.response.message(), `is`("Not Found"))
            assertThat(response.response.code(), `is`(404))
        }
    }

    @Test
    fun fetchExerciseListSuccessTest() = runTest {
        enqueueResponse("Exercises.json")
        val response = apiService.fetchExerciseList()
        if (response is ApiResponse.Success) {
            assertThat(response.data.exercises.size, `is`(20))
            assertThat(response.data.exercises[0].name, `is`("2 Handed Kettlebell swing"))
            assertThat(response.data.exercises[1].name, `is`("Aalex Gambe alte al muro"))
        }
    }

    @Test
    fun fetchExerciseListFailureTest() = runTest {
        enqueueResponse("Error.json", mapOf("Content-Type" to "application/json"))
        val response = apiService.fetchExerciseList(page = 1)
        if (response is ApiResponse.Failure.Error) {
            assertThat(response.response.message(), `is`("Not Found"))
            assertThat(response.response.code(), `is`(404))
        }
    }

    @Test
    fun fetchExerciseImagesByIdSuccessTest() = runTest {
        enqueueResponse("Images.json")
        val response = apiService.fetchExerciseImagesById(exerciseId = 0)
        if (response is ApiResponse.Success) {
            assertThat(response.data.images.size, `is`(20))
            assertThat(response.data.images[0].image, `is`("http://exercise.hellogym.io/media/exercise-images/4/Crunches-2.png"))
            assertThat(response.data.images[0].isMain, `is`(true))
        }
    }

    @Test
    fun fetchExerciseImagesByIdFailureTest() = runTest {
        enqueueResponse("Error.json", mapOf("Content-Type" to "application/json"))
        val response = apiService.fetchExerciseImagesById(exerciseId = 0)
        if (response is ApiResponse.Failure.Error) {
            assertThat(response.response.message(), `is`("Not Found"))
            assertThat(response.response.code(), `is`(404))
        }
    }
}