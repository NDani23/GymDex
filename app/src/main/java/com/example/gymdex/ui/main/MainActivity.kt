package com.example.gymdex.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gymdex.ui.theme.GymDexTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

import com.example.gymdex.navigation.GymDexNavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymDexTheme {
                val navController = rememberNavController()
                val viewModel: HomeViewModel = hiltViewModel()
                GymDexNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}