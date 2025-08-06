package com.example.gymdex.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gymdex.ui.list.ExerciseListScreen
import com.example.gymdex.ui.detail.ExerciseDetailScreen
import com.example.gymdex.ui.filter.FilterScreen
import com.example.gymdex.ui.main.HomeScreen
import com.example.gymdex.ui.main.HomeViewModel
import com.example.gymdex.ui.search.SearchScreen

@Composable
fun GymDexNavGraph(navController: NavHostController, viewModel : HomeViewModel) {
    NavHost(
        navController = navController,
        startDestination = GymDexDestinations.Home.route
    ) {
        composable(GymDexDestinations.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onMuscleGroupClick = { muscleGroupId ->
                    navController.navigate("${GymDexDestinations.List.route}/$muscleGroupId")
                },
            )
        }

        composable(
            route = "${GymDexDestinations.List.route}/{muscleGroupId}?favorites={favorites}&noEquipment={noEquipment}&equipment={equipment}",
            arguments = listOf(
                navArgument("muscleGroupId") { type = NavType.IntType },
                navArgument("favorites") { type = NavType.StringType; defaultValue = "false" },
                navArgument("noEquipment") { type = NavType.StringType; defaultValue = "false" },
                navArgument("equipment") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val muscleGroupId = backStackEntry.arguments?.getInt("muscleGroupId") ?: 0
            val muscleGroupName = viewModel.muscleGroups.value.find { it.id == muscleGroupId }?.name ?: "Unknown Muscle Group"
            val favorites = backStackEntry.arguments?.getString("favorites")?.toBoolean() ?: false
            val noEquipment = backStackEntry.arguments?.getString("noEquipment")?.toBoolean() ?: false
            val equipment = backStackEntry.arguments?.getString("equipment")?.takeIf { it.isNotEmpty() }
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet()

            ExerciseListScreen(
                viewModel = hiltViewModel(),
                onSwipeLeft = { navController.navigate(GymDexDestinations.Filter.route) },
                onSwipeRight = { navController.navigate(GymDexDestinations.Home.route) },
                onExerciseClick = { exerciseId ->
                    navController.navigate("${GymDexDestinations.Detail.route}/$exerciseId")
                },
                onBackClick = { navController.navigate(GymDexDestinations.Home.route) },
                onFilterClick = { navController.navigate(GymDexDestinations.Filter.route) },
                onSearchClick = {navController.navigate(GymDexDestinations.Search.route)},
                muscleGroupId = muscleGroupId,
                muscleGroupName = muscleGroupName,
                favorites = favorites,
                noEquipmentNeeded = noEquipment,
                equipmentIds = equipment
            )
        }

        composable(
            route = "${GymDexDestinations.Detail.route}/{exerciseId}",
            arguments = listOf(navArgument("exerciseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: 0
            ExerciseDetailScreen(exerciseId = exerciseId, viewModel = hiltViewModel(), navController = navController)
        }
        composable(GymDexDestinations.Filter.route) {
            FilterScreen(
                viewModel = hiltViewModel(),
                navController = navController,
                onBackClick = { navController.popBackStack()})
        }
        composable(GymDexDestinations.Search.route) {
            SearchScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}