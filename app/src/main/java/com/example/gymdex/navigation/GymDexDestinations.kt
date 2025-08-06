package com.example.gymdex.navigation

sealed class GymDexDestinations(val route: String) {
    object Home : GymDexDestinations("home")
    object List : GymDexDestinations("list")
    object Detail : GymDexDestinations("detail")
    object Filter : GymDexDestinations("filter")
    object Search : GymDexDestinations("search")
}