package com.example.gymdex.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.compose.material.Scaffold
import com.example.gymdex.model.MuscleGroup
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import com.example.gymdex.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch

@Composable
fun ExerciseListScreen(
    viewModel: ExerciseListViewModel,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onExerciseClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    muscleGroupId : Int,
    muscleGroupName: String,
    favorites: Boolean = false,
    noEquipmentNeeded: Boolean = false,
    equipmentIds: Set<Int>? = null
) {

    LaunchedEffect(muscleGroupId, favorites, noEquipmentNeeded, equipmentIds) {
        viewModel.filterExercises(
            muscleGroupId = muscleGroupId,
            equipmentIds = equipmentIds,
            isFavorite = favorites.takeIf { it },
            noEquipmentNeeded = noEquipmentNeeded
        )
    }
    val exercises = viewModel.exercises.value
    val isLoading = viewModel.isLoading.value
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colors.background,
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(

                            text = muscleGroupName,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            style = MaterialTheme.typography.displayLarge
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onFilterClick() }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Filter",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onSearchClick() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                },
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(colors.background)
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -100f) {
                            onSwipeLeft()
                        } else if (dragAmount > 100f) {
                            onSwipeRight()
                        }
                    }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (exercises.isEmpty() && isLoading) {
                CircularProgressIndicator()
            } else if (exercises.isNotEmpty()) {
                LazyColumn (
                    modifier = Modifier
                        .padding(horizontal = 30.dp)

                ) {
                    items(exercises) { exercise ->
                        Button(
                            onClick = {
                                onExerciseClick(exercise.id)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colors.primary,
                                contentColor = colors.onPrimary
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)
                        ) {
                            Text(
                                text = exercise.name,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    if (isLoading) {
                        item { CircularProgressIndicator() } // Show loading for more pages
                    }
                }
            } else {
                Text(
                    text = "No exercises found",
                    color = Color.White

                )
            }
        }
    }
}